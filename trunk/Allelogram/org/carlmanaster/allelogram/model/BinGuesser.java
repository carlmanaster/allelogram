package org.carlmanaster.allelogram.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

public class BinGuesser {
	private final Vector<Double> values = new Vector<Double>();

	public BinGuesser(double[] ds) {
		for (double d : ds) 
			values.add(d);
		Collections.sort(values);
	}

	public BinGuesser(ArrayList<Allele> alleles) {
		for (Allele allele : alleles)
			values.add(allele.getAdjustedValue());
		Collections.sort(values);
	}

	public ArrayList<Bin> guess(double size) {
		if (values.size() == 0)
			return new ArrayList<Bin>();
		
		Vector<Double> mods = modulos(size);
		mods.add(mods.get(0) + size);
		double greatestGap = 0;
		double offset = 0;
		for (int i = 0; i < mods.size() - 1; ++i) {
			double a = mods.get(i);
			double b = mods.get(i + 1);
			double gap = b - a;
			if (gap < greatestGap) continue;
			greatestGap = gap;
			offset = a + gap / 2;
		}
		
		return makeBins(size, offset);
	}

	private ArrayList<Bin> makeBins(double size, double offset) {
		ArrayList<Bin> bins = new ArrayList<Bin>();
		final Double first = values.get(0);
		final Double last = values.get(values.size() - 1);
		double x = Math.round(first / size - 1) * size + offset;
		while (x < last) {
			bins.add(new Bin(x, x + size));
			x += size;
		}
		return bins;
	}

	private Vector<Double> modulos(double size) {
		Vector<Double> mods = new Vector<Double>();
		for (Double value : values) 
			mods.add(modulo(value, size));
		Collections.sort(mods);
		return mods;
	}

	public static double modulo(double a, double b) {
		if (b == 0)
			return 0;
		double x = Math.floor(a / b);
		return a - x * b;
	}

}
