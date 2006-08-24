package org.carlmanaster.allelogram.model.tests;

import org.carlmanaster.allelogram.model.Classification;

import junit.framework.TestCase;

public class ClassificationTest extends TestCase {
	public void testCreate() throws Exception {
		String[] columns = new String[]{"a", "b"};
		Classification classification = new Classification("name", columns);
		assertNotNull(classification);
		assertEquals("name", classification.getName());
	}
	public void testEquals() throws Exception {
		Classification c1 = new Classification("name", new String[]{"a", "b"});
		Classification c2 = new Classification("name", new String[]{"a", "b"});
		assertTrue(c1.equals(c2));
		Classification c3 = new Classification("x", new String[]{"a", "b"});
		assertFalse(c1.equals(c3));
		Classification c4 = new Classification("x", new String[]{"b", "a"});
		assertFalse(c1.equals(c4));
	}
}
