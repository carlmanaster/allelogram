package org.carlmanaster.predicate;

import java.util.Collection;

public class Or<T> extends Conjunction<T> {
	public Or(Predicate<T> a, Predicate<T> b) {
		super(a, b);
	}

	public Or(Collection<Predicate<T>> collection) {
		super(collection);
	}

	public boolean passes(T t) {
		for (Predicate<T> test : getTests()) 
			if (test.passes(t))
				return true;
		return false;
	}

}
