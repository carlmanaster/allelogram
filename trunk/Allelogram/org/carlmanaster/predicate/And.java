package org.carlmanaster.predicate;

import java.util.Collection;

public class And<T> extends Conjunction<T> {
	
	public And(Predicate<T> a, Predicate<T> b) {
		super(a, b);
	}

	public And(Collection<Predicate<T>> collection) {
		super(collection);
	}

	public boolean passes(T t) {
		for (Predicate<T> test : getTests()) 
			if (!test.passes(t))
				return false;
		return true;
	}

}
