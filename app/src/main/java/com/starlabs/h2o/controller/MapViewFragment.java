package com.starlabs.h2o.controller;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.starlabs.h2o.R;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.WaterReport;

import java.util.List;
import java.util.function.Consumer;

/**
 * Fragment for displaying google map in the HomeActivity
 *
 * @author chase
 */

public class MapViewFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_gmaps, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MapFragment fragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.gmap);
        fragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Move the camera in the map to our current location
        // Check if we have location access permission first. Note we are using Network Location, not GPS
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;

            // Get rough location synchronously
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

            if (lastKnownLocation != null) {
                // Set the location
                LatLng loc = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
            }
        }

        // Get all the water reports from the content provider
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<List<WaterReport>> onWaterReportsReceived = new Consumer<List<WaterReport>>() {
            @Override
            public void accept(List<WaterReport> waterReports) {
                for (WaterReport waterReport : waterReports) {
                    double latitude = waterReport.getLatitude();
                    double longitude = waterReport.getLongitude();
                    LatLng markLocation = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(markLocation).title(waterReport.toString()));
                }
            }
        };
        contentProvider.getAllWaterReports(onWaterReportsReceived);

        // Listens for a tap gesture on the map and then proceeds to the screen for report creation.
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("LOC", latLng);
                ((HomeActivity) getActivity()).switchToWaterReportCreate(bundle);
            }
        });
    }
}
