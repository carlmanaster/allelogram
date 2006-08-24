package org.carlmanaster.allelogram.model;

public class Allele {
	private final Genotype genotype;
	private final double value;
	private final int index;

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
		if (this.value != that.value)	return false;
		if (this.index != that.index)	return false;
		return true;
	}

	public double getRawValue() {return value;}
	public double getAdjustedValue() {return genotype.adjusted(value);}

}
