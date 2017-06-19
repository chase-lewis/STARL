package com.starlabs.h2o.facade;

import android.os.AsyncTask;

import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.model.user.User;

import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;

/**
 * Object used to recover user accounts.
 *
 * @author tejun
 */
public class RecoveryManager {
    private static final String DOMAIN = "mg.tejunareddy.com";
    private static final String FROM_EMAIL = "h20@tejunareddy.com";
    private static final String API_KEY = "NOT_STORED_ON_GITHUB";

    private static RecoveryManager recoveryManager;
    private final ContentProvider contentProvider;
    private Configuration configuration;

    private RecoveryManager(ContentProvider contentProvider) {
        this.contentProvider = contentProvider;
        configuration = new Configuration()
                .domain(DOMAIN)
                .apiKey(API_KEY)
                .from(FROM_EMAIL);
    }

    /**
     * Returns an instance of the Recovery Manager with the given content provider
     *
     * @param contentProvider the content provider to use throughout the manager
     * @return a recovery manager
     */
    public static synchronized RecoveryManager getInstance(ContentProvider contentProvider) {
        if (recoveryManager == null) {
            recoveryManager = new RecoveryManager(contentProvider);
        }

        ContentProvider conProv = recoveryManager.getContentProvider();
        if (!conProv.equals(contentProvider)) {
            recoveryManager = new RecoveryManager(contentProvider);
        }

        return recoveryManager;
    }

    public void resetUserPassword(User user) {
        // Reset the password
        user.resetPassword();
        String newPass = user.getPassword();
        String userEmail = user.getEmail();

        // Set the password in the db
        contentProvider.setUser(user);

        // Send out email notification
        String[] args = {userEmail, newPass};
        new EmailTask().execute(args);
    }

    /**
     * Getter for the content provider
     *
     * @return the content provider this was constructed with
     */
    public ContentProvider getContentProvider() {
        return contentProvider;
    }

    /**
     * Async task to send email in the background.
     */
    private class EmailTask extends AsyncTask<String, Void, Boolean> {

        protected Boolean doInBackground(String... args) {
            Mail.using(configuration)
                    .to(args[0])
                    .subject("H20 Password Reset")
                    .text("Here is your new password: " + args[1])
                    .build()
                    .send();

            return true;
        }
    }
}
