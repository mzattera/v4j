/**
 * 
 */
package io.github.mzattera.v4j.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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

	private final Map<T, Integer> counts;

	int tot = 0;

	public Counter() {
		counts = new HashMap<T, Integer>();
	}

	public Counter(int initialCapacity) {
		counts = new HashMap<T, Integer>(initialCapacity);
	}

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
	 * Counts all given items once.
	 */
	public void countAll(Collection<T> objs) {
		for (T o : objs)
			count(o);
	}

	/**
	 * Counts all given items n times.
	 */
	public void countAll(Collection<T> objs, int n) {
		for (T o : objs)
			count(o, n);
	}

	/**
	 * Counts all items that are already counted in another Counter<>.
	 * 
	 * @return This Counter.
	 */
	public Counter<T> countAll(Counter<T> c) {
		for (Entry<T, Integer> e : c.entrySet())
			count(e.getKey(), e.getValue());

		return this;
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

	/**
	 * 
	 * @return items / count pairs sorted by ascending values of count.
	 */
	public List<Entry<T, Integer>> sorted() {
		List<Entry<T, Integer>> result = new ArrayList<>(counts.entrySet());
		result.sort(new Comparator<Entry<T, Integer>>() {

			@Override
			public int compare(Entry<T, Integer> arg0, Entry<T, Integer> arg1) {
				return Integer.compare(arg0.getValue(), arg1.getValue());
			}
		});

		return result;
	}

	/**
	 * 
	 * @return items / count pairs sorted by descending values of count.
	 */
	public List<Entry<T, Integer>> reversed() {
		List<Entry<T, Integer>> result = sorted();
		Collections.reverse(result);
		return result;
	}

	/**
	 * 
	 * @return size of item set (how many distinct objects have bee counted).
	 */
	public int size() {
		return counts.size();
	}
}
