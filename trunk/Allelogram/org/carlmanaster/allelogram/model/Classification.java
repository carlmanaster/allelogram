package org.carlmanaster.allelogram.model;

import java.util.Vector;

public class Classification extends Vector<String> {
	private final Vector<String> strings;

	public Classification(Vector<String> strings) {
		this.strings = strings;
	}
	
	public synchronized boolean equals(Object obj) {
		if (obj == null)						return false;
		if (obj == this)						return true;
		if (!(obj instanceof Classification))	return false;
		Classification that = (Classification) obj;
		
		return this.strings.equals(that.strings);
	}

	public synchronized int hashCode() {
		return strings.hashCode();
	}

	public String[] getStrings() {
		return strings.toArray(new String[strings.size()]);
	}
}
