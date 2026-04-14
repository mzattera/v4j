/**
 * 
 */
package io.github.mzattera.v4j.applications.words;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;

/**
 * Shows % of interesting words across the text. Interesting words are those
 * that appear more frequently (in a statistically significant way) in some part
 * of the text (e.g. at beginning of a word).
 * 
 * See {@link PrintInterestingWords}.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class InterestingWords {

	/**
	 * Which transcription to use.
	 */
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

	/**
	 * Which transcription type to use.
	 */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.MAJORITY;

	/**
	 * Which Alphabet type to use.
	 */
	public static final Alphabet ALPHABET = Alphabet.SLOT;

	/**
	 * Lines with less words than this will be ignored.
	 */
	public static final int MIN_LINE_LEN = 0;

	/**
	 * Lines with more words than this will be ignored.
	 */
	public static final int MAX_LINE_LEN = Integer.MAX_VALUE;

	// If true, consider only readable words
	private final static boolean READABLE_ONLY = true;

	public static final Random RND = new Random(6021970);

	private InterestingWords() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Prints configuration parameters
			System.out.println("Transcription       : " + TRANSCRIPTION);
			System.out.println("Transcription Type  : " + TRANSCRIPTION_TYPE);
			System.out.println("Alphabet            : " + ALPHABET);
			System.out.println("Min. words in a line: " + MIN_LINE_LEN);
			System.out.println("Max. words in a line: " + MAX_LINE_LEN);
			System.out.println();

			// Get only running text
			IvtffText voy = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, ALPHABET);

			Map<String, IvtffText> voyClusters = Experiment.splitClusters(voy);
			Map<String, IvtffText> rndClusters = Experiment.shuffleClusters(voyClusters, RND);

			System.out.println(
					"% of interesting words by position along the line (first line of each paragraph removed)");
			System.out.println();
			System.out.println("Normal text from the beginning (first line of each paragraph removed)");
			System.out.println();
			examineInterestingWordsByPositionInLine(voyClusters, true, false);
			System.out.println();
			System.out.println("Normal text from the end (first line of each paragraph removed)");
			System.out.println();
			examineInterestingWordsByPositionInLine(voyClusters, true, true);
			System.out.println();
			System.out.println("% of interesting words by position along the line (all lines)");
			System.out.println();
			System.out.println("Normal text from the beginning (all lines)");
			System.out.println();
			examineInterestingWordsByPositionInLine(voyClusters, false, false);
			System.out.println();
			System.out.println("Normal text from the end (all lines)");
			System.out.println();
			examineInterestingWordsByPositionInLine(voyClusters, false, true);
			System.out.println();
			System.out.println("Random text from the beginning - control");
			System.out.println();
			examineInterestingWordsByPositionInLine(rndClusters, false, false);
			System.out.println();
			System.out.println("Random text from the end - control");
			System.out.println();
			examineInterestingWordsByPositionInLine(rndClusters, false, true);
			System.out.println();

			System.out.println("% of interesting words  by position in paragraph.");
			System.out.println();
			System.out.println("Normal text");
			System.out.println();
			examineInterestingWordsByLineInParagraph(voyClusters);
			System.out.println();
			System.out.println("Random text - control");
			System.out.println();
			examineInterestingWordsByLineInParagraph(rndClusters);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	/**
	 * Prints a table with % of interesting words, by their position along the line.
	 * Consider only running text.
	 * 
	 * @param clusterParagraphs Maps each cluster into its corresponding text to
	 *                          examine.
	 * @param skipFirst         If true, skip the first line of each paragraph.
	 * @param reverse           If true, does the calculation from the end of the
	 *                          line, instead than from the beginning.
	 */
	private static void examineInterestingWordsByPositionInLine(Map<String, IvtffText> clusterParagraphs,
			boolean skipFirst, boolean reverse) {

		// Calculate average length in all positions for each cluster
		int maxLen = 0;
		Map<String, double[]> result = new HashMap<>();
		for (String cluster : clusterParagraphs.keySet()) {
			IvtffText txt = Experiment.filterLines(clusterParagraphs.get(cluster), skipFirst, false);
			List<Counter<String>> wordsByPosition;
			if (reverse)
				wordsByPosition = Experiment.getWordsByPositionReversed(txt, READABLE_ONLY, MIN_LINE_LEN, MAX_LINE_LEN);
			else
				wordsByPosition = Experiment.getWordsByPosition(txt, READABLE_ONLY, MIN_LINE_LEN, MAX_LINE_LEN);
			double[] perc = Experiment.getInterestingWordsPercent(wordsByPosition);
			if (perc.length > maxLen)
				maxLen = perc.length;
			result.put(cluster, perc);
		}

		// Print results
		System.out.print("Cluster");
		for (int i = 0; i < maxLen; ++i) {
			if (reverse)
				System.out.print(";%Interesting(fromEnd(" + i + "))");
			else
				System.out.print(";%Interesting(fromBegin(" + i + "))");
		}
		System.out.println();
		for (Entry<String, double[]> e : result.entrySet()) {
			System.out.print(e.getKey() + ";");
			double[] l = e.getValue();
			for (int i = 0; i < maxLen; i++) {
				if (i < l.length)
					System.out.print(l[i] + ";");
				else
					System.out.print(";");
			}
			System.out.println();
		}
	}

	/**
	 * Prints a table with % of interesting words, by their line inside a paragraph.
	 * Consider only running text.
	 * 
	 * @param clusterParagraphs Maps each cluster into its corresponding text to
	 *                          examine.
	 */
	private static void examineInterestingWordsByLineInParagraph(Map<String, IvtffText> clusterParagraphs) {

		// Calculate average length in all positions for each cluster
		int maxLen = 0;
		Map<String, double[]> result = new HashMap<>();
		for (String cluster : clusterParagraphs.keySet()) {
			IvtffText txt = Experiment.filterLines(clusterParagraphs.get(cluster), false, false);
			List<Counter<String>> wordsByPosition = Experiment.getWordsByLine(txt, READABLE_ONLY, MIN_LINE_LEN,
					MAX_LINE_LEN);
			double[] perc = Experiment.getInterestingWordsPercent(wordsByPosition);
			if (perc.length > maxLen)
				maxLen = perc.length;
			result.put(cluster, perc);
		}

		// Print results
		System.out.print("Cluster");
		for (int i = 0; i < maxLen; ++i) {
			System.out.print(";%Interesting(line " + i + ")");
		}
		System.out.println();
		for (Entry<String, double[]> e : result.entrySet()) {
			System.out.print(e.getKey() + ";");
			double[] l = e.getValue();
			for (int i = 0; i < maxLen; i++) {
				if (i < l.length)
					System.out.print(l[i] + ";");
				else
					System.out.print(";");
			}
			System.out.println();
		}
	}
}
