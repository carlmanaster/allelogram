package org.carlmanaster.allelogram.model;

public class Allele {
	private final Genotype genotype;
	private final Double value;
	private final Integer index;

	public Allele(Genotype genotype, double value, int index) {
		this.genotype = genotype;
		this.value = value;
		this.index = index;
	}
	
	public boolean equals(Object obj) {
		if (obj == null)				return false;
		if (obj == this)				return true;
		if (!(obj instanceof Allele)) return false;
		Allele that = (Allele) obj;
		if (!this.value.equals(that.value))	return false;
		if (!this.index.equals(that.index))	return false;
		return true;
	}
	
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + genotype.hashCode();
		hash = hash * 31 + value.hashCode();
		hash = hash * 31 + index.hashCode();
		return hash;
	}

	public double getRawValue() {return value;}
	public double getAdjustedValue() {return genotype.adjusted(value);}

	public String toString() {
		return String.format("%2.2f", getAdjustedValue());
	}
}
