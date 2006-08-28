package org.carlmanaster.allelogram.model.tests;

import org.carlmanaster.allelogram.model.Settings;

import junit.framework.TestCase;

public class SettingsTest extends TestCase {
    public void testStripComments() throws Exception {
        assertEquals("test", Settings.stripComments("test"));
        assertEquals("test", Settings.stripComments("test ! comment"));
        assertEquals("test", Settings.stripComments("\ttest\t! comment"));
    }
}
