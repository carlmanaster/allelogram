package org.carlmanaster.allelogram.model.tests;

import java.util.HashMap;
import java.util.Vector;

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
		settings = new Settings(SETTINGS_STRING);
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
    		HashMap<String, Classification> classifications = settings.getClassifications();
		assertEquals(2, classifications.size());
		assertEquals("a-b", classifications.get("AB").toString());
	}
    public void testSort() throws Exception {
		assertEquals("a-b", settings.getSortClassification().toString());
	}
    public void testColorBy() throws Exception {
		assertEquals("b.c", settings.getColorByClassification().toString());
	}
    public void testInfo() throws Exception {
   		Vector<Classification> classifications = settings.getInfoClassifications();
		assertEquals(2, classifications.size());
		assertEquals("b.c", classifications.get(0).toString());
		assertEquals("a-b", classifications.get(1).toString());
	}
    public void testModifierClickClassifications() throws Exception {
    		assertEquals("a-b", settings.getOptionClickClassification().toString());
    		assertEquals("b.c", settings.getCommandClickClassification().toString());
	}
    
    private static final String SETTINGS_STRING = 
    	"columns\na!comment\nb\nc\nclassifications\nAB:a-b\nBC:b.c\nsort\nAB\ncolor\nBC\ninfo\nBC\nAB\nclick\nAB\nBC\ncontrol\n\n";
    // test presence of required labels
    // test order of required labels
    // test columns exist for each classification
    
}
