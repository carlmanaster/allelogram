package org.carlmanaster.allelogram.model.tests;

import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.carlmanaster.allelogram.model.Classifier;
import org.carlmanaster.allelogram.model.Genotype;
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
    private static final String SETTINGS_STRING = "columns\na!comment\nb\nc\nclassifications\nAB:a-b\nBC:b.c\nsort\nAB\ncolor\nBC\ninfo\nBC\nAB\nclick\nAB\nBC\ncontrol\nAB: x-y\n";
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
    		Map<String, Classifier> classifications = settings.getClassifiers();
		assertEquals(2, classifications.size());
		assertEquals("a-b", classifications.get("AB").toString());
	}
    public void testSort() throws Exception {
		assertEquals("a-b", settings.getSortClassifier().toString());
	}
    public void testColorBy() throws Exception {
		assertEquals("b.c", settings.getColorByClassifier().toString());
	}
    public void testInfo() throws Exception {
   		List<Classifier> classifications = settings.getInfoClassifiers();
		assertEquals(2, classifications.size());
		assertEquals("b.c", classifications.get(0).toString());
		assertEquals("a-b", classifications.get(1).toString());
	}
    public void testModifierClickClassifications() throws Exception {
    		assertEquals("a-b", settings.getOptionClickClassifier().toString());
    		assertEquals("b.c", settings.getCommandClickClassifier().toString());
	}
    public void testControlSubject() throws Exception {
		double[] alleles = new double[]{0,1};
		Genotype control = new Genotype(alleles, settings.getColumns(), new String[]{"x", "y", "z"});
		assertTrue(settings.isControlSubject(control));
		Genotype g = new Genotype(alleles, settings.getColumns(), new String[]{"t", "y", "z"});
		assertFalse(settings.isControlSubject(g));
	}
    public void testRequiredLabelsArePresent() throws Exception {
    		String[] bad = new String[] {
    				"*olumns\na!comment\nb\nc\nclassifications\nAB:a-b\nBC:b.c\nsort\nAB\ncolor\nBC\ninfo\nBC\nAB\nclick\nAB\nBC\ncontrol\nAB: x-y\n",
    				"columns\na!comment\nb\nc\n*lassifications\nAB:a-b\nBC:b.c\nsort\nAB\ncolor\nBC\ninfo\nBC\nAB\nclick\nAB\nBC\ncontrol\nAB: x-y\n",
    				"columns\na!comment\nb\nc\nclassifications\nAB:a-b\nBC:b.c\n*ort\nAB\ncolor\nBC\ninfo\nBC\nAB\nclick\nAB\nBC\ncontrol\nAB: x-y\n",
    				"columns\na!comment\nb\nc\nclassifications\nAB:a-b\nBC:b.c\nsort\nAB\n*olor\nBC\ninfo\nBC\nAB\nclick\nAB\nBC\ncontrol\nAB: x-y\n",
    				"columns\na!comment\nb\nc\nclassifications\nAB:a-b\nBC:b.c\nsort\nAB\ncolor\nBC\n*nfo\nBC\nAB\nclick\nAB\nBC\ncontrol\nAB: x-y\n",
    				"columns\na!comment\nb\nc\nclassifications\nAB:a-b\nBC:b.c\nsort\nAB\ncolor\nBC\ninfo\nBC\nAB\n*lick\nAB\nBC\ncontrol\nAB: x-y\n",
    				"columns\na!comment\nb\nc\nclassifications\nAB:a-b\nBC:b.c\nsort\nAB\ncolor\nBC\ninfo\nBC\nAB\nclick\nAB\nBC\n*ontrol\nAB: x-y\n",
    		};
    		for (String string : bad) {
    			try {
    				new Settings(string);
    				fail();
    			} catch (Exception e) {
    			}
    		}
	}
    public void testValidClassifications() throws Exception {
		try {
			new Settings("columns\na!comment\nb\nc\nclassifications\nAB:a-z\nBC:b.c\nsort\nAB\ncolor\nBC\ninfo\nBC\nAB\nclick\nAB\nBC\ncontrol\nAB: x-y\n");
			fail();
		} catch (Exception e) {
		}
	}
    public void testAllowColonsAsDelimiters() throws Exception {
    		Settings s = new Settings("columns\na!comment\nb\nc\nclassifications\nAB:a:b\nBC:b.c\nsort\nAB\ncolor\nBC\ninfo\nBC\nAB\nclick\nAB\nBC\ncontrol\nAB: x:y\n");
    		assertEquals("a:b", s.getClassifiers().get("AB").toString());
	}
}
