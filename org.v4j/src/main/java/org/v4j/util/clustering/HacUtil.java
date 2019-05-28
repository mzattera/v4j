/**
 * 
 */
package org.v4j.util.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	 * Split given Dendrogram in n clusters
	 * 
	 * @param n
	 *            number of clusters to return.
	 * 
	 * @return a map from each cluster index into set of objects (observations) in
	 *         the cluster.
	 */
	public static Map<Integer, Set<ObservationNode>> split(Dendrogram d, int n) {
		// make sure there are not too many clusters
		n = Math.min(n, getObservationCount(d));

		// Do the split
		List<DendrogramNode> l = new ArrayList<DendrogramNode>(n);
		l.add(d.getRoot());
		while (l.size() < n) {

			// Found merged node with biggest dissimilarity
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
			l.add(0, nd.getLeft());
			l.add(0, nd.getRight());
		}

		// each of the split nodes become the root of the dendrogram for a cluster
		Map<Integer, Set<ObservationNode>> result = new HashMap<Integer, Set<ObservationNode>>(n);
		for (int i = 0; i < l.size(); ++i) {
			result.put(i, getObservations(l.get(i)));
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

	public static List<ObservationNode> leftVisit(Dendrogram d) {
		return leftVisit(d.getRoot());
	}

	public static List<ObservationNode> leftVisit(DendrogramNode node) {
		List<ObservationNode> result = new ArrayList<ObservationNode>();

		if (node instanceof ObservationNode) {
			result.add((ObservationNode) node);
		} else {
			result.addAll(leftVisit(node.getLeft()));
			result.addAll(leftVisit(node.getRight()));
		}

		return result;
	}
}
