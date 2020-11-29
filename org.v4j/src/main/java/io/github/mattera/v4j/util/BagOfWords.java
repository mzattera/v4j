/**
 * 
 */
package io.github.mattera.v4j.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.clustering.Clusterable;

import io.github.mattera.v4j.text.Text;

/**
 * This is a bag of words for words in a given Text.
 * 
 * @author Massimiliano_Zattera
 */
public class BagOfWords implements Clusterable {

	public enum BagOfWordsMode {
		COUNT, // each dimension counts the occurrences for corresponding word.
		RELATIVE_FREQUENCY, // each dimension is the relative frequency of corresponding word in the text.
		ONE_HOT, // each dimension is 1 or 0, depending whether corresponding word is in the text
					// or not.
		TF_IDF // each dimension is the tf-idf frequency for corresponding word
	}

	private Text text;

	/**
	 * 
	 * @return text used to build this bag of words.
	 */
	public Text getText() {
		return text;
	}

	/**
	 * Maps each word that can appear in the BoW into corresponding dimension index.
	 */
	private Map<String, Integer> dimensions;

	/**
	 * @return a map that maps each word that can appear in the BoW into
	 *         corresponding dimension index.
	 */
	public Map<String, Integer> getDimensions() {
		return dimensions;
	}

	// vector representation of this BoW
	private double[] x;

	/**
	 * Returns a vector representing this Clusterable object. Notice that for
	 * performance reasons the internal representation is returned and not cloned,
	 * hence changing the returned vector will change this BoW.
	 * 
	 * @return vector representation of this object.
	 * @see io.github.mattera.v4j.util.clustering.Clusterable#asVector()
	 */
	@Override
	public double[] getPoint() {
		return x;
	}

	// total number of words (non null dimensions) in this BoW
	private int tot = 0;

	/**
	 * 
	 * @return number of different words in this BoW (that is number of non-zero
	 *         dimensions).
	 */
	public int getDifferentWordCount() {
		return tot;
	}

	/**
	 * 
	 * @param text
	 *            the Text used to extract bag of words.
	 * @param dimensions
	 *            lists the words to use in the BoW; maps each word into
	 *            corresponding dimension index.
	 * 
	 */
	public BagOfWords(Text text, Map<String, Integer> dimensions, BagOfWordsMode mode) {
		this.text = text;
		this.dimensions = new HashMap<>(dimensions);
		x = new double[dimensions.size()];
		Counter<String> words = text.getWords(false);
		for (Entry<String, Integer> e : words.entrySet()) {
			Integer i = dimensions.get(e.getKey());
			if (i != null)
				x[i] += e.getValue();
		}
		tot = 0;
		for (int i = 0; i < x.length; ++i)
			if (x[i] > 0)
				++tot;

		// adjust values based on mode
		switch (mode) {
		case COUNT:
			break;
		case RELATIVE_FREQUENCY:
			int tot = words.getTotalCounted();
			for (int i = 0; i < x.length; ++i)
				x[i] /= tot;
			break;
		case ONE_HOT:
			for (int i = 0; i < x.length; ++i)
				if (x[i] > 0.0)
					x[i] = 1.0;
			break;
		case TF_IDF:
			throw new IllegalArgumentException("TF_IDF is not available for a single Text instance; use toBoW()");
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 
	 * @param docs for each of this documents, a BagOfWords will be returned. Notice that documents with no words in dimensions are ignored.
	 * @param dimensions words to use as dimensions for the bag of words.
	 * @param mode Mode to use to build the bag of words.
	 * @return a list of bag of words from the give documents. 
	 */
	public static List<BagOfWords> toBoW(Collection<? extends Text> docs, Map<String, Integer> dimensions,
			BagOfWordsMode mode) {
		List<BagOfWords> result = new ArrayList<>(docs.size());
		if (mode == BagOfWordsMode.TF_IDF) {
			for (Text t : docs) {
				BagOfWords bow = new BagOfWords(t, dimensions, BagOfWordsMode.COUNT);
				if (bow.getDifferentWordCount() > 0)
					result.add(bow);
			}

			// number of documents in which each word in dictionary appears
			int[] df = new int[dimensions.size()];
			for (int i = 0; i < df.length; ++i) {
				for (BagOfWords bow : result)
					if (bow.x[i] > 0)
						++df[i];
			}

			// Adjust weights
			for (BagOfWords bow : result)
				for (int i = 0; i < df.length; ++i)
					if (df[i] > 0)
						bow.x[i] *= Math.log(result.size() / df[i]);

		} else {
			for (Text t : docs) {
				BagOfWords bow = new BagOfWords(t, dimensions, mode);
				if (bow.getDifferentWordCount() > 0)
					result.add(bow);
			}
		}

		return result;
	}
}
