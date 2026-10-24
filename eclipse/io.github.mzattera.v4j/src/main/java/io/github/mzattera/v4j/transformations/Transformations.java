package io.github.mzattera.v4j.transformations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import io.github.mzattera.v4j.util.Counter;

/**
 * "Static" class defining which transformations describe interesting words in
 * different places of the Voynich. It also contains method to apply these
 * transformations.
 * 
 * See https://mzattera.github.io/v4j/013/
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class Transformations {

	/**
	 * Different loci in the manuscript where interesting words might be.
	 */
	public enum Locus {
		FIRST_WORD_IN_LINE, FIRST_LINE_IN_PARAGRAPH, LAST_WORD_IN_LINE, SECOND_WORD_IN_LINE
	}

	private Transformations() {
	}

	/**
	 * This maps each combination of a manuscript locus and cluster, with
	 * transformations defining interesting words at that locus for that cluster.
	 */
	private final static Map<Pair<Locus, String>, List<Transformation>> TRANSFORMATIONS = new HashMap<>();
	static {
		List<Transformation> l;

		///////////// FIRST_LINE_IN_PARAGRAPH

		l = new ArrayList<>();
		l.add(new InsertAtSlotTransformation(3, "p"));
		l.add(new InsertAtSlotTransformation(15, "p"));
		l.add(new InsertAtSlotTransformation(5, "P"));
		l.add(new InsertAtSlotTransformation(3, "f"));
		l.add(new PrefixTransformation("t", 'S'));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.FIRST_LINE_IN_PARAGRAPH, "HA"), l);

		l = new ArrayList<>();
		l.add(new InsertAtSlotTransformation(3, "p"));
		l.add(new InsertAtSlotTransformation(3, "f"));
		l.add(new InsertAtSlotTransformation(5, "P"));
		l.add(new PrefixTransformation("t", 'o'));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.FIRST_LINE_IN_PARAGRAPH, "PA"), l);

		l = new ArrayList<>();
		l.add(new InsertAtSlotTransformation(3, "p"));
		l.add(new InsertAtSlotTransformation(15, "p"));
		l.add(new InsertAtSlotTransformation(3, "f"));
		l.add(new PrefixTransformation("t", 'S'));
		l.add(new PrefixTransformation("t", 'C'));
		l.add(new PrefixTransformation("k", 'S'));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.FIRST_LINE_IN_PARAGRAPH, "HB"), l);

		l = new ArrayList<>();
		l.add(new InsertAtSlotTransformation(3, "p"));
		l.add(new InsertAtSlotTransformation(15, "p"));
		l.add(new InsertAtSlotTransformation(3, "f"));
		l.add(new InsertAtSlotTransformation(5, "P"));
		l.add(new PrefixTransformation("k", 'C'));
		l.add(new PrefixTransformation("k", 'o'));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.FIRST_LINE_IN_PARAGRAPH, "BB"), l);

		l = new ArrayList<>();
		l.add(new InsertAtSlotTransformation(3, "p"));
		l.add(new InsertAtSlotTransformation(3, "f"));
		l.add(new PrefixTransformation("t", 'C'));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.FIRST_LINE_IN_PARAGRAPH, "SB"), l);

		///////////// FIRST_WORD_IN_LINE

		l = new ArrayList<>();
		l.add(new PrefixTransformation("y", 'S'));
		l.add(new PrefixTransformation("y", 'C'));
		l.add(new PrefixTransformation("y", 'd'));
		l.add(new PrefixTransformation("o", 'C'));
		l.add(new PrefixTransformation("o", 'a'));
		l.add(new PrefixTransformation("t", 'o'));
		l.add(new PrefixTransformation("d", 'S'));
		l.add(new InsertAtSlotTransformation(0, "s"));
		l.add(new InsertAtSlotTransformation(18, "E"));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.FIRST_WORD_IN_LINE, "HA"), l);

		l = new ArrayList<>();
		l.add(new PrefixTransformation("d", 'S'));
		l.add(new PrefixTransformation("d", 'C'));
		l.add(new PrefixTransformation("y", 'o'));
		l.add(new PrefixTransformation("y", 'C'));
		l.add(new PrefixTransformation("t", 'o'));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.FIRST_WORD_IN_LINE, "PA"), l);

		l = new ArrayList<>();
		l.add(new PrefixTransformation("d", 'C'));
		l.add(new PrefixTransformation("d", 'S'));
		l.add(new PrefixTransformation("s", 'a'));
		l.add(new PrefixTransformation("s", 'o'));
		l.add(new PrefixTransformation("y", 'S'));
		l.add(new PrefixTransformation("y", 't'));
		l.add(new PrefixTransformation("t", 'C'));
		l.add(new PrefixTransformation("t", 'o'));
		l.add(new PrefixTransformation("t", 'S'));
		l.add(new PrefixTransformation("l", 'C'));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.FIRST_WORD_IN_LINE, "HB"), l);

		l = new ArrayList<>();
		l.add(new PrefixTransformation("d", 'C'));
		l.add(new PrefixTransformation("d", 'S'));
		l.add(new PrefixTransformation("y", 'C'));
		l.add(new PrefixTransformation("y", 'S'));
		l.add(new PrefixTransformation("s", 'o'));
		l.add(new PrefixTransformation("s", 'S'));
		l.add(new PrefixTransformation("p", 'C'));
		l.add(new PrefixTransformation("p", 'o'));
		l.add(new PrefixTransformation("t", 'o'));
		l.add(new PrefixTransformation("t", 'C'));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.FIRST_WORD_IN_LINE, "BB"), l);

		l = new ArrayList<>();
		l.add(new PrefixTransformation("d", 'a'));
		l.add(new PrefixTransformation("d", 'S'));
		l.add(new PrefixTransformation("d", 'C'));
		l.add(new PrefixTransformation("y", 'S'));
		l.add(new PrefixTransformation("y", 'C'));
		l.add(new PrefixTransformation("s", 'a'));
		l.add(new PrefixTransformation("s", 'o'));
		l.add(new PrefixTransformation("S", 'o'));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.FIRST_WORD_IN_LINE, "SB"), l);

		///////////// LAST_WORD_IN_LINE

		l = new ArrayList<>();
		l.add(new InsertAtSlotTransformation(10, "m"));
		l.add(new InsertBeforeTransformation('m', '.', true));
		l.add(new SuffixTransformation("y", 'r'));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.LAST_WORD_IN_LINE, "HA"), l);

		l = new ArrayList<>();
		l.add(new InsertBeforeTransformation('m', '.', true));
		l.add(new SuffixTransformation("y", 'r'));
		l.add(new PrefixTransformation("r", 'a'));
		l.add(new InsertBeforeTransformation('l', 's', true));
		l.add(new InsertBeforeTransformation('l', 'd', true));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.LAST_WORD_IN_LINE, "PA"), l);

		l = new ArrayList<>();
		l.add(new SuffixTransformation("y", 'r'));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.LAST_WORD_IN_LINE, "HB"), l);

		l = new ArrayList<>();
		l.add(new InsertAtSlotTransformation(10, "m"));
		l.add(new InsertBeforeTransformation('m', '.', true));
		l.add(new SuffixTransformation("y", 'r'));
		l.add(new SuffixTransformation("y", 'l'));
		l.add(new PrefixTransformation("l", 'o'));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.LAST_WORD_IN_LINE, "BB"), l);

		l = new ArrayList<>();
		l.add(new SuffixTransformation("m", 'o'));
		l.add(new InsertBeforeTransformation('m', '.', true));
		l.add(new SuffixTransformation("y", 'r'));
		l.add(new InsertBeforeTransformation('l', 'd', true));
		l.add(new InsertBeforeTransformation('l', 'y', true));
		l.add(new InsertBeforeTransformation('t', 'y', true));
		TRANSFORMATIONS.put(new ImmutablePair<>(Locus.LAST_WORD_IN_LINE, "SB"), l);
	}

	/**
	 * @return All transformations as a map mapping each cluster and locus pair into
	 *         corresponding list of transformations.
	 */
	public static Map<Pair<Locus, String>, List<Transformation>> getTransformations() {
		return Map.copyOf(TRANSFORMATIONS);
	}

	/**
	 * @return Transformations for a given cluster and locus.
	 */
	public static List<Transformation> getTransformations(String cluster, Locus locus) {
		return List.copyOf(TRANSFORMATIONS.get(new ImmutablePair<>(locus, cluster)));
	}

	/**
	 * @param transformations Transformations to apply, in the specific order they
	 *                        are provided.
	 * 
	 * @return Words that are the results of applying the transformations.
	 */
	public static Counter<String> getTransformedWords(Counter<String> sample,
			List<? extends Transformation> transformations) {

		Counter<String> result = new Counter<>();
		for (Counter<String> c : Transformations.getTransformationMap(sample, transformations).values()) {
			result.countAll(c);
		}

		return result;
	}

	/**
	 * @return Words that are the results of applying the transformation.
	 */
	public static Counter<String> getTransformedWords(Counter<String> sample, Transformation t) {

		Counter<String> result = new Counter<>();
		for (Entry<String, Integer> e : sample.entrySet()) {
			if (t.revert(e.getKey()) != null)
				result.count(e.getKey(), e.getValue());
		}

		return result;
	}

	/**
	 * @param transformations Transformations to apply, in the specific order they
	 *                        are provided.
	 * 
	 * @return A map from each transformation into words that seem the result of the
	 *         transformation.
	 */
	public static Map<Transformation, Counter<String>> getTransformationMap(Counter<String> words,
			List<? extends Transformation> transformations) {
		return Transformations.getTransformationMap(words, null, transformations, 0);
	}

	/**
	 * @param transformations         Transformations to apply, in the specific
	 *                                order they are provided.
	 * @param population              Population (for comparison, see below) can be
	 *                                null if minRevertedInPopulation is 0.
	 * @param minRevertedInPopulation Returns only words such that, after
	 *                                transformation has been reverted, the
	 *                                "reverted" word appears in population at least
	 *                                this many times,
	 * 
	 * @return A map from each transformation into words that seem the result of the
	 *         transformation.
	 */
	public static Map<Transformation, Counter<String>> getTransformationMap(Counter<String> words,
			Counter<String> population, List<? extends Transformation> transformations, int minRevertedInPopulation) {

		if ((population == null) && (minRevertedInPopulation > 0))
			throw new IllegalArgumentException();
		if ((population != null) && (minRevertedInPopulation < 0))
			throw new IllegalArgumentException();

		Map<Transformation, Counter<String>> result = new HashMap<>();
		Counter<String> myWords = new Counter<>(words);

		for (Transformation r : transformations) {
			if (myWords.isEmpty())
				break;

			for (String w : new ArrayList<>(myWords.itemSet())) {

				String reverted = r.revert(w);
				if (reverted == null)
					continue;

				if ((minRevertedInPopulation > 0) && (population.getCount(reverted) < minRevertedInPopulation))
					continue;

				Counter<String> c2 = result.get(r);
				if (c2 == null) {
					c2 = new Counter<>();
					result.put(r, c2);
				}
				c2.count(w, myWords.getCount(w));
				myWords.remove(w);
			}
		}

		return result;
	}
}