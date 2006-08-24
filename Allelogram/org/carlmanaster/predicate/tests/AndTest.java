package org.carlmanaster.predicate.tests;

import java.util.ArrayList;
import java.util.Collection;

import org.carlmanaster.predicate.And;
import org.carlmanaster.predicate.Predicate;
import org.carlmanaster.predicate.string.Contains;

import junit.framework.TestCase;

public class AndTest extends TestCase {
	final Predicate<String> a = new Contains("a");
	final Predicate<String> b = new Contains("b");
	
	public void testAnd() throws Exception {
		Predicate<String> and = new And<String>(a, b);
		assertAnd(and);
	}

	public void testChained() throws Exception {
		Predicate<String> and = a.and(b);
		assertAnd(and);
	}
	
	public void testChainedWithT() throws Exception {
		Predicate<String> and = a.and("ab");
		assertAnd(and);
	}
	
	public void testCollection() throws Exception {
		Collection<Predicate<String>> collection = new ArrayList<Predicate<String>>();
		collection.add(a);
		collection.add(b);
		Predicate<String> and = new And<String>(collection);
		assertAnd(and);
	}
	
	private void assertAnd(Predicate<String> and) {
		assertFalse(and.passes("a"));
		assertFalse(and.passes("b"));
		assertTrue(and.passes("ab"));
	}
	
}
