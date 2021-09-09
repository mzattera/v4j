/**
 * 
 */
package io.github.mzattera.v4j.applications.clustering;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.Pair;

import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;
import io.github.mattera.v4j.util.clustering.BagOfWords;
import io.github.mattera.v4j.util.clustering.PositiveAngularDistance;

/**
 * This class prints average distance between each point in a set of Clusterable
 * elements, in descending order, to detect outliers.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class OutlierDetection {

	private OutlierDetection() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DistanceMeasure dist = new PositiveAngularDistance();
			ClusteringConfiguration.print();

			// The document to process
			IvtffText doc = VoynichFactory.getDocument(ClusteringConfiguration.TRANSCRIPTION,
					ClusteringConfiguration.TRANSCRIPTION_TYPE);

			// Create BoW for the elements in the document, split accordingly to
			// DOCUMENT_SPLITTER.
			// Our embedding dimensions for the BoW will be the words in the text.
			List<BagOfWords> bow = BagOfWords.toBoW(doc.splitPages(ClusteringConfiguration.DOCUMENT_SPLITTER).values(),
					BagOfWords.buildDimensions(doc), ClusteringConfiguration.BOW_MODE);

			//////////////////////////////////////////////////////

			// calculate distance between each BoW in the document
			double[][] x = new double[bow.size()][bow.size()];
			for (int i = 0; i < bow.size(); ++i) {
				for (int j = 0; j < i; ++j) {
					x[i][j] = dist.compute(bow.get(i).getPoint(), bow.get(j).getPoint());
					x[j][i] = x[i][j];
				}
			}

			// For each BoW, calculate average distance from other points and store it in a
			// Pair
			List<Pair<BagOfWords, Double>> averages = new ArrayList<>();
			double avg = 0.0d;
			for (int i = 0; i < bow.size(); ++i) {
				for (int j = 0; j < bow.size(); ++j) {
					avg += x[i][j];
				}
				avg /= (bow.size() - 1);
				averages.add(new Pair<>(bow.get(i), avg));
			}

			// Sort by decreasing distance
			averages.sort(new Comparator<Pair<BagOfWords, Double>>() {

				@Override
				public int compare(Pair<BagOfWords, Double> o1, Pair<BagOfWords, Double> o2) {
					return Double.compare(o1.getSecond(), o2.getSecond());
				}
			});

			// Print results
			for (Pair<BagOfWords, Double> p : averages) {
				System.out.println(
						((IvtffText) p.getFirst().getText()).getElements().get(0).getId() + "\t= " + p.getSecond());
			}

			// Calculate average and standard deviation
			avg = 0.0d;
			for (Pair<BagOfWords, Double> p : averages) {
				avg += p.getSecond();
			}
			avg /= averages.size();
			double std = 0.0d;
			for (Pair<BagOfWords, Double> p : averages) {
				std += (p.getSecond() - avg) * (p.getSecond() - avg);
			}
			std = Math.sqrt(std / averages.size());

			// Print results
			System.out.println("\nAverage distance  : " + avg);
			System.out.println("Standard deviation: " + std);

			System.out.println("\n[average +- N * std.dev] ranges");
			System.out.println("[" + (avg - std) + "-" + (avg - std) + "]");
			System.out.println("[" + (avg - 2 * std) + "-" + (avg - 2 * std) + "]");
			System.out.println("[" + (avg - 3 * std) + "-" + (avg - 3 * std) + "]");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}
}
