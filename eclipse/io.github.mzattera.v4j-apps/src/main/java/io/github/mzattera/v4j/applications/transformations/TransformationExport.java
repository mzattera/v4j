/* Copyright (c) 2026 Massimiliano "Maxi" Zattera */

package io.github.mzattera.v4j.applications.transformations;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageFilter;
import io.github.mzattera.v4j.text.ivtff.PageHeader;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.transformations.Transformation;
import io.github.mzattera.v4j.transformations.Transformations;
import io.github.mzattera.v4j.transformations.Transformations.Locus;
import io.github.mzattera.v4j.util.Counter;

/**
 * Print a CSV file listing sample words and which transformations apply to
 * them.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public abstract class TransformationExport {

	private static final List<Pair<Locus, Experiment>> EXPERIMENTS = List.of(
			new ImmutablePair<>(Locus.FIRST_LINE_IN_PARAGRAPH,
					TransformationExtractor.FIRST_LINE_IN_PARAGRAPH_EXPERIMENT),
			new ImmutablePair<>(Locus.FIRST_WORD_IN_LINE, TransformationExtractor.FIRST_WORD_IN_LINE_EXPERIMENT),
			new ImmutablePair<>(Locus.LAST_WORD_IN_LINE, TransformationExtractor.LAST_WORD_IN_LINE_EXPERIMENT)
//			new ImmutablePair<>(Locus.SECOND_WORD_IN_LINE, TransformationExtractor.SECOND_WORD_IN_LINE_EXPERIMENT)
	);

	private final static Path OUTPUT_FILE = Paths.get("d:\\i.csv");

	/**
	 * This code applies all possible transformations to each of a set of
	 * experiment, then it sequences them by their ability to cover interesting
	 * words without touching the standard population, finally it creates an
	 * extractor that extract all transformations in the right sequence.
	 */
	public static void main(String[] args) {
		try {
			IvtffText voy = VoynichFactory.getDocument();

			// Create CSV file
			String[] headers = { "Cluster", "Locus", "Word Type", "# in Sample", "% in Sample", "# in Population",
			        "% in Population", "Is Interesting", "Transformation", "From", "From # in Population",
			        "From % in Population" };

			try (BufferedWriter writer = Files.newBufferedWriter(OUTPUT_FILE, StandardCharsets.UTF_8);
			     CSVPrinter csvPrinter = CSVFormat.DEFAULT.builder().setHeader(headers).get().print(writer)) {

				for (String cluster : PageHeader.CLUSTERS) {
					for (Pair<Locus, Experiment> p : EXPERIMENTS) {
						IvtffText clusterText = voy.filterPages(new PageFilter.Builder().cluster(cluster).build());
						processCluster(cluster, clusterText, p.getLeft(), p.getRight(), csvPrinter);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	private static void processCluster(String cluster, IvtffText clusterText, Locus locus, Experiment experiment,
			CSVPrinter csvPrinter) throws IOException {

		// Extract sample and interesting words
		Text[] split = experiment.splitDocument(clusterText);
		Counter<String> sample = split[0].getWords(TransformationExtractor.READABLE_ONLY);
		Counter<String> population = split[1].getWords(TransformationExtractor.READABLE_ONLY);
		Counter<String> interesting = Experiment.getInterestingWords(sample, population);

		// Transformations for this cluster and locus
		List<Transformation> transformations = Transformations.getTransformations(cluster, locus);

		// Words in sample that can be transformed
		Map<String, Transformation> map = Transformations.getWordMap(sample, transformations);

		for (String w : sample.itemSet()) {
			Transformation t = map.get(w);
			csvPrinter.printRecord(cluster, locus, w, sample.getCount(w), sample.getFrequency(w),
					population.getCount(w), population.getFrequency(w), (interesting.getCount(w) > 0),
					(t == null ? "" : t.toString()), (t == null ? "" : t.revert(w)),
					(t == null ? "" : population.getCount(t.revert(w))),
					(t == null ? "" : population.getFrequency(t.revert(w))));
		}
	}
}