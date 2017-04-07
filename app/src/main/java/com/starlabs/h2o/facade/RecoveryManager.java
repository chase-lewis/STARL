package com.starlabs.h2o.facade;

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
    private static final String API_KEY = "key-c4bd9679a394292a3ba07ed55eeaf428";

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
        Mail.using(configuration)
                .to(userEmail)
                .subject("H20 Password Reset")
                .text("Here is your new password: " + newPass)
                .build()
                .send();
    }

    /**
     * Getter for the content provider
     *
     * @return the content provider this was constructed with
     */
    public ContentProvider getContentProvider() {
        return contentProvider;
    }
}
