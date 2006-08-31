package org.carlmanaster.allelogram.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Settings {
	final Vector<String> columns = new Vector<String>();
	private HashMap<String, Classification> classifications = new HashMap<String, Classification>();

    public Settings(String s) throws Exception {
    		ArrayList<String> lines = new ArrayList<String>();
    		for (String line : s.split("\n"))
    			lines.add(stripComments(line));
    		
    		int columnStart = lines.indexOf("columns") + 1;
    		int classificationStart = lines.indexOf("classifications") + 1;
    		int sortStart = lines.indexOf("sort") + 1;
    		
    		for (int i = columnStart; i < classificationStart - 1; ++i)
    			columns.add(lines.get(i));
    		
    		for (int i = classificationStart; i < sortStart - 1; ++i)
    			addClassification(lines.get(i));
	}

	private void addClassification(String string) throws Exception {
		String[] terms = string.split(":");
		String name = terms[0];
		classifications.put(name, new Classification(this.columns, terms[1]));
	}

	public static String stripComments(String line) {
        int commentStart = line.indexOf('!');
        if (commentStart < 0)
            return line;
        return line.substring(0, commentStart).trim();
    }

	public Vector<String> getColumns()							{return columns;}
	public HashMap<String, Classification> getClassifications()		{return classifications;}

}
