package org.carlmanaster.tests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.BinGuesser;
import org.carlmanaster.allelogram.model.Genotype;
import org.carlmanaster.allelogram.model.GenotypeReader;
import org.carlmanaster.allelogram.model.Settings;
import org.carlmanaster.filter.Filter;
import org.carlmanaster.predicate.Predicate;

public class Regressions extends TestCase {
	
	// a smaller unit test that reveals the problem exposed by the not_binnable file:
	public void testBoundaryBefore() throws Exception {
		assertEquals(191.89, BinGuesser.boundaryBefore(3.0, 2.89, 194.11));
	}
	
	public void testGuessBins() throws Exception {
		Settings settings = readSettingsFromResource("data/vic_file_format.txt");
		List<Genotype> genotypes = readGenotypesFromResource("data/not_binnable.txt", settings);
		assertEquals(96, genotypes.size());	// sanity check.
		
		List<Allele> alleles = getAlleles(genotypes);
		BinGuesser guesser = new BinGuesser(alleles);
		assertEquals(7, guesser.guess(2).size());
		assertEquals(5, guesser.guess(3).size());
		assertEquals(4, guesser.guess(4).size());
		assertEquals(4, guesser.guess(5).size());
		assertEquals(7, guesser.bestGuess().size());
	}

	private Settings readSettingsFromResource(String path) throws Exception {
		BufferedReader reader = getResourceReader(path);
		return new Settings(reader);
	}

	private List<Genotype> readGenotypesFromResource(String path, Settings settings) throws Exception {
		BufferedReader reader = getResourceReader(path);
		return new GenotypeReader(settings).readGenotypes(reader);
	}
	
	private BufferedReader getResourceReader(String path) {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		return reader;
	}
	
	
	// stuff borrowed from AllelogramApplet; this should probably be moved somewhere
	// that is more broadly available.
	
	private static List<Allele> getAlleles(List<Genotype> genotypes) {
		List<Allele> alleles = new ArrayList<Allele>();
		for (Genotype genotype : genotypes)
			alleles.addAll(getGenotypeAlleles(genotype));
		return alleles;
	}

	private static List<Allele> getGenotypeAlleles(Genotype genotype) {
		return Filter.out(new Zero()).filtered(genotype.getAlleles());
	}

	private static class Zero extends Predicate<Allele> {
		public boolean passes(Allele allele) {
			return allele.getRawValue() == 0;
		}
	}

}
