/**
 * 
 */
package io.github.mattera.v4j.experiment;

/**
 * A statistical test.
 * 
 * @author Massimiliano_Zattera
 *
 */
public abstract class StatisticalTest<V> {

	/**
	 * Returns the smallest significance level at which one can reject the null
	 * hypothesis.
	 * 
	 * @param observed aggregate measurement on one population.
	 * @param expected aggregate measurement on another (reference) population.
	 */
	public abstract double pValue(V observed, V expected);

	/**
	 * Tests the null hypothesis.
	 * 
	 * @param observed aggregate measurement on one population.
	 * @param expected aggregate measurement on another (reference) population.
	 * @param the maximum chance that the null hypothesis is rejected by mistake.
	 * @return true true if the null hypothesis can be rejected with 100 * (1 -
	 *         alpha) percent confidence.
	 */
	public boolean rejectNullHypothesis(V observed, V expected, double alpha) {
		return pValue(observed, expected) <= alpha;		
	}
}
