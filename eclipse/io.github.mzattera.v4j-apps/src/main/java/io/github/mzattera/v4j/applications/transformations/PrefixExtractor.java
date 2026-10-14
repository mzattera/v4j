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
import io.github.mzattera.v4j.util.Counter;

/**
 * This class extract transformations showing how prefixes were added to regular
 * words to make them interesting, in a sample defined by an EXPERIMENT.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class PrefixExtractor extends TransformationExtractor {

	/**
	 * Experiment to use to split the document for analysis.
	 */
	private static final Experiment EXPERIMENT = TransformationExtractor.FIRST_LINE_IN_PARAGRAPH_EXPERIMENT;

	// If true extract only shortest initials possible, otherwise extract all
	private static final boolean SHORT_INITIALS_ONLY = true;

	// Maximum length of supposed prefixes, when doing the analysis
	private static final int MAX_INITIALS_LENGTH = 1;

	public static void main(String[] args) {
		try {
			PrefixExtractor inst = new PrefixExtractor();
			inst.process(VoynichFactory.getDocument(), EXPERIMENT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	@Override
	protected Collection<? extends PrefixTransformation> extractTransformations(Text sampleText, Text populationText,
			int minPopulationCount) {

		return extractTransformations(sampleText.getWords(TransformationExtractor.READABLE_ONLY),
				populationText.getWords(TransformationExtractor.READABLE_ONLY), MAX_INITIALS_LENGTH, null, null,
				TransformationExtractor.MIN_POPULATION_COUNT, SHORT_INITIALS_ONLY);
	}

	private static Collection<PrefixTransformation> extractTransformations(Counter<String> sample, Counter<String> population,
			int maxLen, Collection<Character> allowedInitials, Collection<Character> allowedStem,
			int minPopulationCount, boolean shortInitials) {

		Set<PrefixTransformation> transformations = new HashSet<>();

		for (String w : sample.itemSet()) {
			String stem = null;

			for (int i = 1; i < Math.min(maxLen + 1, w.length()); ++i) {
				stem = w.substring(i);

				if (population.getCount(stem) < minPopulationCount)
					continue;

				if ((allowedStem != null) && !allowedStem.contains(stem.charAt(0)))
					continue;

				String initials = w.substring(0, i);

				if ((allowedInitials != null) && !allowedInitials.contains(initials.charAt(0)))
					continue;

				if (allowedStem != null) { // initials must not contain letter user assigned to stems
					for (char c : initials.toCharArray()) {
						if (allowedStem.contains(c)) {
							initials = null;
							break;
						}
					}
				}

				if (initials != null) { // Found!

					PrefixTransformation found = new PrefixTransformation(initials, stem.charAt(0));
					transformations.add(found);

					if (shortInitials)
						break;
				}
			}
		}

		return transformations;
	}
}
