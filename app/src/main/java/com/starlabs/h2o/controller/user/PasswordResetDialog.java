package com.starlabs.h2o.controller.user;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.starlabs.h2o.R;
import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.dao.ContentProviderFactory;
import com.starlabs.h2o.facade.RecoveryManager;
import com.starlabs.h2o.model.user.User;

import java.util.function.Consumer;

/**
 * Dialog displayed to reset user password.
 *
 * @author tejun
 */
public class PasswordResetDialog extends DialogFragment {

    private AutoCompleteTextView usernameView;
    private ContentProvider contentProvider;

    private Consumer<User> onUserFound = user -> {
        if (user != null) {
            // Do password reset
            RecoveryManager recoveryManager = RecoveryManager.getInstance(contentProvider);
            recoveryManager.resetUserPassword(user);
            dismiss();

            // Create and show a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
            builder.setMessage("Please check your email for your new password")
                    .setTitle("Password Reset!")
                    .setPositiveButton("Ok", (dialog, id) -> {
                        // User clicked OK button, finish the activity
                        dialog.dismiss();
                    });
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            // User does not exist
            usernameView.setError("User does not exist");
            usernameView.requestFocus();
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.activity_password_recovery, null))
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Attempt to reset user password
                        attemptReset();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismiss the dialog
                        dialog.cancel();
                    }
                });

        return builder.create();
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
        } else if ((username.length() > User.MAX_USER_LENGTH) || (username.length() < User.MIN_USER_LENGTH)) {
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
            contentProvider = ContentProviderFactory.getDefaultContentProvider();
            contentProvider.getSingleUser(onUserFound, username);
        }
    }
}
