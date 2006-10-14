package org.carlmanaster.allelogram.model.tests;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.Bin;
import org.carlmanaster.allelogram.model.BinGuesser;

public class BinGuesserTest extends TestCase {
	public void testEmpty() throws Exception {
		double[] d = new double[0];
		BinGuesser guesser = new BinGuesser(d);
		List<Bin> bins = guesser.guess(2);
		assertEquals(0, bins.size());
	}
	
	public void testAlleleConstructor() throws Exception {
		ArrayList<Allele> alleles = new ArrayList<Allele>();
		BinGuesser guesser = new BinGuesser(alleles);
		List<Bin> bins = guesser.guess(2);
		assertEquals(0, bins.size());
	}

	public void testOneBin() throws Exception {
		double[] d = new double[] {10};
		BinGuesser guesser = new BinGuesser(d);
		List<Bin> bins = guesser.guess(2);
		assertEquals(1, bins.size());
		assertEquals(new Bin(9, 11), bins.get(0));
	}
	
	public void testModulo() throws Exception {
		assertEquals(1.3, BinGuesser.modulo(5.3, 2.0), 0.01);
		assertEquals(0.0, BinGuesser.modulo(5.3, 0.0), 0.01);
		assertEquals(0.0, BinGuesser.modulo(6.0, 2.0), 0.01);
	}
	
	public void testTwoBins() throws Exception {
		double[] d = new double[] {10, 12};
		BinGuesser guesser = new BinGuesser(d);
		List<Bin> bins = guesser.guess(2);
		assertEquals(2, bins.size());
		assertEquals(new Bin(9, 11), bins.get(0));
		assertEquals(new Bin(11, 13), bins.get(1));
	}

	public void testThreeBins() throws Exception {
		double[] d = new double[] {10, 14};
		BinGuesser guesser = new BinGuesser(d);
		List<Bin> bins = guesser.guess(2);
		assertEquals(3, bins.size());
		assertEquals(new Bin(9, 11), bins.get(0));
		assertEquals(new Bin(11, 13), bins.get(1));
		assertEquals(new Bin(13, 15), bins.get(2));
	}
	
	public void testAllBinsAreCreated() throws Exception {
		// because of a bug detected in testQuality()
		double[] d = new double[]{10.1, 14.2, 13.9, 18.6};
		BinGuesser guesser = new BinGuesser(d);
		List<Bin> bins = guesser.guess(4);

		assertEquals(3, bins.size());
	}
	
	public void testQuality() throws Exception {
		double[] d = new double[]{10.1, 14.2, 13.9, 18.6};
		BinGuesser guesser = new BinGuesser(d);
		double quality2 = guesser.quality(guesser.guess(2));
		double quality3 = guesser.quality(guesser.guess(3));
		double quality4 = guesser.quality(guesser.guess(4));
		assertTrue(quality4 < quality2);
		assertTrue(quality4 < quality3);
		
		assertEquals(guesser.guess(4), guesser.bestGuess());
	}
}
