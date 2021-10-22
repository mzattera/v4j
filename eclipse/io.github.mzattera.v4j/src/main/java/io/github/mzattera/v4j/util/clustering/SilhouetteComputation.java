/**
 * 
 */
package io.github.mzattera.v4j.util.clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

/**
 * Provides method to compute Silhouette Coefficient for clustering evaluation.
 * 
 * This is not a nice or fast implementation, but it works :).
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
// TODO: optimize
public class SilhouetteComputation {

	// all clusters
	private Cluster<?>[] clusters;

	// distance to use for evaluation
	private DistanceMeasure dist;

	// silhouette coefficients for each point.
	private Map<Clusterable, Double> s = new HashMap<>();

	// silhouette coefficients for each cluster.
	private Map<Cluster<?>, Double> cs = new HashMap<>();

	// average silhouette coefficients for all clusters.
	private double silhouette = 0.0;

	/**
	 * Builds an evaluator to evaluate given clusters.
	 * 
	 * @param clusters
	 *            Clusters to evaluate.
	 * @param dist
	 *            Distance measure to use for evaluation.
	 */
	public SilhouetteComputation(Collection<? extends Cluster<?>> clusters, DistanceMeasure dist) {

		List<Cluster<?>> ctmp = new ArrayList<>(clusters);
		this.clusters = (Cluster<?>[]) ctmp.toArray(new Cluster<?>[0]);

		this.dist = dist;

		// check how many non empty clusters are provided
		int nonEmpty = 0;
		for (int i = 0; (i < this.clusters.length) && (nonEmpty < 2); ++i)
			if (this.clusters[i].getPoints().size() > 0)
				++nonEmpty;
		if (nonEmpty < 2) {
			// there is at most one non-empty cluster and one or more empty ones. Mark this
			// as bad clustering.
			for (Cluster<?> c : clusters) {
				for (Clusterable p : c.getPoints()) {
					s.put(p, -1.0);
				}
				cs.put(c, -1.0);
			}
			silhouette = -1.0;
		} else {
			doCalculation();
		}
	}

	/**
	 * Do the actual calculation. Notice this is called only if there are at least 2
	 * non empty clusters in the data set.
	 */
	private void doCalculation() {

		// For each point i calculates a(i) and stores it in a
		Map<Clusterable, Double> a = new HashMap<>();
		for (int k = 0; k < clusters.length; ++k) { // for each cluster
			Clusterable[] p = clusters[k].getPoints().toArray(new Clusterable[0]);
			if (p.length == 1)
				a.put(p[0], 0.0);
			else {
				double ta = 0;
				for (int i = 0; i < p.length; ++i) { // for each point in the cluster
					for (int j = 0; j < p.length; ++j) {
						if (i != j)
							ta += dist.compute(p[i].getPoint(), p[j].getPoint());
					}
					if (Double.isNaN(ta))
						throw new ArithmeticException("a(i) isNaN for " + p[i]);
					a.put(p[i], ta / (p.length - 1));
				}
			}
		}

		// For each point i calculates b(i) and stores it in b
		Map<Clusterable, Double> b = new HashMap<>();
		for (int k = 0; k < clusters.length; ++k) { // for each cluster
			Clusterable[] p = clusters[k].getPoints().toArray(new Clusterable[0]);
			for (int i = 0; i < p.length; ++i) { // for each point in the cluster
				double tb = 0, mintb = Double.MAX_VALUE;
				for (int l = 0; l < clusters.length; ++l) { // for each other cluster
					if ((k == l) || (clusters[l].getPoints().size() == 0))
						continue;
					Clusterable[] p2 = clusters[l].getPoints().toArray(new Clusterable[0]);
					for (int j = 0; j < p2.length; ++j) { // for each point in the other cluster
						tb += dist.compute(p[i].getPoint(), p2[j].getPoint());
					}
					if (Double.isNaN(tb))
						throw new ArithmeticException("b(i) isNaN for " + p[i]);
					tb /= p2.length;
					if (mintb > tb)
						mintb = tb;
				}
				b.put(p[i], mintb);
			}
		}

		// For each point i calculates s(i) and stores it in s
		s = new HashMap<>();
		for (int k = 0; k < clusters.length; ++k) { // for each cluster
			Clusterable[] p = clusters[k].getPoints().toArray(new Clusterable[0]);
			if (p.length == 1)
				s.put(p[0], 0.0); // by definition
			else {
				for (int i = 0; i < p.length; ++i) { // for each point in the cluster
					double ts = (b.get(p[i]) - a.get(p[i])) / Math.max(b.get(p[i]), a.get(p[i]));
					if (Double.isNaN(ts))
						ts = 0.0; // means a(i) and b(i) are 0.0
					s.put(p[i], ts);
				}
			}
		}

		// For each cluster calculates its silhouette and stores it in cs
		cs = new HashMap<>();
		for (int k = 0; k < clusters.length; ++k) { // for each cluster
			Clusterable[] p = clusters[k].getPoints().toArray(new Clusterable[0]);
			if (p.length == 0)
				cs.put(clusters[k], -1.0); // empty clusters are badly formed
			else {
				double ts = 0;
				for (int i = 0; i < p.length; ++i) { // for each point in the cluster
					ts += s.get(p[i]);
				}
				ts /= p.length;
				cs.put(clusters[k], ts);
			}
		}

		// calculate average silhouette for all clusters
		silhouette = 0.0;
		for (Cluster<?> c : clusters)
			silhouette += cs.get(c);
		silhouette /= clusters.length;
	}

	/**
	 * 
	 * @return average Silhouette coefficient over all clusters.
	 */
	public double getSilhouette() {
		return silhouette;
	}

	/**
	 * 
	 * @return average Silhouette coefficient for given cluster.
	 */
	public double getSilhouette(Cluster<?> c) {
		return cs.get(c);
	}

	/**
	 * 
	 * @return average Silhouette coefficient for given data point.
	 */
	public double getSilhouette(Clusterable c) {
		return s.get(c);
	}
}
