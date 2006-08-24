package org.carlmanaster.predicate.tests;

import org.carlmanaster.predicate.NonNull;
import org.carlmanaster.predicate.Predicate;

import junit.framework.TestCase;

public class NonNullTest extends TestCase {
	public void testNonNull() throws Exception {
		Predicate<Integer> nonnull = new NonNull<Integer>();
		assertTrue(nonnull.passes(3));
		assertFalse(nonnull.passes(null));
	}
}
