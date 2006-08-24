package org.carlmanaster.predicate.tests;

import org.carlmanaster.predicate.Equals;
import org.carlmanaster.predicate.Not;
import org.carlmanaster.predicate.Predicate;

import junit.framework.TestCase;

public class NotTest extends TestCase {
	final Predicate<String> a = new Equals<String>("a");

	public void testNot() throws Exception {
		Predicate<String> not = new Not<String>(a);
		assertNot(not);
	}
	
	public void testInverse() throws Exception {
		Predicate<String> not = a.inverse();
		assertNot(not);
		not = Predicate.not("a");
		assertNot(not);
	}

	private void assertNot(Predicate<String> not) {
		assertFalse(not.passes("a"));
		assertTrue(not.passes("b"));
		assertTrue(not.passes(null));
	}
}
