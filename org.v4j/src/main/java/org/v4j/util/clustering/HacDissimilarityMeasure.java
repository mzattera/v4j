/**
 * 
 */
package org.v4j.util.clustering;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.opencompare.hac.experiment.DissimilarityMeasure;
import org.opencompare.hac.experiment.Experiment;

/**
 * A distance measure.
 * 
 * This is an abstraction class for interoperability between apache clustering API with hac API (Experiment /
 * DissimilarityMeasure).
 * 
 * @author Massimiliano_Zattera
 *
 */
public class HacDissimilarityMeasure implements DissimilarityMeasure {

	private DistanceMeasure m;
	
	/**
	 * 
	 * @param d the DistanceMeasure to use to calculate dissimilarities.
	 */
	public HacDissimilarityMeasure (DistanceMeasure d) {
		m = d;
	}
	
	/**
	 * @return dissimilarity between i-th and j-th observation of Experiment exp.
	 * 
	 * @see
	 * org.opencompare.hac.experiment.DissimilarityMeasure#computeDissimilarity(org.
	 * opencompare.hac.experiment.Experiment, int, int)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public double computeDissimilarity(Experiment exp, int i, int j) {
		Clusterable c1 = ((ClusterableSet)exp).getItem(i);
		Clusterable c2 = ((ClusterableSet)exp).getItem(j);
		
		return m.compute(c1.getPoint(), c2.getPoint());
	}
}
