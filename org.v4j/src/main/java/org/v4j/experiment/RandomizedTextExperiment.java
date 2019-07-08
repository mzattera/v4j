/**
 * 
 */
package org.v4j.experiment;

import java.util.Collection;

import org.v4j.text.Text;

/**
 * In a RandomizedTextExperiment, the reference population is based on a set of
 * randomly created texts. These texts are based on some RandomizationProcess
 * applied to the population to which the experiment applies.
 * 
 * @author Massimiliano_Zattera
 *
 */
public class RandomizedTextExperiment<T extends Text, V> extends StatisticalHypothesisTestExperiment<T, V> {

	private TextRandomizationProcess<T> randomizer;

	/**
	 * 
	 * @return process used in this experiment to create random texts.
	 */
	public TextRandomizationProcess<T> getTextRandomizationProcess() {
		return randomizer;
	}

	protected void setTextRandomizationProcess(TextRandomizationProcess<T> randomizer) {
		this.randomizer = randomizer;
	}

	// Size of the reference population (times population size)
	// e.g. if this is 3 then the reference population is trice as big as the population.
	private final int refPopSize;

	@Override
	public void setPopulation(Collection<T> population) {
		super.setPopulation(population);
		super.setReferencePopulation(null); // resets reference population
	}

	@Override
	public Collection<T> getReferencePopulation() {

		if (super.getReferencePopulation() == null) {
			super.setReferencePopulation(randomizer.randomize(getPopulation(), refPopSize*getPopulation().size()));
		}

		return super.getReferencePopulation();
	}

	@Override
	public void setReferencePopulation(Collection<T> referencePopulation) {
		throw new UnsupportedOperationException(
				"This experiment creats its own reference population by randomizing its population.");
	}

	public RandomizedTextExperiment(Measurement<T, V> measurement, StatisticalTest<V> test,
			TextRandomizationProcess<T> randomizer, int refPopSize) {
		this(null, measurement, test, randomizer, refPopSize);
	}

	public RandomizedTextExperiment(Collection<T> population, Measurement<T, V> measurement, StatisticalTest<V> test,
			TextRandomizationProcess<T> randomizer, int refPopSize) {
		super(population, measurement, test);
		this.randomizer = randomizer;
		this.refPopSize = refPopSize;
	}
}
