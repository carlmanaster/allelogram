package org.carlmanaster.allelogram.gui;

public class Scale {
	private final double xLo;
	private final double xHi;
	private final double nLo;
	private final double nHi;

	public Scale(double xLo, double xHi, int nLo, int nHi) throws IllegalArgumentException{
		if (xHi == xLo) throw new IllegalArgumentException(String.format("Empty X range in Scale constructor (%s - %s)",xLo, xHi));
		if (nHi == nLo) throw new IllegalArgumentException(String.format("Empty N range in Scale constructor (%s - %s)",nLo, nHi));
		this.xLo = xLo;
		this.xHi = xHi;
		this.nLo = nLo;
		this.nHi = nHi;
	}

	public int toScreen(double x)	{return (int) (nLo + ((x - xLo) / xWidth()) * nWidth());}
	public double toData(int n)	{return xLo + ((n - nLo) / nWidth()) * xWidth();}

	private double nWidth() {return nHi - nLo;}
	private double xWidth() {return xHi - xLo;}
	
	public String toString() {
		return String.format("%2.2f, %2.2f : %2.0f, %2.0f", xLo, xHi, nLo, nHi);
	}
}
