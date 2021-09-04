/**
 * 
 */
package org.v4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import io.github.mattera.v4j.util.clustering.PositiveAngularDistance;
import io.github.mattera.v4j.util.clustering.SilhouetteComputation;

/**
 * Tests clustering and Silhouette.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class KMeansClusteringTest implements RegressionTest {

	private class MyPoint implements Clusterable {

		double[] point;

		public MyPoint(double[] point) {
			this.point = point;
		}

		@Override
		public double[] getPoint() {
			return point;
		}

		public String toString() {
			StringBuffer result = new StringBuffer("[ ");
			for (int i = 0; i < point.length; ++i) {
				result.append(point[i]).append(" ");
			}
			return result.append("]").toString();
		}
	}

	/**
	 * 
	 */
	public KMeansClusteringTest() {
		double d, p[];

		// Create 2 sets of orthogonal vectors
		Collection<MyPoint> points = new ArrayList<>();
		for (int n = 1; n <= 5; ++n) {
			d = 1.0 + Math.random() / 5.0 - 0.1;
			p = new double[2];
			p[0] = 0.0;
			p[1] = d;
			points.add(new MyPoint(p));
			p = new double[2];
			p[0] = d;
			p[1] = 0.0;
			points.add(new MyPoint(p));
		}

		// add a "Strange" point in the middle
		d = 1.0 + Math.random() / 5.0 - 0.1;
		p = new double[2];
		p[0] = d;
		p[1] = d;
		points.add(new MyPoint(p));

		// Create 2 clusters based on these points
		// DistanceMeasure dist = new EuclideanDistance();
		DistanceMeasure dist = new PositiveAngularDistance();
		KMeansPlusPlusClusterer<MyPoint> clusterer = new KMeansPlusPlusClusterer<>(2, 30, dist);
		List<CentroidCluster<MyPoint>> clusters = clusterer.cluster(points);

		// create evaluator
		SilhouetteComputation eval = new SilhouetteComputation(clusters, dist);

		System.out.println("Number of clusters: " + clusters.size() + ": s = " + eval.getSilhouette());
		for (int i = 0; i < clusters.size(); ++i) {
			System.out.println("Cluster: " + i + ": s = " + eval.getSilhouette(clusters.get(i)));
			for (MyPoint mp : clusters.get(i).getPoints())
				System.out.println(mp + " -> " + i + " (s = " + eval.getSilhouette(mp) + ")");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.github.mattera.v4j.RegressionTest#doTest()
	 */
	@Override
	public void doTest() throws Exception {
		// TODO Auto-generated method stub

	}

}
