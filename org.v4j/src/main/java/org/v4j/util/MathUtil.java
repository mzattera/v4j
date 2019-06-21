/**
 * 
 */
package org.v4j.util;

import java.util.Map.Entry;

/**
 * 
 * Static class of math util methods / functions.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class MathUtil {

	private MathUtil() {
	}

	/**
	 * Calculate entropy for a set of symbols.
	 * 
	 * @param c Symbols with their occurrence.
	 * @return
	 */
	public static double entropy(Counter<?> c) {
		double result = 0.0;
		double tot = c.getTotalCounted();
		
		for (Entry<?, Integer> e : c.entrySet()) {
			double p = e.getValue() / tot;
			result += p * log2(p);
		}		
		
		return -result;
	}
	
	private final static double logOf2 = Math.log(2.0);
	
	/**
	 * @return Base 2 log() of n.
	 */
	public static double log2 (double n) {
		return Math.log(n) / logOf2;
	}
}
