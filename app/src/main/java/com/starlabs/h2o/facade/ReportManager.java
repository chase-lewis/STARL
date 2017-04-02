package com.starlabs.h2o.facade;

import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.report.WaterReport;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Facade that manages interactions between water reports and purity reports.
 *
 * @author tejun
 */
public final class ReportManager {
    private static ReportManager reportManager;
    private final ContentProvider contentProvider;

    private ReportManager(ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
    }

    /**
     * Returns an instance of the Report Manager with the given content provider
     *
     * @param contentProvider the content provider to use throughout the manager
     * @return a report manager
     */
    public static ReportManager getInstance(ContentProvider contentProvider) {
        ContentProvider conProv = reportManager.getContentProvider();
        if ((reportManager == null) || !conProv.equals(contentProvider)) {
            reportManager = new ReportManager(contentProvider);
        }

        return reportManager;
    }

    /**
     * Gets all linked purity reports associated with a water report
     *
     * @param waterReport       the water report with purity reports
     * @param onReportsReceived called when purity reports are downloaded
     */
    public void getLinkedPurityReports(WaterReport waterReport,
                                       Consumer<List<PurityReport>> onReportsReceived) {
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<List<PurityReport>> onPurityReports = allPurityReports -> {
            List<PurityReport> filteredPurityReports = new ArrayList<>();

            // Filter out the purity reports and only use the ones with the matching ids
            for (PurityReport pReport : allPurityReports) {
                boolean reportFound = false;
                int i = 0;
                List<Integer> purReports1 = waterReport.getLinkedPurityReports();
                while (!reportFound && (i < purReports1.size())) {
                    if (pReport.getReportNumber() == purReports1.get(i)) {
                        reportFound = true;
                        filteredPurityReports.add(pReport);
                    }
                    i++;
                }
            }

            // Call the callback
            onReportsReceived.accept(filteredPurityReports);
        };
        contentProvider.getAllPurityReports(onPurityReports);
    }

    /**
     * Links a purity report to a water report
     *
     * @param purityReportId the purity report to link with
     * @param waterReportId  the water report to link to
     * @param onFinish       called when linking is done
     */
    public void linkPurityReport(int purityReportId, int waterReportId, Runnable onFinish) {
        // Store association in the water report
        Consumer<WaterReport> waterReportConsumer = waterReport -> {
            // Store this purity report's id in the water report
            waterReport.linkPurityReport(purityReportId);
            contentProvider.setWaterReport(waterReport);

            // Do the next peace of code
            onFinish.run();
        };
        contentProvider.getSingleWaterReport(waterReportConsumer, waterReportId);
    }

    /**
     * Unlinks a purity report from a water report
     *
     * @param purityReport the purity report to unlink
     * @param onFinish     called when it is unlinked
     */
    public void unlinkPurityReport(PurityReport purityReport, Runnable onFinish) {
        // Remove association in the water report
        Consumer<WaterReport> waterReportConsumer = waterReport -> {
            // Store this purity report's id in the water report
            waterReport.unlinkPurityReport(purityReport.getReportNumber());
            contentProvider.setWaterReport(waterReport);

            // Do the next peace of code
            onFinish.run();
        };
        contentProvider.getSingleWaterReport(waterReportConsumer,
                purityReport.getLinkedWaterReportId());
    }

    /**
     * Getter for the content provider
     *
     * @return the content provider this was constructed with
     */
    private ContentProvider getContentProvider() {
        return contentProvider;
    }
}
