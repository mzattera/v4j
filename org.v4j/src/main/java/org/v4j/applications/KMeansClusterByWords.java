/**
 * 
 */
package org.v4j.applications;

import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.MultiKMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.v4j.text.ElementFilter;
import org.v4j.text.ivtff.IvtffPage;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.util.BagOfWords;
import org.v4j.util.BagOfWords.BagOfWordsMode;
import org.v4j.util.clustering.ClusterEvaluator;
import org.v4j.util.clustering.PositiveAngularDistance;
import org.v4j.util.clustering.WordsInPageExperiment;

/**
 * @author Massimiliano_Zattera
 *
 */
public class KMeansClusterByWords {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			IvtffText doc = VoynichFactory.getDocument(TranscriptionType.MAJORITY);

			// removes outliers
			doc = doc.filterPages(new ElementFilter<IvtffPage>() {
				@Override
				public boolean keep(IvtffPage element) {
					// these are identified by looking at hierarchical clusters of 1 single page.
					// Notice they correspond to pages in ? language
					return !element.getId().equals("f116v") && !element.getId().equals("f53r")
							&& !element.getId().equals("f87v") && !element.getId().equals("f65r")
					//
							&& !element.getId().equals("f24v") && !element.getId().equals("f27v")
							&& !element.getId().equals("f48r") && !element.getId().equals("f50r")
							&& !element.getId().equals("f57r") && !element.getId().equals("f65v")
					//
							&& !element.getDescriptor().getIllustrationType().equals("A")
							&& !element.getDescriptor().getIllustrationType().equals("C")
							&& !element.getDescriptor().getIllustrationType().equals("Z");
				}
			});

			DistanceMeasure dist = new PositiveAngularDistance();
			List<? extends Cluster<BagOfWords>> clusters = doWork(doc, dist);
			ClusterEvaluator eval = new ClusterEvaluator(clusters, dist);

			System.out.println("Number of clusters: " + clusters.size());
			for (int i = 0; i < clusters.size(); ++i) {
				System.out.println("Cluster: " + i + ". s=" + eval.getSilhouette(clusters.get(i)));
//				for (BagOfWords bow : clusters.get(i).getPoints())
//					System.out.println(bow.getElement().getId() + ";" + i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	private static List<? extends Cluster<BagOfWords>> doWork(IvtffText doc, DistanceMeasure dist) {

		// Create data point and clustering parameters
		WordsInPageExperiment experiment = new WordsInPageExperiment(doc, BagOfWordsMode.RELATIVE_FREQUENCY);
		KMeansPlusPlusClusterer<BagOfWords> simpleClusterer = new KMeansPlusPlusClusterer<BagOfWords>(10, -1, dist);
		Clusterer<BagOfWords> clusterer = new MultiKMeansPlusPlusClusterer<BagOfWords>(simpleClusterer, 30);
		// Clusterer<BagOfWords> clusterer = new
		// DBSCANClusterer<BagOfWords>(0.5, 5, dist);

		return clusterer.cluster(experiment.getItems());
	}
}
