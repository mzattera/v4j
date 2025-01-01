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
 * Utility class to deal with sampling across distributions. This is used e.g.
 * to sample a value from a softmax distribution, as output of a neural network.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class SamplingUtil {

	private final static Random RND = new Random();

	/**
	 * A sample provides a {@link #sample(INDArray)} method to sample a character in
	 * a distribution, based on a specific strategy.
	 */
	public abstract static class Sampler {

		public static Sampler getGreedySampler () {
			return new Sampler() {
				@Override
				public int sample(INDArray a) {
					return SamplingUtil.greedy(a);
				}				
			};
		}

		public static Sampler getRandomSampler () {
			return new Sampler() {
				@Override
				public int sample(INDArray a) {
					return SamplingUtil.random(a);
				}				
			};
		}

		public static Sampler getTopKSampler (int k) {
			return new Sampler() {
				@Override
				public int sample(INDArray a) {
					return SamplingUtil.topK(a, k);
				}				
			};
		}

		public static Sampler getNucleusSampler (double p) {
			return new Sampler() {
				@Override
				public int sample(INDArray a) {
					return SamplingUtil.nucleus(a, p);
				}				
			};
		}

		public static Sampler getTemperatureSampler (double t) {
			return new Sampler() {
				@Override
				public int sample(INDArray a) {
					return SamplingUtil.temperature(a, t);
				}				
			};
		}
		
		public abstract int  sample (INDArray a);		
	}

	private SamplingUtil() {
	}

	/**
	 * @param a an array of shape [N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return The index of the element of the array with the highest value (the
	 *         index of the element with highest probability accordingly to softmax
	 *         - greedy sampling).
	 */
	public static int greedy(INDArray a) {
		if (a == null || a.rank() != 1 || a.shape()[0] == 0) {
			throw new IllegalArgumentException("Input must be a non-null one-dimensional INDArray.");
		}

		long idx = 0;
		double max = 0.0;
		for (long i = 0; i < a.shape()[0]; ++i) {
			if (a.getDouble(i) > max) {
				max = a.getDouble(i);
				idx = i;
			}
		}

		return (int) idx;
	}

	/**
	 * @param a an array of shape [N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return A random index, distributed accordingly to a.
	 */
	public static int random(INDArray a) {
		return random(a, RND);
	}

	/**
	 * @param a an array of shape [N], reflecting the output of a Keras softmax
	 *          layer.
	 * @param r Random number generator to use.
	 * 
	 * @return A random index, distributed accordingly to a.
	 */
	public static int random(INDArray a, Random r) {

		if (a == null || a.rank() != 1 || a.shape()[0] == 0) {
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
		return random(a, RND);
	}

	/**
	 * @param a an array of shape [N], reflecting the output of a Keras softmax
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
	 * @param a an array of shape [N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return An index in a using top-K sampling.
	 */
	public static int topK(INDArray a, int k) {
		return topK(a, k, RND);
	}

	/**
	 * 
	 * @param a an array of shape [N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return An index in a using top-K sampling.
	 */
	public static int topK(INDArray a, int k, Random r) {

		if (a == null || a.rank() != 1 || a.shape()[0] == 0) {
			throw new IllegalArgumentException("Input must be a non-null one-dimensional INDArray.");
		}

		// put a in a sorted list
		Map<Integer, Double> d = new HashMap<>();
		for (int i = 0; i < (int) a.shape()[0]; ++i) {
			d.put(i, a.getDouble(i));
		}
		List<Entry<Integer, Double>> list = new ArrayList<>(d.entrySet());
		list.sort(Entry.comparingByValue());
		Collections.reverse(list);

		// Put top probabilities in an array
		int aSize = (int) Math.min(a.shape()[0], k);
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
	public static int nucleus(INDArray a, double p) {
		return nucleus(a, p, RND);
	}

	/**
	 * 
	 * @param a an array of shape [N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return An index in a using top-P (nucleus) sampling.
	 */
	public static int nucleus(INDArray a, double p, Random r) {

		if (a == null || a.rank() != 1 || a.shape()[0] == 0) {
			throw new IllegalArgumentException("Input must be a non-null one-dimensional INDArray.");
		}

		// put a in a sorted list
		Map<Integer, Double> d = new HashMap<>();
		for (int i = 0; i < (int)a.shape()[0]; ++i) {
			d.put(i, a.getDouble(i));
		}
		List<Entry<Integer, Double>> list = new ArrayList<>(d.entrySet());
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
		int idx = random(normalize(a2), r);

		// re-translate into an index on the original array
		return list.get(idx).getKey();
	}

	/**
	 * 
	 * @param a an array of shape [0,N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return An index in a using random sampling with given temperature t.
	 */
	public static int temperature(INDArray a, double t) {

		return temperature(a, t, RND);
	}

	/**
	 * 
	 * @param a an array of shape [0,N], reflecting the output of a Keras softmax
	 *          layer.
	 * 
	 * @return An index in a using random sampling with givne temperature t.
	 */
	public static int temperature(INDArray a, double t, Random r) {

		if (a == null || a.rank() != 1 || a.shape()[0] == 0) {
			throw new IllegalArgumentException("Input must be a non-null one-dimensional INDArray.");
		}

		// scale probabilities using temperature
		double[] d = new double[(int) a.shape()[0]];
		for (int i = 0; i < d.length; ++i) {
			d[i] = Math.exp(a.getDouble(i) / t);
		}

		// normalise and random sample
		return random(normalize(d), r);
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
