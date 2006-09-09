package org.carlmanaster.allelogram.model.tests;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.carlmanaster.allelogram.model.Classifier;

public class ClassifierTest extends TestCase {
	public void testCreate() throws Exception {
		String[] columns = new String[]{"a", "b"};
		Classifier classification = new Classifier(columns, new String[]{"-"});
		assertNotNull(classification);
	}
	public void testEquals() throws Exception {
		Classifier c1 = new Classifier(new String[]{"a", "b"}, new String[]{"-"});
		Classifier c2 = new Classifier(new String[]{"a", "b"}, new String[]{"-"});
		assertTrue(c1.equals(c2));
		Classifier c3 = new Classifier(new String[]{"b", "a"}, new String[]{"-"});
		assertFalse(c1.equals(c3));
	}
	public void testToString() throws Exception {
		Classifier c = new Classifier(new String[]{"a", "b"}, new String[]{"-"});
		assertEquals("a-b", c.toString());
	}
	
	public void testStringConstructor() throws Exception {
		List<String> columns = Arrays.asList(new String[]{"a", "b", "ab", "c"});
		for (String s : new String[] {"a", "a-b", "b.c", "ab.c"})
			assertEquals(s, new Classifier(columns, s).toString());
	}
	
	public void testStringMustBeWellFormed() throws Exception {
		String[] strings = new String[]{"a", "b", "ab"};
		List<String> columns = Arrays.asList(strings);
		
		try {
			new Classifier(columns, "Z-b").toString();
			fail();
		} catch (Exception e) {
			assertEquals("Z-b contains names that are not in the list of columns [a, ab, b]", e.getMessage());
		}
	}
}
