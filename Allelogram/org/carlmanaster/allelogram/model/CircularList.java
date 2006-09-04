package org.carlmanaster.allelogram.model;

import java.util.AbstractList;
import java.util.List;

public class CircularList<T> extends AbstractList<T> {
	private final List<T> list;

	public CircularList(List<T> list)	{this.list = list;}
	public T get(int i)				{return list.get(index(i));}
	public T set(int i, T t)			{return list.set(index(i), t);}
	public int size()				{return list.isEmpty() ? 0 : Integer.MAX_VALUE;}
	
	private int index(int i)	{return i % list.size();}
}
