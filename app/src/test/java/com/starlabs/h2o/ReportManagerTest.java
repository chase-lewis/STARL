package com.starlabs.h2o;

import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.LocalContentProvider;
import com.starlabs.h2o.facade.ReportManager;
import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.report.WaterReport;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for Report Manager facade. Note that this does not mock the firebase,
 * meaning that we receive real data.
 *
 * @author tejun
 */
public class ReportManagerTest {
    private ReportManager reportManager;
    private ContentProvider contentProvider;
    private List<Integer> purityIds;
    private List<PurityReport> allData;
    private List<PurityReport> emptyData;

    // Mock objects
    @Mock
    private PurityReport report1;
    @Mock
    private PurityReport report13;
    @Mock
    private PurityReport report21;
    @Mock
    private PurityReport report43;
    @Mock
    private WaterReport waterReport;
    @Mock
    private Consumer<List<PurityReport>> onReportsReceived;

    /**
     * Called before each test.
     */
    @Before
    public void setUp() {
        // Create mocks
        onReportsReceived = (Consumer<List<PurityReport>>) mock(Consumer.class);

        // Create lists to hold data
        purityIds = new ArrayList<>();
        allData = new ArrayList<>();
        emptyData = new ArrayList<>();

        // Create some mock data
        waterReport = mock(WaterReport.class);
        report1 = mock(PurityReport.class);
        when(report1.getReportNumber()).thenReturn(1);
        report13 = mock(PurityReport.class);
        when(report13.getReportNumber()).thenReturn(13);
        report21 = mock(PurityReport.class);
        when(report21.getReportNumber()).thenReturn(21);
        report43 = mock(PurityReport.class);
        when(report43.getReportNumber()).thenReturn(43);

        // Add to list
        allData.add(report1);
        allData.add(report13);
        allData.add(report21);
        allData.add(report43);
    }

    /**
     * Tests getLinkedPurityReports when the water report has no purity reports linked to it
     */
    @Test
    public void testGetLinkedPurityReportsNoDataInDb() {
        purityIds.add(report1.getReportNumber());
        purityIds.add(report13.getReportNumber());

        // Create the class we are testing
        contentProvider = new NoDataContentProvider();
        reportManager = ReportManager.getInstance(contentProvider);

        // Set up the mocks
        when(waterReport.getLinkedPurityReports()).thenReturn(purityIds);

        // Call the method
        reportManager.getLinkedPurityReports(waterReport, onReportsReceived);

        // Verify
        List<PurityReport> expected = new ArrayList<>();
        verify(onReportsReceived).accept(expected);
    }

    /**
     * Tests getLinkedPurityReports when the purity report has no ids but the db returns data.
     */
    @Test
    public void testGetLinkedPurityReportsNoDataInReport() {
        // Create the class we are testing
        contentProvider = new AllDataContentProvider();
        reportManager = ReportManager.getInstance(contentProvider);

        // Set up the mocks
        when(waterReport.getLinkedPurityReports()).thenReturn(purityIds);

        // Call the method
        reportManager.getLinkedPurityReports(waterReport, onReportsReceived);

        // Verify
        List<PurityReport> expected = new ArrayList<>();
        verify(onReportsReceived).accept(expected);
    }

    /**
     * Tests getLinkedPurityReports when the purity report has no ids that match the actual
     * objects in the db.
     */
    @Test
    public void testGetLinkedPurityReportsNoMatchingData() {
        purityIds.add(5);
        purityIds.add(57);

        // Create the class we are testing
        contentProvider = new AllDataContentProvider();
        reportManager = ReportManager.getInstance(contentProvider);

        // Set up the mocks
        when(waterReport.getLinkedPurityReports()).thenReturn(purityIds);

        // Call the method
        reportManager.getLinkedPurityReports(waterReport, onReportsReceived);

        // Verify
        List<PurityReport> expected = new ArrayList<>();
        verify(onReportsReceived).accept(expected);
    }

    /**
     * Tests getLinkedPurityReports when the purity report has all ids that match the actual
     * objects in the db.
     */
    @Test
    public void testGetLinkedPurityReportsAllMatchingData() {
        purityIds.add(report1.getReportNumber());
        purityIds.add(report13.getReportNumber());
        purityIds.add(report21.getReportNumber());
        purityIds.add(report43.getReportNumber());

        // Create the class we are testing
        contentProvider = new AllDataContentProvider();
        reportManager = ReportManager.getInstance(contentProvider);

        // Set up the mocks
        when(waterReport.getLinkedPurityReports()).thenReturn(purityIds);

        // Call the method
        reportManager.getLinkedPurityReports(waterReport, onReportsReceived);

        // Verify
        List<PurityReport> expected = new ArrayList<>();
        expected.add(report1);
        expected.add(report13);
        expected.add(report21);
        expected.add(report43);
        verify(onReportsReceived).accept(expected);
    }

    /**
     * Tests getLinkedPurityReports when the purity report has all ids that match the actual
     * objects in the db.
     */
    @Test
    public void testGetLinkedPurityReportsSomeMatchingData() {
        purityIds.add(84);
        purityIds.add(report13.getReportNumber());
        purityIds.add(53);
        purityIds.add(report43.getReportNumber());

        // Create the class we are testing
        contentProvider = new AllDataContentProvider();
        reportManager = ReportManager.getInstance(contentProvider);

        // Set up the mocks
        when(waterReport.getLinkedPurityReports()).thenReturn(purityIds);

        // Call the method
        reportManager.getLinkedPurityReports(waterReport, onReportsReceived);

        // Verify
        List<PurityReport> expected = new ArrayList<>();
        expected.add(report13);
        expected.add(report43);
        verify(onReportsReceived).accept(expected);
    }

    /**
     * Done after each test.
     */
    @After
    public void tearDown() {
        // Make sure the content provider in the report manager was correct
        assertTrue(reportManager.getContentProvider() == contentProvider);
    }

    /**
     * Content provider that calls the callback with no purity reports.
     */
    private class NoDataContentProvider extends LocalContentProvider {
        @Override
        public void getAllPurityReports(Consumer<List<PurityReport>> callback) {
            callback.accept(emptyData);
        }
    }

    /**
     * Content provider that calls the callback with all the purity reports.
     */
    private class AllDataContentProvider extends LocalContentProvider {
        @Override
        public void getAllPurityReports(Consumer<List<PurityReport>> callback) {
            callback.accept(allData);
        }
    }
}
