package org.carlmanaster.predicate.tests;

import org.carlmanaster.predicate.Predicate;
import org.carlmanaster.predicate.Unique;

import junit.framework.TestCase;

public class UniqueTest extends TestCase {
	public void testUnique() throws Exception {
		Predicate<Integer> unique = new Unique<Integer>();
		assertTrue(unique.passes(1));
		assertTrue(unique.passes(null));
		assertFalse(unique.passes(1));
		assertFalse(unique.passes(null));
	}

	public void testUniqueFromPredicate() throws Exception {
		Predicate<Integer> unique = Predicate.unique();
		assertTrue(unique.passes(1));
		assertTrue(unique.passes(null));
		assertFalse(unique.passes(1));
		assertFalse(unique.passes(null));
	}

	public void testReset() throws Exception {
		Unique<Integer> unique = Predicate.unique();
		assertTrue(unique.passes(1));
		assertTrue(unique.passes(null));
		unique.reset();
		assertTrue(unique.passes(1));
		assertTrue(unique.passes(null));
	}

	public void testNonUnique() throws Exception {
		Predicate<Integer> duplicates = new Unique<Integer>().inverse();
		assertFalse(duplicates.passes(1));
		assertFalse(duplicates.passes(null));
		assertTrue(duplicates.passes(1));
		assertTrue(duplicates.passes(null));
	}

}
