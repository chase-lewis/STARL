package com.starlabs.h2o.controller.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.starlabs.h2o.R;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.facade.RecoveryManager;
import com.starlabs.h2o.model.user.User;

import java.util.function.Consumer;

/**
 * Dialog activity to recover a user's password
 *
 * @author tejun
 */
public class PasswordRecoveryDialogActivity extends Activity {

    private EditText usernameView;
    private ProgressDialog progressDialog;
    private ContentProvider contentProvider;
    private Consumer<User> onUserFound = user -> {
        if (user != null) {
            // Do password reset
            RecoveryManager recoveryManager = RecoveryManager.getInstance(contentProvider);
            recoveryManager.resetUserPassword(user);

            // Create and show a dialog
            progressDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please check your email for your new password")
                    .setTitle("Password Reset!")
                    .setPositiveButton("Ok", (dialog, id) -> {
                        // User clicked OK button, finish the activity
                        finish();
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            // User does not exist
            progressDialog.dismiss();
            usernameView.setError("User does not exist");
            usernameView.requestFocus();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_activity_password_recovery);

        // Setup the username view
        usernameView = (EditText) findViewById(R.id.recovery_username);

        // Setup the button
        Button onReset = (Button) findViewById(R.id.password_reset_button);
        onReset.setOnClickListener(v -> attemptReset());

        Button onCancel = (Button) findViewById(R.id.password_reset_cancel_button);
        onCancel.setOnClickListener(v -> finish());
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
            // Show the loading dialog
            progressDialog = ProgressDialog.show(this, "Loading", "Sending email...");

            // Check if the user exists in the content provider
            contentProvider = ContentProviderFactory.getDefaultContentProvider();
            contentProvider.getSingleUser(onUserFound, username);
        }
    }
}
