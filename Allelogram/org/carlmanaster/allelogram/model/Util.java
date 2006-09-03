package org.carlmanaster.allelogram.model;

public class Util {
    public static String[] splitOnColon(String s) {
		int n = s.indexOf(":");
		String[] terms = new String[2];
		terms[0] = s.substring(0, n).trim();
		terms[1] = s.substring(n + 1).trim();
		return terms;
	}

}
