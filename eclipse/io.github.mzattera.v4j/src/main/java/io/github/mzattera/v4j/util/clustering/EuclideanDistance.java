/**
 * 
 */
package io.github.mzattera.v4j.util.clustering;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

/**
 * Returns angular distance between two vectors. The vectors are assumed to have
 * only positive components (e.g. when they represent word frequencies in a
 * text).
 * 
 * @author Massimiliano_Zattera
 *
 */
public class EuclideanDistance implements DistanceMeasure {

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * io.github.mattera.v4j.util.clustering.DistanceMeasure#getDistance(double[],
	 * double[])
	 */
	@Override
	public double compute(double[] x, double[] y) throws DimensionMismatchException {
		if (x.length != y.length)
			throw new DimensionMismatchException(y.length, x.length);

		double sum = 0.0d;
		for (int i = 0; i < x.length; ++i) {
			sum += (x[i] - y[i]) * (x[i] - y[i]);
		}

		return Math.sqrt(sum);
	}
}
