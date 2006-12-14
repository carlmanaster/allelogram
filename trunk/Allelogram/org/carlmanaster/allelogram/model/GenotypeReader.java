package org.carlmanaster.allelogram.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.carlmanaster.allelogram.util.FileUtil;

public class GenotypeReader {
	private final List<String> columns;
	private final Integer[] alleleIndexes;
	
	public GenotypeReader(Settings settings) {
		columns = settings.getColumns();
		alleleIndexes = settings.getAlleleIndexes();
	}

	public List<Genotype> readGenotypes(File file) throws IOException {
		return readGenotypes(FileUtil.makeReader(file));
	}

	public List<Genotype> readGenotypes(BufferedReader reader) throws IOException {
		ArrayList<Genotype> genotypes = new ArrayList<Genotype>();
		while (reader.ready()) {
			Genotype genotype = makeGenotype(reader.readLine());
			if (genotype != null) 
				genotypes.add(genotype);
		}
		reader.close();
		return genotypes;
	}

	/**
	 * If the first allele is unreadable, something is wrong with the line;
	 * this should happen in files that include a header row, for instance.
	 * Subsequent alleles may be blank; these indicate a homozygote and for
	 * our purposes they should acquire the value of the first allele.
	 */
	private Genotype makeGenotype(String line) {
		String[] split = line.split("\t");
		String[] items = new String[columns.size()];
		Arrays.fill(items, "");
		for (int i = 0; i < split.length; ++i)
			items[i] = split[i];
		double[] alleles = new double[alleleIndexes.length];
		
		alleles[0] = parseDouble(items[alleleIndexes[0]], -1.0);
		if (alleles[0] < 0)
			return null;
		
		for (int i = 1; i < alleleIndexes.length; ++i)
			alleles[i] = parseDouble(items[alleleIndexes[i]], alleles[0]);
		
		try {
			return new Genotype(alleles, columns, items);
		} catch (Exception e) {
			return null;
		}
	}
	
	private static double parseDouble(String s, double defaultValue) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

}
