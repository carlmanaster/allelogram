package org.carlmanaster.predicate;

import java.util.Collection;
import java.util.HashSet;

public class Any<T> extends Predicate<T> {
	private final Collection<T> these;

	public Any(Collection<T> these) {
		this.these = these;
	}

	public Any(T... ts) {
		this.these = new HashSet<T>();
		for (T t : ts) 
			these.add(t);
	}

	public boolean passes(T b) {
		for (T a : these)
			if (Utility.same(a, b))
				return true;
		return false;
	}

}
