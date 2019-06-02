/**
 * 
 */
package org.v4j.util.clustering;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.v4j.text.CompositeText;
import org.v4j.text.Text;
import org.v4j.util.BagOfWords;
import org.v4j.util.BagOfWords.BagOfWordsMode;
import org.v4j.util.Counter;
import org.v4j.util.clustering.hac.ClusterableSet;

/**
 * This class allows to apply page clustering both with hac and apache
 * libraries.
 * 
 * An Experiment provides the observations, that is, the items that need to be
 * clustered.
 * 
 * In this experiment, each observation is a page, represented as a BoW. its
 * dimensions correspond to
 *
 * Observations (pages) can be used both to feed hac or apache clustering.
 * 
 * @author Massimiliano_Zattera
 *
 */
// TODO probably can be generalized much more.
public class WordsInPageExperiment<T extends Text> implements ClusterableSet<BagOfWords> {

	// Maps each word into corresponding dimension index.
	private Map<String, Integer> dimensions;

	// All the observations (pages) for this experiment.
	private List<BagOfWords> observations;

	/**
	 * Cretes an experiment based on the page in given document.
	 * 
	 * @param doc
	 */
	public WordsInPageExperiment(CompositeText<T> doc, BagOfWordsMode mode) {

		/*
		 * IvtffPage[] pages = doc.getPages().toArray(new IvtffPage[0]); String[] words
		 * = doc.getWords(true).itemSet().toArray(new String[0]);
		 * 
		 * // number of pages in which corresponding word appears. int[] numPages = new
		 * int[words.length];
		 * 
		 * for (IvtffPage page : pages) { Set<String> myWords =
		 * page.getWords(true).itemSet(); for (int i = 0; i < words.length; ++i) { if
		 * (myWords.contains(words[i])) numPages[i]++; } }
		 * 
		 * // our dimensions for the clustering will be the words appearing in at least
		 * 2 // distinct pages List<String> d = new ArrayList<String>(words.length); for
		 * (int i = 0; i < words.length; ++i) { if (numPages[i] > 1) d.add(words[i]); }
		 * dimensions = new HashMap<String, Integer>(); for (int i = 0; i < d.size();
		 * ++i) { dimensions.put(d.get(i), i); }
		 */

		// our dimensions for the clustering will be the words appearing in at least 2
		// distinct pages
		Counter<String> words = doc.getWords(true);
		dimensions = new HashMap<String, Integer>();
		int i = 0;
		for (String w : words.itemSet())
			dimensions.put(w, i++);

		// Observations to cluster.
		observations = BagOfWords.toBoW(doc.getElements(), dimensions, mode);
	}

	@Override
	public int size() {
		return getItems().size();
	}

	@Override
	public Collection<BagOfWords> getItems() {
		return observations;
	}

	@Override
	public BagOfWords getItem(int i) {
		return observations.get(i);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.opencompare.hac.experiment.Experiment#getNumberOfObservations()
	 */
	@Override
	public int getNumberOfObservations() {
		return size();
	}
}
