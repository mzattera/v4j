/**
 * 
 */
package org.v4j.util.clustering;

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
public class PositiveAngularDistance implements DistanceMeasure {

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.v4j.util.clustering.DistanceMeasure#getDistance(double[], double[])
	 */
	@Override
	public double compute(double[] x, double[] y) throws DimensionMismatchException {
		if (x.length != y.length)
			throw new DimensionMismatchException(y.length, x.length);

		double a2 = 0.0, b2 = 0.0, ab = 0.0;
		for (int i = 0; i < x.length; ++i) {
			a2 += x[i] * x[i];
			b2 += y[i] * y[i];
			ab += x[i] * y[i];
		}

		double similarity = ab / Math.sqrt(a2 * b2);
		if (Double.isNaN(similarity)) {
			return 0.0;
		} else {
			return 2 * Math.acos(similarity) / Math.PI;
		}
	}
}
