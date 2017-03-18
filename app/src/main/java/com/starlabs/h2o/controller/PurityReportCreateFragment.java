package com.starlabs.h2o.controller;

import android.app.Fragment;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.report.PurityCondition;
import com.starlabs.h2o.model.report.PurityReport;
import com.starlabs.h2o.model.user.User;

import java.util.function.Consumer;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link PurityReportCreateFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link PurityReportCreateFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class PurityReportCreateFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;

    User user;

    private TextView reportReporterName;
    private TextView reportDateText;
    private TextView reportNumText;
    private EditText reportLocLatEditText;
    private EditText reportLocLongEditText;
    private Spinner purityCondSpinner;
    private EditText virusPPMText;
    private EditText contPPMText;

    private PurityReport report;

    public PurityReportCreateFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment PurityReportCreateFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static PurityReportCreateFragment newInstance(String param1, String param2) {
//        PurityReportCreateFragment fragment = new PurityReportCreateFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_purity_report, container, false);

        // Set up the fields for the user profile
        reportDateText = (TextView) view.findViewById(R.id.create_purity_report_date);
        reportNumText = (TextView) view.findViewById(R.id.create_purity_report_num);
        reportLocLatEditText = (EditText) view.findViewById(R.id.create_purity_report_lat);
        reportLocLongEditText = (EditText) view.findViewById(R.id.create_purity_report_long);
        reportReporterName = (TextView) view.findViewById(R.id.create_purity_report_username);
        purityCondSpinner = (Spinner) view.findViewById(R.id.create_purity_report_condition);
        virusPPMText = (EditText) view.findViewById(R.id.create_purity_report_virus_ppm);
        contPPMText = (EditText) view.findViewById(R.id.create_purity_report_cont_ppm);

        // Get the user from session
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        user = contentProvider.getLoggedInUser();

        ArrayAdapter<String> condAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, PurityCondition.values());
        condAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        purityCondSpinner.setAdapter(condAdapter);

        // TODO: Someone remember to send a parcel from main class when editing
//        if (getIntent().hasExtra(PURITY_REPORT_TO_REPORT)) {
//            // TODO get the report from the intent
//            // TODO do we need to change any values? I don't think so, just let the user update them
//        } else {
        // Create a new report
        report = new PurityReport(user.getName(), new Location("H20"), PurityCondition.SAFE, 0, 0);

        // Get the correct id for the new report from the content provider
        Consumer<Integer> onNextIdFound = new Consumer<Integer>() {
            @Override
            public void accept(Integer id) {
                // Set the report number
                report.setReportNumber(id + 1);
                reportNumText.setText(Integer.toString(report.getReportNumber()));

                // Increment next id in the content provider
                contentProvider.setNextPurityReportId(id + 1);
            }
        };
        contentProvider.getNextPurityReportId(onNextIdFound);

//            // Check if report's latLong is being generated due to Map Tap or user's location
//            if (getIntent().hasExtra("fromMapClick")) {
//                report.setLatitude(getIntent().getDoubleExtra("latitude", 0));
//                report.setLongitude(getIntent().getDoubleExtra("longitude", 0));
//            } else {
//                // Set up the location of the report
//                // Check if we have location access permission first. Note we are using Network Location, not GPS
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
//                    String locationProvider = LocationManager.NETWORK_PROVIDER;
//
//                    // Get rough location very synchronously
//                    Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
//
//                    if (lastKnownLocation != null) {
//                        // Set the location in the pojo
//                        report.setLatitude(lastKnownLocation.getLatitude());
//                        report.setLongitude(lastKnownLocation.getLongitude());
//                    }
//                }
//            }
//        }

        // Set all the text views
        reportReporterName.setText(user.getName());
        reportDateText.setText(report.getCreationDate().toString());
        reportNumText.setText(Integer.toString(report.getReportNumber()));
        reportLocLatEditText.setText(Double.toString(report.getLatitude()));
        reportLocLongEditText.setText(Double.toString(report.getLongitude()));
        purityCondSpinner.setSelection(report.getCondition().ordinal());
        virusPPMText.setText(report.getVirusPPM() + "");
        contPPMText.setText(report.getContPPM() + "");

        // Create button setup
        Button reportCreateButton = (Button) view.findViewById(R.id.create_purity_report_create);
        reportCreateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onReportCreatePressed(view);
            }
        });

        // Cancel button setup
        Button reportCancelButton = (Button) view.findViewById(R.id.create_purity_report_cancel);
        reportCancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onCancelPressed(view);
            }
        });

        return view;
    }

    /**
     * Method to create/finalize edit on report
     *
     * @param view the parameter View
     */
    protected void onReportCreatePressed(View view) {
        // Update the values in the model from the UI
        report.setCondition((PurityCondition) purityCondSpinner.getSelectedItem());
        report.setVirusPPM(Integer.parseInt(virusPPMText.getText().toString()));
        report.setContPPM(Integer.parseInt(contPPMText.getText().toString()));

        // Verify the location data is valid
        double latitude;
        double longitude;

        reportLocLatEditText.setError(null);
        reportLocLongEditText.setError(null);

        try {
            latitude = Double.parseDouble(reportLocLatEditText.getText().toString());
        } catch (NumberFormatException e) {
            reportLocLatEditText.setError("Must pass in a valid number");
            return;
        }

        try {
            longitude = Double.parseDouble(reportLocLongEditText.getText().toString());
        } catch (NumberFormatException e) {
            reportLocLongEditText.setError("Must pass in a valid number");
            return;
        }

        if (latitude < -90 || latitude > 90) {
            reportLocLatEditText.setError("Latitude must be between -90 and 90");
            return;
        } else if (longitude < -180 || longitude > 180) {
            reportLocLongEditText.setError("Longitude must be between -180 and 180");
            return;
        } else {
            report.setLatitude(latitude);
            report.setLongitude(longitude);
        }

        // Store data
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        contentProvider.setPurityReport(report);

//        getActivity().getFragmentManager().popBackStackImmediate();
        getActivity().onBackPressed();
    }

    /**
     * Method to exit the activity back to caller.
     *
     * @param view the parameter View
     */
    protected void onCancelPressed(View view) {
//        getActivity().getFragmentManager().popBackStackImmediate();
        getActivity().onBackPressed();
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
