package com.starlabs.h2o.facade;

import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.report.WaterReport;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
    public static synchronized ReportManager getInstance(ContentProvider contentProvider) {
        if (reportManager == null) {
            reportManager = new ReportManager(contentProvider);
        }

        ContentProvider conProv = reportManager.getContentProvider();
        if (!conProv.equals(contentProvider)) {
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
        Consumer<List<PurityReport>> onPurityReports = allPurityReports -> {

            // Filter out the purity reports and only use the ones with the matching ids
            List<PurityReport> filteredPurityReports = allPurityReports.stream()
                    .filter(purityReport -> waterReport.getLinkedPurityReports()
                            .contains(purityReport.getReportNumber()))
                    .collect(Collectors.toList());

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
     * Removes links between purity report from a water report
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
    public ContentProvider getContentProvider() {
        return contentProvider;
    }
}
