package com.starlabs.h2o.controller;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;
import com.starlabs.h2o.R;
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
 * Main screen with navigation drawer to allow transitions between fragments
 *
 * @author chase
 */
public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TO_PROFILE = "HOME_TO_PROFILE";
    // Other instance data
    private final FragmentManager fragmentManager = getFragmentManager();
    // Set up views
    private NavigationView navigationView = null;
    private Toolbar toolbar;
    private int oldId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        User user = contentProvider.getLoggedInUser();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //hide features from restricted users
        if (user.getUserType() == UserType.USER) {
            Menu navMenu = navigationView.getMenu();
            MenuItem menuItem1 = navMenu.getItem(3);
            menuItem1.setVisible(false);
            MenuItem menuItem2 = navMenu.getItem(4);
            menuItem2.setVisible(false);
            MenuItem menuItem3 = navMenu.getItem(5);
            menuItem3.setVisible(false);
        } else if (user.getUserType() == UserType.WORKER) {
            Menu navMenu = navigationView.getMenu();
            MenuItem menuItem2 = navMenu.getItem(4);
            menuItem2.setVisible(false);
            MenuItem menuItem3 = navMenu.getItem(5);
            menuItem3.setVisible(false);
        }

        this.setHeaderInfo(user);

        HomeActivityTransition();
    }

    /**
     * Transitions to right fragment
     */
    private void HomeActivityTransition() {
        Fragment mapFragment = new ViewMapFragment();
        Fragment profileFragment = new ViewUserProfileFragment();
        int id;

        // Transition to the proper fragment
        Intent currIntent = getIntent();
        if (currIntent.hasExtra(TO_PROFILE)) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_home_container, profileFragment);
            transaction.commit();
            id = R.id.nav_profile;
        } else {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_home_container, mapFragment);
            transaction.commit();
            id = R.id.nav_map;
        }
        // Check the proper menu items
        Menu menu1 = navigationView.getMenu();
        MenuItem menuItem1 = menu1.findItem(id);
        menuItem1.setChecked(true);
        oldId = id;
        determineTitle(id);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Fragment fragById = fragmentManager.findFragmentById(R.id.fragment_home_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            // Close drawer if it is open
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragById.getClass() != MapFragment.class) {
            // Transition to default map fragment if it's not open
            switchToMap();
        } else {
            // If on map fragment...
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment newFragment;
        Fragment current = fragmentManager.findFragmentById(R.id.fragment_home_container);

        if (id == R.id.nav_log_out) {
            newFragment = current;
            // Logout!
            finish();
        } else {
            newFragment = fragmentInitialize(id, current);
        }

        // Only switch fragments if not currently accessing that one
        if (newFragment.getClass() != current.getClass()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_home_container, newFragment);
            transaction.commit();

            // Un-check and recheck the proper menu items
            Menu navMenu1 = navigationView.getMenu();
            MenuItem menItemOld = navMenu1.findItem(oldId);
            menItemOld.setChecked(false);
            MenuItem menItemCurr = navMenu1.findItem(id);
            menItemCurr.setChecked(true);
            oldId = id;
            determineTitle(id);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    /**
     * Initializes the proper fragment based on the id that is passed in.
     *
     * @param id      the id to transition to
     * @param current the current fragment
     * @return a fragment to transition to
     */
    public Fragment fragmentInitialize(int id, Fragment current) {
        Fragment newFragment;

        if (id == R.id.nav_map) {
            newFragment = new ViewMapFragment();

        } else if (id == R.id.nav_create_water_report) {
            newFragment = new CreateWaterReportFragment();
        } else if (id == R.id.nav_view_water_reports) {
            newFragment = new ViewWaterReportsFragment();
        } else if (id == R.id.nav_create_purity_report) {
            newFragment = new CreatePurityReportFragment();
        } else if (id == R.id.nav_view_purity_reports) {
            newFragment = new ViewPurityReportsFragment();
        } else if (id == R.id.nav_view_histogram) {
            newFragment = new SetupHistogramFragment();
        } else if (id == R.id.nav_profile) {
            newFragment = new ViewUserProfileFragment();
        } else {
            newFragment = current;
        }

        return newFragment;
    }

    /**
     * Method that switches the fragment to the map and updates the selected
     * item in the nav drawer.
     */
    private void switchToMap() {
        Menu navMenu = navigationView.getMenu();
        onNavigationItemSelected(navMenu.findItem(R.id.nav_map));
        determineTitle(R.id.nav_map);
    }

    /**
     * Method that switches the fragment to the map and updates the selected
     * item in the nav drawer.
     *
     * @param bundle A bundle containing either a LatLng or a water report to edit
     */
    public void switchToWaterReportCreate(Bundle bundle) {
        CreateWaterReportFragment newReport = new CreateWaterReportFragment();
        newReport.setArguments(bundle);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_home_container, newReport);
        transaction.commit();

        // Un-check and recheck the proper menu items
        int id = R.id.nav_create_water_report;
        Menu navMenu = navigationView.getMenu();
        MenuItem menuItem1 = navMenu.findItem(oldId);
        menuItem1.setChecked(false);
        MenuItem menuItem2 = navMenu.findItem(id);
        menuItem2.setChecked(true);
        oldId = id;
        determineTitle(id);
    }

    /**
     * Method that switches the fragment to the histogram
     *
     * @param bundle      A bundle containing water report id, virus or contaminant, and year
     * @param waterReport A water report to display on histogram
     */
    public void switchToHistogram(Bundle bundle, WaterReport waterReport) {
        ViewHistogramFragment frag = new ViewHistogramFragment();
        frag.setArguments(bundle);
        frag.setWaterReport(waterReport);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_home_container, frag);
        transaction.addToBackStack(null);
        transaction.commit();
        determineTitle(R.id.nav_view_histogram);
    }

    /**
     * Set up the user info in the header.
     *
     * @param user the info to use
     */
    public void setHeaderInfo(User user) {
        // Set header information
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.header_name);
        name.setText(user.getName());
        TextView username = (TextView) header.findViewById(R.id.header_username);
        username.setText(user.getUsername());
        ImageView picture = (ImageView) header.findViewById(R.id.header_picture);
        picture.setImageBitmap(User.stringToBitmap(user.getProfilePicture()));
    }

    /**
     * Sets the toolbar's title based on the fragment being shown.
     *
     * @param id the menu item id
     */
    private void determineTitle(int id) {
        if (id == R.id.nav_map) {
            toolbar.setTitle("Nearby Water");
        } else if (id == R.id.nav_create_water_report) {
            toolbar.setTitle("Create Water Report");
        } else if (id == R.id.nav_view_water_reports) {
            toolbar.setTitle("View Water Reports");
        } else if (id == R.id.nav_create_purity_report) {
            toolbar.setTitle("Create Purity Report");
        } else if (id == R.id.nav_view_purity_reports) {
            toolbar.setTitle("View Purity Reports");
        } else if (id == R.id.nav_view_histogram) {
            toolbar.setTitle("History Report");
        } else if (id == R.id.nav_profile) {
            toolbar.setTitle("Edit Your Profile");
        } else {
            toolbar.setTitle("STARLabs H20");
        }
    }
}
