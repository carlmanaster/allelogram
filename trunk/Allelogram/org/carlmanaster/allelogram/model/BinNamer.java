package org.carlmanaster.allelogram.model;

public abstract class BinNamer {
	public final static BinNamer sequential = new Sequential();
	public final static BinNamer alphabetic = new Alphabetic();
	public final static BinNamer size		= new Size();

	public abstract String getName(Bin bin, int index);

	private static class Sequential extends BinNamer {
		public String getName(Bin bin, int index) {
			return Integer.toString(index + 1);
		}
	}

	private static class Alphabetic extends BinNamer {
		public String getName(Bin bin, int index) {
			if (index > 25)
				return sequential.getName(bin, index);
			Character c = (char) ('A' + index);
			return c.toString();
		}
	}

	private static class Size extends BinNamer {
		public String getName(Bin bin, int index) {
			double d = (bin.getLow() + bin.getHigh()) / 2;
			return Long.toString(Math.round(d));
		}
	}

}
