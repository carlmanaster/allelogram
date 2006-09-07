package org.carlmanaster.allelogram.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.CircularList;
import org.carlmanaster.allelogram.model.Classification;
import org.carlmanaster.allelogram.model.Genotype;

public class Colorizer {
	private final Classification classification;
	private final HashMap<Vector<String>, Color> index = new HashMap<Vector<String>, Color>();
	
	public Colorizer(Classification classification, List<Genotype> genotypes) {
		this.classification = classification;
		if (classification == null)
			return;
		HashSet<Vector<String>> set = new HashSet<Vector<String>>();
		for (Genotype genotype : genotypes)
			set.add(classification.classify(genotype));
		
		CircularList<Color> colors = makeColors();
		
		int i = 0;
		for (Vector<String> vector : set)
			index.put(vector, colors.get(i++));
	}

	private static CircularList<Color> makeColors() {
		ArrayList<Color> c = new ArrayList<Color>();
		c.add(Color.BLUE); 
		c.add(Color.RED);
		c.add(Color.GREEN);
		c.add(Color.CYAN);
		c.add(Color.GRAY);
		return new CircularList<Color>(c);
	}

	public Colorizer() {
		this(null, null);
	}

	public Color colorFor(Allele allele) {
		if (classification == null)
			return Color.BLUE;
		return index.get(classification.classify(allele.getGenotype()));
	}

}
