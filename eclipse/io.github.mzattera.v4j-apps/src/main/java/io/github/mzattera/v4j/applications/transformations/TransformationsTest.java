/**
 * 
 */
package io.github.mzattera.v4j.applications.transformations;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.transformations.Transformation;
import io.github.mzattera.v4j.transformations.Transformations;
import io.github.mzattera.v4j.transformations.Transformations.Locus;
import io.github.mzattera.v4j.util.Counter;

/**
 * This extractor tests transformations defined in {@link Transformations}
 * class.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class TransformationsTest extends TransformationExtractor {

	private static final Experiment EXPERIMENT = TransformationExtractor.LAST_WORD_IN_LINE_EXPERIMENT;

	private Locus loc;
	private String cluster;

	public TransformationsTest() {
	}

	public static void main(String[] args) {
		try {
			TransformationsTest inst = new TransformationsTest();
			inst.process(VoynichFactory.getDocument(), EXPERIMENT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	@Override
	protected Pair<Double, Double> processCluster(String cluster, IvtffText clusterText, Experiment experiment) {

		// OK, this is dirty, but bear with me :)
		if (experiment == TransformationExtractor.FIRST_WORD_IN_LINE_EXPERIMENT)
			loc = Locus.FIRST_WORD_IN_LINE;
		else if (experiment == TransformationExtractor.FIRST_LINE_IN_PARAGRAPH_EXPERIMENT)
			loc = Locus.FIRST_LINE_IN_PARAGRAPH;
		else if (experiment == TransformationExtractor.LAST_WORD_IN_LINE_EXPERIMENT)
			loc = Locus.LAST_WORD_IN_LINE;
		else if (experiment == TransformationExtractor.SECOND_WORD_IN_LINE_EXPERIMENT)
			loc = Locus.SECOND_WORD_IN_LINE;
		else
			throw new IllegalArgumentException();
		this.cluster = cluster;

		return super.processCluster(cluster, clusterText, experiment);
	}

	@Override
	protected List<Transformation> extractValidatedTransformations(Text sampleText,
			Text populationText) {

		// OK, this is dirty, but bear with me :)
		return Transformations.TRANSFORMATIONS.get(new ImmutablePair<>(loc, cluster));
	}

	@Override
	protected Collection<? extends Transformation> extractTransformations(Text sampleText,
			Text populationText, int minPopulationCount) {

		// We directly implemented the caller, avoiding re-iterating and re-ordering of
		// transformations
		throw new UnsupportedOperationException();
	}

	@Override
	protected List<Transformation> getValidatedTransformations(
			Collection<? extends Transformation> transformations, Counter<String> sample,
			Counter<String> population, int minTokenCount) {

		// We directly implemented the caller, avoiding re-riltering and re-ordering of
		// transformations
		throw new UnsupportedOperationException();
	}
}