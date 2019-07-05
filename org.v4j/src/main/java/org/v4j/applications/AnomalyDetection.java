/**
 * 
 */
package org.v4j.applications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.v4j.text.CompositeText;
import org.v4j.text.Text;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.PageFilter;
import org.v4j.text.ivtff.PageHeader;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.util.BagOfWords;
import org.v4j.util.BagOfWords.BagOfWordsMode;
import org.v4j.util.Counter;
import org.v4j.util.clustering.PositiveAngularDistance;

/**
 * This class computes average distance between pages in a cluster and detects
 * "outliers".
 * 
 * @author Massimiliano_Zattera
 *
 */
public final class AnomalyDetection {

	private AnomalyDetection() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DistanceMeasure dist = new PositiveAngularDistance();
			IvtffText voy = VoynichFactory.getDocument(TranscriptionType.MAJORITY);

			// Entropy for Voynich sections
			for (String cluster : PageHeader.CLUSTERS) {
				IvtffText doc = voy.filterPages(new PageFilter.Builder().cluster(cluster).build());
				List<Text> outliers = process(doc, dist, 1.0, BagOfWordsMode.TF_IDF);
				
				System.out.println("Cluster " + cluster + " outliers:");
				for (Text t : outliers) {
					System.out.println("\t" + t.getId());					
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	/**
	 * Calculate average distance between BoW created from pages in given document
	 * the returns pages which average distance from other page is outside d
	 * standard deviations.
	 * 
	 * @param d return pages outside d standard deviations.
	 * @param dist measure distance to use.
	 * @param mode BoW creation mode.
	 */
	public static List<Text> process(CompositeText<?> doc, DistanceMeasure dist, double d, BagOfWordsMode mode) {
		// Maps each word into corresponding dimension index.
		Map<String, Integer> dimensions;

		// our dimensions for the BoW will be the words in the text
		Counter<String> words = doc.getWords(true);
		dimensions = new HashMap<String, Integer>();
		int n = 0;
		for (String w : words.itemSet())
			dimensions.put(w, n++);

		// Create BoW for the elements (pages) in the document
		List<BagOfWords> bow = BagOfWords.toBoW(doc.getElements(), dimensions, mode);

		// calculate distance between elements (pages) in the document
		double[][] x = new double[bow.size()][bow.size()];
		for (int i = 0; i < bow.size(); ++i) {
			for (int j = 0; j < i; ++j) {
				x[i][j] = dist.compute(bow.get(i).getPoint(), bow.get(j).getPoint());
				x[j][i] = x[i][j];
			}
		}

		// calculate average distance between elements (pages) in the document
		double avg = 0.0;
		n = bow.size() * (bow.size() - 1) / 2;
		for (int i = 0; i < bow.size(); ++i) {
			for (int j = 0; j < i; ++j) {
				avg += x[i][j];
			}
		}
		avg /= n;

		// calculate standard deviation
		double stddev = 0.0;
		for (int i = 0; i < bow.size(); ++i) {
			for (int j = 0; j < i; ++j) {
				stddev += (x[i][j] - avg) * (x[i][j] - avg);
			}
		}
		stddev = Math.sqrt(stddev / n);

		// Find all elements (pages) which average distance is outside x standard
		// deviations
		List<Text> result = new ArrayList<>();
		for (int i = 0; i < bow.size(); ++i) { // for each page
			double a = 0.0; // average distance
			for (int j = 0; j < bow.size(); ++j) {
				a += x[i][j];
			}
			a /= bow.size()-1;

//			System.out.println(bow.get(i).getText().getId() + ": " + Math.abs(avg -a));

			if (Math.abs(avg -a) > (stddev * d)) { // this is an outlier
				result.add(bow.get(i).getText());
//				System.out.println("   -> ***");
			}
		}
		
		return result;
	}
}
