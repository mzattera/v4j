/**
 * 
 */
package io.github.mattera.v4j.util.clustering.hac;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.github.mattera.v4j.util.clustering.BagOfWords;

/**
 * This class allows to apply clustering of Bag of Words both with hac and
 * Apache libraries.
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
public class BagOfWordsExperiment implements ClusterableSet<BagOfWords> {

	// All the observations (bag of words) for this experiment.
	private List<BagOfWords> observations;

	/**
	 * Creates an experiment based on given list of BoW.
	 */
	public BagOfWordsExperiment(List<BagOfWords> bow) {
		observations = new ArrayList<BagOfWords>(bow);
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
