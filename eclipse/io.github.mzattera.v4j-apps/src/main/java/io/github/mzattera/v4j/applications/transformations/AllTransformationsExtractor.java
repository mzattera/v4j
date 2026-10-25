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
import io.github.mzattera.v4j.transformations.Transformation;

/**
 * Try to extract all possible transformations at once, by calling other
 * extractors.
 * 
 * This was used as an alternative and complementary approach to using
 * {@link TransformationExtractor} for fining best set of transformations.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class AllTransformationsExtractor extends TransformationExtractor {

	/**
	 * Experiment to use to split the document for analysis.
	 */
	private static final Experiment EXPERIMENT = TransformationExtractor.LAST_WORD_IN_LINE_EXPERIMENT;
//	private static final Experiment EXPERIMENT = TransformationExtractor.FIRST_LINE_IN_PARAGRAPH_EXPERIMENT;
//	private static final Experiment EXPERIMENT = TransformationExtractor.SECOND_WORD_IN_LINE_EXPERIMENT;
//	private static final Experiment EXPERIMENT = TransformationExtractor.FIRST_WORD_IN_LINE_EXPERIMENT;

	private final static InsertAtSlotExtractor insSlot = new InsertAtSlotExtractor();
	private final static InsertBeforeExtractor insBefore = new InsertBeforeExtractor();
	private final static PrefixExtractor pref = new PrefixExtractor();
	private final static RemoveAtSlotExtractor rem = new RemoveAtSlotExtractor();
	private final static SubstitutionExtractor sub = new SubstitutionExtractor();
	private final static SuffixExtractor suff = new SuffixExtractor();

	public static void main(String[] args) {
		try {
			AllTransformationsExtractor inst = new AllTransformationsExtractor();
			inst.process(VoynichFactory.getDocument(), EXPERIMENT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	@Override
	protected Collection<? extends Transformation> extractTransformations(Text sampleText,
			Text populationText, int minPopulationCount) {

		Set<Transformation> result = new HashSet<>();
		result.addAll(insSlot.extractTransformations(sampleText, populationText, minPopulationCount));
		result.addAll(insBefore.extractTransformations(sampleText, populationText, minPopulationCount));
		result.addAll(pref.extractTransformations(sampleText, populationText, minPopulationCount));
		result.addAll(rem.extractTransformations(sampleText, populationText, minPopulationCount));
		result.addAll(sub.extractTransformations(sampleText, populationText, minPopulationCount));
		result.addAll(suff.extractTransformations(sampleText, populationText, minPopulationCount));

		return result;
	}
}