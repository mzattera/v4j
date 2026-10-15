package io.github.mzattera.v4j.transformations;

/**
 * Represents a generic transformation.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public interface Transformation {

	/**
	 * Reverse this transformation, returning a word as it was before this
	 * transformation was applied to it.
	 * 
	 * @return Given word, after reverting this transformation to it, or null if the
	 *         transformation could not be applied.
	 */
	String revert(String w);

	/**
	 * This is a convenience method to create a set of transformations to be used in
	 * further analysis.
	 * 
	 * @return A string representing this transformation as a element in a List.
	 */
	String toListElement();
}