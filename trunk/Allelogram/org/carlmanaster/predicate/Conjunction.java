package org.carlmanaster.predicate;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Conjunction<T> extends Predicate<T> {
	private final ArrayList<Predicate<T>> tests = new ArrayList<Predicate<T>>();
	
	protected Conjunction(Predicate<T> a, Predicate<T> b) {
		tests.add(a);
		tests.add(b);
	}

	protected Conjunction(Collection<Predicate<T>> collection) {
		tests.addAll(collection);
	}

	protected ArrayList<Predicate<T>> getTests() {
		return tests;
	}

}
