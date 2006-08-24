package org.carlmanaster.predicate;

public abstract class Predicate<T> {
	public abstract boolean passes(T t);

	public Predicate<T> or(Predicate<T> that)	{return new Or<T>(this, that);}
	public Predicate<T> and(Predicate<T> that)	{return new And<T>(this, that);}
	public Predicate<T> or(T t)				{return or(eq(t));}
	public Predicate<T> and(T t)				{return and(eq(t));}
	public Predicate<T> inverse()				{return new Not<T>(this);}
	
	public static <T> Predicate<T> not(T t)	{return eq(t).inverse();}
	public static <T> Predicate<T> none()		{return new None<T>();}
	public static <T> Predicate<T> all()		{return new All<T>();}
	public static <T> Predicate<T> nonNull()	{return new NonNull<T>();}
	public static <T> Unique<T> unique()		{return new Unique<T>();}

	private static <T> Equals<T> eq(T t)		{return new Equals<T>(t);}
}
