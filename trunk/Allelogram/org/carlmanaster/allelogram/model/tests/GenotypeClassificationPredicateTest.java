package org.carlmanaster.allelogram.model.tests;

import junit.framework.TestCase;

import org.carlmanaster.allelogram.model.Classifier;
import org.carlmanaster.allelogram.model.Genotype;
import org.carlmanaster.allelogram.model.GenotypeClassificationPredicate;

public class GenotypeClassificationPredicateTest extends TestCase {
	public void testPredicate() throws Exception {
		Classifier classification = new Classifier(new String[]{"a", "b"});
		GenotypeClassificationPredicate predicate = new GenotypeClassificationPredicate(classification, new String[]{"A", "B"});
		Genotype g1 = GenotypeTest.makeGenotype("A", "B", "C", 0, 1);
		Genotype g2 = GenotypeTest.makeGenotype("A", "A", "C", 0, 1);
		Genotype g3 = GenotypeTest.makeGenotype("B", "A", "C", 0, 1);
	
		assertTrue(predicate.passes(g1));
		assertFalse(predicate.passes(g2));
		assertFalse(predicate.passes(g3));
	}
	
	public void testValuesMustMatchClassification() throws Exception {
		Classifier classification = new Classifier(new String[]{"a", "b"});
		try {
			new GenotypeClassificationPredicate(classification, new String[]{"A", "B", "C"});
			fail();
		} catch (Exception e) {
			assertTrue(true);
		}
	}
	
	public void testGenotypeConstructor() throws Exception {
		Genotype g1 = GenotypeTest.makeGenotype("A", "B", "C", 0, 1);
		Genotype g2 = GenotypeTest.makeGenotype("A", "A", "C", 0, 1);
		Genotype g3 = GenotypeTest.makeGenotype("B", "A", "C", 0, 1);
		Classifier classification = new Classifier(new String[]{"a", "b"});
		GenotypeClassificationPredicate predicate = new GenotypeClassificationPredicate(classification, g1);
	
		assertTrue(predicate.passes(g1));
		assertFalse(predicate.passes(g2));
		assertFalse(predicate.passes(g3));
	}
}
