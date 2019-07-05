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
	public static double log2(double n) {
		return Math.log(n) / logOf2;
	}

	/**
	 * Reshapes one matrix into a linear array.
	 */
	public static long[] rectify(long[][] expected) {
		return reshape(expected, 1, expected.length * expected[0].length)[0];
	}

	/**
	 * Reshapes one array.
	 * 
	 * @return a double[m][n] array with elements from A.
	 */
	public static long[][] reshape(long[][] A, int m, int n) {
		int origM = A.length;
		int origN = A[0].length;
		if (origM * origN != m * n) {
			throw new IllegalArgumentException("New matrix must be of same area as input matix.");
		}
		long[][] B = new long[m][n];
		long[] A1D = new long[A.length * A[0].length];

		int index = 0;
		for (int i = 0; i < origM; i++) {
			for (int j = 0; j < origN; j++) {
				A1D[index++] = A[i][j];
			}
		}

		index = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				B[j][i] = A1D[index++];
			}

		}
		return B;
	}

	/**
	 * Resizes an array into a new, smaller, one. Eeach cell of the resulting array
	 * contains the sum of cells in the original array that where "shrinked"
	 * together.
	 * 
	 * @return a double[m][n] array with elements from A. If any of m or n are
	 *         bigger than current dimensions, the array is not shrinked in that
	 *         dimension and original dimension is used.
	 */
	public static long[][] shrink(long[][] A, int m, int n) {
		int origM = A.length;
		int origN = A[0].length;
		m = Math.min(m, origM);
		n = Math.min(n, origN);

		long[][] result = new long[m][n];
		for (int i = 0; i < origM; i++) {
			for (int j = 0; j < origN; j++) {
				int r = i * m / origM;
				int c = j * n / origN;
				result[r][c] += A[i][j];
			}
		}

		return result;
	}
}
