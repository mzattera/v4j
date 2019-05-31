/**
 * 
 */
package org.v4j.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.v4j.text.Text;

/**
 * This is a bag of words for words in a given Text.
 * 
 * @author Massimiliano_Zattera
 */
public class BagOfWords implements Clusterable {

	public enum BagOfWordsMode {
		COUNT, // each dimension counts the occurrences for corresponding word.
		RELATIVE_FREQUENCY, // each dimension is the relative frequency of corresponding word in the text.
		ONE_HOT // each dimension is 1 or 0, depending whether corresponding word is in the text
				// or not.
		// TF_IDF // each dimention is the tf-idf frequenccy fro corresponding work
	}

	private Text element;

	/**
	 * 
	 * @return element used to build this bag of words.
	 */
	public Text getElement() {
		return element;
	}

	/**
	 * Maps each word that can appear in the BoW into corresponding dimension index.
	 */
	private Map<String, Integer> dimensions;
	
	/**
	 * @return a map that maps each word that can appear in the BoW into corresponding dimension index.
	 */
	public Map<String, Integer> getDimensions(){
		return dimensions;
	}

	private double[] x;

	/**
	 * Returns a vector representing this Clusterable object. Notice that for
	 * performance reasons the internal representation is returned and not cloned,
	 * hence changing the returned vector will change this BoW.
	 * 
	 * @return vector representation of this object.
	 * @see org.v4j.util.clustering.Clusterable#asVector()
	 */
	@Override
	public double[] getPoint() {
		return x;
	}

	/**
	 * 
	 * @param element
	 *            the TextElement used to extract bag of words.
	 * @param dimensions
	 *            lists the words to use in the BoW; maps each word into
	 *            corresponding dimension index.
	 * 
	 */
	public BagOfWords(Text element, Map<String, Integer> dimensions, BagOfWordsMode mode) {
		this.element = element;
		this.dimensions = new HashMap<>(dimensions);
		x = new double[dimensions.size()];
		Counter<String> words = element.getWords(false);
		for (Entry<String, Integer> e : words.entrySet()) {
			Integer i = dimensions.get(e.getKey());
			if (i != null)
				x[i] += e.getValue();
		}

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
				if (x[i] > 1.0)
					x[i] = 1.0;
			break;
		default:
			throw new IllegalArgumentException();
		}
	}
}
