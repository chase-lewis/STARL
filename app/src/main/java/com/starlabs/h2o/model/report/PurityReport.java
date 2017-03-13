package com.starlabs.h2o.model.report;

import android.location.Location;

import java.util.Date;
import java.util.Random;

/**
 * Created by Sungjae Hyun on 2017-03-08.
 */

public class PurityReport {
    private String workerName;
    private Date creationDate;
    private int reportNumber;
    private double latitude;
    private double longitude;
    private WaterType type;
    private PurityCondition condition;
    private double virusPPM;
    private double contPPM;

    public PurityReport(String workerName, Location location, WaterType type, PurityCondition condition
            , double virusPPM, double contPPM) {
        this.workerName = workerName;
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.type = type;
        this.condition = condition;
        this.creationDate = new Date();
        this.reportNumber = new Random().nextInt();
        this.virusPPM = virusPPM;
        this.contPPM = contPPM;
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
    public double getVirusPPM() {
        return virusPPM;
    }

    /**
     * Setter for the virusPPM
     *
     * @param virusPPM the virus PPM to be set
     */
    public void setVirusPPM(double virusPPM) {
        this.virusPPM = virusPPM;
    }

    /**
     * Getter for the contaminant PPM
     *
     * @return The longitude the report was created
     */
    public double getContPPM() {
        return contPPM;
    }

    /**
     * Setter for the contaminant PPM
     *
     * @param contPPM the contaminant PPM to be set
     */
    public void setContPPM(double contPPM) {
        this.contPPM = contPPM;
    }

}


