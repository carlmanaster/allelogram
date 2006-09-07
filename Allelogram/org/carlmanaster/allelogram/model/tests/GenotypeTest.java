package org.carlmanaster.allelogram.model.tests;

import java.util.List;

import org.carlmanaster.allelogram.model.Genotype;

import junit.framework.TestCase;

public class GenotypeTest extends TestCase {
	public void testCreate() throws Exception {
		Genotype g = new Genotype(new double[]{0,0});
		assertNotNull(g);
	}
	public void testHomozygous() throws Exception {
		assertTrue(new Genotype(new double[]{0,0}).isHomozygous());
		assertFalse(new Genotype(new double[]{0,1}).isHomozygous());
	}
	public void testRequireAtLeastTwoAlleles() throws Exception {
		try {
			new Genotype(null); fail();
		} catch (Exception e) {
		}
		try {
			new Genotype(new double[]{}); fail();
		} catch (Exception e) {
		}
		try {
			new Genotype(new double[]{0}); fail();
		} catch (Exception e) {
		}
	}
	public void testCreateWithFields() throws Exception {
		double[] alleles = new double[] {0, 0};
		String[] keys	= new String[] {"a", "b", "c"};
		String[] values	= new String[] {"A", "B", "C"};
		Genotype g = new Genotype(alleles, keys, values);
		assertEquals("A", g.get("a"));
	}
	public void testRequireMatchingKeyAndValueLengths() throws Exception {
		double[] alleles = new double[] {0, 0};
		String[] keys	= new String[] {"a", "b", "c"};
		String[] values	= new String[] {"A", "B"};
		try {
			new Genotype(alleles, keys, values);	fail();
		} catch (Exception e) {
		}
		try {
			String[] noKeys = null;
			new Genotype(alleles, noKeys, values);	fail();
		} catch (Exception e) {
		}
		try {
			new Genotype(alleles, keys, null);	fail();
		} catch (Exception e) {
		}
	}
	public void testAlleleOrderDoesntMatter() throws Exception {
		Genotype g1 = new Genotype(new double[]{0,1});
		Genotype g2 = new Genotype(new double[]{1,0});
		assertTrue(g1.getAdjustedAlleleValues(2).equals(g2.getAdjustedAlleleValues(2)));
	}
	public void testGetRawAlleles() throws Exception {
		getRawAllelesReturnsWhatItWasGiven(new double[]{0,0});
		getRawAllelesReturnsWhatItWasGiven(new double[]{0,1});
	}
	private void getRawAllelesReturnsWhatItWasGiven(double[] alleles) throws Exception {
		Genotype g = new Genotype(alleles);
		List<Double> raw = g.getRawAlleleValues(2);
		assertEquals(2, raw.size());
		assertEquals(alleles[0], raw.get(0));
		assertEquals(alleles[1], raw.get(1));
	}
	public void testOffset() throws Exception {
		Genotype g = new Genotype(new double[]{0,1});
		g.offsetBy(0.5);
		List<Double> adjusted = g.getAdjustedAlleleValues(2);
		assertEquals(2, adjusted.size());
		assertEquals(0.5, adjusted.get(0));
		assertEquals(1.5, adjusted.get(1));
		g.clearOffset();
		adjusted = g.getAdjustedAlleleValues(2);
		assertEquals(0.0, adjusted.get(0));
		assertEquals(1.0, adjusted.get(1));
	}

	public static Genotype makeGenotype(String f1, String f2, String f3, double a1, double a2) throws Exception {
		String[] keys	= new String[] {"a", "b", "c"};
		String[] values	= new String[] {f1, f2, f3};
		double[] alleles = new double[] {a1, a2};
		return new Genotype(alleles, keys, values);
	}

}
