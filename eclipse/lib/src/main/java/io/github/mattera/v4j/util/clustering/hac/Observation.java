/**
 * 
 */
package io.github.mattera.v4j.util.clustering.hac;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.opencompare.hac.dendrogram.ObservationNode;

/**
 * This is a wrapper around an observation in hac to expose it as a Clusterable.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class Observation<T extends Clusterable> implements Clusterable {

	private int observation;

	/**
	 * @return the observation
	 */
	public int getObservation() {
		return observation;
	}

	private T item = null;

	/**
	 * @return the item in the Experiment associated to this observation.
	 */
	public T getItem() {
		return item;
	}

	/**
	 * Creates a Clusterable object from given observation. Notice that if the
	 * instance is created with this method getItem() will return null.
	 */
	public Observation(int observation) {
		this(null, observation);
	}

	/**
	 * Creates a Clusterable object from given observation in given experiment.
	 */
	public Observation(ClusterableSet<T> experiment, ObservationNode node) {
		this(experiment, node.getObservation());
	}

	/**
	 * Creates a Clusterable object from given observation in given experiment.
	 */
	public Observation(ClusterableSet<T> experiment, int observation) {
		this.observation = observation;
		if (experiment != null)
			this.item = experiment.getItem(observation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.commons.math3.ml.clustering.Clusterable#getPoint()
	 */
	@Override
	public double[] getPoint() {
		return item.getPoint();
	}
}
