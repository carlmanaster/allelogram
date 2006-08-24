package org.carlmanaster.predicate;


public class Not<T> extends Predicate<T> {
	private final Predicate<T> that;

	public Not(Predicate<T> that) {
		this.that = that;
	}

	public boolean passes(T t) {
		return !that.passes(t);
	}

}
