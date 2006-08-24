package org.carlmanaster.predicate.string;

import org.carlmanaster.predicate.Predicate;

public class Contains extends Predicate<String> {
	private final String substring;

	public Contains(String substring) {
		this.substring = substring == null ? null : substring.toLowerCase();
	}

	public boolean passes(String s) {
		if (s == null || substring == null)
			return false;
		return s.toLowerCase().contains(substring);
	}

}
