/**
 * 
 */
package io.github.mzattera.v4j.applications.words;

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
 * Prints interesting words. Interesting words are those that appear more
 * frequently (in a statistically significant way) in some part of the text
 * (e.g. at beginning of a word).
 * 
 * See {@link InterestingWords}.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class PrintInterestingWords {

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

	private PrintInterestingWords() {
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

			System.out.println("Interesting words in first word of paragraphs");
			System.out.println();
			printInterestingWordsByPositionInLine(voyClusters, false, false);
			System.out.println();
			System.out.println("Interesting words in last word of paragraphs");
			System.out.println();
			printInterestingWordsByPositionInLine(voyClusters, false, true);
			System.out.println();

			System.out.println("Interesting words in first line of paragraphs");
			System.out.println();
			printInterestingWordsByLineInParagraph(voyClusters);
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	/**
	 * Prints interesting words, by their position along the line. Consider only
	 * running text.
	 * 
	 * @param clusterParagraphs Maps each cluster into its corresponding text to
	 *                          examine.
	 * @param skipFirst         If true, skip the first line of each paragraph.
	 * @param reverse           If true, does the calculation from the end of the
	 *                          line, instead than from the beginning.
	 */
	private static void printInterestingWordsByPositionInLine(Map<String, IvtffText> clusterParagraphs,
			boolean skipFirst, boolean reverse) {

		for (String cluster : clusterParagraphs.keySet()) {

			System.out.println("\nCluster: " + cluster + "\n");

			IvtffText txt = Experiment.filterLines(clusterParagraphs.get(cluster), skipFirst, false);
			List<Counter<String>> wordsByPosition;
			if (reverse)
				wordsByPosition = Experiment.getWordsByPositionReversed(txt, READABLE_ONLY, MIN_LINE_LEN, MAX_LINE_LEN);
			else
				wordsByPosition = Experiment.getWordsByPosition(txt, READABLE_ONLY, MIN_LINE_LEN, MAX_LINE_LEN);

			int i = 0;
			List<Counter<String>> interesting = Experiment.getInterestingWords(wordsByPosition);
			for (Entry<String, Integer> e : interesting.get(0).reversed()) {
				System.out.println(e.getKey() + "\t" + e.getValue());
				if (++i == 5)
					break;
			}
		}
	}

	/**
	 * Prints a table with % of interesting words, by their line inside a paragraph.
	 * Consider only running text.
	 * 
	 * @param clusterParagraphs Maps each cluster into its corresponding text to
	 *                          examine.
	 */
	private static void printInterestingWordsByLineInParagraph(Map<String, IvtffText> clusterParagraphs) {

		for (String cluster : clusterParagraphs.keySet()) {

			System.out.println("\nCluster: " + cluster + "\n");

			IvtffText txt = Experiment.filterLines(clusterParagraphs.get(cluster), false, false);
			List<Counter<String>> wordsByPosition = Experiment.getWordsByLine(txt, READABLE_ONLY, MIN_LINE_LEN,
					MAX_LINE_LEN);

			int i = 0;
			List<Counter<String>> interesting = Experiment.getInterestingWords(wordsByPosition);
			for (Entry<String, Integer> e : interesting.get(0).reversed()) {
				System.out.println(e.getKey() + "\t" + e.getValue());
				if (++i == 5)
					break;
			}
		}
	}
}
