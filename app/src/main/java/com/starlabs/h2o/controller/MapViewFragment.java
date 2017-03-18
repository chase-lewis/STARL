package com.starlabs.h2o.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.starlabs.h2o.R;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.WaterReport;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by chase on 3/17/17.
 */

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private static View view;
    MapView mMapView;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_gmaps, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        return view;


//        return inflater.inflate(R.layout.fragment_gmaps, container, false);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

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
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                Intent intent = new Intent("NON", CreateWaterReportActivity.class);
//                intent.putExtra("latitude", latLng.latitude);
//                intent.putExtra("longitude", latLng.longitude);
//                intent.putExtra("fromMapClick", "fromMapClick");
//                startActivity(intent);
//            }
//        });
    }
}
