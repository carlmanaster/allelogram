package org.carlmanaster.predicate.tests;

import java.util.ArrayList;
import java.util.Collection;

import org.carlmanaster.predicate.Any;
import org.carlmanaster.predicate.Predicate;

import junit.framework.TestCase;

public class AnyTest extends TestCase {
	Collection<Integer> list;

	protected void setUp() throws Exception {
		list = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++)
			list.add(i);
	}

	public void testAny() throws Exception {
		Predicate<Integer> any = new Any<Integer>(list);
		assertTrue(any.passes(0));
		assertFalse(any.passes(5));
		assertFalse(any.passes(null));
		list.add(null);
		assertTrue(any.passes(null));
	}
	
	public void testVarargsConstructor() throws Exception {
		Any<Integer> any = new Any<Integer>(1, 2, 3);
		assertTrue(any.passes(1));
		assertFalse(any.passes(0));
	}
	
}
