/**
 * 
 */
package io.github.mzattera.v4j.applications.transformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.txt.TextString;
import io.github.mzattera.v4j.util.Counter;

/**
 * This extractor is composed by a list of extractors, which extract rules one
 * after the other.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class SequenceExtractor extends TransformationExtractor {

	private final List<TransformationExtractor> extractors = new ArrayList<>();

	public SequenceExtractor(List<? extends TransformationExtractor> extractors) {
		this.extractors.addAll(extractors);
	}

	@Override
	protected List<Transformations.Transformation> extractValidatedTransformations(Text sampleText, Text populationText) {

		List<Transformations.Transformation> result = new ArrayList<>();
		Counter<String> mySample = sampleText.getWords(TransformationExtractor.READABLE_ONLY);
		Counter<String> myPopulation = populationText.getWords(TransformationExtractor.READABLE_ONLY);
		Counter<String> interesting = Experiment.getInterestingWords(mySample, myPopulation);
		int minTokens = Math.max(TransformationExtractor.MIN_TOKEN_COUNT, interesting.getTotalCounted() / 50 + 1);

		for (TransformationExtractor tex : extractors) {

			// Get transformations from one extractor
			List<Transformations.Transformation> validated = tex.getValidatedTransformations(
					tex.extractTransformations(new TextString(mySample), new TextString(myPopulation),
							TransformationExtractor.MIN_POPULATION_COUNT),
					mySample, myPopulation, minTokens);
			result.addAll(validated);

			// Remove words for which the transformations apply from sample and population
			for (Counter<String> c : Transformations.getTransformationMap(mySample, validated).values())
				for (String w : c.itemSet())
					mySample.remove(w);
			for (Counter<String> c : Transformations.getTransformationMap(myPopulation, validated).values())
				for (String w : c.itemSet())
					myPopulation.remove(w);

			if (mySample.isEmpty() || myPopulation.isEmpty())
				break;
		}

		return result;
	}

	@Override
	protected Collection<? extends Transformations.Transformation> extractTransformations(Text sampleText, Text populationText,
			int minPopulationCount) {

		// We directly implemented the caller, avoiding re-riltering and re-ordering of
		// transformations
		throw new UnsupportedOperationException();
	}

	@Override
	protected List<Transformations.Transformation> getValidatedTransformations(Collection<? extends Transformations.Transformation> transformations,
			Counter<String> sample, Counter<String> population, int minTokenCount) {

		// We directly implemented the caller, avoiding re-riltering and re-ordering of
		// transformations
		throw new UnsupportedOperationException();
	}
}