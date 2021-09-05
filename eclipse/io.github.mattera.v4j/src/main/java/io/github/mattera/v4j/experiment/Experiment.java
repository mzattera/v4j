/**
 * 
 */
package io.github.mattera.v4j.experiment;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An experiment is a class that can perform measurements on a set of items of
 * type T ("population"). Measurements are returned as objects of type V. Based
 * on these measurements, the experiment is conducted and its results returned
 * (as an object of type R). Before being useful, an experiment needs to be set
 * up by assigning its population to it.
 * 
 * @author Massimiliano_Zattera
 *
 */
public abstract class Experiment<T, V, R> {

	private Collection<T> population = null;

	/**
	 * @return the population used in this experiment.
	 */
	public Collection<T> getPopulation() {
		return population;
	}

	/**
	 * Sets the population used in this experiment to a single item.
	 * Used mostly for testing.
	 */
	public void setPopulation(T item) {
		this.population = new ArrayList<T>(1);
		this.population.add(item);
	}

	/**
	 * Sets the population used in this experiment.
	 */
	public void setPopulation(Collection<T> population) {
		this.population = population;
	}

	private Measurement<T, V> measurement;

	/**
	 * @return the measurement used in this experiment.
	 */
	public Measurement<T, V> getMeasurement() {
		return measurement;
	}

	protected void setMeasurement(Measurement<T, V> measurement) {
		this.measurement = measurement;
	}
	
	/**
	 * @return true if the experiment is set up, that is, ready to be run.
	 */
	public boolean isSetup() {
		return getPopulation() != null;
	}

	/**
	 * Constructor.
	 * The experiment still needs to be set up before running.
	 */
	protected Experiment(Measurement<T, V> measurement) {
		this(null, measurement);
	}

	/**
	 * Constructor. Creates an experiment that is already setup with given
	 * population and measurement.
	 */
	protected Experiment(Collection<T> population, Measurement<T, V> measurement) {
		this.population = population;
		this.measurement = measurement;
	}

	/**
	 * 
	 * @return the result of applying the measurement to the population.
	 */
	public V measure() {
		return getMeasurement().measure(population);
	}
	
	/**
	 * Runs the experiment. 
	 * 
	 * @return result of running the experiment.
	 */
	public abstract R runExperiment();
}
