package org.carlmanaster.allelogram.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

public class Classification {
	private final ArrayList<String> columns	= new ArrayList<String>();
	private final ArrayList<String> delimiters	= new ArrayList<String>();

	public Classification(String[] columns, String[] delimiters) {
		for (String column : columns)
			this.columns.add(column);
		for (String delimiter : delimiters)
			this.delimiters.add(delimiter);
		for (int i = 0; i < columns.length - delimiters.length; ++i)
			this.delimiters.add("");
	}
	
	public Classification(String[] columns) {
		this(columns, new String[0]);
	}

	/*
	 * Build the classification from a string.  The string
	 * must have the format <column><delimiter>...<column>,
	 * and delimiters are exactly one character long.
	 */
	public Classification(Collection<String> columns, String string) throws Exception {
		ArrayList<String> list = new ArrayList<String>(columns);
		Collections.sort(list);
		Collections.reverse(list);
		String s = string;
		while (s.length() > 0) {
			boolean changed = false;
			for (String column : list) {
				if (!s.startsWith(column))
					continue;
				changed = true;
				this.columns.add(column);
				s = s.substring(column.length()); 
				if (s.length() > 0) {
					this.delimiters.add(s.substring(0, 1));
					s = s.substring(1);
				}
			}
			if (!changed) {
				Collections.reverse(list);
				throw new Exception(string + " contains names that are not in the list of columns " + list.toString());
			}
		}
		this.delimiters.add("");
	}

	public ArrayList<String> getColumns()	{return columns;}

	public boolean equals(Object obj) {
		if (obj == null)							return false;
		if (obj == this)							return true;
		if (!(obj instanceof Classification))		return false;
		Classification that = (Classification) obj;
		if (!this.columns.equals(that.columns))	return false;
		return true;
	}
	
	public int hashCode() {
		return columns.hashCode();
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (int i = 0; i < columns.size(); ++i) {
			sb.append(columns.get(i));
			sb.append(delimiters.get(i));
		}
		
		return sb.toString();
	}

	public String[] parse(String string) {	
		String s = string.trim();
		String[] result = new String[columns.size()];
		for (int i = 0; i < delimiters.size() - 1; ++i) {
			int n = s.indexOf(delimiters.get(i));
			result[i] = s.substring(0, n);
			s = s.substring(n + 1);
		}
		result[result.length - 1] = s;
		return result;
	}

	public Vector<String> classify(Genotype genotype) {
		Vector<String> result = new Vector<String>();
		for (String column : columns) 
			result.add(genotype.get(column));
		return result;
	}

	public String string(Genotype genotype) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < columns.size(); ++i) {
			sb.append(genotype.get(columns.get(i)));
			sb.append(delimiters.get(i));
		}
		return sb.toString();
	}
}
