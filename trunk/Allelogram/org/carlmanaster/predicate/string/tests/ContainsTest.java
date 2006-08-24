package org.carlmanaster.predicate.string.tests;

import org.carlmanaster.predicate.string.Contains;

import junit.framework.TestCase;

public class ContainsTest extends TestCase {
	public void testContains() throws Exception {
		Contains test = new Contains("a");
		assertTrue(test.passes("bab"));
		assertFalse(test.passes(""));
		assertFalse(test.passes("bb"));
		assertTrue(test.passes("BAB"));
		assertFalse(test.passes(null));
		Contains nullTest = new Contains(null);
		assertFalse(nullTest.passes(""));
		assertFalse(nullTest.passes(null));
	}

}
