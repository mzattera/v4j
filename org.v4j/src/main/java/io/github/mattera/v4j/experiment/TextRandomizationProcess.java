/**
 * 
 */
package io.github.mattera.v4j.experiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.github.mattera.v4j.text.Text;

/**
 * A TextRandomizationProcess produces a random text by starting from an
 * existing one.
 * 
 * @author Massimiliano_Zattera
 *
 */
public abstract class TextRandomizationProcess<T extends Text> {

	// Random number generator to be used in any randomization process.
	// Using this Random instance might ensure repeatability of experiments.
	private final Random rnd;

	/**
	 * 
	 * @return Random number generator to be used by this class in any randomization
	 *         process. Using this Random instance might ensure repeatability of
	 *         experiments.
	 */
	public Random getRandom() {
		return rnd;
	}

	protected TextRandomizationProcess() {
		this(new Random(System.currentTimeMillis()));
	}

	protected TextRandomizationProcess(Random rnd) {
		this.rnd = rnd;
	}

	/**
	 * @return a randomized version of given text.
	 */
	public abstract T randomize(T txt);

	/**
	 * @return a set of n text, each a randomized version of a text in given
	 *         collection.
	 */
	public List<T> randomize(Collection<T> txt, int n) {
		List<T> population = new ArrayList<>(txt);

		// Builds reference population by randomizing samples from the population.
		List<T> result = new ArrayList<>(n);
		int idx = 0; // we try to ensure we take all samples in population with same frequency
		for (int i = 0; i < n; ++i) {
			T t = population.get(idx);
			idx = (idx+1) % txt.size();
			result.add(randomize(t));
		}

		Collections.shuffle(result, rnd);
		return result;
	}
}
