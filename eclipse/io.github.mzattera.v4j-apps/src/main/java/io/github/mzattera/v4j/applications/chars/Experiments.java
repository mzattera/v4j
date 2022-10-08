/**
 * 
 */
package io.github.mzattera.v4j.applications.chars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

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
	 * This experiment compares random lines of paragraphs which are not first or
	 * last.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class Null extends TwoSamplesCharDistributionTest {

		@Override
		public Text[] splitDocument(Text txt) {
			// Consider only paragraphs
			IvtffText paragraphs = ((IvtffText) txt).filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);

			List<IvtffLine> x = new ArrayList<>();
			List<IvtffLine> y = new ArrayList<>();

			// Consider all but first and last line
			for (IvtffPage p : paragraphs.getElements()) {
				boolean lastParEnd = true;
				for (IvtffLine l : p.getElements()) {
					boolean thisParEnd = (l.getText().indexOf("<$>") != -1);
					if (!lastParEnd && !thisParEnd)
						x.add(l);

					lastParEnd = thisParEnd;
				}
			}

			// Randomly mix other lines
			Random rnd = new Random();
			while (x.size() > y.size())
				y.add(x.remove(rnd.nextInt(x.size())));

			return new IvtffText[] { new IvtffText(paragraphs, x), new IvtffText(paragraphs, y) };
		}
	}

	/**
	 * Uses Chi-Square test to validate assumptions about character distributions.
	 * 
	 * This experiment compares first line of pages with reminder of the text.
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

					parEnd = (l.getText().indexOf("<$>") != -1);
				}
			}

			return new IvtffText[] { new IvtffText(paragraphs, first), new IvtffText(paragraphs, others) };
		}
	}

	/**
	 * Uses Chi-Square test to validate assumptions about character distributions.
	 * 
	 * This experiment compares last line of paragraphs with reminder of the text.
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
				if (l.getText().indexOf("<$>") != -1)
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
	 * @param skipFirst  If true, ignore first word in each line. Notice that if this is true, Counter[0] will be empty.
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
	 *         last position in a line, Counter[1] is the same for second last position
	 *         and so on. "Unreadable" words are ignored.
	 */
	public static List<Counter<String>> getWordsByPositionReversed(IvtffText doc) {
		return getWordsByPositionReversed(doc, 0, Integer.MAX_VALUE, false, false);
	}

	/**
	 * 
	 * @param minLineLen If line has less than this number of words, ignore it.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         last position in a line, Counter[1] is the same for second last position
	 *         and so on. "Unreadable" words are ignored.
	 */
	public static List<Counter<String>> getWordsByPositionReversed(IvtffText doc, int minLineLen) {
		return getWordsByPositionReversed(doc, minLineLen, Integer.MAX_VALUE, false, false);
	}

	/**
	 * 
	 * @param minLineLen If line has less than this number of words, ignore it.
	 * @param maxLineLen If line has more than this number of words, ignore it.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         last position in a line, Counter[1] is the same for second last position
	 *         and so on. "Unreadable" words are ignored.
	 */
	public static List<Counter<String>> getWordsByPositionReversed(IvtffText doc, int minLineLen, int maxLineLen) {
		return getWordsByPositionReversed(doc, minLineLen, maxLineLen, false, false);
	}
	
	/**
	 * 
	 * @param minLineLen If line has less than this number of words, ignore it.
	 * @param maxLineLen If line has more than this number of words, ignore it.
	 * @param skipFirst  If true, ignore first word in each line.
	 * @param skipFirst  If true, ignore last word in each line. Notice that if this is true, Counter[0] will be empty.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         last position in a line, Counter[1] is the same for second last position
	 *         and so on. "Unreadable" words are ignored.
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
					if (!doc.getAlphabet().isUnreadable(w[w.length-1-i]))
						result.get(i).count(w[w.length-1-i]);
				}
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
}
