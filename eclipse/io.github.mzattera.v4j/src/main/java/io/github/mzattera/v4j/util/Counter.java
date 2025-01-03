/* Copyright (c) 2018-2022 Massimiliano "Maxi" Zattera */

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

	/**
	 * Creates a counter counting all terms in an Array.
	 * 
	 * @param objs
	 */
	public Counter(T[] objs) {
		this();
		countAll(objs);
	}

	/**
	 * Creates a counter counting all terms in a Collection.
	 * 
	 * @param objs
	 */
	public Counter(Collection<T> objs) {
		this();
		countAll(objs);
	}

	/**
	 * Creates a counter counting all terms in a Collection.
	 * 
	 * @param objs
	 */
	public Counter(Counter<? extends T> objs) {
		this();
		countAll(objs);
	}

	/**
	 * 
	 * @return Total number of items counted.
	 */
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
	 * Counts given item n times (notice negative numbers are allowed, as long as
	 * the total count is not negative).
	 * 
	 * @return number of times obj was counted (including this).
	 */
	public int count(T obj, int n) {
		int c = getCount(obj) + n;

		// TODO Notice this is only for semantic, because if an object is counted N and
		// then -N (that is counted 0) it should be counted more than an object counted
		// -M, hence getHighestCounyted() should return the first object, but it does
		// not because it has been removed. Hence we allow only positive total counts
		// (or 0) and we assume if total count is 0 the item has never been counted.
		if (c < 0)
			throw new IllegalArgumentException("Negative total count for " + obj);

		if (c == 0) {
			counts.remove(obj);
		} else {
			counts.put(obj, c);
		}
		tot += n;
		return c;
	}

	/**
	 * Counts all given items once.
	 */
	public void countAll(T[] objs) {
		for (T o : objs)
			count(o);
	}

	/**
	 * Counts all given items once.
	 */
	public void countAll(Collection<T> objs) {
		for (T o : objs)
			count(o);
	}

	/**
	 * Counts all items that are already counted in another Counter<>. Each item is
	 * counted the corresponding number of times.
	 * 
	 * @return This Counter.
	 */
	public Counter<T> countAll(Counter<? extends T> c) {
		for (Entry<? extends T, Integer> e : c.entrySet())
			count(e.getKey(), e.getValue());

		return this;
	}

	/**
	 * Set count for given item to 0.
	 * 
	 * @param obj
	 */
	public void remove(T obj) {
		count(obj, -getCount(obj));
	}

	/**
	 * Resets the counter.
	 * 
	 * @return Total number of items counted so far.
	 */
	public int clear() {
		int result = tot;
		counts.clear();
		tot = 0;
		return result;
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
	 * @return number of times obj was counted divided by the total number of items
	 *         counted.
	 */
	public double getFrequency(T obj) {
		return ((double) getCount(obj)) / tot;
	}

	/**
	 * 
	 * @return Entropy associated to given object. This is calculated based on the
	 *         object frequency.
	 */
	public double getEntropy(T obj) {
		return MathUtil.entropy(getFrequency(obj));
	}

	/**
	 * 
	 * @return item that was recurring most often. Notice more than one such items
	 *         might exist.
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
	 * @return item that was recurring least of the times (but at least once).
	 *         Notice more than one such items might exist.
	 */
	public T getLowestCounted() {
		int min = Integer.MAX_VALUE;
		T result = null;

		for (Map.Entry<T, Integer> entry : counts.entrySet()) {
			if (entry.getValue() < min) {
				min = entry.getValue();
				result = entry.getKey();
			}
		}

		return result;
	}

	/**
	 * 
	 * @return number of times least recurring item was counted.
	 */
	public int getLowestCount() {
		int min = Integer.MAX_VALUE;

		for (int c : counts.values()) {
			if (c < min)
				min = c;
		}

		return min;
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
	 * @return size of item set (how many distinct objects have been counted).
	 */
	public int size() {
		return counts.size();
	}

	/**
	 * Prints the contents of the Counter; each item is printed with its count in
	 * ascending order of count.
	 */
	public void print() {
		print(false);
	}

	/**
	 * Prints the contents of the Counter; each item is printed with its count.
	 * 
	 * @param reversed If true, print items in descending count value. Otherwise
	 *                 they are printed in ascending order.
	 */
	public void print(boolean reversed) {
		if (reversed) {
			for (Entry<T, Integer> e : reversed()) {
				System.out.println(e.getKey() + ":\t" + e.getValue());
			}
		} else {
			for (Entry<T, Integer> e : sorted()) {
				System.out.println(e.getKey() + ":\t" + e.getValue());
			}
		}
	}
}
