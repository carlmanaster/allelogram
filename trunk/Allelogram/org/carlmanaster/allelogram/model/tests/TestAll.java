package org.carlmanaster.allelogram.model.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestAll extends TestSuite{
	public static Test suite() {
		TestSuite suite = new TestSuite();
		
		suite.addTestSuite(GenotypeTest.class);
		suite.addTestSuite(ClassificationTest.class);
		suite.addTestSuite(GenotypeComparatorTest.class);
		suite.addTestSuite(GenotypeClassificationPredicateTest.class);
		suite.addTestSuite(BinTest.class);
		
		return suite;
	}
}
