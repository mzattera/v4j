/**
 * 
 */
package org.v4j.applications;

import java.util.List;

import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.opencompare.hac.HierarchicalAgglomerativeClusterer;
import org.opencompare.hac.agglomeration.AgglomerationMethod;
import org.opencompare.hac.agglomeration.AverageLinkage;
import org.opencompare.hac.dendrogram.Dendrogram;
import org.opencompare.hac.dendrogram.DendrogramBuilder;
import org.opencompare.hac.experiment.DissimilarityMeasure;
import org.v4j.text.CompositeText;
import org.v4j.text.ElementFilter;
import org.v4j.text.ivtff.IvtffPage;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.util.BagOfWords.BagOfWordsMode;
import org.v4j.util.clustering.PositiveAngularDistance;
import org.v4j.util.clustering.SilhouetteComputation;
import org.v4j.util.clustering.WordsInPageExperiment;
import org.v4j.util.clustering.hac.ClusterableSet;
import org.v4j.util.clustering.hac.HacDissimilarityMeasure;
import org.v4j.util.clustering.hac.HacUtil;
import org.v4j.util.clustering.hac.Observation;

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
//			doc = doc.filterPages(new PageFilter.Builder().illustrationType("S").build());
			doc = doc.filterPages(new ElementFilter<IvtffPage>() {
			@Override
			public boolean keep(IvtffPage element) {
				return element.getDescriptor().getIllustrationType().equals("A")
						|| element.getDescriptor().getIllustrationType().equals("C")
						|| element.getDescriptor().getIllustrationType().equals("Z");
			}
		});

			// removes outliers
/*		doc = doc.filterPages(new ElementFilter<IvtffPage>() {
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
			});*/

			int numberOfClusters = 2;
			int minClusterSize = 3;
			BagOfWordsMode bowMode = BagOfWordsMode.TF_IDF;
			DistanceMeasure distance = new PositiveAngularDistance();
			AgglomerationMethod mode = new AverageLinkage();
			WordsInPageExperiment<IvtffPage> experiment = new WordsInPageExperiment<>(doc, bowMode);
			List<Cluster<Observation<?>>> clusters = doWork(experiment, numberOfClusters, minClusterSize, distance, mode);
			SilhouetteComputation cmp = new SilhouetteComputation(clusters, distance);

			// Print cluster stats
			System.out.println("Hierarchical Clusterng.");
			System.out.println("Distance: " + distance.getClass().getName());
			System.out.println("BoW Mode: " + bowMode.getClass().getName());
			System.out.println("Agglomeration Mode: " + mode.getClass().getName());
			
			System.out.println("Number of clusters: " + clusters.size() + " (s= " + cmp.getSilhouette() + ")");
			for (int i = 0; i < clusters.size(); ++i) {
				System.out.println("Cluster " + i + ": size " + clusters.get(i).getPoints().size() + " ("
						+ cmp.getSilhouette(clusters.get(i)) + ")");
			}

			// Print cluster items
			for (int i = 0; i < clusters.size(); ++i) {
				for (Observation<?> o : clusters.get(i).getPoints()) {
					System.out.println(
							experiment.getItem(o.getObservation()).getText().getId() + ";" + i + ";" + cmp.getSilhouette(o));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	public static List<Cluster<Observation<?>>> doWork(CompositeText<?> doc, int numClusters, int minSize,
			DistanceMeasure measure, AgglomerationMethod agglomerationMethod) {
		WordsInPageExperiment<?> experiment = new WordsInPageExperiment<>(doc, BagOfWordsMode.TF_IDF);
		return doWork(experiment, numClusters, minSize, measure, agglomerationMethod);
	}

	public static List<Cluster<Observation<?>>> doWork(ClusterableSet<? extends Clusterable> experiment,
			int numClusters, int minSize, DistanceMeasure measure, AgglomerationMethod agglomerationMethod) {

		// Create dendrogram.
		DissimilarityMeasure dissimilarityMeasure = new HacDissimilarityMeasure(measure);
		DendrogramBuilder dendrogramBuilder = new DendrogramBuilder(experiment.getNumberOfObservations());
		HierarchicalAgglomerativeClusterer clusterer = new HierarchicalAgglomerativeClusterer(experiment,
				dissimilarityMeasure, agglomerationMethod);
		clusterer.cluster(dendrogramBuilder);
		Dendrogram dendrogram = dendrogramBuilder.getDendrogram();

		// Form the dendrogram create clusters
		return HacUtil.split(dendrogram, numClusters, minSize, experiment);
	}
}
