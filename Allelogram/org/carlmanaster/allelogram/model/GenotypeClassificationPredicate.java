package org.carlmanaster.allelogram.model;

import org.carlmanaster.predicate.Predicate;

public class GenotypeClassificationPredicate extends Predicate<Genotype> {
	private final String[] values;
	private final String[] columns;

	public GenotypeClassificationPredicate(Classifier classification, String[] values) throws Exception {
		if (classification.getColumns().size() != values.length)
			throw new Exception("values must have the same length as classification.");
		this.columns = columnsOf(classification);
		this.values = values;
	}

	public GenotypeClassificationPredicate(Classifier classification, Genotype genotype) {
		this.columns = columnsOf(classification);
		this.values = valuesOf(genotype, columns);
	}

	public GenotypeClassificationPredicate(Classifier classifier, Classification classification) throws Exception {
		this(classifier, classification.getStrings());
	}

	public boolean passes(Genotype genotype) {
		for (int i = 0; i < columns.length; ++i)
			if (!genotype.get(columns[i]).equalsIgnoreCase(values[i]))
				return false;
		return true;
	}

	private static String[] columnsOf(Classifier classification) {
		return classification.getColumns().toArray(new String[classification.getColumns().size()]);
	}
	
	private static String[] valuesOf(Genotype genotype, String[] columns) {
		String[] values = new String[columns.length];
		for (int i = 0; i < columns.length; ++i)
			values[i] = genotype.get(columns[i]);
		return values;
	}
	
}
