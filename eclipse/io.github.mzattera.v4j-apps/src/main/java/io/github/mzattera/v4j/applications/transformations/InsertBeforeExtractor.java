/**
 * 
 */
package io.github.mzattera.v4j.applications.transformations;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.mzattera.v4j.applications.chars.CharDistributionAnalysis;
import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.util.Counter;

/**
 * This class extract transformations showing how chars were inserted into
 * regular words, before specific chars, to make them interesting, in a sample
 * defined by an EXPERIMENT.
 * 
 * Notice this can look for characters added at beginning or end of words, but
 * there are other specific classes that look for prefixes and suffixes.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class InsertBeforeExtractor extends TransformationExtractor {

	/**
	 * Experiment to use to split the document for analysis.
	 */
//	private static final Experiment EXPERIMENT = TransformationExtractor.FIRST_LINE_IN_PARAGRAPH_EXPERIMENT;
	private static final Experiment EXPERIMENT = TransformationExtractor.LAST_WORD_IN_LINE_EXPERIMENT;

	// If true, considers also insertion at beginning or end of words (that could be
	// handled as prefixes and suffixes)
	private static final boolean AT_BEGIN_AND_END = true;

	// If true, uses most frequent chars as candidates, otherwise uses ALLOWED.
	// Least used characters will be forbidden.
	private static final boolean USE_CHAR_FREQUENCY = false;

	// If USE_CHAR_FREQUENCY == false, use these list of allowed characters; if null
	// or empty this is ignored
	private static final Collection<Character> ALLOWED = List.of();

	// Always forbid these characters (even with USE_CHAR_FREQUENCY = true); if null
	// or empty this is ignored
	private static final Collection<Character> FORBIDDEN = List.of('o', 'r');

	public static void main(String[] args) {
		try {
			InsertBeforeExtractor inst = new InsertBeforeExtractor();
			inst.process(VoynichFactory.getDocument(), EXPERIMENT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	@Override
	protected Collection<? extends InsertBeforeTransformation> extractTransformations(Text sampleText, Text populationText,
			int minPopulationCount) {

		if (USE_CHAR_FREQUENCY) {
			List<Character>[] cdis = CharDistributionAnalysis.process(sampleText, populationText);
			cdis[1].addAll(FORBIDDEN);
			return extractTransformations(sampleText.getWords(TransformationExtractor.READABLE_ONLY),
					populationText.getWords(TransformationExtractor.READABLE_ONLY), cdis[0], cdis[1],
					TransformationExtractor.MIN_POPULATION_COUNT);
		} else {
			return extractTransformations(sampleText.getWords(TransformationExtractor.READABLE_ONLY),
					populationText.getWords(TransformationExtractor.READABLE_ONLY), ALLOWED, FORBIDDEN,
					TransformationExtractor.MIN_POPULATION_COUNT);
		}
	}

	private static Collection<InsertBeforeTransformation> extractTransformations(Counter<String> sample, Counter<String> population,
			Collection<Character> allowed, Collection<Character> forbidden, int minPopulationCount) {

		Set<InsertBeforeTransformation> result = new HashSet<>();

		for (String w : sample.itemSet()) {

			final int begin = AT_BEGIN_AND_END ? 0 : 1;
			final int end = AT_BEGIN_AND_END ? w.length() : w.length() - 1;

			for (int i = begin; i < end; ++i) {

				char insertion = w.charAt(i);

				if ((allowed != null) && (allowed.size() > 0) && !allowed.contains(insertion))
					continue;
				if ((forbidden != null) && (forbidden.size() > 0) && forbidden.contains(insertion))
					continue;

				String tail = w.substring(i + 1);
				String rev = w.substring(0, i) + tail;

				if (population.getCount(rev) < minPopulationCount)
					continue;

				InsertBeforeTransformation found = new InsertBeforeTransformation(insertion, tail.length() > 0 ? tail.charAt(0) : '.', AT_BEGIN_AND_END);
				result.add(found);
			}
		}

		return result;
	}
}