package org.carlmanaster.allelogram.model.tests;

import java.util.ArrayList;

import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.Bin;
import org.carlmanaster.allelogram.model.Genotype;

import junit.framework.TestCase;

public class BinTest extends TestCase {
	private Bin bin;

	protected void setUp() throws Exception {
		bin = new Bin(0.0, 1.0);
	}
	
	public void testContains() throws Exception {
		assertTrue(bin.contains(0.0));
		assertFalse(bin.contains(1.0));
	}

	public void testContainsAllele() throws Exception {
		Genotype g = GenotypeTest.makeGenotype("C", "B", "C", 0.0, 0.3);
		ArrayList<Allele> alleles = g.getAlleles();
		assertTrue(bin.contains(alleles.get(0)));
		assertTrue(bin.contains(alleles.get(1)));
		g.offsetBy(0.9);
		assertTrue(bin.contains(alleles.get(0)));
		assertFalse(bin.contains(alleles.get(1)));
	}
	
	public void testToString() throws Exception {
		assertEquals("(0.0, 1.0)", bin.toString());
	}
}
