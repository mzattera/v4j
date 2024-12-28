/* Copyright (c) 2022-2024 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.nd4j.linalg.api.ndarray.INDArray;

/**
 * Utility class to deal with Keras model.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class KerasUtil {

	private final static Random rnd = new Random();

	private KerasUtil() {
	}

	/**
	 * @param a an array of shape [0,N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return The index of the element of the array with the highest value (the
	 *         index of the element with highest probability accordingly to softmax
	 *         - greedy sampling).
	 */
	public static int greedy(INDArray a) {
		if (a == null || a.rank() != 2 || a.shape()[1] == 0) {
			throw new IllegalArgumentException("Input must be a non-null one-dimensional INDArray.");
		}

		long idx = 0;
		double max = 0.0;
		for (long i = 0; i < a.shape()[1]; ++i) {
			if (a.getDouble(0, i) > max) {
				max = a.getDouble(0, i);
				idx = i;
			}
		}

		return (int) idx;
	}

	/**
	 * @param a an array of shape [0,N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return A random index, distributed accordingly to a.
	 */
	public static int random(INDArray a) {
		return random(a, rnd);
	}

	/**
	 * @param a an array of shape [0,N], reflecting the output of a Keras softmax
	 *          layer.
	 * @param r Random number generator to use.
	 * 
	 * @return A random index, distributed accordingly to a.
	 */
	public static int random(INDArray a, Random r) {

		if (a == null || a.rank() != 2 || a.shape()[1] == 0) {
			throw new IllegalArgumentException("Input must be a non-null one-dimensional INDArray.");
		}

		return random(a.toDoubleVector(), r);
	}

	/**
	 * @param a an array reflecting the output of a Keras softmax layer.
	 * 
	 * @return A random index, distributed accordingly to a.
	 */
	public static int random(double[] a) {
		return random(a, rnd);
	}

	/**
	 * @param a an array of shape [0,N], reflecting the output of a Keras softmax
	 *          layer.
	 * @param r Random number generator to use.
	 * 
	 * @return A random index, distributed accordingly to a.
	 */
	public static int random(double[] a, Random r) {

		// Cumulative probabilities
		double[] p = new double[a.length];
		p[0] = a[0];
		for (int i = 1; i < p.length; ++i) {
			p[i] = p[i - 1] + a[i];
		}

		double e = r.nextDouble();
		for (int i = 0; i < p.length; ++i)
			if (p[i] >= e)
				return i;

		return p.length - 1; // safeguard for rounding
	}

	/**
	 * 
	 * @param a an array of shape [0,N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return An index in a using top-K sampling.
	 */
	public static long topK(INDArray a, int k) {
		return topK(a, k, rnd);
	}

	/**
	 * 
	 * @param a an array of shape [0,N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return An index in a using top-K sampling.
	 */
	public static long topK(INDArray a, int k, Random r) {

		if (a == null || a.rank() != 2 || a.shape()[1] == 0) {
			throw new IllegalArgumentException("Input must be a non-null one-dimensional INDArray.");
		}

		// put a in a sorted list
		Map<Long, Double> d = new HashMap<>();
		for (long i = 0; i < a.shape()[1]; ++i) {
			d.put(i, a.getDouble(0, i));
		}
		List<Entry<Long, Double>> list = new ArrayList<>(d.entrySet());
		list.sort(Entry.comparingByValue());
		Collections.reverse(list);

		// Put top probabilities in an array
		int aSize = (int) Math.min(a.shape()[1], k);
		double[] a2 = new double[aSize];
		for (int i = 0; i < aSize; ++i) {
			a2[i] = list.get(i).getValue();
		}

		// normalise and random sample
		// TODO this should work with long
		int idx = random(normalize(a2), r);

		// re-translate into an index on the original array
		return list.get(idx).getKey();
	}

	/**
	 * 
	 * @param a an array of shape [0,N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return An index in a using top-P (nucleus) sampling.
	 */
	public static long nucleus(INDArray a, double p) {
		return nucleus(a, p, rnd);
	}

	/**
	 * 
	 * @param a an array of shape [0,N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return An index in a using top-P (nucleus) sampling.
	 */
	public static long nucleus(INDArray a, double p, Random r) {

		if (a == null || a.rank() != 2 || a.shape()[1] == 0) {
			throw new IllegalArgumentException("Input must be a non-null one-dimensional INDArray.");
		}

		// put a in a sorted list
		Map<Long, Double> d = new HashMap<>();
		for (long i = 0; i < a.shape()[1]; ++i) {
			d.put(i, a.getDouble(0, i));
		}
		List<Entry<Long, Double>> list = new ArrayList<>(d.entrySet());
		list.sort(Entry.comparingByValue());
		Collections.reverse(list);

		// Put probabilities up to p in an array
		double s = 0.0;
		int aSize = 0;
		for (; aSize < list.size(); ++aSize) {
			s += list.get(aSize).getValue();
			if (s >= p)
				break;
		}
		double[] a2 = new double[aSize + 1];
		for (int i = 0; i <= aSize; ++i) {
			a2[i] = list.get(i).getValue();
		}

		// normalise and random sample
		// TODO this should workj with long
		int idx = random(normalize(a2),r);

		// re-translate into an index on the original array
		return list.get(idx).getKey();
	}

	/**
	 * 
	 * @param a an array of shape [0,N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return An index in a using random sampling with givne temperature t.
	 */
	public static long temperature(INDArray a, double t) {

		return temperature(a, t, rnd);
	}

	/**
	 * 
	 * @param a an array of shape [0,N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return An index in a using random sampling with givne temperature t.
	 */
	public static long temperature(INDArray a, double t, Random r) {

		if (a == null || a.rank() != 2 || a.shape()[1] == 0) {
			throw new IllegalArgumentException("Input must be a non-null one-dimensional INDArray.");
		}

		// scale probabilities using temperature
		double[] d = new double[(int) a.shape()[1]];
		for (int i = 0; i < d.length; ++i) {
			d[i] = Math.exp(a.getDouble(0, i) / t);
		}

		// normalise and random sample
		return random(normalize(d),r);
	}

	/**
	 * Normalizes given vector.
	 * 
	 * @return a "normalized' so the sum of all values in a is 1.0.
	 */
	public static double[] normalize(double[] a) {
		double sum = 0.0;
		for (double d : a)
			sum += d;
		for (int i = 0; i < a.length; ++i)
			a[i] = a[i] / sum;
		return a;
	}
}
