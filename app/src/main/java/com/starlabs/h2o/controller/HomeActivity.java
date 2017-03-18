package com.starlabs.h2o.controller;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.user.User;
import com.starlabs.h2o.model.user.UserType;

/**
 * Main screen with navigation drawer to allow transitions between fragments
 *
 * @author clewis
 */
public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    User user = new User("user", "pass", UserType.MANAGER);


    NavigationView navigationView = null;
    Toolbar toolbar = null;
    FragmentManager fragmentManager = getFragmentManager();
    Fragment mapFragment = new MapViewFragment();
    Fragment waterReportCreateFragment = new WaterReportCreateFragment();
    Fragment purityReportCreateFragment = new PurityReportCreateFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //FIXME: Actually read in the user from the login
        user.setName("Bob Ross");

        //FIXME:Temporary testing code... remove later
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        contentProvider.setLoggedInUser(user);


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_home_container, mapFragment);
        transaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);

        if (user.getUserType() == UserType.USER) {
            navigationView.getMenu().getItem(3).setVisible(false);
            navigationView.getMenu().getItem(4).setVisible(false);
            navigationView.getMenu().getItem(5).setVisible(false);
        } else if (user.getUserType() == UserType.WORKER) {
            navigationView.getMenu().getItem(4).setVisible(false);
            navigationView.getMenu().getItem(5).setVisible(false);
        }

        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.header_name);
        name.setText(user.getName());
        TextView username = (TextView) header.findViewById(R.id.header_username);
        username.setText(user.getUsername());

//        fragmentManager.addOnBackStackChangedListener(() -> );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.findFragmentById(R.id.fragment_home_container).getClass() != mapFragment.getClass()) {
            //FIXME: Replace with code that simulates user pressing map
            //Actually this seems to work for now... review later
            onNavigationItemSelected(navigationView.getMenu().getItem(0));
            navigationView.getMenu().getItem(0).setChecked(true);
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
        if (id == R.id.action_settings) {
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
//            if (!(current instanceof WaterReportCreateFragment)) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("User", user);
//                if (waterReportCreateFragment.getArguments() == null) {
//                    waterReportCreateFragment.setArguments(bundle);
//                } else {
//                    waterReportCreateFragment.getArguments().clear();
//                    waterReportCreateFragment.getArguments().putAll(bundle);
//                }
//            }
            newFragment = waterReportCreateFragment;
        } else if (id == R.id.nav_view_water_reports) {
            newFragment = current;
        } else if (id == R.id.nav_create_purity_report) {
            newFragment = purityReportCreateFragment;
        } else if (id == R.id.nav_view_purity_reports) {
            newFragment = current;
        } else if (id == R.id.nav_view_histogram) {
            newFragment = current;
        } else {
            newFragment = current;
        }

        if (newFragment.getClass() != current.getClass()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_home_container, newFragment);
//            transaction.addToBackStack("HOME_FRAG");
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
