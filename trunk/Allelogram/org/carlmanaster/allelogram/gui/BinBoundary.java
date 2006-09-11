package org.carlmanaster.allelogram.gui;

import org.carlmanaster.allelogram.model.Bin;

public class BinBoundary {
	private final static Bin noBin = new Bin(0, 0);
	private final Bin above;
	private final Bin below;
	private final int v;

	public int getV() {
		return v;
	}

	public BinBoundary(Bin above, Bin below, int v) {
		this.above = above == null ? noBin : above;
		this.below = below == null ? noBin : below;
		this.v = v;
	}

	public void set(double d) {
		above.setHigh(d);
		below.setLow(d);
	}

}
