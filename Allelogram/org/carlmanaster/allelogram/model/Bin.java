package org.carlmanaster.allelogram.model;

public class Bin {
	private final Double lo;
	private final Double hi;

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
	
	public boolean equals(Object obj) {
		if (obj == null)				return false;
		if (obj == this)				return true;
		if (!(obj instanceof Bin))	return false;
		Bin that = (Bin) obj;
		if (!this.lo.equals(that.lo))	return false;
		if (!this.hi.equals(that.hi))	return false;
		return true;
	}
	
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + lo.hashCode();
		hash = hash * 31 + hi.hashCode();
		return hash;
	}

	public String toString() {
		return String.format("(%1.1f, %1.1f)", lo, hi);
	}

	public double getLow()	{return lo;}
	public double getHigh()	{return hi;}
}
