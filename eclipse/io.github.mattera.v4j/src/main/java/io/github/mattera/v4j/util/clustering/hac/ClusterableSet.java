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
 * API, which requires Clusterable items, and hac API, which needs an Experiment as an input.
 * 
 * This class represents a collection of Clusterable items as an Experiment.
 *  
 * @author Massimiliano "Maxi" Zattera
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
