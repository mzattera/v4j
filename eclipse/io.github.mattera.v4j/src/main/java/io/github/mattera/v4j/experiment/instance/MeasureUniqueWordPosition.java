/**
 * 
 */
package io.github.mattera.v4j.experiment.instance;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import io.github.mattera.v4j.experiment.Measurement;
import io.github.mattera.v4j.text.ivtff.IvtffLine;
import io.github.mattera.v4j.text.ivtff.IvtffPage;
import io.github.mattera.v4j.util.Counter;
import io.github.mattera.v4j.util.MathUtil;

import java.util.Set;

/**
 * This measurement measures where unique words appear in a page. It returns a
 * double[][] as big as the biggest page being measured (in chars) where each
 * element measure the number of times a unique word was above given character
 * in the page.
 * 
 * @author Massimiliano_Zattera
 *
 */

public final class MeasureUniqueWordPosition extends Measurement<IvtffPage, long[][]> {

	// if > 0 we shrink the results to this number of rows.
	private final int m;

	// if > 0 we shrink the results to this number of columns.
	private final int n;

	public MeasureUniqueWordPosition() {
		super(null);
		m = Integer.MAX_VALUE;
		n = Integer.MAX_VALUE;
	}

	/**
	 * Constructor. This measurements shrinks the returned matrix (see
	 * MathUtil.shrink()).
	 * 
	 * @param m number of rows of the returned shrinked matrix.
	 * @param n number of columns of the returned shrinked matrix.
	 */
	public MeasureUniqueWordPosition(int m, int n) {
		super(null);
		this.m = m;
		this.n = n;
	}

	@Override
	public long[][] measure(Collection<IvtffPage> population) {

		// Creates matrix big enough to contain results
		int r = 0, c = 0;
		for (IvtffPage page : population) {
			List<IvtffLine> lines = page.getElements();
			r = Math.max(r, lines.size());
			for (IvtffLine l : lines) {
				c = Math.max(c, l.getPlainText().length());
			}
		}

		// get unique words in the population
		Set<String> unique = new HashSet<String>();
		for (IvtffPage p : population) {
			Counter<String> count = p.getWords(true);
			for (Entry<String, Integer> e : count.entrySet()) {
				if (e.getValue() == 1)
					unique.add(e.getKey());
			}
		}

		long[][] result = new long[r][c];

		// TODO: implement
		for (IvtffPage page : population) {
			r = 0;
			for (IvtffLine line : page.getElements()) {
				c = 0;
				for (String w : line.splitWords()) {
					if (unique.contains(w)) {
						// Unique word: all characters in the page are marked as such
						for (int i = 0; i < w.length(); ++i) {
							++result[r][c++];
						}
					} else {
						c += w.length();
					}
					++c; // count for space between words
				}
				++r;
			} // for each line in page
		} // for each page

		return MathUtil.shrink(result, m, n);
	}
}
