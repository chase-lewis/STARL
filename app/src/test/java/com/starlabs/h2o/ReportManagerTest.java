package com.starlabs.h2o;

import com.starlabs.h2o.dao.ContentProvider;
import com.starlabs.h2o.facade.ReportManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Unit tests for Report Manager facade.
 *
 * @author tejun
 */
public class ReportManagerTest {
    private ReportManager reportManager;

    @Mock private ContentProvider mockContentProvider;

    @Before
    public void setUp() {
        mockContentProvider = mock(ContentProvider.class);
        reportManager = ReportManager.getInstance(mockContentProvider);
    }

    @Test
    public void test() {
        // Do nothing
    }

    @After
    public void tearDown() {
        // Make sure the content provider in the report manager was correct
        assertTrue(reportManager.getContentProvider() == mockContentProvider);
    }
}
