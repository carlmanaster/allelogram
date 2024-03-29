package org.carlmanaster.allelogram.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Genotype {
	private final ArrayList<Allele> alleles = new ArrayList<Allele>();
	private final HashMap<String, String> fields = new HashMap<String, String>();
	private double offset;
	private boolean excluded;

	public Genotype(double[] alleles) throws Exception {
		if (alleles == null)		throw new Exception("alleles must not be null.");
		if (alleles.length < 2)	throw new Exception("there must be at least 2 alleles.");
		
		Arrays.sort(alleles);
		
		for (int i = 0; i < alleles.length; ++i)
			this.alleles.add(new Allele(this, alleles[i]));
	}

	public Genotype(double[] alleles, String[] keys, String[] values) throws Exception {
		this(alleles);
		for (int i = 0; i < keys.length; ++i)
			fields.put(keys[i], values[i]);
	}
	
	public Genotype(double[] alleles, List<String> keys, String[] values) throws Exception{
		this(alleles, keys.toArray(new String[keys.size()]), values);
	}

	public ArrayList<Allele> getAlleles() {
		return alleles;
	}
	
	public boolean isHomozygous() {
		HashSet<Double> set = new HashSet<Double>();
		List<Double> values = getRawAlleleValues(2);
		for (Double value : values)
			set.add(value);
		return set.size() == 1;
	}

	public String get(String field) {
		return fields.get(field);
	}

	public List<Double> getRawAlleleValues(int requiredSize) {
		ArrayList<Double> result = new ArrayList<Double>();
		for (Allele allele : alleles)
			result.add(allele.getRawValue());
		
		Collections.sort(result);
		return result;
	}

	public List<Double> getAdjustedAlleleValues(int requiredSize) {
		List<Double> result = new ArrayList<Double>();
		for (Allele allele : alleles)
			result.add(allele.getAdjustedValue());
		return result;
	}

	public void offsetBy(double offset) {
		this.offset = offset;
	}

	public void clearOffset() {
		offsetBy(0);
	}

	public double adjusted(double value) {
		return value + offset;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (String field : fields.keySet())
			sb.append(field + ":" + get(field) + ", ");
		
		sb.append(':');

		for (Allele allele : alleles) 
			sb.append(String.format("%1.1f, ", allele.getRawValue()));
			
		return sb.toString();
	}

	public double getOffset() {
		return offset;
	}

	public boolean isExcluded() {
		return excluded;
	}

	public void setExcluded(boolean excluded) {
		this.excluded = excluded; 
		if (excluded)
			fields.put("Use", "FALSE");
		else
			fields.put("Use", "TRUE");				
	}

}
