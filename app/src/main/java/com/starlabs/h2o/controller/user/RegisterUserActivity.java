package com.starlabs.h2o.controller.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.starlabs.h2o.R;
import com.starlabs.h2o.controller.HomeActivity;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.user.User;
import com.starlabs.h2o.model.user.UserType;

import java.util.function.Consumer;


/**
 * The registration screen for username/password authentication
 *
 * @author tejun, chase
 */
public class RegisterUserActivity extends AppCompatActivity {
    // UI references
    private EditText mUsernameView;
    private EditText mPasswordView;
    private EditText mPasswordRetypeView;
    private Spinner mUserTypeView;
    private ContentProvider contentProvider;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.register_username);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mPasswordRetypeView = (EditText) findViewById(R.id.register_retype_password);

        // Set up the register button
        Button registerButton = (Button) findViewById(R.id.register_register_button);
        registerButton.setOnClickListener(view -> attemptRegister());

        // Set up the cancel button
        Button cancelSignInButton = (Button) findViewById(R.id.register_cancel_button);
        cancelSignInButton.setOnClickListener(view -> finish());

        // Set up the user type spinner
        mUserTypeView = (Spinner) findViewById(R.id.register_user_type);
        ArrayAdapter<UserType> adapter = new ArrayAdapter<>(this, R.layout.activity_register_spinner, UserType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUserTypeView.setAdapter(adapter);
    }

    /**
     * Attempts to register the account specified by the login form.
     * If there are form errors (invalid user, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the register attempt.
        Editable usName = mUsernameView.getText();
        final String username = usName.toString();
        Editable passName = mPasswordView.getText();
        final String password = passName.toString();
        Editable rePassName = mPasswordRetypeView.getText();
        final String retypePassword = rePassName.toString();

        // Check for valid fields
        if (TextUtils.isEmpty(username)) {
            // Check if the username is empty
            mUsernameView.setError("A username is required");
            mUsernameView.requestFocus();
            return;
        } else if (!User.isUsernameValid(username)) {
            // Check if the username is not valid
            mUsernameView.setError("The username must be 3-20 characters and contain a digit");
            mUsernameView.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            // Check if the password is not empty
            mPasswordView.setError("A password is required");
            mPasswordView.requestFocus();
            return;
        } else if (!User.isPasswordValid(password)) {
            // Check if the password is not valid
            mPasswordView.setError("The password must be: 4-15 characters, 1 digit, 1 uppercase, 1 lowercase");
            mPasswordView.requestFocus();
            return;
        } else if (!password.equals(retypePassword)) {
            // Check if retype password does not match
            mPasswordRetypeView.setError("Passwords do not match");
            mPasswordRetypeView.requestFocus();
            return;
        }

        // Show loading screen
        progressDialog = ProgressDialog.show(this, "Loading", "Registering...");

        // Check if username already exists in our content provider
        contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<User> onUserFound = user -> {
            if (user == null) {
                // Good, username does not exist
                UserType userType = (UserType) mUserTypeView.getSelectedItem();
                User newUser = new User(username, password, userType);

                // Set the user in the current session
                contentProvider.setLoggedInUser(newUser);

                // Transition to the Profile fragment in the Home Activity
                progressDialog.dismiss();
                Intent intent = new Intent(RegisterUserActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.TO_PROFILE, "From register");
                startActivity(intent);
                finish();
            } else {
                // Bad, username has been taken
                progressDialog.dismiss();
                mUsernameView.setError("This username is already taken");
                mUsernameView.requestFocus();
            }
        };
        contentProvider.getSingleUser(onUserFound, username);
    }
}

