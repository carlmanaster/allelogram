package org.carlmanaster.allelogram.gui;

public class Scale {
	private final double xLo;
	private final double xHi;
	private final double nLo;
	private final double nHi;

	public Scale(double xLo, double xHi, int nLo, int nHi) throws Exception {
		if (xHi == xLo) throw new Exception("Empty range in Scale constructor");
		if (nHi == nLo) throw new Exception("Empty range in Scale constructor");
		this.xLo = xLo;
		this.xHi = xHi;
		this.nLo = nLo;
		this.nHi = nHi;
	}

	public int toScreen(double x)	{return (int) (nLo + ((x - xLo) / xWidth()) * nWidth());}
	public double toData(int n)	{return xLo + ((n - nLo) / nWidth()) * xWidth();}

	private double nWidth() {return nHi - nLo;}
	private double xWidth() {return xHi - xLo;}
}
