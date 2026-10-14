/**
 * 
 */
package io.github.mzattera.v4j.applications.transformations;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermClassification;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermDecomposition;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.util.Counter;

/**
 * This class extract transformations showing how chars were changed in regular
 * words to make them interesting, in a sample defined by an EXPERIMENT. Only
 * REGULAR & SPEARABLE words, accordingly to SLOT alphabet decomposition are
 * considered.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class SubstitutionExtractor extends TransformationExtractor {

	/**
	 * Experiment to use to split the document for analysis.
	 */
	private static final Experiment EXPERIMENT = TransformationExtractor.LAST_WORD_IN_LINE_EXPERIMENT;

	public static void main(String[] args) {
		try {
			SubstitutionExtractor inst = new SubstitutionExtractor();
			inst.process(VoynichFactory.getDocument(), EXPERIMENT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	@Override
	protected Collection<? extends SubstitutionTransformation> extractTransformations(Text sampleText, Text populationText,
			int minPopulationCount) {

		Set<SubstitutionTransformation> result = new HashSet<>();
		Counter<String> sample = sampleText.getWords(true);
		Counter<String> population = populationText.getWords(true);

		int slotCount = SlotAlphabet.SLOTS.size();

		for (String w : sample.itemSet()) {

			TermDecomposition d = SlotAlphabet.decompose(w);
			if (d.classification == TermClassification.UNSTRUCTURED)
				continue;

			for (int i = 0; i < slotCount * 2; ++i) {

				int slot;
				String newChar;
				if (i < slotCount) {
					slot = i;
					newChar = d.slots1[slot];
					continue;
				} else {
					slot = i - slotCount;
					newChar = d.slots2[slot];
				}
				if (newChar.isEmpty())
					continue;

				for (String oldChar : SlotAlphabet.SLOTS.get(slot)) {

					if (newChar.equals(oldChar))
						continue;
					SubstitutionTransformation candidate = new SubstitutionTransformation(slot, newChar, oldChar);
					String rev = candidate.revert(w);
					if (population.getCount(rev) < minPopulationCount)
						continue;
					result.add(candidate);
				}
			}
		}

		return result;
	}
}