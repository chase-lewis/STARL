package com.starlabs.h2o.model.report;

import java.util.Date;
import java.util.Random;

/**
 * Represents a Purity Report.
 *
 * @author Sungjae Hyun
 */

public class PurityReport {
    private String workerName;
    private Date creationDate;
    private int reportNumber;
    private PurityCondition condition;
    private int virusPPM;
    private int contPPM;
    private int linkedWaterReportId;

    public PurityReport(String workerName) {
        this.workerName = workerName;
        this.condition = PurityCondition.SAFE;
        this.creationDate = new Date();
        Random randObject = new Random();
        this.reportNumber = randObject.nextInt();
        this.virusPPM = 0;
        this.contPPM = 0;
        this.linkedWaterReportId = -1;
    }

    public PurityReport() {
        // Don't remove this firebase needs a no-arg constructor!
    }

    @Override
    public String toString() {
        return "Report " + reportNumber + ": Created " + creationDate.toString();
    }

    /**
     * Gets the creation time
     *
     * @return The time the report was created
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Setter for the creation time
     *
     * @param creationTime The time the report was created
     */
    public void setCreationDate(Date creationTime) {
        this.creationDate = creationTime;
    }

    /**
     * Getter for the report number
     *
     * @return The number of the report
     */
    public int getReportNumber() {
        return reportNumber;
    }

    /**
     * Setter for the report number
     *
     * @param reportNumber The number of the report
     */
    public void setReportNumber(int reportNumber) {
        this.reportNumber = reportNumber;
    }

    /**
     * Getter for the workerName
     *
     * @return The name of the user who created the report
     */
    public String getworkerName() {
        return workerName;
    }

    /**
     * Setter for the workerName
     *
     * @param workerName The name of the user who created the report
     */
    public void setReporterName(String workerName) {
        this.workerName = workerName;
    }

    /**
     * Getter for the waterConditon
     *
     * @return The water condition
     */
    public PurityCondition getCondition() {
        return condition;
    }

    /**
     * Setter for the waterCondition
     *
     * @param condition The condition of the water
     */
    public void setCondition(PurityCondition condition) {
        this.condition = condition;
    }

    /**
     * Getter for the virusPPM
     *
     * @return The virus PPM the report was created
     */
    public int getVirusPPM() {
        return virusPPM;
    }

    /**
     * Setter for the virusPPM
     *
     * @param virusPPM the virus PPM to be set
     */
    public void setVirusPPM(int virusPPM) {
        this.virusPPM = virusPPM;
    }

    /**
     * Getter for the contaminant PPM
     *
     * @return The longitude the report was created
     */
    public int getContPPM() {
        return contPPM;
    }

    /**
     * Setter for the contaminant PPM
     *
     * @param contPPM the contaminant PPM to be set
     */
    public void setContPPM(int contPPM) {
        this.contPPM = contPPM;
    }

    /**
     * Getter for the water report id this is linked to
     *
     * @return the corresponding water report id
     */
    public int getLinkedWaterReportId() {
        return linkedWaterReportId;
    }

    /**
     * Setter to link a purity report to a water report
     *
     * @param linkedWaterReportId the water report id
     */
    public void setLinkedWaterReportId(int linkedWaterReportId) {
        this.linkedWaterReportId = linkedWaterReportId;
    }
}


