/* Copyright (c) 2022 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.experiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.inference.AlternativeHypothesis;
import org.apache.commons.math3.stat.inference.BinomialTest;

import io.github.mzattera.v4j.text.ElementSplitter;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffLine;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.LineFilter;
import io.github.mzattera.v4j.text.ivtff.ParseException;
import io.github.mzattera.v4j.text.txt.TextString;
import io.github.mzattera.v4j.util.Counter;

/**
 * Each Experiment inner class, divides a Text in two parts, accordingly some
 * rules, these are typically used to collect and compare statistical properties
 * of the two parts (e.g. to check if char distribution differs).
 * 
 * In addition, this class also provides static methods to conveniently slice
 * and dice Voynich text for analysis, and to collect data about text, useful in
 * analysis.
 * 
 * Notice JUnit tests exist for this class (<code>ExperimentClassessTest</code>,
 * <code>ExperimentMethodsTest</code>); keep them updated if you change this
 * class.
 * 
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public abstract class Experiment {

	/**
	 * This Experiment splits first line of pages from reminder of the text. Notice
	 * only the text in running paragraphs is considered.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class FirstLineInPage extends Experiment {

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
	 * This Experiment splits first line of paragraphs from reminder of the text.
	 * Notice only the text in running paragraphs is considered.
	 * 
	 * A flag can be passed to the constructor to decide whether to skip first word
	 * in the paragraph (notice this applies only for the first line of each
	 * paragraph).
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class FirstLineInParagraph extends Experiment {

		private final boolean skipFirstInFirstLine;

		/**
		 * 
		 * @param skipFirstInFirstLine If true, skip first word in first line: notice
		 *                             other lines will contain all words.
		 */

		public FirstLineInParagraph(boolean skipFirstInFirstLine) {
			this.skipFirstInFirstLine = skipFirstInFirstLine;
		}

		@Override
		public Text[] splitDocument(Text txt) {
			// Consider only paragraphs
			IvtffText paragraphs = ((IvtffText) txt).filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);
			Alphabet a = txt.getAlphabet();

			List<IvtffLine> first = new ArrayList<>();
			List<IvtffLine> others = new ArrayList<>();

			for (IvtffPage p : paragraphs.getElements()) {
				boolean parEnd = true; // Was last line a paragraph end?
				for (IvtffLine l : p.getElements()) {
					if (parEnd) { // first line of paragraph

						if (skipFirstInFirstLine) { // remove first word from line, if required
							IvtffLine n = new IvtffLine(l);
							String[] w = l.splitWords();
							if (w.length > 0) {
								try {
									n.setText(
											StringUtils.join(w, a.getSpace(), 1, w.length) + (l.isLast() ? "<$>" : ""));
								} catch (ParseException e) { // shall never happen
								}
							}
							first.add(n);

						} else { // ad the line as it is
							first.add(l);
						}
					} else { // not first line
						others.add(l);
					}

					parEnd = l.isLast();
				}
			}

			return new IvtffText[] { new IvtffText(paragraphs, first), new IvtffText(paragraphs, others) };
		}
	}

	/**
	 * This Experiment splits first word in paragraphs from all others. Notice only
	 * the text in running paragraphs is considered.
	 * 
	 * In the constructor, a flag is used to indicate whether "unreadable" words
	 * should be considered or not.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class FirstWordInParagraph extends Experiment {

		private final boolean readableOnly;

		/**
		 * @param readableOnly if true, consider only the words that do not contain any
		 *                     unreadable characters.
		 */
		public FirstWordInParagraph(boolean readableOnly) {
			this.readableOnly = readableOnly;
		}

		@Override
		public Text[] splitDocument(Text txt) {

			IvtffText[] parts = (IvtffText[]) (new Experiment.FirstLineInParagraph(false).splitDocument(txt));

			// Words in first lines, by position
			List<Counter<String>> words = Experiment.getWordsByPosition(parts[0], readableOnly);

			// First words in first lines
			Counter<String> first = words.get(0);

			// Other words in first line
			Counter<String> other = new Counter<>();
			for (int i = 1; i < words.size(); ++i)
				other.countAll(words.get(i));

			// Other words in other lines
			other.countAll(parts[1].getWords(readableOnly));

			return new Text[] { new TextString(first, txt.getAlphabet()), new TextString(other, txt.getAlphabet()) };
		}
	}

	/**
	 * This Experiment splits last line of paragraphs from reminder of the text.
	 * Notice only the text in running paragraphs is considered.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class LastLineInParagraph extends Experiment {

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
	 * This Experiment splits first word of each line from all other words in the
	 * text. In the constructor, an optional flag can be passed to skip first line
	 * of each paragraph. In addition, a flag is used to indicate whether
	 * "unreadable" words should be considered or not.
	 * 
	 * Notice only the text in running paragraphs is considered.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class FirstWordInLine extends Experiment {

		private final boolean skipFirst;

		private final boolean readableOnly;

		/**
		 * 
		 * @param skipFirst    If true, skip first line of paragraphs.
		 * @param readableOnly if true, consider only the words that do not contain any
		 *                     unreadable characters.
		 */
		public FirstWordInLine(boolean skipFirst, boolean readableOnly) {
			this.skipFirst = skipFirst;
			this.readableOnly = readableOnly;
		}

		@Override
		public Text[] splitDocument(Text txt) {
			Alphabet a = txt.getAlphabet();

			// Consider only paragraphs
			IvtffText paragraphs = ((IvtffText) txt).filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);

			if (skipFirst)
				paragraphs = Experiment.filterLines(paragraphs, true, false);

			List<String> first = new ArrayList<>();
			List<String> others = new ArrayList<>();

			for (IvtffLine l : paragraphs.getLines()) {
				String[] tokens = l.splitWords();
				if (tokens.length == 0)
					continue;
				if (!readableOnly || !a.isUnreadable(tokens[0]))
					first.add(tokens[0]);
				for (int i = 1; i < tokens.length; ++i)
					if (!readableOnly || !a.isUnreadable(tokens[i]))
						others.add(tokens[i]);
			}

			char space = a.getSpace();
			return new Text[] { new TextString(StringUtils.join(first, space), a),
					new TextString(StringUtils.join(others, space), a) };
		}
	}

	/**
	 * Uses Chi-Square test to validate assumptions about character distributions.
	 * 
	 * This experiment compares last word of each line with all other words in the
	 * text. In the constructor, an optional flag can be passed to skip first line
	 * of each paragraph. In addition, a flag is used to indicate whether
	 * "unreadable" words should be considered or not.
	 * 
	 * Notice only the text in running paragraphs is considered.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class LastWordInLine extends Experiment {

		private final boolean skipFirst;

		private final boolean readableOnly;

		/**
		 * 
		 * @param skipFirst    If true, skip first line of paragraphs.
		 * @param readableOnly if true, consider only the words that do not contain any
		 *                     unreadable characters.
		 */
		public LastWordInLine(boolean skipFirst, boolean readableOnly) {
			this.skipFirst = skipFirst;
			this.readableOnly = readableOnly;
		}

		@Override
		public Text[] splitDocument(Text txt) {
			Alphabet a = txt.getAlphabet();

			// Consider only paragraphs
			IvtffText paragraphs = ((IvtffText) txt).filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);

			if (skipFirst)
				paragraphs = Experiment.filterLines(paragraphs, true, false);

			List<String> last = new ArrayList<>();
			List<String> others = new ArrayList<>();

			for (IvtffLine l : paragraphs.getLines()) {
				String[] tokens = l.splitWords();
				if (tokens.length == 0)
					continue;
				for (int i = 0; i < tokens.length - 1; ++i)
					if (!readableOnly || !a.isUnreadable(tokens[i]))
						others.add(tokens[i]);
				if (!readableOnly || !a.isUnreadable(tokens[tokens.length - 1]))
					last.add(tokens[tokens.length - 1]);
			}

			char space = a.getSpace();
			return new Text[] { new TextString(StringUtils.join(last, space), a),
					new TextString(StringUtils.join(others, space), a) };
		}
	}

	/**
	 * This Experiment splits words found in a given position in the line from all
	 * other words in the text. In the constructor, an optional flag can be passed
	 * to skip first line of each paragraph; in addition, the position in the line
	 * you are interested in must be provided. Finally, a flag is used to indicate
	 * whether "unreadable" words should be considered or not.
	 * 
	 * Notice only the text in running paragraphs is considered. In addition, the
	 * position of words in line do not change if you decide to ignore the first
	 * word of each line.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class WordInPositionInLine extends Experiment {

		private final boolean skipFirstLine;

		private final boolean skipFirstWordInLine;

		private final boolean readableOnly;

		private final int position;

		/**
		 * 
		 * @param skipFirstLine       If true, skip first line of paragraphs.
		 * @param skipFirstWordInLine If true, skip first word in each line.
		 * @param readableOnly        if true, consider only the words that do not
		 *                            contain any unreadable characters.
		 */
		public WordInPositionInLine(int position, boolean skipFirstLine, boolean skipFirstWordInLine,
				boolean readableOnly) {
			this.position = position;
			this.skipFirstLine = skipFirstLine;
			this.skipFirstWordInLine = skipFirstWordInLine;
			this.readableOnly = readableOnly;
		}

		@Override
		public Text[] splitDocument(Text txt) {
			Alphabet a = txt.getAlphabet();

			// Consider only paragraphs
			IvtffText paragraphs = ((IvtffText) txt).filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);

			if (skipFirstLine)
				paragraphs = Experiment.filterLines(paragraphs, true, false);

			List<String> result = new ArrayList<>();
			List<String> others = new ArrayList<>();

			for (IvtffPage p : paragraphs.getElements()) {
				for (IvtffLine l : p.getElements()) {
					String[] w = l.splitWords();
					for (int i = (skipFirstWordInLine ? 1 : 0); i < w.length; ++i) {
						if (!readableOnly || !a.isUnreadable(w[i])) {
							if (i == position)
								result.add(w[i]);
							else
								others.add(w[i]);
						}
					}

				}
			}

			char space = a.getSpace();
			return new Text[] { new TextString(StringUtils.join(result, space), a),
					new TextString(StringUtils.join(others, space), a) };
		}
	}

	/**
	 * This experiment first split the text using Experiment passed in constructor;
	 * it then it extracts only initial letter from words in each part. A TextString
	 * is returned for each part, with all initials, without spaces.
	 * 
	 * A flag in the constructor is used to indicate whether "unreadable" chars be
	 * considered or not. Pay attention that this is NOT the same thing as ignoring
	 * unreadable words in the nested class.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class Initials extends Experiment {

		private final Experiment experiment;

		private final boolean readableOnly;

		/**
		 * @param readableOnly if true, consider only readable characters.
		 */
		public Initials(Experiment experiment, boolean readableOnly) {
			this.experiment = experiment;
			this.readableOnly = readableOnly;
		}

		@Override
		public Text[] splitDocument(Text doc) {
			Text[] splitted = experiment.splitDocument(doc);
			return new Text[] { getInitials(splitted[0]), getInitials(splitted[1]) };
		}

		public Text getInitials(Text doc) {

			Alphabet a = doc.getAlphabet();
			StringBuilder initials = new StringBuilder();

			for (String w : doc.splitWords()) {
				char c = w.charAt(0);
				if (!readableOnly || !a.isUreadableChar(c))
					initials.append(c);
			}

			return new TextString(initials.toString(), a);
		}
	}

	/**
	 * This experiment first split the text using Experiment passed in constructor;
	 * it then extracts only final letter from words in each part. A TextString is
	 * returned for each part, with all finals, without spaces. Notice only regular
	 * chars are considered.
	 * 
	 * A flag in the constructor is used to indicate whether "unreadable" chars be
	 * considered or not. Pay attention that this is NOT the same thing as ignoring
	 * unreadable words in the nested class.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class Finals extends Experiment {

		private final Experiment experiment;

		private final boolean readableOnly;

		/**
		 * @param readableOnly if true, consider only readable characters.
		 */
		public Finals(Experiment experiment, boolean readableOnly) {
			this.experiment = experiment;
			this.readableOnly = readableOnly;
		}

		@Override
		public Text[] splitDocument(Text doc) {
			Text[] splitted = experiment.splitDocument(doc);
			return new Text[] { getFinals(splitted[0]), getFinals(splitted[1]) };
		}

		public Text getFinals(Text doc) {

			Alphabet a = doc.getAlphabet();
			StringBuilder finals = new StringBuilder();

			for (String w : doc.splitWords()) {
				char c = w.charAt(w.length() - 1);
				if (!readableOnly || !a.isUreadableChar(c))
					finals.append(c);
			}

			return new TextString(finals.toString(), a);
		}
	}

	/**
	 * This experiment first split the text using Experiment passed in constructor;
	 * it then replace the second part (the "population") with "standard" population
	 * (see {@link #getStandardWordsPopulation(IvtffText, boolean)}).
	 * 
	 * In addition, in the constructor, a flag is used to indicate whether
	 * "unreadable" words should be considered part of the standard population or
	 * not.
	 * 
	 * Pay attention to the order in which this is applied. Character-level
	 * experiment (e.g., Initials or Finals) return only letter as part of the
	 * referencve population, those needs to be applied after this one, or the
	 * characters will be replaced by the words in the standard population.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 *
	 */
	public static class WithStandardPopulation extends Experiment {

		private final Experiment experiment;

		private final boolean readableOnly;

		/**
		 * @param readableOnly if true, consider only the words that do not contain any
		 *                     unreadable characters.
		 */
		public WithStandardPopulation(Experiment experiment, boolean readableOnly) {
			this.experiment = experiment;
			this.readableOnly = readableOnly;
		}

		@Override
		public Text[] splitDocument(Text txt) {
			return new Text[] { experiment.splitDocument(txt)[0], new TextString(
					Experiment.getStandardWordsPopulation((IvtffText) txt, readableOnly), txt.getAlphabet()) };
		}

	}

	/**
	 * Split a document in two parts, accordingly to the rules for the Experiment.
	 * These two parts could, for example, be compared to look for interesting
	 * differences.
	 */
	public abstract Text[] splitDocument(Text doc);

	/**
	 * @param readableOnly if true, consider only the words that do not contain any
	 *                     unreadable characters.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first position in a line, Counter[1] is the same for second position
	 *         and so on.
	 */
	public static List<Counter<String>> getWordsByPosition(IvtffText doc, boolean readableOnly) {
		return getWordsByPosition(doc, readableOnly, 0, Integer.MAX_VALUE, false, false);
	}

	/**
	 * @param readableOnly if true, consider only the words that do not contain any
	 *                     unreadable characters.
	 * @param minLineLen   If line has less than this number of words, ignore it.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first position in a line, Counter[1] is the same for second position
	 *         and so on.
	 */
	public static List<Counter<String>> getWordsByPosition(IvtffText doc, boolean readableOnly, int minLineLen) {
		return getWordsByPosition(doc, readableOnly, minLineLen, Integer.MAX_VALUE, false, false);
	}

	/**
	 * @param readableOnly if true, consider only the words that do not contain any
	 *                     unreadable characters.
	 * @param minLineLen   If line has less than this number of words, ignore it.
	 * @param maxLineLen   If line has more than this number of words, ignore it.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first position in a line, Counter[1] is the same for second position
	 *         and so on.
	 */
	public static List<Counter<String>> getWordsByPosition(IvtffText doc, boolean readableOnly, int minLineLen,
			int maxLineLen) {
		return getWordsByPosition(doc, readableOnly, minLineLen, maxLineLen, false, false);
	}

	/**
	 * Notice this considers the entirety of given text, not only the text in
	 * paragraphs.
	 * 
	 * @param readableOnly if true, consider only the words that do not contain any
	 *                     unreadable characters.
	 * @param minLineLen   If line has less than this number of words, ignore it.
	 * @param maxLineLen   If line has more than this number of words, ignore it.
	 * @param skipFirst    If true, ignore first word in each line, therefore
	 *                     Counter[0] will be empty.
	 * @param skipLast     If true, ignore last word in each line.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first position in a line, Counter[1] is the same for second position
	 *         and so on.
	 */
	public static List<Counter<String>> getWordsByPosition(IvtffText doc, boolean readableOnly, int minLineLen,
			int maxLineLen, boolean skipFirst, boolean skipLast) {

		List<Counter<String>> result = new ArrayList<>(50);
		if (skipFirst)
			result.add(new Counter<>()); // add first counter (which will stay empty).
		int min = skipFirst ? 1 : 0;
		Alphabet a = doc.getAlphabet();

		for (IvtffPage p : doc.getElements()) {
			for (IvtffLine l : p.getElements()) {
				String[] w = l.splitWords();
				if ((w.length < minLineLen) || (w.length > maxLineLen))
					continue;
				for (int i = min; i < (skipLast ? w.length - 1 : w.length); ++i) {
					if (result.size() <= i)
						result.add(new Counter<>());
					if (!readableOnly || !a.isUnreadable(w[i])) {
						result.get(i).count(w[i]);
					}
				}
			}
		}

		return result;
	}

	/**
	 * * @param readableOnly if true, consider only the words that do not contain
	 * any unreadable characters.
	 * 
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         last position in a line, Counter[1] is the same for second last
	 *         position and so on.
	 */
	public static List<Counter<String>> getWordsByPositionReversed(IvtffText doc, boolean readableOnly) {
		return getWordsByPositionReversed(doc, readableOnly, 0, Integer.MAX_VALUE, false, false);
	}

	/**
	 * @param readableOnly if true, consider only the words that do not contain any
	 *                     unreadable characters.
	 * @param minLineLen   If line has less than this number of words, ignore it.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         last position in a line, Counter[1] is the same for second last
	 *         position and so on.
	 */
	public static List<Counter<String>> getWordsByPositionReversed(IvtffText doc, boolean readableOnly,
			int minLineLen) {
		return getWordsByPositionReversed(doc, readableOnly, minLineLen, Integer.MAX_VALUE, false, false);
	}

	/**
	 * @param readableOnly if true, consider only the words that do not contain any
	 *                     unreadable characters.
	 * @param minLineLen   If line has less than this number of words, ignore it.
	 * @param maxLineLen   If line has more than this number of words, ignore it.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         last position in a line, Counter[1] is the same for second last
	 *         position and so on.
	 */
	public static List<Counter<String>> getWordsByPositionReversed(IvtffText doc, boolean readableOnly, int minLineLen,
			int maxLineLen) {
		return getWordsByPositionReversed(doc, readableOnly, minLineLen, maxLineLen, false, false);
	}

	/**
	 * @param readableOnly if true, consider only the words that do not contain any
	 *                     unreadable characters.
	 * @param minLineLen   If line has less than this number of words, ignore it.
	 * @param maxLineLen   If line has more than this number of words, ignore it.
	 * @param skipFirst    If true, ignore first word in each line.
	 * @param skipLast     If true, ignore last word in each line, therefore
	 *                     Counter[0] will be empty.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         last position in a line, Counter[1] is the same for second last
	 *         position and so on.
	 */
	public static List<Counter<String>> getWordsByPositionReversed(IvtffText doc, boolean readableOnly, int minLineLen,
			int maxLineLen, boolean skipFirst, boolean skipLast) {
		List<Counter<String>> result = new ArrayList<>(50);
		if (skipLast)
			result.add(new Counter<>()); // add first counter (which will stay empty).
		int min = skipLast ? 1 : 0;
		Alphabet a = doc.getAlphabet();

		for (IvtffPage p : doc.getElements()) {
			for (IvtffLine l : p.getElements()) {
				String[] w = l.splitWords();
				if ((w.length < minLineLen) || (w.length > maxLineLen))
					continue;
				for (int i = min; i < (skipFirst ? w.length - 1 : w.length); ++i) {
					if (result.size() <= i)
						result.add(new Counter<>());
					if (!readableOnly || !a.isUnreadable(w[w.length - 1 - i]))
						result.get(i).count(w[w.length - 1 - i]);
				}
			}
		}

		return result;
	}

	/**
	 * @param readableOnly if true, consider only the words that do not contain any
	 *                     unreadable characters.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first line in a paragraph, Counter[1] is the same for second line and
	 *         so on. Notice only the text in running paragraphs is considered.
	 */
	public static List<Counter<String>> getWordsByLine(IvtffText txt, boolean readableOnly) {
		return getWordsByLine(txt, readableOnly, 0, Integer.MAX_VALUE, false, false);
	}

	/**
	 * @param readableOnly if true, consider only the words that do not contain any
	 *                     unreadable characters.
	 * @param minLineLen   If line has less than this number of words, ignore it.
	 * @param maxLineLen   If line has more than this number of words, ignore it.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first line in a paragraph, Counter[1] is the same for second line and
	 *         so on. Notice only the text in running paragraphs is considered.
	 */
	public static List<Counter<String>> getWordsByLine(IvtffText txt, boolean readableOnly, int minLineLen,
			int maxLineLen) {
		return getWordsByLine(txt, readableOnly, minLineLen, maxLineLen, false, false);
	}

	/**
	 * @param readableOnly if true, consider only the words that do not contain any
	 *                     unreadable characters.
	 * @param minLineLen   If line has less than this number of words, ignore it.
	 * @param maxLineLen   If line has more than this number of words, ignore it.
	 * @param skipFirst    If true, ignore first line in each paragraph, therefore
	 *                     Counter[0] will be empty.
	 * @param skipLast     If true, ignore last line in each paragraph.
	 * @return A list of Counter, where Counter[0] counts words appearing in doc at
	 *         first line in a paragraph, Counter[1] is the same for second line and
	 *         so on. Notice only the text in running paragraphs is considered.
	 */
	public static List<Counter<String>> getWordsByLine(IvtffText txt, boolean readableOnly, int minLineLen,
			int maxLineLen, boolean skipFirst, boolean skipLast) {

		IvtffText paragraphs = ((IvtffText) txt).filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);
		Alphabet a = txt.getAlphabet();

		List<Counter<String>> result = new ArrayList<>();

		int pos = 0;
		for (IvtffPage p : paragraphs.getElements()) {
			for (IvtffLine l : p.getElements()) {

				// Always ensure results addresses all rows
				if (pos >= result.size())
					result.add(new Counter<String>());

				if ((pos > 0) || !skipFirst) { // skip first line eventually
					if (!l.isLast() || !skipLast) { // skip last line eventually

						Counter<String> c = result.get(pos);

						String[] w = l.splitWords();
						if ((w.length >= minLineLen) && (w.length <= maxLineLen)) {
							for (String s : w) {
								if (!readableOnly || !a.isUnreadable(s))
									c.count(s);
							}
						}
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
	 * @return Input document, after filtering. Notice only text in running
	 *         paragraphs is returned.
	 */
	public static IvtffText filterLines(IvtffText doc, boolean discardFirstLine, boolean discardLastLine) {
		// note the order is important because how Experiments work
		if (discardFirstLine)
			doc = (IvtffText) new FirstLineInParagraph(false).splitDocument(doc)[1];
		if (discardLastLine)
			doc = (IvtffText) new LastLineInParagraph().splitDocument(doc)[1];
		return doc;
	}

	/**
	 * Returns "standard" population of words in a text. We know from analysis that
	 * words in first line of paragraphs and those at the beginning or end of a line
	 * have particular statistical behaviors (e.g., in length, char distribution,
	 * etc.). This method returns all other words in the text. Notice only the text
	 * in running paragraphs is considered.
	 * 
	 * @param readableOnly if true, consider only the words that do not contain any
	 *                     unreadable characters.
	 * @return "Standard" word population for given text.
	 */
	public static Counter<String> getStandardWordsPopulation(IvtffText txt, boolean readableOnly) {

		List<Counter<String>> other = Experiment.getWordsByPosition(Experiment.filterLines(txt, true, true),
				readableOnly, 0, Integer.MAX_VALUE, true, true);
		Counter<String> population = new Counter<>();
		for (int i = 1; i < other.size(); ++i) {
			population.countAll(other.get(i));
		}

		return population;
	}

	private static final BinomialTest BINOMIAL = new BinomialTest();

	/**
	 * @param sample
	 * @param population
	 * @return A list of words that appear more frequently in sample (alpha=1%) than
	 *         in the population. Only readable words are counted.
	 */
	public static Counter<String> getInterestingWords(Text sample, Text population) {
		return getInterestingWords(sample.getWords(true), population.getWords(true), 0.01);
	}

	/**
	 * @param sample
	 * @param population
	 * @return A list of words that appear more frequently in sample than in the
	 *         population. Only readable words are counted.
	 */
	public static Counter<String> getInterestingWords(Text sample, Text population, double alpha) {
		return getInterestingWords(sample.getWords(true), population.getWords(true), alpha);
	}

	/**
	 * @param sample
	 * @param population
	 * @return A list of words that appear more frequently in sample (alpha=1%) than
	 *         in the population.
	 */
	public static Counter<String> getInterestingWords(Counter<String> sample, Counter<String> population) {
		return getInterestingWords(sample, population, 0.01);
	}

	/**
	 * @param sample
	 * @param population
	 * @param alpha
	 * @return A list of words that appear more frequently in sample (given alpha)
	 *         than in the population.
	 */
	public static Counter<String> getInterestingWords(Counter<String> sample, Counter<String> population,
			double alpha) {
		Counter<String> result = new Counter<>();

		for (String w : sample.itemSet()) {
			if (isInterestingWord(w, sample, population, alpha))
				result.count(w, sample.getCount(w));
		}

		return result;
	}

	/**
	 * @param w          Word to test.
	 * @param sample
	 * @param population
	 * @param alpha
	 * @return True if given word appear more frequently in sample (alpha=1%) than
	 *         in the population.
	 */
	public static boolean isInterestingWord(String w, Counter<String> sample, Counter<String> population) {
		return isInterestingWord(w, sample, population, 0.01);
	}

	/**
	 * @param w          Word to test
	 * @param observed   Observed frequency of word in sample.
	 * @param population
	 * @param alpha
	 * @return True if given words appears more frequently in sample (given alpha)
	 *         than in the population.
	 */
	public static boolean isInterestingWord(String w, Counter<String> sample, Counter<String> population,
			double alpha) {
		double observed = (double) sample.getCount(w) / sample.getTotalCounted();
		double expected = (double) population.getCount(w) / population.getTotalCounted();

		if (observed <= expected)
			return false;
		double sig = BINOMIAL.binomialTest(sample.getTotalCounted(), sample.getCount(w), expected,
				AlternativeHypothesis.GREATER_THAN);
		return (sig < alpha);
	}

	/**
	 * @param wordsByPosition A List of Counters, counting words appearing in
	 *                        specific position (e.g. at a given position in a
	 *                        line).
	 * @return A double[] of same size than wordsByPosition with % of "interesting"
	 *         words in each Counter. "Interesting" words are those appearing in one
	 *         Counter with a higher frequency than in the sum of other Counters
	 *         (alpha=1%).
	 */
	public static double[] getInterestingWordsPercent(List<Counter<String>> wordsByPosition) {
		double[] result = new double[wordsByPosition.size()];
		for (int i = 0; i < wordsByPosition.size(); ++i) {

			// Words in position i vs. all other words
			Counter<String> sample = wordsByPosition.get(i);
			Counter<String> population = new Counter<>();
			for (int j = 0; j < wordsByPosition.size(); ++j) {
				if (i == j)
					continue;
				population.countAll(wordsByPosition.get(j));
			}

			Counter<String> interesting = getInterestingWords(sample, population);
			result[i] = (double) interesting.getTotalCounted() / sample.getTotalCounted();
		}

		return result;
	}

	/**
	 * @return A Map from each cluster (as defined in Note 003) into corresponding
	 *         text. Pages not belonging to any cluster (cluster="?") are not
	 *         returned.
	 */
	public static Map<String, IvtffText> splitClusters(IvtffText txt) {
		Map<String, IvtffText> clusterParagraphs = txt.splitPages(new ElementSplitter<IvtffPage>() {

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
	public static Map<String, IvtffText> shuffleClusters(Map<String, IvtffText> txts) {
		return shuffleClusters(txts, new Random(System.currentTimeMillis()));
	}

	/**
	 * @param A Map containing a set of Voynich texts.
	 * 
	 * @return The input map, where each Voynich text is randomly scrambled. The
	 *         number of pages, lines, and words in the lines is kept, but words are
	 *         randomly scrambled around.
	 */
	public static Map<String, IvtffText> shuffleClusters(Map<String, IvtffText> txts, Random rnd) {
		Map<String, IvtffText> result = new HashMap<>();
		for (Entry<String, IvtffText> e : txts.entrySet()) {
			result.put(e.getKey(), e.getValue().shuffledText(rnd));
		}
		return result;
	}
}
