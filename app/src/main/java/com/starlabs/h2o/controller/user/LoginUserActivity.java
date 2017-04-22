package com.starlabs.h2o.controller.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.controller.HomeActivity;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.model.user.User;

import java.util.function.Consumer;

/**
 * The login screen for username/password authentication
 *
 * @author tejun, chase
 */
public class LoginUserActivity extends AppCompatActivity {

    // UI references
    private EditText mUsernameView;
    private EditText mPasswordView;
    private TextView mForgotPasswordView;
    private ContentProvider contentProvider;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout
        setContentView(R.layout.activity_login);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        // Set up the username form
        mUsernameView = (EditText) findViewById(R.id.login_username);
        mUsernameView.setText("");

        // Set up the password form
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mPasswordView.setText("");
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> (id == R.id.login)
                || (id == EditorInfo.IME_NULL));

        // Set up the forgot password text
        mForgotPasswordView = (TextView) findViewById(R.id.login_forgot_password);
        mForgotPasswordView.setOnClickListener(view -> openForgotPassword());

        // Set up the sign in button
        Button mSignInButton = (Button) findViewById(R.id.login_sign_in);
        mSignInButton.setOnClickListener(view -> attemptLogin());

        // Set up the cancel button
        Button cancelSignInButton = (Button) findViewById(R.id.login_cancel);
        cancelSignInButton.setOnClickListener(view -> finish());

        // Animations
        getWindow().setEnterTransition(new Fade(Fade.IN));
        getWindow().setExitTransition(new Fade(Fade.OUT));
    }

    /**
     * Transitions user to the forgot password activity.
     *
     * @return always true (always consumed event)
     */
    private boolean openForgotPassword() {
        Intent intent = new Intent(LoginUserActivity.this, PasswordRecoveryDialogActivity.class);
        startActivity(intent);
        return true;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        Editable usName = mUsernameView.getText();
        final String username = usName.toString();
        Editable passName = mPasswordView.getText();
        final String password = passName.toString();

        // Check for valid fields
        if (TextUtils.isEmpty(username)) {
            // Check if the username is empty
            mUsernameView.setError("A username is required");
            mUsernameView.requestFocus();
            return;
        } else if ((username.length() > User.MAX_USER_LENGTH) || (username.length() < User.MIN_USER_LENGTH)) {
            // Check if the username is not valid
            mUsernameView.setError("The username must be 3-20 characters");
            mUsernameView.requestFocus();
            return;
        } else if (TextUtils.isEmpty(password)) {
            // Check if the password is not empty
            mPasswordView.setError("A password is required");
            mPasswordView.requestFocus();
            return;
        }

        // Show spinner
        progressDialog = ProgressDialog.show(this, "Loading", "Logging in...");

        // Check if the user exists in the content provider
        contentProvider = ContentProviderFactory.getDefaultContentProvider();
        Consumer<User> onUserFound = user -> {
            // Turn off the progress bar

            if (user != null && user.isCorrectPassword(password)) {
                // Password matches!
                contentProvider.setLoggedInUser(user);

                // Transition to the Map fragment in the Home Activity
                progressDialog.dismiss();
                Intent profileIntent = new Intent(LoginUserActivity.this, HomeActivity.class);
                startActivity(profileIntent);
                finish();
            } else if (user == null) {
                // No user found
                progressDialog.dismiss();
                mPasswordView.setError("The username does not exist!");
                mPasswordView.requestFocus();
            } else {
                // Incorrect username / password
                progressDialog.dismiss();
                mPasswordView.setError("The username and password combination was incorrect!");
                mPasswordView.requestFocus();
            }
        };
        contentProvider.getSingleUser(onUserFound, username);
    }
}

