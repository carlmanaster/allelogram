package org.carlmanaster.allelogram.model;

public class Allele {
	private final Genotype genotype;
	private final Double value;
	public Allele(Genotype genotype, double value) {
		this.genotype = genotype;
		this.value = value;
	}
	
	public double getRawValue()			{return value;}
	public double getAdjustedValue()	{return genotype.adjusted(value);}
	public double getOffset()			{return genotype.getOffset();}

	public String toString() {
		return String.format("%2.2f", getAdjustedValue());
	}

	public Genotype getGenotype() {return genotype;}

}
