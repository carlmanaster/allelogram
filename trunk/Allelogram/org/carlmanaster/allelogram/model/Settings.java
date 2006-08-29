package org.carlmanaster.allelogram.model;

import java.util.ArrayList;
import java.util.Vector;

public class Settings {
	final Vector<String> columns = new Vector<String>();
	private Vector<Classification> classifications = new Vector<Classification>();

    public Settings(String s) {
    		ArrayList<String> lines = new ArrayList<String>();
    		for (String line : s.split("\n"))
    			lines.add(stripComments(line));
    		
    		int columnStart = lines.indexOf("columns") + 1;
    		int classificationStart = lines.indexOf("classifications") + 1;
    		int sortStart = lines.indexOf("sort") + 1;
    		
    		for (int i = columnStart; i < classificationStart - 1; ++i)
    			columns.add(lines.get(i));
    		
    		for (int i = classificationStart; i < sortStart - 1; ++i)
    			classifications.add(makeClassification(lines.get(i)));
	}

	private Classification makeClassification(String string) {
		String[] terms = string.split(":");
		String name = terms[0];
		String[] columns = terms[1].split("-");
		Classification c = new Classification(name, columns);
		return c;
	}

	public static String stripComments(String line) {
        int commentStart = line.indexOf('!');
        if (commentStart < 0)
            return line;
        return line.substring(0, commentStart).trim();
    }

	public Vector<String> getColumns()					{return columns;}
	public Vector<Classification> getClassifications()		{return classifications;}

}
