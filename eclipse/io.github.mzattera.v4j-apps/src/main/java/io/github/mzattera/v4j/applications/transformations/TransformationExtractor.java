/* Copyright (c) 2026 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.applications.transformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.stat.inference.AlternativeHypothesis;
import org.apache.commons.math3.stat.inference.BinomialTest;

import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermClassification;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermDecomposition;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageFilter;
import io.github.mzattera.v4j.text.ivtff.PageHeader;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.transformations.Transformation;
import io.github.mzattera.v4j.transformations.Transformations;
import io.github.mzattera.v4j.util.Counter;

/**
 * Parent abstract class for all transformation extractors.
 * 
 * A transformation extractor is able to examine some text to extract
 * transformations patterns in the sample population, compared to the standard
 * population.
 * 
 * Its {@link #main(String[])} method systematically extract the best possible
 * set of transformations from one text.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public abstract class TransformationExtractor {
	
	////// Below parameters to configure experiments and transformation extractors 

	// If true, consider only readable words
	public final static boolean READABLE_ONLY = true;

	// If true, skip second word in line when creating standard population
	private final static boolean SKIP_SECOND_WORD = true;

	// If true, skip first line of paragraphs when creating samples
	private final static boolean SKIP_FIRST_LINE = true;

	public static final Experiment FIRST_WORD_IN_LINE_EXPERIMENT = new Experiment.WithStandardPopulation(
			new Experiment.FirstWordInLine(SKIP_FIRST_LINE, READABLE_ONLY), READABLE_ONLY, SKIP_SECOND_WORD);

	public static final Experiment FIRST_LINE_IN_PARAGRAPH_EXPERIMENT = new Experiment.WithStandardPopulation(
			new Experiment.FirstLineInParagraph(false), READABLE_ONLY, SKIP_SECOND_WORD);

	public static final Experiment LAST_WORD_IN_LINE_EXPERIMENT = new Experiment.WithStandardPopulation(
			new Experiment.LastWordInLine(SKIP_FIRST_LINE, READABLE_ONLY), READABLE_ONLY, SKIP_SECOND_WORD);

	public static final Experiment SECOND_WORD_IN_LINE_EXPERIMENT = !SKIP_SECOND_WORD ? null
			: new Experiment.WithStandardPopulation(
					new Experiment.WordInPositionInLine(1, SKIP_FIRST_LINE, true, READABLE_ONLY), READABLE_ONLY,
					SKIP_SECOND_WORD);

	// Number of times a word resulting from reverse transformation must appear in
	// population for the transformation to be considered valid
	public static final int MIN_POPULATION_COUNT = 3;

	// Number of times (TOKENS) a transformation must be used not to be filtered out
	// during
	// transformation validation. Notice this is further constrained in code by the
	// number of interesting words
	public static final int MIN_TOKEN_COUNT = 3;

	// Alpha cut-off for transformation significance
	public static final double ALPHA = 0.01;

	// Further cut off; if ratio between % of sample types and % population types a
	// transformation applies to is less than this, ignore the transformation.
	public static final double PERCENT_THRESHOLD = 5.0d;

	public static final BinomialTest BINOMIAL = new BinomialTest();
	
	/**
	 * Runs complete analysis of given text. The analysis is done separately for
	 * each cluster.
	 * 
	 * @param experiment An experiment used to split text into sample and
	 *                   population.
	 * @return A map mapping each cluster to a pair of doubles with % of words to
	 *         which transformations apply in sample and population.
	 */
	public Map<String, Pair<Double, Double>> process(IvtffText doc, Experiment experiment) {

		Map<String, Pair<Double, Double>> result = new HashMap<>();

		for (String cluster : PageHeader.CLUSTERS) {
			System.out.println("===[Cluster " + cluster + "]===\n");

			// Get sample and reference population
			IvtffText clusterText = doc.filterPages(new PageFilter.Builder().cluster(cluster).build());
			result.put(cluster, processCluster(cluster, clusterText, experiment));
		}

		return result;
	}

	/**
	 * @return A pair of doubles with % of words to which transformations apply in
	 *         sample and population.
	 */
	protected Pair<Double, Double> processCluster(String cluster, IvtffText clusterText, Experiment experiment) {

		Text[] split = experiment.splitDocument(clusterText);
		Counter<String> sample = split[0].getWords(READABLE_ONLY);
		Counter<String> population = split[1].getWords(READABLE_ONLY);

		// Interesting words
		Counter<String> interesting = Experiment.getInterestingWords(sample, population);
		Counter<String> uninteresting = new Counter<>(); // NON-intersting words in the sample
		for (Entry<String, Integer> w : sample.entrySet())
			if (interesting.getCount(w.getKey()) == 0)
				uninteresting.count(w.getKey(), w.getValue());

		List<? extends Transformation> transformations = extractValidatedTransformations(split[0],
				split[1]);
//		List<? extends Transformation> transformations = extractValidatedTransformations(new TextString(interesting), split[1]);

		System.out.println(
				"Population :\t" + population.getTotalCounted() + " tokens\t" + population.itemCount() + " word types");
		System.out.println(
				"Sample     :\t" + sample.getTotalCounted() + " tokens\t" + sample.itemCount() + " word types");
		System.out.println("Interesting:\t" + interesting.getTotalCounted() + " tokens\t" + interesting.itemCount()
				+ " word types");
//		System.out.println("Uninteresting:\t" + uninteresting.getTotalCounted() + " tokens\t" + uninteresting.itemCount() + " word types");
		System.out.println();

//		System.out.println("Applied transformations for population:");
//		System.out.println();
//		printTransformationStats(population, transformations);

//		System.out.println();
//		System.out.println("Extracted transformations applied to sample:");
//		System.out.println();
		printTransformationStats(sample, transformations);

//		System.out.println();
//		System.out.println("Applied transformations for sample (STRICT):");
//		System.out.println();
//		printTransformationStats(sample, population, transformations, 1);
//
//		System.out.println();
//		System.out.println("Final Rules: ");
//		System.out.println();
//		for (Transformation t : transformations) {
//			System.out.println(t.toListElement());
//		}

		System.out.println();
//		System.out.println("Affected words: ");
//		System.out.println();
		printStats(population, interesting, uninteresting, transformations);
		System.out.println();

//		System.out.println();
//		printReminderStats(interesting, population, transformations);

		System.out.println();
		return new ImmutablePair<>(
				100.0d * Transformations.getTransformedWords(sample, transformations).getTotalCounted() / sample.getTotalCounted(),
				100.0d * Transformations.getTransformedWords(population, transformations).getTotalCounted()
						/ population.getTotalCounted());
	}

	/**
	 * @return Transformations extracted from given text, already provided as sample
	 *         and population parts, after they went through standard validation.
	 */
	protected List<Transformation> extractValidatedTransformations(Text sampleText,
			Text populationText) {

		Counter<String> sample = sampleText.getWords(READABLE_ONLY);
		Counter<String> population = populationText.getWords(READABLE_ONLY);
		Counter<String> interesting = Experiment.getInterestingWords(sample, population);

		List<Transformation> transformations = getValidatedTransformations(
				extractTransformations(sampleText, populationText, MIN_POPULATION_COUNT), sample,
				population, Math.max(MIN_TOKEN_COUNT, interesting.getTotalCounted() / 50 + 1));

		return transformations;
	}

	/**
	 * Extract transformations, accordingly to below constrains. Note that multiple
	 * transformations might apply to same word.
	 * 
	 * @param minPopulationCount Number of times the stem must exist in the
	 *                           population for the decomposition to be considered
	 *                           valid.
	 */
	protected abstract Collection<? extends Transformation> extractTransformations(Text sampleText,
			Text populationText, int minPopulationCount);

	/**
	 * Filter transformations keeping only those that apply more significatively
	 * (see ALPHA) in the sample, compared to population.
	 * 
	 * @param minTokenCount discard all transformations that apply to less than this
	 *                      number of TOKENS (not types) in sample.
	 * @return
	 * 
	 * @return A sorted list of validated transformations. Notice that, because one
	 *         transformation might apply to multiple types, the order in which you
	 *         apply results is significant.
	 */
	protected List<Transformation> getValidatedTransformations(
			Collection<? extends Transformation> transformations, Counter<String> sample,
			Counter<String> population, int minTokenCount) {

		List<Transformation> myTransformations = new ArrayList<>(transformations);
		Counter<String> mySample = new Counter<>(sample);
		Counter<String> myPopulation = new Counter<>(population);

		List<Transformation> sorted = new ArrayList<>();
		while (!myTransformations.isEmpty()) {

			// Notice this changes the three lists
			Transformation best = getBestTransformation(mySample, myPopulation, myTransformations,
					minTokenCount);
			if (best == null)
				break;

			sorted.add(best);
		}

		return sorted;
	}

	/**
	 * @return The "best" transformation, that is that applies significantly
	 *         statistically more to sample compared to population and which impacts
	 *         a higher percentage of word types in sample compared to population
	 *         (as ratio between %s)
	 */
	private Transformation getBestTransformation(Counter<String> sample, Counter<String> population,
			Collection<? extends Transformation> transformations, int minTokenCount) {

		double bestWeight = 0.0;
		Transformation best = null;

		for (Transformation transformation : transformations) {
			Counter<String> sc = Transformations.getTransformedWords(sample, transformation);
			Counter<String> pc = Transformations.getTransformedWords(population, transformation);

			if (sc.getTotalCounted() < minTokenCount) // Not enough tokens affected
				continue;

			// Probability of this transformation in the population
			double p = ((double) pc.itemCount()) / population.itemCount();

			// Discard all transformations that are not statistically significant
			double sig = BINOMIAL.binomialTest(sample.itemCount(), sc.itemCount(), p,
					AlternativeHypothesis.GREATER_THAN);
			if (sig >= ALPHA) {
				continue;
			}

			// Weight is the ratio between % of types affected in sample vs. population
			double weight = (pc.getTotalCounted() == 0) ? Double.MAX_VALUE : // Guard
					((double) sc.getTotalCounted() / sample.getTotalCounted())
							/ ((double) pc.getTotalCounted() / population.getTotalCounted());

			if (weight < PERCENT_THRESHOLD) // Transformation not "effective" enough
				continue;

			if ((best == null) || weight > bestWeight) {
				bestWeight = weight;
				best = transformation;
			}
		}

		if (best == null)
			return null;

		// Removes matched transformation and word types
		transformations.remove(best);
		for (String w : Transformations.getTransformedWords(sample, best).itemSet())
			sample.remove(w);
		for (String w : Transformations.getTransformedWords(population, best).itemSet())
			population.remove(w);

		return best;
	}

	/**
	 * Prints reverse transformation statistics.
	 */
	public void printReverseTransformStats(Counter<String> words,
			List<? extends Transformation> transformations) {

		Counter<String> cnt = Transformations.getTransformedWords(words, transformations);
		int tokens = cnt.getTotalCounted();
		int types = cnt.itemCount();
		int sampleTokens = words.getTotalCounted();
		int sampleTypes = words.itemCount();

		System.out.print(words.getTotalCounted() + ";" + tokens + ";" + (100.0 * tokens / sampleTokens) + ";");
		System.out.print(words.itemCount() + ";" + types + ";" + (100.0 * types / sampleTypes) + "\n");
	}

	/**
	 * Print all transformations and the words to which they were applied.
	 * 
	 * @param transformations Transformations to apply, in the specific order they
	 *                        are provided.
	 */
	public void printTransformationStats(Counter<String> words,
			List<? extends Transformation> transformations) {
		printTransformationStats(words, null, transformations, 0);
	}

	/**
	 * Print all transformations and the words to which they were applied.
	 * 
	 * @param transformations         Transformations to apply, in the specific
	 *                                order they are provided.
	 * @param population              Population (for comparison, see below) can be
	 *                                null if minRevertedInPopulation is 0.
	 * @param minRevertedInPopulation Returns only words such that, after
	 *                                transformation has been reverted, the
	 *                                "reverted" word appears in population at least
	 *                                this many times,
	 */
	public void printTransformationStats(Counter<String> words, Counter<String> population,
			List<? extends Transformation> transformations, int minReversedInPopulation) {

		Map<Transformation, Counter<String>> map = Transformations.getTransformationMap(words, population,
				transformations, minReversedInPopulation);
		for (Transformation t : transformations) {
			Counter<String> c = map.get(t);
			if (c == null)
				continue;
			System.out.println(t + " [" + c.getTotalCounted() + ", " + c.itemCount() + "]\n\t" + findTopWords(c, 5));
		}
	}

	/**
	 * Print some statistics about how good transformations apply.
	 */
	@SuppressWarnings("unused")
	private void printStats(Counter<String> population, Counter<String> interesting, Counter<String> uninteresting,
			List<? extends Transformation> transformations) {

		Counter<String> popStats = merge(Transformations.getTransformationMap(population, transformations));
		Counter<String> intStats = merge(Transformations.getTransformationMap(interesting, transformations));
		Counter<String> uintStats = merge(Transformations.getTransformationMap(uninteresting, transformations));
		Counter<String> popStatsStrict = merge(Transformations.getTransformationMap(population, population, transformations, 1));
		Counter<String> intStatsStrict = merge(Transformations.getTransformationMap(interesting, population, transformations, 1));
		Counter<String> uintStatsStrict = merge(Transformations.getTransformationMap(uninteresting, population, transformations, 1));

		System.out.println(" Population   Interesting" // \tOther Words in Sample"
				+ "");
		System.out.println("Tokens Types  Tokens Types" // \t Tokens Types"
		);

		System.out.printf("%04.1f%%", 100.0d * popStats.getTotalCounted() / population.getTotalCounted());
		System.out.printf("  %04.1f%%", 100.0d * popStats.itemCount() / population.itemCount());
		System.out.printf("  %04.1f%%", 100.0d * intStats.getTotalCounted() / interesting.getTotalCounted());
		System.out.printf("  %04.1f%%", 100.0d * intStats.itemCount() / interesting.itemCount());
//		System.out.printf("\t    %04.1f%%", 100.0d * uintStats.getTotalCounted() / uninteresting.getTotalCounted());
//		System.out.printf("   %04.1f%%", 100.0d * uintStats.itemCount() / uninteresting.itemCount());
//		System.out.printf("\t%4.1f", (100.0d * intStats.getTotalCounted() / interesting.getTotalCounted())
//				/ (100.0d * popStats.getTotalCounted() / population.getTotalCounted()));
//		System.out.printf("\t%4.1f%n", (100.0d * intStats.itemCount() / interesting.itemCount())
//				/ (100.0d * popStats.itemCount() / population.itemCount()));

//		System.out.printf("Strict\t%04.1f%%", 100.0d * popStatsStrict.getTotalCounted() / population.getTotalCounted());
//		System.out.printf("\t%04.1f%%", 100.0d * popStatsStrict.itemCount() / population.itemCount());
//		System.out.printf("\t%04.1f%%", 100.0d * intStatsStrict.getTotalCounted() / interesting.getTotalCounted());
//		System.out.printf("\t%04.1f%%", 100.0d * intStatsStrict.itemCount() / interesting.itemCount());
//		System.out.printf("\t%04.1f%%", 100.0d * uintStatsStrict.getTotalCounted() / uninteresting.getTotalCounted());
//		System.out.printf("\t%04.1f%%", 100.0d * uintStatsStrict.itemCount() / uninteresting.itemCount());
//		System.out.printf("\t%4.1f", (100.0d * intStatsStrict.getTotalCounted() / interesting.getTotalCounted())
//				/ (100.0d * popStatsStrict.getTotalCounted() / population.getTotalCounted()));
//		System.out.printf("\t%4.1f%n", (100.0d * intStatsStrict.itemCount() / interesting.itemCount())
//				/ (100.0d * popStatsStrict.itemCount() / population.itemCount()));
	}

	/**
	 * Prints analysys about words in sample to which the transformations cannot
	 * apply.
	 * 
	 * @param interesting
	 * @param transformations
	 */
	@SuppressWarnings("unused")
	private void printReminderStats(Counter<String> sample, Counter<String> population,
			List<? extends Transformation> transformations) {
		Counter<String> transformed = merge(Transformations.getTransformationMap(sample, transformations));
		Counter<String> remainder = new Counter<>();

		for (String w : sample.itemSet()) {
			if (transformed.getCount(w) == 0)
				remainder.count(w, population.getCount(w) + 1);
		}

		System.out.println("Top intersting words that cannot be explained by transformations,");
		System.out.println("sorted by number of tiomes they appear in population");
		for (Entry<String, Integer> e : remainder.sorted()) {
			System.out.print(e.getKey() + " (" + (e.getValue() - 1) + ") ");
		}

		Map<String, TermDecomposition> decompositions = SlotAlphabet.decompose(remainder.itemSet());
		Counter<TermClassification> tc = new Counter<>();
		for (TermDecomposition d : decompositions.values())
			tc.count(d.classification);

		System.out.println();
		System.out.println("Type of words (SLOT decomposition):");
		for (Entry<TermClassification, Integer> e : tc.reversed())
			System.out.println(e.getKey() + "\t" + (100.d * e.getValue() / tc.getTotalCounted()));
	}

	private Counter<String> merge(Map<Transformation, Counter<String>> transformationMap) {
		Counter<String> result = new Counter<>();
		for (Counter<String> c : transformationMap.values())
			result.countAll(c);
		return result;
	}

	/**
	 * @return A string showing the top n tokens in words.
	 */
	protected static String findTopWords(Counter<String> words, int n) {
		StringBuilder result = new StringBuilder();
		int found = 0;
		for (Entry<String, Integer> e : words.reversed()) {
			result.append(e.getKey()).append(" (").append(e.getValue()).append(") ");
			if (++found >= n)
				break;
		}

		return result.toString().trim();
	}

	private static final List<Experiment> EXPERIMENTS = List.of(
//			FIRST_LINE_IN_PARAGRAPH_EXPERIMENT
//	  FIRST_WORD_IN_LINE_EXPERIMENT
//			  SECOND_WORD_IN_LINE_EXPERIMENT
			LAST_WORD_IN_LINE_EXPERIMENT);

	private static final List<TransformationExtractor> EXTRACTORS = List.of(new InsertAtSlotExtractor(),
			new InsertBeforeExtractor(), new PrefixExtractor(), new RemoveAtSlotExtractor(),
			new SubstitutionExtractor(), new SuffixExtractor());

	/**
	 * This code applies all possible transformations to each of a set of
	 * experiment, then it sequences them by their ability to cover interesting
	 * words without touching the standard population, finally it creates an
	 * extractor that extract all transformations in the right sequence.
	 */
	public static void main(String[] args) {
		try {

			IvtffText voy = VoynichFactory.getDocument();

			// Maps each experiment into a list of pairs, coupling each extractor with its
			// "performance index"
			// The index is the ration between % tokens in sample / % tokens in population
			// to which transformations apply
			Map<Experiment, List<Pair<TransformationExtractor, Double>>> map = new HashMap<>();

			for (Experiment exp : EXPERIMENTS) {

				List<Pair<TransformationExtractor, Double>> transforms = new ArrayList<>();
				map.put(exp, transforms);

				for (TransformationExtractor tex : EXTRACTORS) {

					// Calculate "performance" for this combination of experiment and extractor
					Map<String, Pair<Double, Double>> results = tex.process(voy, exp);
					double perf = 0.0d;
					for (Pair<Double, Double> p : results.values()) {
						perf += p.getLeft() / p.getRight();
					}
					transforms.add(new ImmutablePair<>(tex, perf / results.size()));
				}
			}

			System.out.println("\n\n*************************************************************");
			System.out.println("*** FINAL TRANSFORMATIONS");
			System.out.println("*************************************************************\n");

			// For each experiment apply extractors from best to worse and print results
			for (Entry<Experiment, List<Pair<TransformationExtractor, Double>>> e : map.entrySet()) {

				Experiment exp = e.getKey();
				System.out.println("\n=== EXPERIMENT: " + exp);

				List<Pair<TransformationExtractor, Double>> extractors = e.getValue();
				Collections.sort(extractors, new Comparator<>() {

					@Override
					public int compare(Pair<TransformationExtractor, Double> o1,
							Pair<TransformationExtractor, Double> o2) {
						return Double.compare(o1.getRight(), o2.getRight());
					}
				});

				// Apply the transformers to each experiment, in the best sequence possible
				new SequenceExtractor(extractors.stream().map(Pair::getKey).collect(Collectors.toList())).process(voy,
						exp);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}
}