package org.carlmanaster.allelogram.model.tests;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.carlmanaster.allelogram.model.CircularList;

public class CircularListTest extends TestCase {

	public void testRotaryList() throws Exception {
		Integer[] a = new Integer[]{0,1,2,3,4};
		List<Integer> b = Arrays.asList(a);
		List<Integer> list = new CircularList<Integer>(b);
		Integer zero = 0;
		assertEquals(zero, list.get(0));
		assertEquals(zero, list.get(5));
		Integer four = 4;
		assertEquals(four, list.get(4));
		assertEquals(four,list.get(24));
	}
}
