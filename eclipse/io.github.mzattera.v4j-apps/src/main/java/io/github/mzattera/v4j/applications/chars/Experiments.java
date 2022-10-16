/**
 * 
 */
package io.github.mzattera.v4j.applications.chars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.math3.stat.inference.AlternativeHypothesis;
import org.apache.commons.math3.stat.inference.BinomialTest;

import io.github.mzattera.v4j.text.ElementSplitter;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffLine;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.LineFilter;
import io.github.mzattera.v4j.text.txt.TextString;
import io.github.mzattera.v4j.util.Counter;

/**
 * This class is a container for subclasses of CharDistributionExperiment that
 * implement different experiments.
 * 
 * It also provides static methods to conveniently split Voynich text into
 * useful parts fro analysis (e.g., all first words in a line).
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class Experiments {

	/**
	 * Uses Chi-Square test to validate assumptions about character distributions.
	 * 
	 * This experiment compares first line of pages with reminder of the text.
	 * Notice only the text in running paragraphs is considered.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class FirstLineInPage extends TwoSamplesCharDistributionTest {

		@Override
		public Text[] splitDocument(Text txt) {
			// Consider only paragraphs
			IvtffText paragraphs = ((IvtffText) txt).filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);

			List<IvtffLine> x = new ArrayList<>();
			List<IvtffLine> y = new ArrayList<>();

			for (IvtffPage p : paragraphs.getElements()) {
				List<IvtffLine> l = p.getElements();
				x.add(l.get(0));
				for (int i = 1; i < l.size(); ++i)
					y.add(l.get(i));
			}

			return new IvtffText[] { new IvtffText(paragraphs, x), new IvtffText(paragraphs, y) };
		}
	}

	/**
	 * Uses Chi-Square test to validate assumptions about character distributions.
	 * 
	 * This experiment compares first line of paragraphs with reminder of the text.
	 * Notice only the text in running paragraphs is considered.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class FirstParagraphLine extends TwoSamplesCharDistributionTest {

		@Override
		public Text[] splitDocument(Text txt) {
			// Consider only paragraphs
			IvtffText paragraphs = ((IvtffText) txt).filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);

			List<IvtffLine> first = new ArrayList<>();
			List<IvtffLine> others = new ArrayList<>();

			for (IvtffPage p : paragraphs.getElements()) {
				boolean parEnd = true; // Was last line a paragraph end?
				for (IvtffLine l : p.getElements()) {
					if (parEnd)
						first.add(l);
					else
						others.add(l);

					parEnd = l.isLast();
				}
			}

			return new IvtffText[] { new IvtffText(paragraphs, first), new IvtffText(paragraphs, others) };
		}
	}

	/**
	 * Uses Chi-Square test to validate assumptions about character distributions.
	 * 
	 * This experiment compares last line of paragraphs with reminder of the text.
	 * Notice only the text in running paragraphs is considered.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class LastParagraphLine extends TwoSamplesCharDistributionTest {

		@Override
		public Text[] splitDocument(Text txt) {
			// Consider only paragraphs
			IvtffText paragraphs = ((IvtffText) txt).filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);

			List<IvtffLine> last = new ArrayList<>();
			List<IvtffLine> others = new ArrayList<>();

			for (IvtffLine l : paragraphs.getLines()) {
				if (l.isLast())
					last.add(l);
				else
					others.add(l);
			}

			return new IvtffText[] { new IvtffText(paragraphs, last), new IvtffText(paragraphs, others) };
		}
	}

	/**
	 * Uses Chi-Square test to validate assumptions about character distributions.
	 * 
	 * This experiment compares first letter of each line with first letter of other
	 * words in the text.
	 * Notice only the text in running paragraphs is considered.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class FirstLetter extends TwoSamplesCharDistributionTest {

		@Override
		public Text[] splitDocument(Text txt) {
			// Consider only paragraphs
			IvtffText paragraphs = ((IvtffText) txt).filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);

			StringBuilder first = new StringBuilder();
			StringBuilder others = new StringBuilder();

			for (IvtffLine l : paragraphs.getLines()) {
				String[] tokens = l.splitWords();
				if (tokens.length == 0)
					continue;
				first.append(tokens[0].charAt(0));
				for (int i = 1; i < tokens.length; ++i)
					others.append(tokens[i].charAt(0));
			}

			return new Text[] { new TextString(first.toString(), Alphabet.SLOT),
					new TextString(others.toString(), Alphabet.SLOT) };
		}
	}

	/**
	 * Uses Chi-Square test to validate assumptions about character distributions.
	 * 
	 * This experiment compares last letter of each line with last letter of other
	 * words in the text.
	 * Notice only the text in running paragraphs is considered.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class LastLetter extends TwoSamplesCharDistributionTest {

		@Override
		public Text[] splitDocument(Text txt) {
			// Consider only paragraphs
			IvtffText paragraphs = ((IvtffText) txt).filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);

			StringBuilder last = new StringBuilder();
			StringBuilder others = new StringBuilder();

			for (IvtffLine l : paragraphs.getLines()) {
				String[] tokens = l.splitWords();
				if (tokens.length == 0)
					continue;
				for (int i = 0; i < tokens.length - 1; ++i)
					others.append(tokens[i].charAt(tokens[i].length() - 1));
				last.append(tokens[tokens.length - 1].charAt(tokens[tokens.length - 1].length() - 1));
			}

			return new Text[] { new TextString(last.toString(), Alphabet.SLOT),
					new TextString(others.toString(), Alphabet.SLOT) };
		}
	}

	/**
	 * 
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first position in a line, Counter[1] is the same for second position
	 *         and so on. "Unreadable" words are ignored.
	 */
	public static List<Counter<String>> getWordsByPosition(IvtffText doc) {
		return getWordsByPosition(doc, 0, Integer.MAX_VALUE, false, false);
	}

	/**
	 * 
	 * @param minLineLen If line has less than this number of words, ignore it.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first position in a line, Counter[1] is the same for second position
	 *         and so on.
	 */
	public static List<Counter<String>> getWordsByPosition(IvtffText doc, int minLineLen) {
		return getWordsByPosition(doc, minLineLen, Integer.MAX_VALUE, false, false);
	}

	/**
	 * 
	 * @param minLineLen If line has less than this number of words, ignore it.
	 * @param maxLineLen If line has more than this number of words, ignore it.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first position in a line, Counter[1] is the same for second position
	 *         and so on. "Unreadable" words are ignored.
	 */
	public static List<Counter<String>> getWordsByPosition(IvtffText doc, int minLineLen, int maxLineLen) {
		return getWordsByPosition(doc, minLineLen, maxLineLen, false, false);
	}

	/**
	 * 
	 * @param minLineLen If line has less than this number of words, ignore it.
	 * @param maxLineLen If line has more than this number of words, ignore it.
	 * @param skipFirst  If true, ignore first word in each line. Notice that if
	 *                   this is true, Counter[0] will be empty.
	 * @param skipLast   If true, ignore last word in each line.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first position in a line, Counter[1] is the same for second position
	 *         and so on. "Unreadable" words are ignored.
	 */
	public static List<Counter<String>> getWordsByPosition(IvtffText doc, int minLineLen, int maxLineLen,
			boolean skipFirst, boolean skipLast) {
		List<Counter<String>> result = new ArrayList<>(50);
		if (skipFirst)
			result.add(new Counter<>()); // add first counter (which will stay empty).
		int min = skipFirst ? 1 : 0;

		for (IvtffPage p : doc.getElements()) {
			for (IvtffLine l : p.getElements()) {
				String[] w = l.splitWords();
				if ((w.length < minLineLen) || (w.length > maxLineLen))
					continue;
				for (int i = min; i < (skipLast ? w.length - 1 : w.length); ++i) {
					if (result.size() <= i)
						result.add(new Counter<>());
					if (!doc.getAlphabet().isUnreadable(w[i]))
						result.get(i).count(w[i]);
				}
			}
		}

		return result;
	}

	/**
	 * 
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         last position in a line, Counter[1] is the same for second last
	 *         position and so on. "Unreadable" words are ignored.
	 */
	public static List<Counter<String>> getWordsByPositionReversed(IvtffText doc) {
		return getWordsByPositionReversed(doc, 0, Integer.MAX_VALUE, false, false);
	}

	/**
	 * 
	 * @param minLineLen If line has less than this number of words, ignore it.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         last position in a line, Counter[1] is the same for second last
	 *         position and so on. "Unreadable" words are ignored.
	 */
	public static List<Counter<String>> getWordsByPositionReversed(IvtffText doc, int minLineLen) {
		return getWordsByPositionReversed(doc, minLineLen, Integer.MAX_VALUE, false, false);
	}

	/**
	 * 
	 * @param minLineLen If line has less than this number of words, ignore it.
	 * @param maxLineLen If line has more than this number of words, ignore it.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         last position in a line, Counter[1] is the same for second last
	 *         position and so on. "Unreadable" words are ignored.
	 */
	public static List<Counter<String>> getWordsByPositionReversed(IvtffText doc, int minLineLen, int maxLineLen) {
		return getWordsByPositionReversed(doc, minLineLen, maxLineLen, false, false);
	}

	/**
	 * 
	 * @param minLineLen If line has less than this number of words, ignore it.
	 * @param maxLineLen If line has more than this number of words, ignore it.
	 * @param skipFirst  If true, ignore last word in each line.
	 * @param skipLast   If true, ignore last word in each line. Notice that if this
	 *                   is true, Counter[0] will be empty.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         last position in a line, Counter[1] is the same for second last
	 *         position and so on. "Unreadable" words are ignored.
	 */
	public static List<Counter<String>> getWordsByPositionReversed(IvtffText doc, int minLineLen, int maxLineLen,
			boolean skipFirst, boolean skipLast) {
		List<Counter<String>> result = new ArrayList<>(50);
		if (skipLast)
			result.add(new Counter<>()); // add first counter (which will stay empty).
		int min = skipLast ? 1 : 0;

		for (IvtffPage p : doc.getElements()) {
			for (IvtffLine l : p.getElements()) {
				String[] w = l.splitWords();
				if ((w.length < minLineLen) || (w.length > maxLineLen))
					continue;
				for (int i = min; i < (skipFirst ? w.length - 1 : w.length); ++i) {
					if (result.size() <= i)
						result.add(new Counter<>());
					if (!doc.getAlphabet().isUnreadable(w[w.length - 1 - i]))
						result.get(i).count(w[w.length - 1 - i]);
				}
			}
		}

		return result;
	}

	/**
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first line in a paragraph, Counter[1] is the same for second line and
	 *         so on. "Unreadable" words are ignored.
	 */
	public static List<Counter<String>> getWordsByLine(IvtffText txt) {
		return getWordsByLine(txt, 0, Integer.MAX_VALUE);
	}

	/**
	 * @param minLineLen If line has less than this number of words, ignore it.
	 * @param maxLineLen If line has more than this number of words, ignore it.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first line in a paragraph, Counter[1] is the same for second line and
	 *         so on. "Unreadable" words are ignored.
	 */
	public static List<Counter<String>> getWordsByLine(IvtffText txt, int minLineLen, int maxLineLen) {

		List<Counter<String>> result = new ArrayList<>();

		int pos = 0;
		for (IvtffPage p : txt.getElements()) {
			for (IvtffLine l : p.getElements()) {

				// Always ensure results addresses all rows
				if (pos >= result.size())
					result.add(new Counter<String>());
				Counter<String> c = result.get(pos);

				String[] w = l.splitWords();
				if ((w.length >= minLineLen) && (w.length <= maxLineLen)) {
					for (String s : w) {
						if (!txt.getAlphabet().isUnreadable(s))
							c.count(s);
					}
				}

				if (l.isLast()) // end of paragraph
					pos = 0;
				else
					pos++;
			}
		}

		return result;
	}

	/**
	 * @param discardFirstLine If true, remove first line of each paragraph.
	 * @param discardLastLine  If true, remove last line of each paragraph.
	 * @return Input document, after filtering.
	 */
	public static IvtffText filterLines(IvtffText doc, boolean discardFirstLine, boolean discardLastLine) {
		// note the order is important because how Experiments work
		if (discardFirstLine)
			doc = (IvtffText) new FirstParagraphLine().splitDocument(doc)[1];
		if (discardLastLine)
			doc = (IvtffText) new LastParagraphLine().splitDocument(doc)[1];
		return doc;
	}

	/**
	 * 
	 * @param counter     Word distribution to observe.
	 * @param bins        The words to consider as "bins" for chi-squared test. Each
	 *                    word is mapped into an index of the returned array.
	 * @param addGericBin If true, crate an additional element at end of result,
	 *                    counting all words not in <code>bins</code>; otherwise
	 *                    they are ignored.
	 * @return An array with count of words in each bin.
	 */
	public static long[] observe(Counter<String> counter, Map<String, Integer> bins, boolean addGenericBin) {
		return observe(counter, bins, addGenericBin, null);
	}

	/**
	 * 
	 * @param counter     Word distribution to observe.
	 * @param bins        The words to consider as "bins" for chi-squared test. Each
	 *                    word is mapped into an index of the returned array.
	 * @param addGericBin If true, crate an additional element at end of result,
	 *                    counting all words not in <code>bins</code>; otherwise
	 *                    they are ignored.
	 * @param current     If not null, counts will be added to this array; it is
	 *                    expected this not null and having the right length.
	 * @return An array with count of words in each bin.
	 */
	public static long[] observe(Counter<String> counter, Map<String, Integer> bins, boolean addGenericBin,
			long[] current) {
		if (current == null)
			current = new long[addGenericBin ? bins.size() + 1 : bins.size() + 1];

		for (Entry<String, Integer> e : counter.entrySet()) {
			if (bins.containsKey(e.getKey())) { // the word is a feature; record its observed count
				current[bins.get(e.getKey())] += e.getValue();
			} else { // add this to "all other stuff bin"
				if (addGenericBin)
					current[bins.size()] += e.getValue();
			}
		}
		return current;
	}

	/**
	 * @return A Map from each cluster (as defined in Note 003) into corresponding.
	 */
	public static Map<String, IvtffText> splitClusters(IvtffText voynichParagraphs) {
		Map<String, IvtffText> clusterParagraphs = voynichParagraphs.splitPages(new ElementSplitter<IvtffPage>() {

			@Override
			public String getCategory(IvtffPage element) {
				return element.getDescriptor().getCluster();
			}
		});
		clusterParagraphs.remove("?");
		return clusterParagraphs;
	}

	/**
	 * @param A Map containing a set of Voynich texts.
	 * 
	 * @return The input map, where each Voynich text is randomly scrambled. The
	 *         number of pages, lines, and words in the lines is kept, but words are
	 *         randomly scrambled around.
	 */
	public static Map<String, IvtffText> shuffleClusters(Map<String, IvtffText> clusterParagraphs) {
		return shuffleClusters(clusterParagraphs, new Random(System.currentTimeMillis()));
	}

	/**
	 * @param A Map containing a set of Voynich texts.
	 * 
	 * @return The input map, where each Voynich text is randomly scrambled. The
	 *         number of pages, lines, and words in the lines is kept, but words are
	 *         randomly scrambled around.
	 */
	public static Map<String, IvtffText> shuffleClusters(Map<String, IvtffText> clusterParagraphs, Random rnd) {
		Map<String, IvtffText> shuffledParagraphs = new HashMap<>();
		for (Entry<String, IvtffText> e : clusterParagraphs.entrySet()) {
			shuffledParagraphs.put(e.getKey(), e.getValue().shuffledText(rnd));
		}
		return shuffledParagraphs;
	}

	private static final BinomialTest BINOMIAL = new BinomialTest();

	/**
	 * @param sample
	 * @param population
	 * @return A list of words that appear more frequently in sample (alpha=1%) than
	 *         in the population.
	 */
	public static Counter<String> getInterstingWords(Counter<String> sample, Counter<String> population) {
		Counter<String> result = new Counter<>();

		for (String w : sample.itemSet()) {
			double observed = (double) sample.getCount(w) / sample.getTotalCounted();
			double expected = (double) population.getCount(w) / population.getTotalCounted();
			if (observed <= expected)
				continue;
			double alpha = BINOMIAL.binomialTest(sample.getTotalCounted(), sample.getCount(w), expected,
					AlternativeHypothesis.GREATER_THAN);
			if (alpha < 0.01) {
				result.count(w, sample.getCount(w));
			}
		}

		return result;
	}

	/**
	 * @param wordsByPosition A List of Counters, counting words appearing in
	 *                        specific position (e.g. at a given position in a
	 *                        line).
	 * @return A double[] of same size than wordsByPosition with % of "interesting"
	 *         words in each Counter. "Interesting" words are those appearing in one
	 *         Counter with a higher frequency than in other Counters (alpha=1%).
	 */
	public static double[] getInterestingWordsPercent(List<Counter<String>> wordsByPosition) {
		double[] result = new double[wordsByPosition.size()];
		for (int i = 0; i < wordsByPosition.size(); ++i) {
	
			// Words in position i vs. all other words
			Counter<String> first = wordsByPosition.get(i);
			Counter<String> other = new Counter<>();
			for (int j = 0; j < wordsByPosition.size(); ++j) {
				if (i == j)
					continue;
				other.countAll(wordsByPosition.get(j));
			}
	
			Counter<String> interesting = getInterstingWords(first, other);
			result[i] = (double) interesting.getTotalCounted() / wordsByPosition.get(i).getTotalCounted();
		}
	
		return result;
	}
}
