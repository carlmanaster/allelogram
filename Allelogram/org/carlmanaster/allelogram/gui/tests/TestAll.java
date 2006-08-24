package org.carlmanaster.allelogram.gui.tests;

import junit.framework.TestSuite;

public class TestAll extends TestSuite{
	public static TestSuite suite() {
		TestAll suite = new TestAll();
		suite.addTestSuite(ScaleTest.class);
		return suite;
	}
}
