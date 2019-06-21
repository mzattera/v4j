/**
 * 
 */
package org.v4j.util.clustering.hac;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.v4j.text.CompositeText;
import org.v4j.text.Text;
import org.v4j.util.BagOfWords;
import org.v4j.util.BagOfWords.BagOfWordsMode;
import org.v4j.util.Counter;

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
	 * Creates an experiment based on the elements in given document.
	 * 
	 * @param doc
	 */
	public WordsInPageExperiment(CompositeText<T> doc, BagOfWordsMode mode) {
		// our dimensions for the clustering will be the words in the text
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
