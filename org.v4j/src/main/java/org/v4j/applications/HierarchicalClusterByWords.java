/**
 * 
 */
package org.v4j.applications;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.opencompare.hac.HierarchicalAgglomerativeClusterer;
import org.opencompare.hac.agglomeration.AgglomerationMethod;
import org.opencompare.hac.agglomeration.AverageLinkage;
import org.opencompare.hac.dendrogram.Dendrogram;
import org.opencompare.hac.dendrogram.DendrogramBuilder;
import org.opencompare.hac.dendrogram.ObservationNode;
import org.opencompare.hac.experiment.DissimilarityMeasure;
import org.v4j.text.ElementFilter;
import org.v4j.text.ivtff.IvtffPage;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.util.BagOfWords.BagOfWordsMode;
import org.v4j.util.clustering.HacDissimilarityMeasure;
import org.v4j.util.clustering.HacUtil;
import org.v4j.util.clustering.PositiveAngularDistance;
import org.v4j.util.clustering.WordsInPageExperiment;

/**
 * Uses hac (hierarchical clustering) library to cluster pages in Voynich by the
 * word they share.
 * 
 * @author Massimiliano_Zattera
 *
 */
public class HierarchicalClusterByWords {

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
			
			doWork(doc, "D:\\Voynich Mobile\\Git - v4j\\org.v4j\\src\\main\\resources\\Output\\Cluster.csv");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	private static void doWork(IvtffText doc, String string) {

		// Create dendrogram. The Experiment is the set of pages, each treated as a BoW.
		// Here also define the distance measure and the agglomeration method.
		WordsInPageExperiment experiment = new WordsInPageExperiment(doc, BagOfWordsMode.RELATIVE_FREQUENCY);
		DissimilarityMeasure dissimilarityMeasure = new HacDissimilarityMeasure(new PositiveAngularDistance());
		AgglomerationMethod agglomerationMethod = new AverageLinkage();

		// Create dendrogram.
		DendrogramBuilder dendrogramBuilder = new DendrogramBuilder(experiment.getNumberOfObservations());
		HierarchicalAgglomerativeClusterer clusterer = new HierarchicalAgglomerativeClusterer(experiment,
				dissimilarityMeasure, agglomerationMethod);
		clusterer.cluster(dendrogramBuilder);
		Dendrogram dendrogram = dendrogramBuilder.getDendrogram();

		// Form the dendrogram create clusters
		// TODO make this the return type and move the printing in main() (see KMeansCLuster).
		Map<Integer, Set<ObservationNode>> clusters = HacUtil.split(dendrogram, 5);
		for (Entry<Integer, Set<ObservationNode>> cluster : clusters.entrySet()) {
			for (ObservationNode node : cluster.getValue()) {
				System.out.println(
						experiment.getItem(node.getObservation()).getElement().getId() + ";" + cluster.getKey());
			}
		}

		// Print left-first visit of the dendrogram
		// for (ObservationNode node : HacUtil.leftVisit(dendrogram)) {
		// PageHeader ph =
		// experiment.getClusterableObject(node.getObservation()).getElement().getDescriptor();
		// System.out.println(ph.getId() + " - " + ph.getIllustrationType() +
		// ph.getLanguage());
		// }
	}
}
