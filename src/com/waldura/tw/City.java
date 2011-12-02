package com.waldura.tw;

/**
 * A city is identified by a uniqune number.
 * Package members are given access to an identity relationship between
 * cities and numbers: they can converts between <code>City</code>
 * instances and numbers using {@link #valueOf(int)} and {@link #getIndex()}.
 * This relationship is used by the
 * {@link com.waldura.tw.DenseRoutesMap DensesRoutesMap} 
 * to store cities in an array.
 * 
 * @author Renaud Waldura &lt;renaud+tw@waldura.com&gt;
 * @author David Tolpin &lt;david.tolpin@gmail.com&gt;
 * @version $Id: City.java 2367 2007-08-20 21:47:25Z renaud $
 */

public final class City implements Comparable<City>
{
	/**
	 * The largest possible number of cities.
	 */
	public static final int MAX_NUMBER = 1000;
	
	private static final City[] cities = new City[MAX_NUMBER];

	public static City valueOf(int index) {
		if(cities[index]==null)
			cities[index] = new City(index);
		return cities[index];
	}
	

	private int index;
	/*
     * Private constructor.
     * @param index
     */
	private City(int index)	{
		this.index = index;
	}

	 public String toString() {
		return String.valueOf(index);
	}

    /**
     * Two cities are considered equal if they are the same object.
     * 
     * @see java.lang.Object#equals(Object)
     */    
    public boolean equals(Object o) {
        return this == o;
    }
    

    /**
     * Compare two cities by index.
     * 
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(City c) {
        return this.index - c.index;
    }
}
