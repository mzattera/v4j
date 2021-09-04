/**
 * 
 */
package io.github.mattera.v4j.util.clustering;

import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.evaluation.ClusterEvaluator;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

/**
 * This is a wrapper around another MultiKMeansPlusPlusClusterer that performs clustering using
 * different number of clusters and returns best result.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class MultiSizeClusterer<T extends Clusterable> extends Clusterer<T> {

	private int minSize;

	private int maxSize;

	private int numTrials;

	private DistanceMeasure measure;

	private ClusterEvaluator<T> evaluator;

	/**
	 * Constructor.
	 * 
	 * @param minSize minimum number of clusters to try.
	 * @param maxSize maximum number of clusters to try.
	 * @param numTrials for each number of clusters, try the clustering algorithm this many times, and keep the best result.
	 * @param measure distance measure to use when clustering
	 * @param evaluator ClusterEvaluator to use to evaluate how good each clustering is.
	 */
	public MultiSizeClusterer(int minSize, int maxSize, int numTrials, DistanceMeasure measure,
			ClusterEvaluator<T> evaluator) {
		super(measure);

		if (minSize > maxSize)
			throw new IllegalArgumentException("Minimum number of clusters must be less or equal than maximum one.");

		this.minSize = minSize;
		this.maxSize = maxSize;
		this.numTrials = numTrials;
		this.measure = measure;
		this.evaluator = evaluator;
	}

	/**
	 * Performs clustering using the parameters passed in the constructor.
	 */
	@Override
	public List<? extends Cluster<T>> cluster(Collection<T> points)
			throws MathIllegalArgumentException, ConvergenceException {

		List<? extends Cluster<T>> bestResult = null;
		double bestScore = 0.0;
		for (int n = minSize; n <= maxSize; ++n) {
			Clusterer<T> clusterer = new MultiKMeansPlusPlusClusterer<T>(new KMeansPlusPlusClusterer<T>(n, -1, measure),
					numTrials, evaluator);

			List<? extends Cluster<T>> result = clusterer.cluster(points);
			double score = evaluator.score(result);
			if ((bestResult == null) || evaluator.isBetterScore(score, bestScore)) {
				bestResult = result;
				bestScore = score;
			}
		}

		return bestResult;
	}

}
