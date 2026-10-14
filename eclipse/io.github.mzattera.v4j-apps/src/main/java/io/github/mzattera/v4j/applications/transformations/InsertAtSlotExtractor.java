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
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermClassification;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermDecomposition;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.util.Counter;

/**
 * This class extract transformations showing how chars were inserted at a given
 * slot of population words to make them interesting, in a sample defined by an
 * EXPERIMENT. Only REGULAR & SPEARABLE words, accordingly to SLOT alphabet
 * decomposition are considered.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class InsertAtSlotExtractor extends TransformationExtractor {

	// Experiment to use to split the document for analysis when running as a
	// separate app.
	private static final Experiment EXPERIMENT = TransformationExtractor.FIRST_LINE_IN_PARAGRAPH_EXPERIMENT;

	// If true, uses most frequent chars as candidates, otherwise uses ALLOWED.
	// Least used characters will be forbidden.
	private static final boolean USE_CHAR_FREQUENCY = false;

	// If USE_CHAR_FREQUENCY == false, use these list of allowed characters; if null
	// or empty this is ignored
	private static final Collection<Character> ALLOWED = List.of();

	// Always forbid these characters (even with USE_CHAR_FREQUENCY = true); if null
	// or empty this is ignored
	private static final Collection<Character> FORBIDDEN = List.of();

	public static void main(String[] args) {
		try {
			InsertAtSlotExtractor inst = new InsertAtSlotExtractor();
			inst.process(VoynichFactory.getDocument(), EXPERIMENT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	@Override
	protected Collection<? extends InsertAtSlotTransformation> extractTransformations(Text sampleText, Text populationText,
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

	private static Collection<InsertAtSlotTransformation> extractTransformations(Counter<String> sample, Counter<String> population,
			Collection<Character> allowed, Collection<Character> forbidden, int minPopulationCount) {

		Set<InsertAtSlotTransformation> result = new HashSet<>();
		int slotCount = SlotAlphabet.SLOTS.size();

		for (String w : sample.itemSet()) {

			TermDecomposition d = SlotAlphabet.decompose(w);
			if (d.classification == TermClassification.UNSTRUCTURED)
				continue;

			for (int i = 0; i < slotCount * 2; ++i) {

				// Find which character could have been inserted at this slot
				String insertedChar;
				int slot = i % slotCount;
				if (i < slotCount) {
					insertedChar = d.slots1[slot];
					if (insertedChar.isBlank())
						continue;
				} else {
					insertedChar = d.slots2[slot];
					if (insertedChar.isBlank())
						continue;
				}

				// Filter based on allowed and forbidden list
				if ((allowed != null) && (allowed.size() > 0) && !allowed.contains(insertedChar.charAt(0)))
					continue;
				if ((forbidden != null) && (forbidden.size() > 0) && forbidden.contains(insertedChar.charAt(0)))
					continue;

				// Revert and check words appear in the standard population
				InsertAtSlotTransformation found = new InsertAtSlotTransformation(i, insertedChar);
				if (population.getCount(found.revert(w)) < minPopulationCount)
					continue;
				result.add(found);
			}
		}

		return result;
	}
}