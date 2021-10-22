/**
 * 
 */
package io.github.mzattera.v4j;

/**
 * A class which instances have unique string IDs.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public interface Identifiable {

	/**
	 * 
	 * @return a unique identifier for this instance.
	 */
	public String getId();
}
