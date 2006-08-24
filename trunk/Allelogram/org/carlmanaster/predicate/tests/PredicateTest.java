package org.carlmanaster.predicate.tests;

import org.carlmanaster.predicate.Equals;
import org.carlmanaster.predicate.Predicate;

import junit.framework.TestCase;

public class PredicateTest extends TestCase {
	public void testNone() throws Exception {
		Predicate<Integer> none = Predicate.none();
		assertFalse(none.passes(3));
		assertFalse(none.passes(null));
	}
	public void testAll() throws Exception {
		Predicate<Integer> all = Predicate.all();
		assertTrue(all.passes(3));
		assertTrue(all.passes(null));
	}
	public void testEquals() throws Exception {
		Predicate<Integer> equals = new Equals<Integer>(new Integer(3));
		assertTrue(equals.passes(3));
		assertFalse(equals.passes(null));
		assertFalse(equals.passes(1));
		Predicate<Integer> equalsNull = new Equals<Integer>(null);
		assertFalse(equalsNull.passes(3));
		assertTrue(equalsNull.passes(null));
	}
	public void testNonNull() throws Exception {
		Predicate<Integer> nonnull = Predicate.nonNull();
		assertTrue(nonnull.passes(3));
		assertFalse(nonnull.passes(null));
	}
}
