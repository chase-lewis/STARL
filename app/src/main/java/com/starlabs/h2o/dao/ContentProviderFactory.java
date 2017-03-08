package com.starlabs.h2o.dao;

/**
 * Used to abstract the construction of Content Providers.
 *
 * @author tejun
 */

public class ContentProviderFactory {

    private ContentProviderFactory() {
        // Private constructor to disable object creation
    }

    /**
     * Constructs and returns the default stateless content provider.
     *
     * @return a content provider
     */
    public static ContentProvider getDefaultContentProvider() {
        ContentProvider contentProvider = new FirebaseContentProvider();
        return contentProvider;
    }
}
