/**
 * 
 */
package io.github.mzattera.v4j.applications.chars;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffLine;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.LineFilter;
import io.github.mzattera.v4j.text.txt.TextString;

/**
 * This is a container for subclasses of CharDistributionExperiment that
 * implement different experiments.
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
		protected Text[] splitDocument(Text txt) {
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
		protected Text[] splitDocument(Text txt) {
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
		protected Text[] splitDocument(Text txt) {
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
		protected Text[] splitDocument(Text txt) {
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
		protected Text[] splitDocument(Text txt) {
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
		protected Text[] splitDocument(Text txt) {
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
}
