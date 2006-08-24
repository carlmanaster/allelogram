package org.carlmanaster.predicate;

import java.util.HashSet;

public class Unique<T> extends Predicate<T> {
	final HashSet<T> set = new HashSet<T>();
	
	public void reset() {set.clear();}

	public boolean passes(T t) {
		if (set.contains(t))
			return false;
		set.add(t);
		return true;
	}
}
