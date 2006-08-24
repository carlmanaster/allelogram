package org.carlmanaster.allelogram.model;

import java.util.ArrayList;

public class Classification {
	private final String name;
	private final ArrayList<String> columns = new ArrayList<String>();

	public Classification(String name, String[] columns) {
		this.name = name;
		for (String column : columns)
			this.columns.add(column);
	}

	public String getName()				{return name;}
	public ArrayList<String> getColumns()	{return columns;}

	public boolean equals(Object obj) {
		if (obj == null)							return false;
		if (obj == this)							return true;
		if (!(obj instanceof Classification))		return false;
		Classification that = (Classification) obj;
		if (!this.name.equals(that.name))			return false;
		if (!this.columns.equals(that.columns))	return false;
		return true;
	}
}
