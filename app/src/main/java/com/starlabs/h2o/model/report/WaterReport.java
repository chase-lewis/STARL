package com.starlabs.h2o.model.report;

import android.location.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * POJO for a Water Report
 *
 * @author chase, tejun
 */
public class WaterReport {
    private String reporterName;
    private Date creationDate;
    private int reportNumber;
    private double latitude;
    private double longitude;
    private WaterType type;
    private WaterCondition condition;
    private List<Integer> purityReportIds;

    /**
     * Constructor that takes in a report name and location
     * @param reporterName name of user creating report
     * @param location location of report
     */
    public WaterReport(String reporterName, Location location) {
        this.reporterName = reporterName;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.type = WaterType.BOTTLED;
        this.condition = WaterCondition.POTABLE;
        this.creationDate = new Date();
        Random rand = new Random();
        this.reportNumber = rand.nextInt();
        this.purityReportIds = new ArrayList<>();
    }

    /**
     * Default no arg constructor
     */
    public WaterReport() {
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
     * Getter for the reporterName
     *
     * @return The name of the user who created the report
     */
    public String getReporterName() {
        return reporterName;
    }

    /**
     * Setter for the reporterName
     *
     * @param reporterName The name of the user who created the report
     */
    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    /**
     * Getter for the latitude
     *
     * @return The latitude the report was created
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Setter for the latitude
     *
     * @param latitude The latitude to be set (-90 to 90)
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Getter for the longitude
     *
     * @return The longitude the report was created
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Setter for the longitude
     *
     * @param longitude The longitude to be set (-90 to 90)
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Setter for the location
     *
     * @param location The location the report was created
     */
    public void setLocation(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    /**
     * Getter for the waterType
     *
     * @return The type of the water
     */
    public WaterType getType() {
        return type;
    }

    /**
     * Setter for the waterType
     *
     * @param type The type of the water
     */
    public void setType(WaterType type) {
        this.type = type;
    }

    /**
     * Getter for the waterCondition
     *
     * @return The water condition
     */
    public WaterCondition getCondition() {
        return condition;
    }

    /**
     * Setter for the waterCondition
     *
     * @param condition The condition of the water
     */
    public void setCondition(WaterCondition condition) {
        this.condition = condition;
    }

    /**
     * Add a specific purity report id to this water report.
     *
     * @param purityReportId the id of the purity report
     */
    public void linkPurityReport(int purityReportId) {
        if (purityReportIds == null) {
            this.resetLinkedPurityReports();
        }
        purityReportIds.add(purityReportId);
    }

    /**
     * Returns a list of the purity report ids associated with this water report.
     *
     * @return a read-only list
     */
    public List<Integer> getLinkedPurityReports() {
        // Make a copy of the list to prevent changes from affecting the model
        return new ArrayList<>(purityReportIds);
    }

    /**
     * Removes the specified purity report id from the list of associated purity reports.
     *
     * @param purityReportId the id to remove
     */
    public void unlinkPurityReport(int purityReportId) {
        purityReportIds.remove((Integer) purityReportId);
    }

    /**
     * Removes all linked purity reports!
     */
    public void resetLinkedPurityReports() {
        this.purityReportIds = new ArrayList<>();
    }
}
