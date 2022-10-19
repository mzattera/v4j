/**
 * 
 */
package io.github.mzattera.v4j.applications.chars;

import java.util.ArrayList;
import java.util.List;

import io.github.mzattera.v4j.applications.chars.ChiSquared.CharDistribution;
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
 * This is a super class for a set of experiments that use Chi-Square test to
 * validate assumptions about character distributions.
 * 
 * Each experiment divides a text in two parts, accordingly some rules, then it
 * checks whether the distribution of characters is significantly different
 * across the two parts using Chi-Square test. Each subclass implements a
 * different way of splitting the text (see Note 010
 * (https://mzattera.github.io/v4j/010/)).
 * 
 * This class <code>main()</code> method executes a set of tests defined in the
 * <code>Experiments</code> class and generates a table highlighting
 * statistically significative deviations.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public abstract class TwoSamplesCharDistributionTest {

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
	private static final boolean COMPACT = true;

	protected TwoSamplesCharDistributionTest() {
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
			new Experiments.FirstLineInPage().process(voynich);
			System.out.print("\n\n[ First line in paragraph ];\n");
			new Experiments.FirstLineInParagraph().process(voynich);
			System.out.print("\n\n[ Last line in paragraph  ];\n");
			new Experiments.LastLineInParagraph().process(voynich);
			System.out.print("\n\n[ First letter in a line  ];\n");
			new Experiments.Initials(new Experiments.FirstWordInLine(false, false), true).process(voynich);
			System.out.print("\n\n[ Last letter in a line   ];\n");
			new Experiments.Finals(new Experiments.LastWordInLine(false, false), true).process(voynich);

//			new Experiments.FirstInParagraphVsStandard().executeExperiments(voynich);
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
	public void process(IvtffText doc) {

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
			processCluster(cluster, clusterText, COMPACT);

			System.out.println();
		} // For each cluster
	}

	/**
	 * Processes a single text (typically representing a cluster).
	 * 
	 * @param cluster     Cluster name (optional)
	 * @param clusterText Text to process.
	 * @param compact     If true, print a compact version of the distribution
	 *                    table.
	 * @return Two list, with characters appearing more and less than they should,
	 *         based on chi-squared text and ALPHA.
	 */
	public List<Character>[] processCluster(String cluster, IvtffText clusterText, boolean compact) {

		@SuppressWarnings("unchecked")
		List<Character>[] result = new ArrayList[2];
		result[0] = new ArrayList<Character>();
		result[1] = new ArrayList<Character>();

		cluster = (cluster == null) ? "" : cluster;
		Alphabet a = clusterText.getAlphabet();
		char[] chars;
		if (a.getCodeString().equals(Alphabet.SLOT.getCodeString())) {
			// Nicer display for Slot alphabet ;)
			chars = new char[] { 'q', 's', 'd', 'l', 'r', 'C', 'S', 'f', 'k', 'p', 't', 'F', 'K', 'P', 'T', 'e', 'E',
					'B', 'o', 'a', 'i', 'J', 'U', 'n', 'm', 'y' };
		} else {
			chars = a.getRegularChars();
		}

		// Creates an "adjusted" distribution for the whole text, where each bin is big
		// enough for chi-square
		// (merges smaller bins together)
		Text[] parts = splitDocument(clusterText);
		// We look into char distribution of the two parts together, since
		// splitDocument()
		// might not return the whole clusterText. This is the only way to ensure
		// charDistribution
		// reflects the part of text we are looking into.
		Text allText = new TextString(parts[0].getPlainText() + a.getSpace() + parts[1].getPlainText(), a);
		List<CharDistribution> charDistribution = ChiSquared.getCharDistribution(allText, false);
		List<CharDistribution> adjustedDistribution = ChiSquared.adjustDistribution(charDistribution,
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

			// Find distribution for current char (note we do not use the adjusted one)
			CharDistribution cd = null;
			for (CharDistribution d : charDistribution) {
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
			double expectedCount = cd.getFrequency()
					* Math.min(parts[0].getChars().getTotalCounted(), parts[1].getChars().getTotalCounted());
			// Observe actual frequencies of the char (2 categories: the char and all
			// others)
			long[] obs1 = ChiSquared.observe(parts[0], chars[i], false);
			long[] obs2 = ChiSquared.observe(parts[1], chars[i], false);
			// Does the character appear more or less than expected?
			double p1 = ((double) obs1[0] / (obs1[0] + obs1[1]));
			double p2 = ((double) obs2[0] / (obs2[0] + obs2[1]));
			boolean more = p1 > p2;

			if (expectedCount < 5.0d) {// expected count for char is too low to use Chi Squared
				System.out.print("?");
			} else {
				try {
					confidence = ChiSquared.chiSquareTestDataSetsComparison(obs1, obs2);
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
				} catch (Exception e) {
					// Numbers are too small for chi-square
					System.out.print("?");
				}
			} // expected count high enough to use chi-squared

			if (!compact) {
				System.out.print(" (" + obs1[0] + " / " + obs1[1] + ") ");
				System.out.printf("(%.2f%% / %.2f%%)", (p1 * 100.0), (p2 * 100.0));
			}
			System.out.print(";");

		} // For each char

		return result;
	}

	/**
	 * Split a document in two parts; these are the parts compared accordingly to
	 * the rules for the experiment.
	 */
	public abstract Text[] splitDocument(Text doc);
}
