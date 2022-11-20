/**
 * 
 */
package io.github.mzattera.v4j.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.stat.inference.ChiSquareTest;

import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.util.Counter;

/**
 * Utility class to use chi-squared test in text analysis.
 * 
 * STATUS: Just implemented
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
// TODO write test
public final class ChiSquared {

	/**
	 * Describes distribution of a category (or "bin") of chars in a text.
	 * A bin is made by a set of chars, with their count and frequency.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class CharDistribution {

		private List<Character> chars = new ArrayList<>();

		/**
		 * @return Characters that form this category (or "bin") in a character
		 *         distribution.
		 * 
		 */
		public List<Character> getChars() {
			return chars;
		}

		private long count;

		/**
		 * 
		 * @return Count of characters in this category in the text.
		 */
		public long getCount() {
			return count;
		}

		private double frequency;

		/**
		 * 
		 * @return Relative frequency of characters in this category in the text.
		 */
		public double getFrequency() {
			return frequency;
		}

		public CharDistribution() {
			chars = new ArrayList<>();
			this.count = 0;
			this.frequency = 0.0d;
		}

		public CharDistribution(char c) {
			this(c, 0, 0.0d);
		}

		public CharDistribution(char c, long count, double frequency) {
			chars = new ArrayList<>();
			chars.add(c);
			this.count = count;
			this.frequency = frequency;
		}

		/**
		 * Merges two distributions by joining the characters and summing up count and
		 * relative frequencies.
		 * 
		 * @param other
		 * @return
		 */
		public CharDistribution merge(CharDistribution other) {
			for (char c : other.chars) {
				if (this.chars.contains(c))
					throw new IllegalArgumentException("Can only merge disjointed distributions.");
			}

			this.chars.addAll(other.chars);
			this.count += other.count;
			this.frequency += other.frequency;

			return this;
		}
	}

	private ChiSquared() {
	}

	/**
	 * Check whether observed distribution of character in a document matches the
	 * theoretical expected distribution.
	 *
	 * @param expected List with expected distribution of observed categories.
	 * @param toUpper  If true, text will be converted to upper case before
	 *                 performing the observation (if supported by the alphabet of
	 *                 the text).
	 * 
	 * @return The significance level, also denoted as alpha or α, is the
	 *         probability of rejecting the null hypothesis ('The two samples come
	 *         from a common distribution') when it is true.
	 */
	public static double chiSquareTest(Text txt, List<CharDistribution> expected, boolean toUpper) {
		
		long[] observed = ChiSquared.observe(txt, expected, toUpper, false);

		if (observed.length > expected.size()) {
			// We found additional characters not in the distribution; add a category
			expected = new ArrayList<>(expected);
			expected.add(new CharDistribution());
		}
		return ChiSquared.chiSquareTest(expected, observed);
	}

	/**
	 * Check whether observed distribution matches the theoretical expected
	 * distribution.
	 *
	 * @param expected List with expected distribution of observed categories.
	 * @param observed array of observed counts.
	 * 
	 * @return The significance level, also denoted as alpha or α, is the
	 *         probability of rejecting the null hypothesis ('The two samples come
	 *         from a common distribution') when it is true.
	 */
	public static double chiSquareTest(List<CharDistribution> expected, long[] observed) {
		double[] e = new double[Math.max(expected.size(), observed.length)];

		// Deal with cases where we observed some missing category
		int i = 0;
		for (; i < expected.size(); ++i)
			e[i] = expected.get(i).frequency;
		for (; i < observed.length; ++i)
			e[i] = 0.0d;

		return ChiSquared.chiSquareTest(e, observed);
	}

	/**
	 * Check whether observed distribution matches the theoretical expected
	 * distribution.
	 *
	 * @param expected array of expected frequencies.
	 * @param observed array of observed counts.
	 * 
	 * @return The significance level, also denoted as alpha or α, is the
	 *         probability of rejecting the null hypothesis ('The two samples come
	 *         from a common distribution') when it is true.
	 */
	public static double chiSquareTest(final double[] expected, final long[] observed) {
		return new ChiSquareTest().chiSquareTest(expected, observed);
	}

	/**
	 * Check weather given character is distributed differently in the two text
	 * samples.
	 * 
	 * @param toUpper If true, text will be converted to upper case before
	 *                performing the observation (if supported by the alphabet of
	 *                the text).
	 * 
	 * @return The significance level, also denoted as alpha or α, is the
	 *         probability of rejecting the null hypothesis ('The two samples come
	 *         from a common distribution') when it is true.
	 */
	public static double chiSquareTestDataSetsComparison(Text part1, Text part2, char c, boolean toUpper) {

		List<CharDistribution> categories = new ArrayList<>();
		categories.add(new CharDistribution(c, 0, 0));

		return ChiSquared.chiSquareTestDataSetsComparison(part1, part2, categories, toUpper);
	}

	/**
	 * Check weather given categories are distributed differently in the two text
	 * samples.
	 * 
	 * @param categories Categories used for observation.
	 * @param toUpper    If true, text will be converted to upper case before
	 *                   performing the observation (if supported by the alphabet of
	 *                   the text).
	 * 
	 * @return The significance level, also denoted as alpha or α, is the
	 *         probability of rejecting the null hypothesis ('The two samples come
	 *         from a common distribution') when it is true.
	 */
	public static double chiSquareTestDataSetsComparison(Text txt1, Text txt2, List<CharDistribution> categories,
			boolean toUpper) {

		long[] obs1 = observe(txt1, categories, toUpper, true);
		long[] obs2 = observe(txt2, categories, toUpper, true);

		return chiSquareTestDataSetsComparison(obs1, obs2);
	}

	/**
	 * Check weather two samples show a different distribution of categories.
	 * 
	 * @param obs1 observed counts of the first data set.
	 * @param obs2 observed counts of the of the second data set.
	 * 
	 * @return The significance level, also denoted as alpha or α, is the
	 *         probability of rejecting the null hypothesis ('The two samples come
	 *         from a common distribution') when it is true.
	 * 
	 * @throws DimensionMismatchException the the length of the arrays does not
	 *                                    match.
	 * @throws NotPositiveException       if any count is negative.
	 * @throws ZeroException              if either all counts of <code>obs1</code>
	 *                                    or <code>obs2</code> are zero.
	 * @throws MaxCountExceededException  if an error occurs during computation.
	 */
	public static double chiSquareTestDataSetsComparison(long[] obs1, long[] obs2) {

		// Check entries which are both 0, these cannot be used
		int z = 0;
		for (int i = 0; i < obs1.length; ++i) {
			if ((obs1[i] == 0) && (obs2[i] == 0))
				++z;
		}

		// Remove entries which are both 0
		if (z > 0) {
			long[] t1 = new long[obs1.length - z];
			long[] t2 = new long[t1.length];
			z = 0;
			for (int i = 0; i < obs1.length; ++i) {
				if ((obs1[i] == 0) && (obs2[i] == 0))
					continue;

				t1[z] = obs1[i];
				t2[z] = obs2[i];
				++z;
			}

			obs1 = t1;
			obs2 = t2;
		}

		return new ChiSquareTest().chiSquareTestDataSetsComparison(obs1, obs2);
	}

	/**
	 * Creates an observation by binning given character in the test in its own
	 * category.
	 * 
	 * @param doc     The text to analyze.
	 * @param toUpper If true, text will be converted to upper case before
	 *                performing the observation (if supported by the alphabet of
	 *                the text).
	 * 
	 * @return An array of size two with the first entry counting the occurrences of
	 *         given character and the other counting all remaining characters (if
	 *         any). . Notice only regular characters are considered.
	 */

	public static long[] observe(Text doc, char c, boolean toUpper) {

		List<CharDistribution> categories = new ArrayList<>();
		categories.add(new CharDistribution(c, 0, 0));
		return observe(doc, categories, toUpper, true);
	}

	/**
	 * Creates an observation by binning characters in the test accordingly to given
	 * categories.
	 * 
	 * @param doc        The text to analyze.
	 * @param categories A list of Character distributions, each distribution lists
	 *                   the characters for each category.
	 * @param toUpper    If true, text will be converted to upper case before
	 *                   performing the observation (if supported by the alphabet of
	 *                   the text).
	 * @param extend     If true and a character is not found in given categories,
	 *                   then extend <code>observed>/code>.
	 * 
	 * @return Number of characters in each category, aligned with
	 *         <code>categories</code>; if there are unmatched characters and
	 *                   <code>extend>/code> is <code>true</code> they will be added
	 *                   to last element of the array, which will be bigger than
	 *                   <code>categories</code>. Notice only regular characters are
	 *                   considered.
	 */

	public static long[] observe(Text doc, List<CharDistribution> categories, boolean toUpper, boolean extend) {
		long[] result = new long[categories.size()];
		Counter<Character> n = doc.getChars(toUpper);

		long missing = n.getTotalCounted();
		for (int i = 0; i < categories.size(); ++i) {
			for (char c : categories.get(i).chars) {
				result[i] += n.getCount(c);
				missing -= result[i];
			}
		}

		if (extend && (missing > 0)) {
			// there are unmatched chars
			long[] tmp = new long[result.length + 1];
			System.arraycopy(result, 0, tmp, 0, result.length);
			tmp[result.length] = missing;

			return tmp;
		} else
			return result;
	}

	/**
	 * @param toUpper If true, will convert text to upper case, if supported, before
	 *                building the distribution.
	 * 
	 * @return Distribution of characters in given text; only regular characters in
	 *         plain text are considered. A CharDistribution is created for each character in the text.
	 */
	public static List<CharDistribution> getCharDistribution(Text doc, boolean toUpper) {

		// Count characters
		Counter<Character> c = doc.getChars(toUpper);

		// Build corresponding distribution
		List<CharDistribution> result = new ArrayList<>(c.size());
		for (Entry<Character, Integer> e : c.entrySet())
			result.add(new CharDistribution(e.getKey(), e.getValue(), ((double) e.getValue() / c.getTotalCounted())));

		return result;
	}

	/**
	 * Adjust a distribution, such that expected frequency of each group of
	 * characters is >5; this is needed to do a valid chi-sqared test.
	 * 
	 * @param distribution The original, not adjusted, distribution with all
	 *                     categories (typically returned by
	 *                     {@link #getCharDistribution}).
	 * @param sampleSize   Size of the sample.
	 * @return An adjusted distribution, such that the expected number of occurrence
	 *         for each category is >5. This is obtained by merging together
	 *         categories with expected count <5 in the original distribution.
	 * @throws IllegalArgumentException if the resulting distribution will have too
	 *                                  few categories.
	 */
	public static List<CharDistribution> adjustDistribution(List<CharDistribution> distribution, long sampleSize) {

		List<CharDistribution> result = new ArrayList<>(distribution.size());
		CharDistribution merged = new CharDistribution(); // will collect all bins that are too small.
		CharDistribution smallest = null; // smallest but "big enough" bin.

		for (CharDistribution d : distribution) {
			if ((d.frequency * sampleSize) < 5) {
				// Expected number of occurrence is too small for this char; we merge it with
				// other small ones
				merged.merge(d);
			} else {
				if ((smallest == null) || (d.frequency < smallest.frequency))
					smallest = d;
				result.add(d);
			}
		}

		if ((merged.frequency * sampleSize) < 5) {
			// Merged group is still too small, we add it to the smallest one we found,
			// which we know is big enough
			if (smallest == null)
				throw new IllegalArgumentException("Sample too small.");
			smallest.merge(merged);
		} else {
			// Merged is big enough, add it
			result.add(merged);
		}

		if (distribution.size() < 2)
			throw new IllegalArgumentException("Not enough degrees of feeedom.");

		return result;
	}
}
