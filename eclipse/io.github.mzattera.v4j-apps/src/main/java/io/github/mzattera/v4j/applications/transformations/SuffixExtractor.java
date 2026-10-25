/* Copyright (c) 2026 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.applications.transformations;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.transformations.SuffixTransformation;
import io.github.mzattera.v4j.util.Counter;

/**
 * This class extract transformations showing how suffixes were added to regular
 * words to make them interesting, in a sample defined by an EXPERIMENT.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class SuffixExtractor extends TransformationExtractor {

	/**
	 * Experiment to use to split the document for analysis.
	 */
	private static final Experiment EXPERIMENT = TransformationExtractor.LAST_WORD_IN_LINE_EXPERIMENT;

	// If true extract only shortest endings possible, otherwise extract all
	private static final boolean SHORT_ENDINGS_ONLY = true;

	// Maximum length of supposed endings, when doing the analysis
	private static final int MAX_ENDINGS_LENGTH = 1;

	public static void main(String[] args) {
		try {
			SuffixExtractor inst = new SuffixExtractor();
			inst.process(VoynichFactory.getDocument(), EXPERIMENT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	@Override
	protected Collection<? extends SuffixTransformation> extractTransformations(Text sampleText, Text populationText,
			int minPopulationCount) {

		return extractTransformations(sampleText, populationText, MAX_ENDINGS_LENGTH, null, null,
				TransformationExtractor.MIN_POPULATION_COUNT, SHORT_ENDINGS_ONLY);
	}

	private static Set<SuffixTransformation> extractTransformations(Text sampleText, Text populationText,
			int maxEndingsLength, Collection<Character> allowedEndings, Collection<Character> allowedStem,
			int minPopulationCount, boolean shortEndingsOnly) {

		Set<SuffixTransformation> result = new HashSet<>();
		Counter<String> sample = sampleText.getWords(true);
		Counter<String> population = populationText.getWords(true);

		for (String w : sample.itemSet()) {
			for (int i = w.length() - 1; i >= Math.max(1, w.length() - maxEndingsLength); --i) {

				String stem = w.substring(0, i);

				if (population.getCount(stem) < minPopulationCount)
					continue;

				if ((allowedStem != null) && !allowedStem.contains(stem.charAt(stem.length() - 1)))
					continue;

				String suffix = w.substring(i);

				if ((allowedEndings != null) && !allowedEndings.contains(suffix.charAt(suffix.length() - 1)))
					continue;

				if (allowedStem != null) { // endings must not contain letter user assigned to stems
					for (char c : suffix.toCharArray()) {
						if (allowedStem.contains(c)) {
							suffix = null;
							break;
						}
					}
				}

				if (suffix != null) { // Found!

					SuffixTransformation found = new SuffixTransformation(suffix, stem.charAt(stem.length() - 1));
					if (population.getCount(found.revert(w)) < minPopulationCount)
						continue;
					result.add(found);
					if (shortEndingsOnly)
						break;
				}
			}
		}

		return result;
	}
}