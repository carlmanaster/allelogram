package org.carlmanaster.filter.tests;

import junit.framework.TestSuite;

public class TestAll extends TestSuite{
	public static TestSuite suite() {
		TestAll suite = new TestAll();
		suite.addTestSuite(FilterTest.class);
		return suite;
	}
}
