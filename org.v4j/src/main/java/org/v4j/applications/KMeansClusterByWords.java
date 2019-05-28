/**
 * 
 */
package org.v4j.applications;

import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.v4j.text.ElementFilter;
import org.v4j.text.ivtff.IvtffPage;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.util.BagOfWords;
import org.v4j.util.BagOfWords.BagOfWordsMode;
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
//			doc = doc.filterPages(new ElementFilter<IvtffPage>() {
//				@Override
//				public boolean keep(IvtffPage element) {
//					// these are identified by looking at hierarchical clusters of 1 single page.
//					// Notice they correspond to pages in ? language
//					return !element.getId().equals("f116v") && !element.getId().equals("f53r")
//							&& !element.getId().equals("f87v") && !element.getId().equals("f65r")
//							//
//							&& !element.getId().equals("f24v") && !element.getId().equals("f27v")
//							&& !element.getId().equals("f48r") && !element.getId().equals("f50r")
//							&& !element.getId().equals("f57r") && !element.getId().equals("f65v")
//							//
//							&& !element.getDescriptor().getIllustrationType().equals("A")
//							&& !element.getDescriptor().getIllustrationType().equals("C")
//							&& !element.getDescriptor().getIllustrationType().equals("Z");
//				}
//			});

			doWork(doc, "D:\\Voynich Mobile\\Git - v4j\\org.v4j\\src\\main\\resources\\Output\\Cluster.csv");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	private static void doWork(IvtffText doc, String string) {

		// Create data point and clustering parameters
		WordsInPageExperiment experiment = new WordsInPageExperiment(doc, BagOfWordsMode.RELATIVE_FREQUENCY);
		DistanceMeasure dist = new PositiveAngularDistance();
//		DistanceMeasure dist = new EuclideanDistance();
//		KMeansPlusPlusClusterer<BagOfWords<IvtffPage>> simpleClusterer = new KMeansPlusPlusClusterer<BagOfWords<IvtffPage>>(
//				5, -1, dist);
//		Clusterer<BagOfWords<IvtffPage>> clusterer = new MultiKMeansPlusPlusClusterer<BagOfWords<IvtffPage>>(
//				simpleClusterer, 30);
		
		// Best DBSCAN result relative freq. Bow + PositiveAngularDistance
		Clusterer<BagOfWords<IvtffPage>> clusterer = new DBSCANClusterer<BagOfWords<IvtffPage>>(0.5, 5, dist);

		List<? extends Cluster<BagOfWords<IvtffPage>>> clusters = clusterer.cluster(experiment.getItems());
		System.out.println("Number of clusters: " + clusters.size());
		for (int i = 0; i < clusters.size(); ++i)
			for (BagOfWords<IvtffPage> bow : clusters.get(i).getPoints())
				System.out.println(bow.getElement().getId() + ";" + i);
	}
}
