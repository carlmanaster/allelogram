package org.carlmanaster.predicate;

class Utility {
	public static <T> boolean same(T a, T b) {
		return a == null ? b == null : a.equals(b);
	}
}
