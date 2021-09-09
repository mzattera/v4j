/**
 * 
 */
package io.github.mzattera.v4j.applications.clustering;

import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.evaluation.ClusterEvaluator;
import org.apache.commons.math3.ml.distance.DistanceMeasure;

import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;
import io.github.mattera.v4j.util.clustering.BagOfWords;
import io.github.mattera.v4j.util.clustering.MultiSizeClusterer;
import io.github.mattera.v4j.util.clustering.PositiveAngularDistance;
import io.github.mattera.v4j.util.clustering.SilhouetteEvaluator;
import io.github.mattera.v4j.util.clustering.hac.BagOfWordsExperiment;

/**
 * @author Massimiliano_Zattera
 *
 */
public final class KMeansClusterByWords {

	private KMeansClusterByWords() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ClusteringConfiguration.print();

			// The document to process
			IvtffText doc = VoynichFactory.getDocument(ClusteringConfiguration.TRANSCRIPTION,
					ClusteringConfiguration.TRANSCRIPTION_TYPE);

			// Remove outlier pages
			doc = doc.filterPages(ClusteringConfiguration.OUTLIER_FILTER);

			//////////////////////////////////////////////////////

			// Distance measure for clustering
			DistanceMeasure distance = new PositiveAngularDistance();

			// Minimum number of clusters to create
			int minSize = 2;

			// Maximum number of clusters to create
			int maxSize = 4;

			// For each possible cluster size, perform this number of trials and return the
			// best solution.
			//
			// *** NOTE *** if this is too big (e.g. 50), there is a chance that clusters
			// with single elements are created, if the trial picks up a page which is quite
			// dissimilar from the rest (that will be isolates in its own cluster).
			int numTrials = 1;

			// This is the "evaluator" we use to determine how good one trial is (how good
			// the clustering is)
			ClusterEvaluator<BagOfWords> eval = new SilhouetteEvaluator<>(distance);

			// Print cluster stats
			System.out.println("K-Means Clusterng (best clusters over possible sizes and initial random setup)");
			System.out.println("Distance: " + distance.getClass().getName());
			System.out.println("Evaluator: " + eval.getClass().getName());
			System.out.println("Min. # of clusters: " + minSize);
			System.out.println("Max. # of clusters: " + maxSize);
			System.out.println("# random initializations (trials) per cluster size: " + numTrials);

			// Create an Experiment out of BoW for the elements in the document, split
			// accordingly to
			// DOCUMENT_SPLITTER.
			// Our embedding dimensions for the BoW will be the words in the text.
			BagOfWordsExperiment experiment = new BagOfWordsExperiment(
					BagOfWords.toBoW(doc.splitPages(ClusteringConfiguration.DOCUMENT_SPLITTER).values(),
							BagOfWords.buildDimensions(doc), ClusteringConfiguration.BOW_MODE));

			// Creates a Clusterer and perform the clustering
			// Notice that any clusterer in the Apache API could be used instead
			Clusterer<BagOfWords> clusterer = new MultiSizeClusterer<>(minSize, maxSize, numTrials, distance, eval);
			List<? extends Cluster<BagOfWords>> clusters = clusterer.cluster(experiment.getItems());

			System.out.println("\nNumber of clusters: " + clusters.size());
			for (int i = 0; i < clusters.size(); ++i)
				System.out.println("Cluster " + i + " [" + clusters.get(i).getPoints().size() + " items]");

			System.out.println("\nCluster mapping:");
			for (int i = 0; i < clusters.size(); ++i) {
				for (BagOfWords bow : clusters.get(i).getPoints()) {

					// TODO this is really ad hoc....should be made more generic
					String id = null;
					if (ClusteringConfiguration.DOCUMENT_SPLITTER == ClusteringConfiguration.SPLIT_BY_PARCHMENTS) {
						id = ((IvtffText) bow.getText()).getElements().get(0).getDescriptor().getParchment() + "";
					} else {
						id = ((IvtffText) bow.getText()).getElements().get(0).getDescriptor().getId();
					}
					System.out.println(id + ";" + i);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}
}
