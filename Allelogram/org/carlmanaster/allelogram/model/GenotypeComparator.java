package org.carlmanaster.allelogram.model;

import java.util.ArrayList;
import java.util.Comparator;

public class GenotypeComparator implements Comparator<Genotype> {
	private final Classifier classification;

	public GenotypeComparator(Classifier classification) {
		this.classification = classification;
	}

	public int compare(Genotype g1, Genotype g2) {
		ArrayList<String> columns = classification.getColumns();
		for (String column : columns) {
			int result = g1.get(column).compareTo(g2.get(column));
			if (result != 0)
				return result;
		}
		
		return 0;
	}

}
