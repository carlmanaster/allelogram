package org.carlmanaster.predicate;

public class None<T> extends Predicate<T> {
	public boolean passes(T t) {return false;}
}
