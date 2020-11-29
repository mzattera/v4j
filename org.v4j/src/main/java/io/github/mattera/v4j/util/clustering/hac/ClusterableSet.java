/**
 * 
 */
package io.github.mattera.v4j.util.clustering.hac;

import java.util.Collection;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.opencompare.hac.experiment.Experiment;

/**
 * A set of objects that can be clustered.
 * 
 * This is an abstraction class for interoperability between Apache clustering
 * API with hac API (Experiment / DissimilarityMeasure).
 * 
 * @author Massimiliano_Zattera
 *
 */
public interface ClusterableSet<T extends Clusterable> extends Experiment {

	/**
	 * 
	 * @return total number of items in this set.
	 */
	public int size();

	/**
	 * 
	 * @return all Clusterable items in this set.
	 */
	public Collection<T> getItems();

	/**
	 * 
	 * @return the i-th Clusterable object in this set.
	 */
	public T getItem(int i);
}
