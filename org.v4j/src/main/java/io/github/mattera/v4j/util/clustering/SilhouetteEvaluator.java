/**
 * 
 */
package io.github.mattera.v4j.util.clustering;

import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.evaluation.ClusterEvaluator;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

/**
 * This uses Silhouette Index to evaluate how good a clustering is.
 * 
 * This to bridge my SilhouetteCalculator with Apache math libraries.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class SilhouetteEvaluator<T extends Clusterable> extends ClusterEvaluator<T> {

	// distance to use for evaluation
	private DistanceMeasure dist;

	/**
	 * Builds an evaluator.
	 * 
	 * @param dist
	 *            Distance measure to use for evaluation.
	 */
	public SilhouetteEvaluator(DistanceMeasure dist)  {
		super (dist); // why the superclass doesn't let us fetch the distance?
		this.dist = dist;
	}
	
	@Override
	public double score(List<? extends Cluster<T>> clusters) {
		SilhouetteComputation eval = new SilhouetteComputation(clusters, dist);
		return -eval.getSilhouette(); // see comments below
	}

/*	Don-t ask me why, but if I change this to follow same ordering than Silhouette index, it fails.
 * Therefore, I just changed sign of Silhouette Index in score()
 * @Override
	public boolean isBetterScore(double score1, double score2) {
		// must override because Silhouette logic is inverse than default.
		return (score1 > score2);
	}*/
}
