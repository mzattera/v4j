/**
 * 
 */
package org.v4j.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Keep track of number of times an object was stored.
 * 
 * @author Massimiliano "Maxi" Zattera.
 *
 */
public class Counter<T> {

	Map<T, Integer> counts = new HashMap<T, Integer>();

	int tot = 0;

	public int getTotalCounted() {
		return tot;
	}

	/**
	 * Counts given item once.
	 * 
	 * @return number of times obj was counted (including this).
	 */
	public int count(T obj) {
		return count(obj, 1);
	}

	/**
	 * Counts given item n times.
	 * 
	 * @return number of times obj was counted (including this).
	 */
	public int count(T obj, int n) {
		int c = counts.getOrDefault(obj, 0) + n;
		counts.put(obj, c);
		tot += n;
		return c;
	}

	/**
	 * 
	 * @return number of times obj was counted.
	 */
	public int getCount(T obj) {
		return counts.getOrDefault(obj, 0);
	}

	/**
	 * 
	 * @return item that was recurring most of the time.
	 */
	public T getHighestCounted() {
		int max = 0;
		T result = null;

		for (Map.Entry<T, Integer> entry : counts.entrySet()) {
			if (entry.getValue() > max) {
				max = entry.getValue();
				result = entry.getKey();
			}
		}

		return result;
	}

	/**
	 * 
	 * @return number of times most recurring item was counted.
	 */
	public int getHighestCount() {
		int max = 0;

		for (int c : counts.values()) {
			if (c > max)
				max = c;
		}

		return max;
	}

	/**
	 * 
	 * @return all items that have been counted.
	 */
	public Set<T> itemSet() {
		return counts.keySet();
	}

	/**
	 * 
	 * @return a set of items / count pairs.
	 */
	public Set<Entry<T, Integer>> entrySet() {
		return counts.entrySet();
	}
}
