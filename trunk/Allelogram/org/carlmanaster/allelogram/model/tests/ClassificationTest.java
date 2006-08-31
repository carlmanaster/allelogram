package org.carlmanaster.allelogram.model.tests;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.carlmanaster.allelogram.model.Classification;

public class ClassificationTest extends TestCase {
	public void testCreate() throws Exception {
		String[] columns = new String[]{"a", "b"};
		Classification classification = new Classification(columns, new String[]{"-"});
		assertNotNull(classification);
	}
	public void testEquals() throws Exception {
		Classification c1 = new Classification(new String[]{"a", "b"}, new String[]{"-"});
		Classification c2 = new Classification(new String[]{"a", "b"}, new String[]{"-"});
		assertTrue(c1.equals(c2));
		Classification c3 = new Classification(new String[]{"b", "a"}, new String[]{"-"});
		assertFalse(c1.equals(c3));
	}
	public void testToString() throws Exception {
		Classification c = new Classification(new String[]{"a", "b"}, new String[]{"-"});
		assertEquals("a-b", c.toString());
	}
	
	public void testStringConstructor() throws Exception {
		List<String> columns = Arrays.asList(new String[]{"a", "b", "ab", "c"});
		for (String s : new String[] {"a", "a-b", "b.c", "ab.c"})
			assertEquals(s, new Classification(columns, s).toString());
	}
	
	public void testStringMustBeWellFormed() throws Exception {
		String[] strings = new String[]{"a", "b", "ab"};
		List<String> columns = Arrays.asList(strings);
		
		try {
			new Classification(columns, "Z-b").toString();
			fail();
		} catch (Exception e) {
			assertEquals("Z-b contains names that are not in the list of columns [a, ab, b]", e.getMessage());
		}
	}
}
