package org.carlmanaster.allelogram.model.tests;

import java.util.ArrayList;
import java.util.Collections;

import org.carlmanaster.allelogram.model.Classification;
import org.carlmanaster.allelogram.model.Genotype;
import org.carlmanaster.allelogram.model.GenotypeComparator;

import junit.framework.TestCase;

public class GenotypeComparatorTest extends TestCase {
	public void testSort() throws Exception {
		Genotype g1 = GenotypeTest.makeGenotype("A", "B", "C", 0, 1);
		Genotype g2 = GenotypeTest.makeGenotype("A", "A", "C", 0, 1);
		Genotype g3 = GenotypeTest.makeGenotype("B", "A", "C", 0, 1);
		ArrayList<Genotype> genotypes = new ArrayList<Genotype>();
		genotypes.add(g1);
		genotypes.add(g2);
		genotypes.add(g3);
		
		Classification c1 = new Classification("x", new String[]{"a", "b"});
		GenotypeComparator comparator = new GenotypeComparator(c1);
		Collections.sort(genotypes, comparator);
		assertEquals(g2, genotypes.get(0));
		assertEquals(g1, genotypes.get(1));
		assertEquals(g3, genotypes.get(2));

		c1 = new Classification("y", new String[]{"b", "a"});
		comparator = new GenotypeComparator(c1);
		Collections.sort(genotypes, comparator);
		assertEquals(g2, genotypes.get(0));
		assertEquals(g3, genotypes.get(1));
		assertEquals(g1, genotypes.get(2));
	}
}
