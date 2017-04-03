package com.starlabs.h2o.dao;

import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.report.WaterReport;
import com.starlabs.h2o.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Content provider that stores information locally.
 *
 * @author tejun
 */
public class LocalContentProvider extends SessionContentProvider implements ContentProvider {
    protected LocalContentProvider() {
        // Do nothing
    }

    @Override
    public void getAllUsers(Consumer<List<User>> callback) {
        callback.accept(new ArrayList<>());
    }

    @Override
    public void getSingleUser(Consumer<User> callback, String username) {
        callback.accept(null);
    }

    @Override
    public void setUser(User user) {
        // Do nothing
    }

    @Override
    public void getAllWaterReports(Consumer<List<WaterReport>> callback) {
        callback.accept(new ArrayList<>());
    }

    @Override
    public void getSingleWaterReport(Consumer<WaterReport> callback, int reportNumber) {
        callback.accept(null);
    }

    @Override
    public void setWaterReport(WaterReport waterReport) {
        // Do nothing
    }

    @Override
    public void getNextWaterReportId(Consumer<Integer> callback) {
        callback.accept(null);
    }

    @Override
    public void setNextWaterReportId(int reportNumber) {
        // Do nothing
    }

    @Override
    public void getAllPurityReports(Consumer<List<PurityReport>> callback) {
        callback.accept(new ArrayList<>());
    }

    @Override
    public void getSinglePurityReport(Consumer<PurityReport> callback, int reportNumber) {
        callback.accept(null);
    }

    @Override
    public void setPurityReport(PurityReport purityReport) {
        // Do nothing
    }

    @Override
    public void getNextPurityReportId(Consumer<Integer> callback) {
        callback.accept(null);
    }

    @Override
    public void setNextPurityReportId(int reportNumber) {
        // Do nothing
    }
}
