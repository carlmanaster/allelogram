package org.carlmanaster.predicate.tests;

import java.util.AbstractCollection;
import java.util.ArrayList;

import org.carlmanaster.predicate.Equals;
import org.carlmanaster.predicate.Or;
import org.carlmanaster.predicate.Predicate;

import junit.framework.TestCase;

public class OrTest extends TestCase {
	final Predicate<String> a = new Equals<String>("a");
	final Predicate<String> b = new Equals<String>("b");
	
	public void testOr() throws Exception {
		Predicate<String> or = new Or<String>(a, b);
		assertOr(or);
	}
	
	public void testChained() throws Exception {
		Predicate<String> or = a.or(b);
		assertOr(or);
	}
	
	public void testChainedWithT() throws Exception {
		Predicate<String> or = a.or("b");
		assertOr(or);
	}
	
	public void testCollection() throws Exception {
		AbstractCollection<Predicate<String>> collection = new ArrayList<Predicate<String>>();
		collection.add(a);
		collection.add(b);
		Predicate<String> or = new Or<String>(collection);
		assertOr(or);
	}

	private void assertOr(Predicate<String> or) {
		assertTrue(or.passes("a"));
		assertTrue(or.passes("b"));
		assertFalse(or.passes("c"));
	}
	
}
