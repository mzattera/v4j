/**
 * 
 */
package org.v4j.util.clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

/**
 * Provides method to evaluate how good a clustering is.
 * 
 * @author Massimiliano_Zattera
 *
 */
public class ClusterEvaluator {

	private Cluster<?>[] clusters;

	private DistanceMeasure dist;

	// a(i) coefficients for each point.
	private Map<Clusterable, Double> a;

	// b(i) coefficients for each point.
	private Map<Clusterable, Double> b;

	// silhouette coefficients for each point.
	private Map<Clusterable, Double> s;

	// silhouette coefficients for each point.
	private Map<Cluster<?>, Double> cs;

	public ClusterEvaluator(Collection<? extends Cluster<?>> clusters, DistanceMeasure dist) {

		List<Cluster<?>> ctmp = new ArrayList<>(clusters);
		this.clusters = (Cluster[]) ctmp.toArray(new Cluster[0]);

		this.dist = dist;
		doCalculation();
	}

	private void doCalculation() {

		// For each point i calculates a(i) and stores it in a
		a = new HashMap<>();
		for (int k = 0; k < clusters.length; ++k) { // for each cluster
			Clusterable[] p = clusters[k].getPoints().toArray(new Clusterable[0]);
			double ta = 0;
			for (int i = 0; i < p.length; ++i) { // for each point in the cluster
				for (int j = 0; j < p.length; ++j) {
					if (i != j)
						ta += dist.compute(p[i].getPoint(), p[j].getPoint());
				}
				a.put(p[i], ta / (p.length - 1));
			}
		}

		// For each point i calculates b(i) and stores it in b
		b = new HashMap<>();
		for (int k = 0; k < clusters.length; ++k) { // for each cluster
			Clusterable[] p = clusters[k].getPoints().toArray(new Clusterable[0]);
			for (int i = 0; i < p.length; ++i) { // for each point in the cluster
				double tb = 0, mintb = Double.MAX_VALUE;
				for (int l = 0; l < clusters.length; ++l) { // for each other cluster
					if (k == l)
						continue;
					Clusterable[] p2 = clusters[l].getPoints().toArray(new Clusterable[0]);
					for (int j = 0; j < p2.length; ++j) { // for each point in the other cluster
						tb += dist.compute(p[i].getPoint(), p2[j].getPoint());
					}
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
				s.put(p[0], 0.0);
			else {
				for (int i = 0; i < p.length; ++i) { // for each point in the cluster
					double ts = (b.get(p[i]) - a.get(p[i])) / Math.max(b.get(p[i]), a.get(p[i]));
					s.put(p[i], ts);
				}
			}
		}

		// For each cluster calculates its silhouette and stores it in cs
		cs = new HashMap<>();
		for (int k = 0; k < clusters.length; ++k) { // for each cluster
			Clusterable[] p = clusters[k].getPoints().toArray(new Clusterable[0]);
			double ts = 0;
			for (int i = 0; i < p.length; ++i) { // for each point in the cluster
				ts += s.get(p[i]);
			}
			ts /= p.length;
			cs.put(clusters[k], ts);
		}
	}
	
	public double getSilhouette (Cluster<?> c) {
		return cs.get(c);
	}
	
	public double getSilhouette (Clusterable c) {
		return s.get(c);
	}
}
