/**
 * 
 */
package io.github.mattera.v4j.experiment;

import java.util.Collection;

/**
 * A StatisticalExperiment is a special kind of experiment where the measurement
 * on the population is compared with same measure performed on a "reference
 * population", to assess if the population is just a sample of the reference
 * population. Basically, the reference population represent the "null
 * hypothesis" distribution, while the experiment population is the sampling
 * distribution. The method runExperiment() will return true if the population
 * satisfies the null hypothesis.
 * 
 * @author Massimiliano_Zattera
 *
 */
public abstract class StatisticalHypothesisTestExperiment<T, V> extends Experiment<T, V, Boolean> {

	private Collection<T> referencePopulation = null;

	/**
	 * @return the reference population used in this experiment.
	 */
	public Collection<T> getReferencePopulation() {
		return referencePopulation;
	}

	/**
	 * Sets the reference population used in this experiment.
	 */
	public void setReferencePopulation(Collection<T> referencePopulation) {
		this.referencePopulation = referencePopulation;
	}

	private StatisticalTest<V> test;

	/**
	 * 
	 * @return the statistical test used in this experiment.
	 */
	public StatisticalTest<V> getStatisticalTest() {
		return test;
	}

	protected void setStatisticalTest(StatisticalTest<V> test) {
		this.test = test;
	}

	private static final double DEFAULT_ALPHA = 0.05;

	private double alpha = DEFAULT_ALPHA;

	/**
	 * 
	 * @return confidence for the statistical test.
	 */
	public double getAlpha() {
		return alpha;
	}

	/**
	 * Sets confidence for the statistical test.
	 * 
	 * @param alpha
	 */
	protected void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	/**
	 * @return true if the experiment is set up, that is, ready to be run.
	 */
	@Override
	public boolean isSetup() {
		return super.isSetup() && (getReferencePopulation() != null) && (getStatisticalTest() != null);
	}

	/**
	 * Constructor. The experiment still needs to be set up before running.
	 */
	protected StatisticalHypothesisTestExperiment(Measurement<T, V> measurement, StatisticalTest<V> test) {
		this(null, null, measurement, test);
	}

	/**
	 * Constructor. The experiment still needs to be set up before running.
	 */
	protected StatisticalHypothesisTestExperiment(Collection<T> population, Measurement<T, V> measurement,
			StatisticalTest<V> test) {
		this(population, null, measurement, test);
	}

	protected StatisticalHypothesisTestExperiment(Collection<T> population, Collection<T> referencePopulation,
			Measurement<T, V> measurement, StatisticalTest<V> test) {
		this(population, referencePopulation, measurement, test, DEFAULT_ALPHA);
	}

	protected StatisticalHypothesisTestExperiment(Collection<T> population, Collection<T> referencePopulation,
			Measurement<T, V> measurement, StatisticalTest<V> test, double alpha) {
		super(population, measurement);
		this.referencePopulation = referencePopulation;
		this.test = test;
		this.alpha = alpha;
	}

	/**
	 * 
	 * @return the result of applying the measurement to the reference population.
	 */
	public V measureReferencePopulation() {
		return getMeasurement().measure(getReferencePopulation());
	}

	/**
	 * Tests the null hypothesis that population and reference population conform to
	 * the same distribution, or that the population conforms to the reference
	 * population.
	 * 
	 * @return true true if the null hypothesis can be rejected with 100 * (1 -
	 *         alpha) percent confidence.
	 */
	public boolean rejectNullHypothesis() {
		if (!isSetup())
			throw new IllegalStateException("Experiment is not properly set up.");
		return getStatisticalTest().rejectNullHypothesis(measure(), getMeasurement().measure(getReferencePopulation()),
				alpha);
	}

	/**
	 * Returns the smallest significance level at which one can reject the null
	 * hypothesis, based on the current population and reference population.
	 */
	public double pValue() {
		if (!isSetup())
			throw new IllegalStateException("Experiment is not properly set up.");
		return getStatisticalTest().pValue(measure(), getMeasurement().measure(getReferencePopulation()));
	}

	/**
	 * @return true true if the null hypothesis can be rejected with 100 * (1 -
	 *         alpha) percent confidence.
	 */
	@Override
	public Boolean runExperiment() {
		return rejectNullHypothesis();
	}
}
