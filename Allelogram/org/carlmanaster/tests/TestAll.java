package org.carlmanaster.tests;

import junit.framework.TestSuite;

public class TestAll extends TestSuite{
	public static TestSuite suite() {
		TestAll suite = new TestAll();
		suite.addTest(org.carlmanaster.predicate.tests.TestAll.suite());
		suite.addTest(org.carlmanaster.filter.tests.TestAll.suite());
		suite.addTest(org.carlmanaster.allelogram.model.tests.TestAll.suite());
		suite.addTest(org.carlmanaster.allelogram.gui.tests.TestAll.suite());
		return suite;
	}
}
