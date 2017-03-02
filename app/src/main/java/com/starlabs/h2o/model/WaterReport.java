package com.starlabs.h2o.model;

import android.location.Location;

import java.util.Date;
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

    public WaterReport(String reporterName, Location location, WaterType type, WaterCondition condition) {
        this.reporterName = reporterName;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.type = type;
        this.condition = condition;
        this.creationDate = new Date();
        this.reportNumber = new Random().nextInt();
    }

    public WaterReport() {
        // Don't remove this firebase needs a no-arg constructor!
    }

    @Override
    public String toString() {
        return reportNumber + ": " + reporterName;
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
     * Getter for the longitude
     *
     * @return The longitude the report was created
     */
    public double getLongitude() {
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
     * Setter for the longitude
     *
     * @param longitude The longitude to be set (-90 to 90)
     */
    public void setLongitude(double longitude) {
        this.latitude = longitude;
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
     * Getter for the waterConditon
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
}
