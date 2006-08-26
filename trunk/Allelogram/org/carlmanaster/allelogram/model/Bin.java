package org.carlmanaster.allelogram.model;

public class Bin {
	private final double lo;
	private final double hi;

	public Bin(double lo, double hi) {
		this.lo = lo;
		this.hi = hi;
	}

	public boolean contains(double x) {
		if (x < lo)	return false;
		if (x >= hi)	return false;
		return true;
	}

	public boolean contains(Allele allele) {
		return contains(allele.getAdjustedValue());
	}

}
