package com.starlabs.h2o.controller.user;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.starlabs.h2o.R;
import com.starlabs.h2o.controller.HomeActivity;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.user.User;

/**
 * Activity to edit the user profile
 *
 * @author tejun, chase
 */
public class ViewUserProfileFragment extends Fragment {

    // Intent message ids
    public static final String TO_MAIN = "TO_MAIN";

    // Field views
    private EditText nameField;
    private EditText emailField;
    private EditText addressField;

    // User passed into this activity
    private User user;

    public ViewUserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_profile, container, false);

        // Set up the fields for the user profile
        nameField = (EditText) view.findViewById(R.id.user_profile_name_field);
        emailField = (EditText) view.findViewById(R.id.user_profile_email);
        addressField = (EditText) view.findViewById(R.id.user_profile_address);

        // Get the user from the session
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        user = contentProvider.getLoggedInUser();

        // Set up the text pre-defined values
        nameField.setText(user.getName());
        emailField.setText(user.getEmail());
        addressField.setText(user.getAddress());

        // Focus
        nameField.requestFocus();

        // Done button setup
        Button profileDoneButton = (Button) view.findViewById(R.id.profile_done_button);
        profileDoneButton.setOnClickListener(this::onProfileDonePressed);

        // Cancel button setup
        Button profileCancelButton = (Button) view.findViewById(R.id.profile_cancel_button);
        profileCancelButton.setOnClickListener(this::onCancelPressed);

        return view;
    }

    /**
     * Method to exit the activity to main.
     *
     * @param view the parameter View
     */
    protected void onProfileDonePressed(View view) {
        // Update the user model from the fields
        user.setName(nameField.getText().toString());
        user.setEmail(emailField.getText().toString());
        user.setAddress(addressField.getText().toString());

        // Store the user in our content provider, overriding all previous data for that user
        ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
        contentProvider.setUser(user);

        // Also store it in the session
        contentProvider.setLoggedInUser(user);

        // Transition back to the map fragment
        ((HomeActivity) getActivity()).setHeaderInfo(user);
        getActivity().onBackPressed();
    }

    /**
     * Method to exit the activity back to caller.
     *
     * @param view the parameter View
     */
    protected void onCancelPressed(View view) {
        getActivity().onBackPressed();
    }

}


