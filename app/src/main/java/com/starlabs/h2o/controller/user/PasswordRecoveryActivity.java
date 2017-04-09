package com.starlabs.h2o.controller.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.starlabs.h2o.R;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.facade.RecoveryManager;
import com.starlabs.h2o.model.user.User;

import java.util.function.Consumer;

public class PasswordRecoveryActivity extends AppCompatActivity {

    private AutoCompleteTextView usernameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery);

        // Setup the username view
        usernameView = (AutoCompleteTextView) findViewById(R.id.password_reset_username);

        // Setup the button
        Button onReset = (Button) findViewById(R.id.password_reset_button);
        onReset.setOnClickListener(v -> attemptReset());
    }

    /**
     * Attempts to reset the user password.
     */
    private void attemptReset() {
        // Reset errors
        usernameView.setError(null);

        // Store values at the time of the login attempt.
        Editable usName = usernameView.getText();
        final String username = usName.toString();

        boolean cancel = false;
        View focusView = null;

        // Check for valid fields
        if (TextUtils.isEmpty(username)) {
            // Check if the username is empty
            usernameView.setError("A username is required");
            focusView = usernameView;
            cancel = true;
        } else if ((username.length() > User.MAX_USER_LENGTH)
                || (username.length() < User.MIN_USER_LENGTH)) {
            // Check if the username is not valid
            usernameView.setError("The username must be 3-20 characters");
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt password reset
            // form field with an error.
            focusView.requestFocus();
        } else {

            // Check if the user exists in the content provider
            ContentProvider contentProvider = ContentProviderFactory.getDefaultContentProvider();
            Consumer<User> onUserFound = user -> {
                if (user != null) {
                    // Do password reset
                    RecoveryManager recoveryManager = RecoveryManager.getInstance(contentProvider);
                    recoveryManager.resetUserPassword(user);
                    finish();
                } else {
                    // User does not exist
                    usernameView.setError("User does not exist");
                    usernameView.requestFocus();
                }
            };
            contentProvider.getSingleUser(onUserFound, username);
        }
    }
}
