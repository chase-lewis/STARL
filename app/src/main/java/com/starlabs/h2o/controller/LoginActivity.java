package com.starlabs.h2o.controller;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.starlabs.h2o.R;
import com.starlabs.h2o.model.User;

/**
 * The login screen for username/password authentication
 *
 * @author tejun
 */
public class LoginActivity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private ProgressBar mProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout
        setContentView(R.layout.activity_login);

        // Set up the username form
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.login_username);
        mUsernameView.setText("");

        // Set up the password form
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mPasswordView.setText("");
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        // Set up the sign in button
        Button mSignInButton = (Button) findViewById(R.id.login_sign_in);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // Set up the cancel button
        Button cancelSignInButton = (Button) findViewById(R.id.login_cancel);
        cancelSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Set up the progress bar that shows logging in
        // Note: This is hidden on startup
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for valid fields
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError("This field is required");
            focusView = mUsernameView;
            cancel = true;
        } else if (!checkUsername(username)) {
            mUsernameView.setError("This username is not valid");
            focusView = mUsernameView;
            cancel = true;
        } else if (TextUtils.isEmpty(username)) {
            mPasswordView.setError("This field is required");
            focusView = mPasswordView;
            cancel = true;
        } else if (!checkPassword(password)) {
            mPasswordView.setError("This password is not valid");
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner and wait for 5 seconds for the user to be logged in.
            // Happens in the background
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);

            // Firebase database authentication
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

            // Create a listener for all the children of users
            mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userRef : dataSnapshot.getChildren()) {
                        // Found a user with a matching username
                        if (userRef.getKey().equals(username)) {
                            // Extract out the user object from firebase
                            User user = userRef.getValue(User.class);

                            if (user.getPassword().equals(password)) {
                                // Password matches!
                                // Call the async success method
                                mAuthTask.onPostExecute(true);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Do nothing yet
                }
            });
        }
    }

    private boolean checkUsername(String username) {
        return username.length() >= 4;
    }

    private boolean checkPassword(String password) {
        return password.length() >= 4;
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
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mUsername = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // Sleep for 5 seconds while attempting to login with firebase
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                return false;
            }

            // Login failed
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                // Login failed due to invalid username or a network connection
                mPasswordView.setError("The username and password combination was incorrect!");
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

