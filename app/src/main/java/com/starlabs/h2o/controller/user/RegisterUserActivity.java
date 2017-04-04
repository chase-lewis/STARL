package com.starlabs.h2o.controller.user;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
    public static final String REG_INTENT = "USER_TEMP";

    // Keep track of the login task to ensure we can cancel it if requested
    private UserLoginTask mAuthTask = null;

    // UI references
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private EditText mPasswordRetypeView;
    private ProgressBar mProgressView;
    private Spinner mUserTypeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.register_username);
        mPasswordView = (EditText) findViewById(R.id.register_password);
        mPasswordRetypeView = (EditText) findViewById(R.id.register_retype_password);

        // Set up the register button
        Button registerButton = (Button) findViewById(R.id.register_register_button);
        registerButton.setOnClickListener(view -> attemptRegister());

        // Set up the cancel button
        Button cancelSignInButton = (Button) findViewById(R.id.register_cancel_button);
        cancelSignInButton.setOnClickListener(view -> finish());

        // Set up the progress bar for registration
        mProgressView = (ProgressBar) findViewById(R.id.register_progress);

        // Set up the user type spinner
        mUserTypeView = (Spinner) findViewById(R.id.register_user_type);
        ArrayAdapter<UserType> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, UserType.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mUserTypeView.setAdapter(adapter);
    }

    /**
     * Attempts to register the account specified by the login form.
     * If there are form errors (invalid user, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

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

        boolean cancel = false;
        View focusView = null;

        // Check for valid fields
        if (TextUtils.isEmpty(username)) {
            // Check if the username is empty
            mUsernameView.setError("A username is required");
            focusView = mUsernameView;
            cancel = true;
        } else if (!User.isUsernameValid(username)) {
            // Check if the username is not valid
            mUsernameView.setError("The username must be 3-20 characters " +
                    "and contain a digit");
            focusView = mUsernameView;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            // Check if the password is not empty
            mPasswordView.setError("A password is required");
            focusView = mPasswordView;
            cancel = true;
        } else if (!User.isPasswordValid(password)) {
            // Check if the password is not valid
            mPasswordView.setError("The password must be: 4-15 characters," +
                    "1 digit, 1 uppercase, 1 lowercase");
            focusView = mPasswordView;
            cancel = true;
        } else if (!password.equals(retypePassword)) {
            // Check if retype password does not match
            mPasswordRetypeView.setError("Passwords do not match");
            focusView = mPasswordRetypeView;
            cancel = true;
        } else {
            // Create new AsyncTask for user being registered
            mAuthTask = new UserLoginTask(username, password);

            // Check if username already exists in our content provider
            ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
            Consumer<User> onUserFound = user -> mAuthTask.setFoundUser(true);
            contentProvider.getSingleUser(onUserFound, username);
        }

        if (cancel) {
            // There was an error; don't attempt registration and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        mProgressView.setIndeterminate(show);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final int WAIT = 2000;
        private boolean foundUser;

        UserLoginTask(String userName, String password) {
            mUsername = userName;
            mPassword = password;
            setFoundUser(false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(WAIT);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            User user;
            mAuthTask = null;
            showProgress(false);

            if (success && !foundUser) {
                // Create the new user from the fields
                UserType userType = (UserType) mUserTypeView.getSelectedItem();
                user = new User(mUsername, mPassword, userType);

                // Set the user in the current session
                ContentProvider contentProvider
                        = ContentProviderFactory.getDefaultContentProvider();
                contentProvider.setLoggedInUser(user);

                // Transition to the Profile fragment in the Home Activity
                Intent intent = new Intent(RegisterUserActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.TO_PROFILE, "From register");
                startActivity(intent);
                finish();
            } else if (foundUser) {
                // Username is already taken
                mUsernameView.setError("This username is already taken");
                mUsernameView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

        /**
         * Setter for the found user boolean
         *
         * @param foundUser whether the username already exists
         */
        void setFoundUser(boolean foundUser) {
            this.foundUser = foundUser;
        }
    }

}

