/* Copyright (c) 2019-2022 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.util.clustering;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

/**
 * Returns angular distance between two vectors.Vectors can have both positive or negative values.
 * 
 * @author Massimiliano_Zattera
 *
 */
public class AngularDistance implements DistanceMeasure {

	private static final long serialVersionUID = 1L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mattera.v4j.util.clustering.DistanceMeasure#getDistance(double[], double[])
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
			return Math.acos(Math.min(similarity, 1.0)) / Math.PI; // this because similarity can be > 1.0 (for rounding probably)
		}
	}
}
