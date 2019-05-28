/**
 * 
 */
package org.v4j.util;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.math3.ml.clustering.Clusterable;
import org.v4j.text.TextElement;


/**
 * This is a bag of words for words in a given TextElement.
 * 
 * @author Massimiliano_Zattera
 */
public class BagOfWords<T extends TextElement> implements Clusterable {

	public enum BagOfWordsMode {
		COUNT, // each dimension counts the occurrences for corresponding word.
		RELATIVE_FREQUENCY, // each dimension is the relative frequency of corresponding word in the text.
		ONE_HOT // each dimension is 1 or 0, depending whether corresponding word is in the text
				// or not.
	}

	private T element;

	/**
	 * 
	 * @return element used to build this bag of words.
	 */
	public T getElement() {
		return element;
	}

	double[] x;

	/**
	 * 
	 * @return vector representation of this DataObject.
	 * @see org.v4j.util.clustering.Clusterable#asVector()
	 */
	@Override
	public double[] getPoint() {
		return x;
	}

	/**
	 * 
	 * @param el
	 *            the TextElement used to extract bag of words.
	 * @param dimensions
	 *            lists the words to use in the BoW; maps each word into
	 *            corresponding dimension index.
	 * 
	 */
	public BagOfWords(T el, Map<String, Integer> dimensions, BagOfWordsMode mode) {
		element = el;
		x = new double[dimensions.size()];
		Counter<String> words = el.getWords(false);
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
		
		int t = 0;
		for (int i = 0; i < x.length; ++i)
			if (x[i] > 0)
				++t;
	}
}
