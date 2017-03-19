package com.starlabs.h2o.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
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

import com.starlabs.h2o.R;
import com.starlabs.h2o.controller.purity_report.PurityReportCreateFragment;
import com.starlabs.h2o.controller.purity_report.ViewPurityReportsFragment;
import com.starlabs.h2o.controller.water_report.ViewWaterReportsFragment;
import com.starlabs.h2o.controller.water_report.WaterReportCreateFragment;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.user.User;
import com.starlabs.h2o.model.user.UserType;

/**
 * Main screen with navigation drawer to allow transitions between fragments
 *
 * @author chase
 */
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    User user;
    NavigationView navigationView = null;
    Toolbar toolbar = null;
    FragmentManager fragmentManager = getFragmentManager();
    Fragment mapFragment = new MapViewFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        user = contentProvider.getLoggedInUser();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_home_container, mapFragment);
        transaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        //hide features from restricted users
        if (user.getUserType() == UserType.USER) {
            navigationView.getMenu().getItem(3).setVisible(false);
            navigationView.getMenu().getItem(4).setVisible(false);
            navigationView.getMenu().getItem(5).setVisible(false);
        } else if (user.getUserType() == UserType.WORKER) {
            navigationView.getMenu().getItem(4).setVisible(false);
            navigationView.getMenu().getItem(5).setVisible(false);
        }

        //set header information
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.header_name);
        name.setText(user.getName());
        TextView username = (TextView) header.findViewById(R.id.header_username);
        username.setText(user.getUsername());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.findFragmentById(R.id.fragment_home_container).getClass() != mapFragment.getClass()) {
            switchToMap();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit_profile) {
            Intent profileIntent = new Intent(HomeActivity.this, ViewUserProfileActivity.class);
            startActivity(profileIntent);
            return true;
        } else if (id == R.id.action_logout) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment newFragment;

        Fragment current = fragmentManager.findFragmentById(R.id.fragment_home_container);
        //TODO: Only switch fragments if not currently accessing that one
        if (id == R.id.nav_map) {
            newFragment = mapFragment;
        } else if (id == R.id.nav_create_water_report) {
            newFragment = new WaterReportCreateFragment();
        } else if (id == R.id.nav_view_water_reports) {
            newFragment = new ViewWaterReportsFragment();
        } else if (id == R.id.nav_create_purity_report) {
            newFragment = new PurityReportCreateFragment();
        } else if (id == R.id.nav_view_purity_reports) {
            newFragment = new ViewPurityReportsFragment();
        } else if (id == R.id.nav_view_histogram) {
            newFragment = current;
        } else {
            newFragment = current;
        }

        if (newFragment.getClass() != current.getClass()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_home_container, newFragment);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Method that switches the fragment to the map and updates the selected
     * item in the nav drawer.
     */
    public void switchToMap() {
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    /**
     * Method that switches the fragment to the map and updates the selected
     * item in the nav drawer.
     *
     * @param bundle A bundle containing either a LatLng or a water report to edit
     */
    public void switchToWaterReportCreate(Bundle bundle) {
        WaterReportCreateFragment newReport = new WaterReportCreateFragment();
        newReport.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_home_container, newReport);
        transaction.commit();
        navigationView.getMenu().getItem(1).setChecked(true);
    }
}
