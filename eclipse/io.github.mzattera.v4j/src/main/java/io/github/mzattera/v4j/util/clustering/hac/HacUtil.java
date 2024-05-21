/* Copyright (c) 2019-2021 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.util.clustering.hac;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.opencompare.hac.dendrogram.Dendrogram;
import org.opencompare.hac.dendrogram.DendrogramNode;
import org.opencompare.hac.dendrogram.MergeNode;
import org.opencompare.hac.dendrogram.ObservationNode;

/**
 * Various utilities to deal with hac (hierarchical clustering) library.
 * 
 * @author Massimiliano_Zattera
 *
 */
public final class HacUtil {

	/**
	 * 
	 * @return total number of objects (observations) in given dendrogram.
	 */
	public static int getObservationCount(Dendrogram d) {
		return d.getRoot().getObservationCount();
	}

	/**
	 * Split given Dendrogram in n clusters by splitting iteratively the most
	 * diverse node in two clusters.
	 * 
	 * @param n
	 *            number of clusters to return.
	 * @param minSize
	 *            clusters smaller than this size are discarded and not returned.
	 * 
	 * @return Possible empty list of at most n clusters each with at lest minSize
	 *         elements.
	 */
	// TODO maybe make a proper Clusterer instead of this method
	public static List<Cluster<Observation<?>>> split(Dendrogram d, int n, int minSize,
			ClusterableSet<? extends Clusterable> experiment) {

		// make sure there are not too many clusters
		n = Math.min(n, getObservationCount(d));

		List<Cluster<Observation<?>>> result = new ArrayList<Cluster<Observation<?>>>(n);
		List<DendrogramNode> l = new ArrayList<DendrogramNode>(n);

		if (d.getRoot().getObservationCount() < minSize)
			return result;

		// Do the split
		l.add(d.getRoot());
		while ((l.size() > 0) && (l.size() < n)) {

			// Find merged node with biggest dissimilarity
			double maxDistance = Double.MIN_VALUE;
			int maxIdx = -1;
			for (int i = 0; i < l.size(); ++i) {
				DendrogramNode nd = l.get(i);
				if ((nd instanceof MergeNode) && ((MergeNode) nd).getDissimilarity() > maxDistance) {
					maxDistance = ((MergeNode) nd).getDissimilarity();
					maxIdx = i;
				}
			}

			// split it
			DendrogramNode nd = l.remove(maxIdx);
			if (nd.getLeft().getObservationCount() >= minSize)
				l.add(0, nd.getLeft());
			if (nd.getRight().getObservationCount() >= minSize)
				l.add(0, nd.getRight());
		}

		// for each node left after the split, take its observation and make a cluster
		// out of it.
		for (DendrogramNode node : l) {
			Cluster<Observation<?>> cluster = new Cluster<>();
			for (ObservationNode observation : getObservations(node)) {
				cluster.addPoint(new Observation<>(experiment, observation));
			}
			result.add(cluster);
		}

		return result;
	}

	/**
	 * 
	 * @return all observations contained below given node.
	 */
	public static Set<ObservationNode> getObservations(DendrogramNode node) {
		Set<ObservationNode> result = new HashSet<ObservationNode>();

		if (node instanceof ObservationNode) {
			result.add((ObservationNode) node);
		} else {
			result.addAll(getObservations(node.getLeft()));
			result.addAll(getObservations(node.getRight()));
		}

		return result;
	}

	/*
	 * public static List<ObservationNode> leftVisit(Dendrogram d) { return
	 * leftVisit(d.getRoot()); }
	 * 
	 * public static List<ObservationNode> leftVisit(DendrogramNode node) {
	 * List<ObservationNode> result = new ArrayList<ObservationNode>();
	 * 
	 * if (node instanceof ObservationNode) { result.add((ObservationNode) node); }
	 * else { result.addAll(leftVisit(node.getLeft()));
	 * result.addAll(leftVisit(node.getRight())); }
	 * 
	 * return result; }
	 */
}
