/**
 * 
 */
package io.github.mattera.v4j.experiment;

import java.util.Collection;

/**
 * Results of running a measure on a single item can be aggregated together in a
 * single measure result. An AggregationMethod is a way to aggregate several
 * measures into a single one.
 * 
 * @author Massimiliano_Zattera
 *
 */
public interface AggregationMethod<V> {
	
	/**
	 * @return The result of aggregating all measures into one.
	 */
	public V aggregate(Collection<V> measures);
}
