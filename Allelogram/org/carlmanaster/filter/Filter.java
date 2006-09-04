package org.carlmanaster.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.carlmanaster.predicate.Equals;
import org.carlmanaster.predicate.Predicate;


public class Filter<T> {
	private final Predicate<T> test;
	
	public static <T> Filter<T> in(Predicate<T> test)	{return new Filter<T>(test);}
	public static <T> Filter<T> out(Predicate<T> test)	{return in(test.inverse());}
	public static <T> Filter<T> in(T t)				{return in(new Equals<T>(t));}
	public static <T> Filter<T> out(T t)				{return out(new Equals<T>(t));}
	private Filter(Predicate<T> test)					{this.test = test;}

	public List<T> filtered(Collection<T> list) {
		ArrayList<T> result = new ArrayList<T>();
		for (T t : list) 
			if (test.passes(t))
				result.add(t);
		return result;
	}

	public List<T> filtered(T[] array) {
		return filtered(Arrays.asList(array));
	}

}
