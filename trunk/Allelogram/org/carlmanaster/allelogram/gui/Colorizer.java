package org.carlmanaster.allelogram.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.carlmanaster.allelogram.model.Allele;
import org.carlmanaster.allelogram.model.CircularList;
import org.carlmanaster.allelogram.model.Classifier;
import org.carlmanaster.allelogram.model.Genotype;

public class Colorizer {
	private final Classifier classifier;
	private final HashMap<String, Color> index = new HashMap<String, Color>();
	
	public Colorizer(Classifier classifier, List<Genotype> genotypes) {
		this.classifier = classifier;
		if (classifier == null)
			return;
		HashSet<String> set = new HashSet<String>();
		for (Genotype genotype : genotypes)
			set.add(classifier.string(genotype));
		
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(set);
		Collections.sort(list);
		
		CircularList<Color> colors = makeColors();
		
		int i = 0;
		for (String string : list )
			index.put(string, colors.get(i++));
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
		if (classifier == null)
			return Color.BLUE;
		return index.get(classifier.string(allele.getGenotype()));
	}

}
