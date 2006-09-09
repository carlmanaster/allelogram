package org.carlmanaster.allelogram.model.tests;

import java.util.Vector;

import org.carlmanaster.allelogram.model.Classification;

import junit.framework.TestCase;

public class ClassificationTest extends TestCase {
	public void testEquals() throws Exception {
		Vector<String> v = new Vector<String>();
		v.add("a");
		v.add("b");
		Classification a = new Classification(v);
		Vector<String> v2 = new Vector<String>();
		v2.add("a");
		v2.add("b");
		v2.add("c");
		Classification b = new Classification(v2);
		assertFalse(a.equals(b));
		v2.remove("c");
		assertTrue(a.equals(b));
	}
}
