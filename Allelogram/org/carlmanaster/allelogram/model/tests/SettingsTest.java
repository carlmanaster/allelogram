package org.carlmanaster.allelogram.model.tests;

import junit.framework.TestCase;

import org.carlmanaster.allelogram.model.Classification;
import org.carlmanaster.allelogram.model.Settings;

/*
 * 		Settings file will look something like this:
 * 
 		columns
 			a
 			b
 			c
 		classifications
 			AB:a-b
 			BC:b.c
 		sort
 			AB
 		color
 			BC
 		info
 			AB
 			BC
 		click
 			AB ! option or alt
 			BC ! command or control
 		control
 			AB:123-456
 *
 */

public class SettingsTest extends TestCase {
    private Settings settings;
    
    protected void setUp() throws Exception {
		String s = "columns\na!comment\nb\nc\nclassifications\nAB:a-b\nBC:b.c\nsort";
		settings = new Settings(s);
	}
    
	public void testStripComments() throws Exception {
        assertEquals("test", Settings.stripComments("test"));
        assertEquals("test", Settings.stripComments("test ! comment"));
        assertEquals("test", Settings.stripComments("\ttest\t! comment"));
    }
    public void testColumns() throws Exception {
    		assertEquals(3, settings.getColumns().size());
		assertEquals("a", settings.getColumns().get(0));
	}
    public void testClassifications() throws Exception {
    		for (Classification classification : settings.getClassifications())
    			System.err.println(classification);
		assertEquals(2, settings.getClassifications().size());
	}
    
    // test presence of required labels
    // test order of required labels
    // test columns exist for each classification
    
}
