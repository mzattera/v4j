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
import io.github.mzattera.v4j.transformations.RemoveAtSlotTransformation;
import io.github.mzattera.v4j.util.Counter;

/**
 * This class extract transformations showing how chars were removed from a
 * particular slot in words to make them interesting, in a sample defined by an
 * EXPERIMENT. Only REGULAR & SEPARABLE words, accordingly to SLOT alphabet
 * decomposition are considered.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class RemoveAtSlotExtractor extends TransformationExtractor {

	/**
	 * Experiment to use to split the document for analysis.
	 */
	private static final Experiment EXPERIMENT = TransformationExtractor.LAST_WORD_IN_LINE_EXPERIMENT;

	public static void main(String[] args) {
		try {
			RemoveAtSlotExtractor inst = new RemoveAtSlotExtractor();
			inst.process(VoynichFactory.getDocument(), EXPERIMENT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	@Override
	protected Collection<? extends RemoveAtSlotTransformation> extractTransformations(Text sampleText, Text populationText,
			int minPopulationCount) {

		Set<RemoveAtSlotTransformation> result = new HashSet<>();
		Counter<String> sample = sampleText.getWords(true);
		Counter<String> population = populationText.getWords(true);

		int slotCount = SlotAlphabet.SLOTS.size();

		for (String w : sample.itemSet()) {

			TermDecomposition d = SlotAlphabet.decompose(w);
			if (d.classification == TermClassification.UNSTRUCTURED)
				continue;

			for (int i = 0; i < slotCount * 2; ++i) {

				int slot;
				if (i < slotCount) {
					slot = i;
					if (!d.slots1[slot].isEmpty())
						continue;
				} else {
					slot = i - slotCount;
					if (!d.slots2[slot].isEmpty())
						continue;
				}

				for (String removedChar : SlotAlphabet.SLOTS.get(slot)) {

					RemoveAtSlotTransformation candidate = new RemoveAtSlotTransformation(slot, removedChar);
					String rev = candidate.revert(w);
					if (population.getCount(rev) < minPopulationCount)
						continue;
					result.add(new RemoveAtSlotTransformation(slot, removedChar));
				}
			}
		}

		return result;
	}
}