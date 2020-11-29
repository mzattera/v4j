/**
 * 
 */
package io.github.mattera.v4j.experiment;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A measurement performs a measure on each member of a population (of type T)
 * and returns an object representing the aggregated result of such measurement
 * (as an object of type V).
 * 
 * @author Massimiliano_Zattera
 *
 */
public abstract class Measurement<T, V> {

	protected final AggregationMethod<V> aggregator;

	/**
	 * Constructor.
	 * 
	 * @param aggregator AggregationMethod to use to merge each single measure into
	 *                   a measure for a whole population.
	 */
	protected Measurement(AggregationMethod<V> aggregator) {
		this.aggregator = aggregator;
	}

	/**
	 * @return the result of applying this measure on a single item.
	 */
	public V measure(T item) {
		Set<T> set = new HashSet<>(1);
		set.add(item);
		return measure(set);
	}

	/**
	 * @return the result of applying this measure on a collection (population) of
	 *         items.
	 */
	public abstract V measure(Collection<T> population);
}
