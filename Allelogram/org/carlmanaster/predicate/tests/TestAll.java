package org.carlmanaster.predicate.tests;

import org.carlmanaster.predicate.string.tests.ContainsTest;

import junit.framework.TestSuite;

public class TestAll extends TestSuite{
	public static TestSuite suite() {
		TestAll suite = new TestAll();
		suite.addTestSuite(ContainsTest.class);
		suite.addTestSuite(PredicateTest.class);
		suite.addTestSuite(OrTest.class);
		suite.addTestSuite(AndTest.class);
		suite.addTestSuite(NotTest.class);
		suite.addTestSuite(AnyTest.class);
		suite.addTestSuite(NonNullTest.class);
		suite.addTestSuite(UniqueTest.class);
		return suite;
	}
}
