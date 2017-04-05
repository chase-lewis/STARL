package com.starlabs.h2o.controller;

import android.app.Fragment;

import org.junit.Test;
import com.starlabs.h2o.R;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.starlabs.h2o.controller.purity_report.CreatePurityReportFragment;
import com.starlabs.h2o.controller.purity_report.ViewPurityReportsFragment;
import com.starlabs.h2o.controller.report.SetupHistogramFragment;
import com.starlabs.h2o.controller.report.ViewHistogramFragment;
import com.starlabs.h2o.controller.report.ViewMapFragment;
import com.starlabs.h2o.controller.user.ViewUserProfileFragment;
import com.starlabs.h2o.controller.water_report.CreateWaterReportFragment;
import com.starlabs.h2o.controller.water_report.ViewWaterReportsFragment;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.WaterReport;
import com.starlabs.h2o.model.user.User;
import com.starlabs.h2o.model.user.UserType;

/**
 * Created by kavin on 4/5/2017.
 *
 * Tests Home Activity
 */

public class HomeActivityTest {

    @Test
    public void fragmentCheckTest() {
        HomeActivity homeActivityTester = new HomeActivity();
        Fragment current = new Fragment();
        boolean checkValue;

        int id = R.id.nav_map;
        checkValue = (homeActivityTester.fragmentCheck(id, current) instanceof  ViewMapFragment);
        assertTrue(checkValue);

        id = R.id.nav_create_water_report;
        checkValue = (homeActivityTester.fragmentCheck(id, current) instanceof  CreateWaterReportFragment);
        assertTrue(checkValue);

        id = R.id.nav_view_water_reports;
        checkValue = (homeActivityTester.fragmentCheck(id, current) instanceof  ViewWaterReportsFragment);
        assertTrue(checkValue);

        id = R.id.nav_create_purity_report;
        checkValue = (homeActivityTester.fragmentCheck(id, current) instanceof  CreatePurityReportFragment);
        assertTrue(checkValue);

        id = R.id.nav_view_purity_reports;
        checkValue = (homeActivityTester.fragmentCheck(id, current) instanceof  ViewPurityReportsFragment);
        assertTrue(checkValue);

        id = R.id.nav_view_histogram;
        checkValue = (homeActivityTester.fragmentCheck(id, current) instanceof  SetupHistogramFragment);
        assertTrue(checkValue);

        id = R.id.nav_profile;
        checkValue = (homeActivityTester.fragmentCheck(id, current) instanceof  ViewUserProfileFragment);
        assertTrue(checkValue);

        id = -1;
        checkValue = (homeActivityTester.fragmentCheck(id, current) == current);
        assertTrue(checkValue);

    }
}

