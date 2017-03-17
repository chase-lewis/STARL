package com.starlabs.h2o.dao;

import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.report.WaterReport;
import com.starlabs.h2o.model.user.User;

import java.util.List;
import java.util.function.Consumer;

/**
 * Any class that implements this interface must provide the requested data.
 *
 * @author tejun
 */

public interface ContentProvider {
    /**
     * Gets the user for this session.
     *
     * @return the logged in user
     */
    User getLoggedInUser();

    /**
     * Sets the user for this session
     *
     * @param loggedInUser the logged in user
     */
    void setLoggedInUser(User loggedInUser);

    /**
     * Gets all users. Calls the callback when all users have been received.
     *
     * @param callback Called with a list of users
     */
    void getAllUsers(Consumer<List<User>> callback);

    /**
     * Gets a single user object from the username. Calls the callback when the user is received.
     *
     * @param callback Called with the user object
     * @param username The username to look for
     */
    void getSingleUser(Consumer<User> callback, String username);

    /**
     * Saves the user.
     *
     * @param user The user to save
     */
    void setUser(User user);

    /**
     * Gets all water reports. Calls the callback when all water reports have been received.
     *
     * @param callback Called with a list of water reports
     */
    void getAllWaterReports(Consumer<List<WaterReport>> callback);

    /**
     * Gets a single water report object from the reportNumber. Calls the callback when the water report is received.
     *
     * @param callback     Called with the water report object
     * @param reportNumber The reportNumber to look for
     */
    void getSingleWaterReport(Consumer<WaterReport> callback, int reportNumber);

    /**
     * Saves the water report.
     *
     * @param waterReport The water report to save
     */
    void setWaterReport(WaterReport waterReport);

    /**
     * Gets the id for the next water report. Calls the callback when this next id has been received.
     *
     * @param callback Called with the next integer report number
     */
    void getNextWaterReportId(Consumer<Integer> callback);

    /**
     * Sets the next reportNumber to the specified reportNumber for water reports
     *
     * @param reportNumber The reportNumber to save
     */
    void setNextWaterReportId(int reportNumber);

    /**
     * Gets all purity reports. Calls the callback when all purity reports have been received.
     *
     * @param callback Called with a list of purity reports
     */
    void getAllPurityReports(Consumer<List<PurityReport>> callback);


    /**
     * Gets a single purity report object from the reportNumber. Calls the callback when the purity report is received.
     *
     * @param callback     Called with the water report object
     * @param reportNumber The reportNumber to look for
     */
    void getSinglePurityReport(Consumer<PurityReport> callback, int reportNumber);

    /**
     * Saves the purity report.
     *
     * @param purityReport The purity report to save
     */
    void setPurityReport(PurityReport purityReport);

    /**
     * Gets the id for the next purity report. Calls the callback when this next id has been received.
     *
     * @param callback Called with the next integer report number
     */
    void getNextPurityReportId(Consumer<Integer> callback);

    /**
     * Sets the next reportNumber to the specified reportNumber for purity reports
     *
     * @param reportNumber The reportNumber to save
     */
    void setNextPurityReportId(int reportNumber);
}
