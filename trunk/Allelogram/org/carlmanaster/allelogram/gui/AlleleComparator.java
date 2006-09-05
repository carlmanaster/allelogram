package org.carlmanaster.allelogram.gui;

import java.util.Comparator;

import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.GenotypeComparator;

public class AlleleComparator implements Comparator<Allele> {
	private final GenotypeComparator genotypeComparator;

	public AlleleComparator(GenotypeComparator comparator) {
		this.genotypeComparator = comparator;
	}

	public int compare(Allele a, Allele b) {
		int i = compareGenotypes(a, b);
		if (i != 0)
			return i;
		return compareAlleleValues(a, b);
	}

	private int compareGenotypes(Allele a, Allele b) {
		if (genotypeComparator == null)
			return 0;
		return genotypeComparator.compare(a.getGenotype(), b.getGenotype());
	}

	private int compareAlleleValues(Allele a, Allele b) {
		return Double.compare(a.getAdjustedValue(), b.getAdjustedValue());
	}
}
