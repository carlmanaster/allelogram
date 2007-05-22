package org.carlmanaster.filter.tests;

import java.util.ArrayList;
import java.util.Collection;

import org.carlmanaster.filter.Filter;
import org.carlmanaster.predicate.All;
import org.carlmanaster.predicate.Equals;
import org.carlmanaster.predicate.None;

import junit.framework.TestCase;

public class FilterTest extends TestCase {
	Collection<Integer> list;

	protected void setUp() throws Exception {
		list = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++)
			list.add(i);
	}

	public void testAll() throws Exception {
		Filter<Integer> all = Filter.in(new All<Integer>());
		assertEquals(list.size(), all.filtered(list).size());
	}

	public void testNone() throws Exception {
		Filter<Integer> none = Filter.in(new None<Integer>());
		assertEquals(0, none.filtered(list).size());
	}

	public void testOne() throws Exception {
		Filter<Integer> one = Filter.in(new Equals<Integer>(2));
		assertEquals(1, one.filtered(list).size());
	}
	
	public void testTConstructor() throws Exception {
		Filter<Integer> one = Filter.in(3);
		assertEquals(1, one.filtered(list).size());
		one = Filter.in(3);
		assertEquals(1, one.filtered(list).size());
	}
	
	public void testFilterOut() throws Exception {
		Filter<Integer> filter = Filter.out(new All<Integer>());
		assertEquals(0, filter.filtered(list).size());
	}
	
	public void testArray() throws Exception {
		Filter<Integer> filter = Filter.in(new Equals<Integer>(2));
		Integer[] array = new Integer[] {1,2,3,4};
		assertEquals(1, filter.filtered(array).size());
	}
}
