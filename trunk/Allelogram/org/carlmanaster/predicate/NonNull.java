package org.carlmanaster.predicate;

public class NonNull<T> extends Predicate<T> {
	public boolean passes(T t) {
		return t != null;
	}

}
