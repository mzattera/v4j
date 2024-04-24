/**
 * 
 */
package io.github.mzattera.v4j.applications.chars;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.stat.inference.AlternativeHypothesis;
import org.apache.commons.math3.stat.inference.BinomialTest;

import io.github.mzattera.v4j.experiment.ChiSquared;
import io.github.mzattera.v4j.experiment.ChiSquared.CharBin;
import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageFilter;
import io.github.mzattera.v4j.text.ivtff.PageHeader;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.text.txt.TextString;

/**
 * This class performs several experiments. Each experiment divides a text in
 * two parts, accordingly some rules, then it checks whether the distribution of
 * characters is significantly different across the two parts using Chi-Square
 * test. Each subclass implements a different way of splitting the text (see
 * Note 010 (https://mzattera.github.io/v4j/010/)).
 * 
 * This class <code>main()</code> method executes a set of tests defined in the
 * <code>Experiments</code> class and generates a table highlighting
 * statistically significative deviations.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public abstract class CharDistributionAnalysis {

	/**
	 * Which transcription to use.
	 */
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

	/**
	 * Which transcription type to use.
	 */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.CONCORDANCE;

	/**
	 * Which Alphabet type to use.
	 */
	public static final Alphabet ALPHABET = Alphabet.SLOT;

	/**
	 * Confidence level to use.
	 */
	private static final double ALPHA = 0.01d;

	/**
	 * Confidence level (second level, for display purposes).
	 */
	private static final double ALPHA2 = 0.05d;

	/** Compact output */
	private static final boolean COMPACT = false;

	protected CharDistributionAnalysis() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Prints configuration parameters
			System.out.println("Transcription     : " + TRANSCRIPTION);
			System.out.println("Transcription Type: " + TRANSCRIPTION_TYPE);
			System.out.println("Alphabet          : " + ALPHABET);
			System.out.println("Alpha             : " + ALPHA);
			System.out.println("Alpha 2           : " + ALPHA2);
			System.out.println();

			IvtffText voynich = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, ALPHABET);

			System.out.print("[ First line in page      ];\n");
			process(voynich, new Experiment.FirstLineInPage());
			System.out.print("\n\n[ First line in paragraph ];\n");
			process(voynich, new Experiment.FirstLineInParagraph(false));
			System.out.print("\n\n[ Last line in paragraph  ];\n");
			process(voynich, new Experiment.LastLineInParagraph());
			System.out.print("\n\n[ First letter in a line  ];\n");
			process(voynich, new Experiment.Initials(new Experiment.FirstWordInLine(false, false), true));
			System.out.print("\n\n[ Last letter in a line   ];\n");
			process(voynich, new Experiment.Finals(new Experiment.LastWordInLine(false, false), true));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	/**
	 * Execute this experiment using given text. The experiment runs separately for
	 * each cluster.
	 */
	private static void process(IvtffText doc, Experiment experiment) {

		char[] chars;
		if (doc.getAlphabet().getCodeString().equals(Alphabet.SLOT.getCodeString())) {
			// Nicer display for Slot alphabet ;)
			chars = new char[] { 'q', 's', 'd', 'l', 'r', 'C', 'S', 'f', 'k', 'p', 't', 'F', 'K', 'P', 'T', 'e', 'E',
					'B', 'o', 'a', 'i', 'J', 'U', 'n', 'm', 'y' };
		} else {
			chars = doc.getAlphabet().getRegularChars();
		}

		// Header with chars
		System.out.print("Cluster;Significance [alpha];");
		for (int i = 0; i < chars.length; ++i)
			System.out.print(chars[i] + ";");
		System.out.println();

		// Do analysis for each cluster
		for (String cluster : PageHeader.CLUSTERS) {

// TEST			IvtffText clusterText = doc.filterPages(new PageFilter.Builder().cluster(cluster).build()).shuffledText();
			IvtffText clusterText = doc.filterPages(new PageFilter.Builder().cluster(cluster).build());
			process(cluster, clusterText, COMPACT, experiment);

			System.out.println();
		} // For each cluster
	}

	private static final BinomialTest BINOMIAL = new BinomialTest();

	/**
	 * Processes a single text (typically representing a cluster).
	 * 
	 * @param cluster    Cluster name (optional)
	 * @param txt        Text to process.
	 * @param compact    If true, print a compact version of the distribution table.
	 * @param experiment
	 * @return Two lists, with characters appearing more and less than they should,
	 *         based on chi-squared text and ALPHA.
	 */
	public static List<Character>[] process(String cluster, IvtffText txt, boolean compact, Experiment experiment) {

		@SuppressWarnings("unchecked")
		List<Character>[] result = new ArrayList[2];
		result[0] = new ArrayList<Character>();
		result[1] = new ArrayList<Character>();

		cluster = (cluster == null) ? "" : cluster;
		Alphabet a = txt.getAlphabet();
		char[] chars;
		if (a.getCodeString().equals(Alphabet.SLOT.getCodeString())) {
			// Nicer display for Slot alphabet ;)
			chars = new char[] { 'q', 's', 'd', 'l', 'r', 'C', 'S', 'f', 'k', 'p', 't', 'F', 'K', 'P', 'T', 'e', 'E',
					'B', 'o', 'a', 'i', 'J', 'U', 'n', 'm', 'y' };
		} else {
			chars = a.getRegularChars();
		}

		/**
		 * Creates an "adjusted" distribution for the whole text, where each bin is big
		 * enough for chi-square (merges smaller bins together)
		 **/
		Text[] parts = experiment.splitDocument(txt);
		/**
		 * We look into char distribution of the two parts together, since
		 * splitDocument() might not return the whole text. This is the only way to
		 * ensure charDistribution reflects the part of text we are looking into.
		 **/
		Text allText = new TextString(parts[0].getPlainText() + a.getSpace() + parts[1].getPlainText(), a);
		List<CharBin> charDistribution = ChiSquared.getCharDistribution(allText, false);
		List<CharBin> adjustedDistribution = ChiSquared.adjustDistribution(charDistribution,
				Math.min(parts[0].getChars().getTotalCounted(), parts[1].getChars().getTotalCounted()));

		// Compare the two parts, considering the whole alphabet at once, to see if
		// there is a significative difference
		System.out.print(cluster + ";");
		double confidence = ChiSquared.chiSquareTestDataSetsComparison(parts[0], parts[1], adjustedDistribution, false);
		System.out.printf("%.2f%%;", confidence * 100);
		if (confidence > ALPHA) { // Not a difference in the two part that is statistically significant; exit
			return result;
		}

		// Do analysis for each char individually
		for (int i = 0; i < chars.length; ++i) {

			// Find distribution for current char (note we do not use the adjusted one, to
			// have all chars, not those found). Notice each CharBin contains only one char.
			CharBin cd = null;
			for (CharBin d : charDistribution) {
				if (d.getChars().get(0) == chars[i]) {
					cd = d;
					break;
				}
			}

			if (cd == null) {
				// getCharDistribution() did not find this character.
				System.out.print("?;");
				continue;
			}

			// How many times do we expect the character to appear in any of the two parts,
			// as a minimum?
//			double expectedCount = cd.getFrequency()
//					* Math.min(parts[0].getChars().getTotalCounted(), parts[1].getChars().getTotalCounted());
			
			// Observe actual frequencies of the char (2 categories: the char and all
			// others)
			long[] obs1 = ChiSquared.observe(parts[0], chars[i], false);
			long[] obs2 = ChiSquared.observe(parts[1], chars[i], false);
			// Does the character appear more or less than expected?
			double freq1 = ((double) obs1[0] / (obs1[0] + obs1[1]));
			double freq2 = ((double) obs2[0] / (obs2[0] + obs2[1]));
			boolean more = freq1 > freq2;

//			if (expectedCount < 5.0d) {// expected count for char is too low to use Chi Squared
//				System.out.print("?");
//			} else {
//				try {
//			confidence = ChiSquared.chiSquareTestDataSetsComparison(obs1, obs2);
			confidence = BINOMIAL.binomialTest((int) (obs1[0] + obs1[1]), (int) (obs1[0]), freq2,
					(more ? AlternativeHypothesis.GREATER_THAN : AlternativeHypothesis.LESS_THAN));

			if (confidence <= ALPHA) { // Difference is significant
				if (more) {
					result[0].add(chars[i]);
					System.out.print("++");
				} else {
					result[1].add(chars[i]);
					System.out.print("--");
				}
			} else if (confidence < ALPHA2) { // We still display it if confidence is better than alha2
				if (more)
					System.out.print("+");
				else
					System.out.print("-");
			} else { // Not significative, still print tendency
				if (more)
					System.out.print("^");
				else
					System.out.print("v");
			}
//				} catch (Exception e) {
//					// Numbers are too small for chi-square
//				}
//			} // expected count high enough to use chi-squared

			if (!compact) {
				System.out.print(" (" + obs1[0] + " / " + obs1[1] + ") ");
				System.out.printf("(%.2f%% / %.2f%%)", (freq1 * 100.0), (freq2 * 100.0));
			}
			System.out.print(";");

		} // For each char

		return result;
	}
}
