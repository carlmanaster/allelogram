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
	private final HashMap<String, Classification> classifications = new HashMap<String, Classification>();
	private final Classification sortClassification;
	private final Classification colorByClassification;
	private final Vector<Classification> infoClassifications = new Vector<Classification>();
	private final Classification optionClickClassification;
	private final Classification commandClickClassification;
	private final Classification controlClassification;
	private final GenotypeClassificationPredicate controlSubject;
	private final Integer[] alleleIndexes;

	public Settings(File file) throws Exception {
		this(FileUtil.readAll(FileUtil.makeReader(file)));
	}

    public Settings(String s) throws Exception {
    		ArrayList<String> lines = new ArrayList<String>();
    		for (String line : s.split("\n"))
    			lines.add(stripComments(line));
    		
    		int columnStart			= lines.indexOf("columns") + 1;
    		int classificationStart	= lines.indexOf("classifications") + 1;
    		int sortLine				= lines.indexOf("sort") + 1;
    		int colorByLine			= lines.indexOf("color") + 1;
    		int infoStart			= lines.indexOf("info") + 1;
    		int clickStart			= lines.indexOf("click") + 1;
    		int controlLine			= lines.indexOf("control") + 1;
    		
    		if (columnStart < 1)						throw new Exception("Settings must contain a 'columns' section.");
    		if (classificationStart < columnStart)		throw new Exception("Settings must contain a 'classifications' section after the 'columns' section.");
    		if (sortLine < classificationStart)		throw new Exception("Settings must contain a 'sort' section after the 'classifications' section.");
    		if (colorByLine < sortLine)				throw new Exception("Settings must contain a 'color' section after the 'sort' section.");
    		if (infoStart < colorByLine)				throw new Exception("Settings must contain an 'info' section after the 'color' section.");
    		if (clickStart < infoStart)				throw new Exception("Settings must contain a 'click' section after the 'info' section.");
    		if (controlLine < clickStart)				throw new Exception("Settings must contain a 'control' section after the 'click' section.");

    		for (int i = columnStart; i < classificationStart - 1; ++i)
    			columns.add(lines.get(i));
    		
    		for (int i = classificationStart; i < sortLine - 1; ++i)
    			addClassification(lines.get(i));
    		
    		sortClassification	= classifications.get(lines.get(sortLine));
    		colorByClassification	= classifications.get(lines.get(colorByLine));
    		
    		for (int i = infoStart; i < clickStart - 1; ++i)
    			infoClassifications.add(classifications.get(lines.get(i)));
    		
    		ArrayList<String> clicks = new ArrayList<String>();
    		for (int i = clickStart; i < controlLine - 1; ++i)
    			clicks.add(lines.get(i));
    		
    		int n = clicks.size();
    		optionClickClassification		= n < 1 ? null : classifications.get(clicks.get(0));
    		commandClickClassification	= n < 2 ? null : classifications.get(clicks.get(1));
    		
    		String[] terms = Util.splitOnColon(lines.get(controlLine));
    		if (terms.length == 2) {
    			controlClassification = classifications.get(terms[0]);
    			controlSubject = new GenotypeClassificationPredicate(controlClassification, controlClassification.parse(terms[1]));
    		} else {
    			controlClassification = null;
    			controlSubject = null;
    		}
    		
    		HashSet<Integer> set = new HashSet<Integer>();
    		for (int i = 0; i < columns.size(); ++i)
    			if (columns.get(i).toLowerCase().startsWith("allele"))
    				set.add(i);
    		alleleIndexes = set.toArray(new Integer[set.size()]);
    		Arrays.sort(alleleIndexes);
	}
    
	private void addClassification(String string) throws Exception {
		String[] terms = Util.splitOnColon(string);
		String name = terms[0];
		classifications.put(name, new Classification(this.columns, terms[1]));
	}

	public static String stripComments(String line) {
        int commentStart = line.indexOf('!');
        if (commentStart < 0)
            return line.trim();
        return line.substring(0, commentStart).trim();
    }


	public List<String> getColumns()							{return columns;}
	public Map<String, Classification> getClassifications()		{return classifications;}
	public Classification getSortClassification()				{return sortClassification;}
	public Classification getColorByClassification()			{return colorByClassification;}
	public List<Classification> getInfoClassifications()		{return infoClassifications;}
	public Classification getOptionClickClassification()		{return optionClickClassification;}
	public Classification getCommandClickClassification()		{return commandClickClassification;}
	public boolean isControlSubject(Genotype control)			{return controlSubject.passes(control);}
	public Integer[] getAlleleIndexes()						{return alleleIndexes;}

	public String info(Genotype genotype) {
		StringBuffer sb = new StringBuffer();
		for (Classification classification : infoClassifications)
			sb.append(classification.string(genotype) + "\n");
		return sb.toString();
	}

}
