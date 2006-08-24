package org.carlmanaster.predicate;

public class Equals<T> extends Predicate<T> {
	private final T t;

	public Equals(T t) {
		this.t = t;
	}

	public boolean passes(T t) {
		return Utility.same(this.t, t);
	}
	
}
