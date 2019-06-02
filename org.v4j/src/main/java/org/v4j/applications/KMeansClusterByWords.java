/**
 * 
 */
package org.v4j.applications;

import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.evaluation.ClusterEvaluator;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.v4j.text.CompositeText;
import org.v4j.text.ElementFilter;
import org.v4j.text.ivtff.IvtffPage;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.util.BagOfWords;
import org.v4j.util.BagOfWords.BagOfWordsMode;
import org.v4j.util.clustering.MultiSizeClusterer;
import org.v4j.util.clustering.PositiveAngularDistance;
import org.v4j.util.clustering.SilhouetteComputation;
import org.v4j.util.clustering.SilhouetteEvaluator;
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
			ClusterEvaluator<BagOfWords> eval = new SilhouetteEvaluator<>(dist);
			List<? extends Cluster<BagOfWords>> clusters = doWork(doc, dist, eval, BagOfWordsMode.TF_IDF);
			SilhouetteComputation cmp = new SilhouetteComputation(clusters, dist);

			System.out.println("Number of clusters: " + clusters.size() + ": s = " + cmp.getSilhouette());
			for (int i = 0; i < clusters.size(); ++i)
				System.out.println("Cluster " + i + " [" + clusters.get(i).getPoints().size() + " items] : s = " + cmp.getSilhouette(clusters.get(i)));

			for (int i = 0; i < clusters.size(); ++i) {
				for (BagOfWords bow : clusters.get(i).getPoints())
					System.out.println(bow.getText().getId() + ";" + i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	private static List<? extends Cluster<BagOfWords>> doWork(CompositeText<?> doc, DistanceMeasure dist,
			ClusterEvaluator<BagOfWords> eval, BagOfWordsMode mode) {

		// Each element in the document becomes a BoW we can cluster.
		WordsInPageExperiment<?> experiment = new WordsInPageExperiment<>(doc, mode);

		Clusterer<BagOfWords> clusterer = new MultiSizeClusterer<>(2, 20, 100, dist, eval);
		return clusterer.cluster(experiment.getItems());
	}
}
