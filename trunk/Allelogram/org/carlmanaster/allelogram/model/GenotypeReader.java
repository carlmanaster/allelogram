package org.carlmanaster.allelogram.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.carlmanaster.allelogram.util.FileUtil;

public class GenotypeReader {
	private final Vector<String> columns;
	private final Integer[] alleleIndexes;
	
	public GenotypeReader(Settings settings) {
		columns = settings.getColumns();
		alleleIndexes = settings.getAlleleIndexes();
	}

	public ArrayList<Genotype> readGenotypes(File file) throws IOException, Exception {
		BufferedReader reader = FileUtil.makeReader(file);
		ArrayList<Genotype> genotypes = new ArrayList<Genotype>();
		while (reader.ready()) {
			Genotype g = makeGenotype(reader.readLine());
			if (g != null) 
				genotypes.add(g);
		}
		reader.close();
		return genotypes;
	}

	private Genotype makeGenotype(String line) throws Exception {
		String[] split = line.split("\t");
		String[] items = new String[columns.size()];
		Arrays.fill(items, "");
		for (int i = 0; i < split.length; ++i)
			items[i] = split[i];
		double[] alleles = new double[alleleIndexes.length];
		
		alleles[0] = parseDouble(items[alleleIndexes[0]], -1.0);
		if (alleles[0] < 0)
			return null;
		
		for (int i = 1; i < alleleIndexes.length; ++i) {
			String s = alleleIndexes[i] >= items.length ? "" : items[alleleIndexes[i]];
			alleles[i] = parseDouble(s, alleles[0]);
		}
		
		return new Genotype(alleles, columns, items);
	}
	
	private static double parseDouble(String s, double defaultValue) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

}
