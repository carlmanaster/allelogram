package org.carlmanaster.allelogram.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.carlmanaster.allelogram.util.FileUtil;

public class Settings {
	private final Vector<String> columns = new Vector<String>();
	private final HashMap<String, Classifier> classifiers = new HashMap<String, Classifier>();
	private final Classifier sortClassifier;
	private final Classifier colorByClassifier;
	private final Vector<Classifier> infoClassifiers = new Vector<Classifier>();
	private final Classifier optionClickClassifier;
	private final Classifier commandClickClassifier;
	private final Classifier controlClassifier;
	private final GenotypeClassificationPredicate controlSubject;
	private final Integer[] alleleIndexes;

	public Settings(File file) throws Exception {
		this(FileUtil.readAll(FileUtil.makeReader(file)));
	}

    public Settings(String s) throws Exception {
    		ArrayList<String> lines = new ArrayList<String>();
    		for (String line : s.split("\n"))
    			lines.add(stripComments(line));
    		
    		int columnStart		= lines.indexOf("columns") + 1;
    		int classifierStart	= lines.indexOf("classifications") + 1;
    		int sortLine			= lines.indexOf("sort") + 1;
    		int colorByLine		= lines.indexOf("color") + 1;
    		int infoStart		= lines.indexOf("info") + 1;
    		int clickStart		= lines.indexOf("click") + 1;
    		int controlLine		= lines.indexOf("control") + 1;
    		
    		if (columnStart < 1)					throw new Exception("Settings must contain a 'columns' section.");
    		if (classifierStart < columnStart)	throw new Exception("Settings must contain a 'classifications' section after the 'columns' section.");
    		if (sortLine < classifierStart)		throw new Exception("Settings must contain a 'sort' section after the 'classifications' section.");
    		if (colorByLine < sortLine)			throw new Exception("Settings must contain a 'color' section after the 'sort' section.");
    		if (infoStart < colorByLine)			throw new Exception("Settings must contain an 'info' section after the 'color' section.");
    		if (clickStart < infoStart)			throw new Exception("Settings must contain a 'click' section after the 'info' section.");
    		if (controlLine < clickStart)			throw new Exception("Settings must contain a 'control' section after the 'click' section.");

    		for (int i = columnStart; i < classifierStart - 1; ++i)
    			columns.add(lines.get(i));
    		
    		for (int i = classifierStart; i < sortLine - 1; ++i)
    			addClassifier(lines.get(i));
    		
    		sortClassifier		= classifiers.get(lines.get(sortLine));
    		colorByClassifier	= classifiers.get(lines.get(colorByLine));
    		
    		for (int i = infoStart; i < clickStart - 1; ++i)
    			infoClassifiers.add(classifiers.get(lines.get(i)));
    		
    		ArrayList<String> clicks = new ArrayList<String>();
    		for (int i = clickStart; i < controlLine - 1; ++i)
    			clicks.add(lines.get(i));
    		
    		int n = clicks.size();
    		optionClickClassifier		= n < 1 ? null : classifiers.get(clicks.get(0));
    		commandClickClassifier	= n < 2 ? null : classifiers.get(clicks.get(1));
    		
    		String[] terms = Util.splitOnColon(lines.get(controlLine));
    		if (terms.length == 2) {
    			controlClassifier = classifiers.get(terms[0]);
    			controlSubject = new GenotypeClassificationPredicate(controlClassifier, controlClassifier.parse(terms[1]));
    		} else {
    			controlClassifier = null;
    			controlSubject = null;
    		}
    		
    		HashSet<Integer> set = new HashSet<Integer>();
    		for (int i = 0; i < columns.size(); ++i)
    			if (columns.get(i).toLowerCase().startsWith("allele"))
    				set.add(i);
    		alleleIndexes = set.toArray(new Integer[set.size()]);
    		Arrays.sort(alleleIndexes);
	}
    
	private void addClassifier(String string) throws Exception {
		String[] terms = Util.splitOnColon(string);
		String name = terms[0];
		classifiers.put(name, new Classifier(this.columns, terms[1]));
	}

	public static String stripComments(String line) {
        int commentStart = line.indexOf('!');
        if (commentStart < 0)
            return line.trim();
        return line.substring(0, commentStart).trim();
    }


	public List<String> getColumns()							{return columns;}
	public Map<String, Classifier> getClassifiers()			{return classifiers;}
	public Classifier getSortClassifier()						{return sortClassifier;}
	public Classifier getColorByClassifier()					{return colorByClassifier;}
	public List<Classifier> getInfoClassifiers()				{return infoClassifiers;}
	public Classifier getOptionClickClassifier()				{return optionClickClassifier;}
	public Classifier getCommandClickClassifier()				{return commandClickClassifier;}
	public boolean isControlSubject(Genotype control)			{return controlSubject.passes(control);}
	public Integer[] getAlleleIndexes()						{return alleleIndexes;}
	public GenotypeClassificationPredicate getControlSubject()	{return controlSubject;}

	public String info(Genotype genotype) {
		StringBuffer sb = new StringBuffer();
		ArrayList<Allele> alleles = genotype.getAlleles();
		for (Allele allele : alleles)
			sb.append (String.format("%1.1f ", allele.getAdjustedValue()));
		sb.append('\n');
		for (Classifier classifier : infoClassifiers)
			sb.append(classifier.string(genotype) + "\n");
		return sb.toString();
	}


}
