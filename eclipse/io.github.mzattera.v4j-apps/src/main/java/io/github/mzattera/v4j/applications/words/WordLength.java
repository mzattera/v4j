/**
 * 
 */
package io.github.mzattera.v4j.applications.words;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.LineFilter;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;

/**
 * Shows length of words by their position in a line, both from the beginning
 * and the end, and by line in paragraph.
 * 
 * See VOGT, Elmar (2012): The Line as a Functional Unit in the Voynich
 * Manuscript: Some Statistical Observations
 * 
 * https://voynichthoughts.files.wordpress.com/2012/11/the_voynich_line.pdf
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class WordLength {

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

	public static final Random RND = new Random(6021970);

	/**
	 * Skip first line of paragraphs when doing the analysis?
	 */
	private static final boolean SKIP_FIRST = false;

	private WordLength() {
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
			IvtffText voy = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, ALPHABET)
					.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);

			Map<String, IvtffText> voyClusters = Experiment.splitClusters(voy);
			Map<String, IvtffText> rndClusters = Experiment.shuffleClusters(voyClusters, RND);

			System.out.println("Length by position along the line"
					+ (SKIP_FIRST ? " (first line of each paragraph removed)." : "."));
			System.out.println();
			System.out.println("Voynich - from the beginning");
			System.out.println();
			examineLengthByPositionInLine(voyClusters, SKIP_FIRST, false);
			System.out.println();
			System.out.println("Voynich - from the end");
			System.out.println();
			examineLengthByPositionInLine(voyClusters, SKIP_FIRST, true);
			System.out.println();
			System.out.println("Randomly shuffled text (control) - from the beginning");
			System.out.println();
			examineLengthByPositionInLine(rndClusters, SKIP_FIRST, false);
			System.out.println();
			System.out.println("Randomly shuffled text (control) - from the end");
			System.out.println();
			examineLengthByPositionInLine(rndClusters, SKIP_FIRST, true);
			System.out.println();

			System.out.println("Length by position in paragraph.");
			System.out.println();
			System.out.println("Voynich - from the beginning");
			System.out.println();
			examineLengthByLineInParagraph(voyClusters, false);
			System.out.println();
			System.out.println("Voynich - from the end");
			System.out.println();
			examineLengthByLineInParagraph(voyClusters, true);
			System.out.println();
			System.out.println("Randomly shuffled text (control) - from the beginning");
			System.out.println();
			examineLengthByLineInParagraph(rndClusters, false);
			System.out.println();
			System.out.println("Randomly shuffled text (control) - from the end");
			System.out.println();
			examineLengthByLineInParagraph(rndClusters, true);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	/**
	 * Prints a table with average length of words, by their position in line. Looks
	 * only running paragraph text.
	 * 
	 * @param clusterParagraphs Maps each cluster into its corresponding text to
	 *                          examine.
	 * @param skipFirst         If true, skip the first line of each paragraph.
	 * @param reverse           If true, does the calculation from the end of the
	 *                          line, instead than from the beginning.
	 */
	private static void examineLengthByPositionInLine(Map<String, IvtffText> clusterParagraphs, boolean skipFirst,
			boolean reverse) {

		// Calculate average length in all positions for each cluster
		int maxLen = 0;
		Map<String, double[]> len = new HashMap<>();
		for (String cluster : clusterParagraphs.keySet()) {
			IvtffText txt = Experiment.filterLines(clusterParagraphs.get(cluster), skipFirst, false);
			List<Counter<String>> wordsByPosition;
			if (reverse)
				wordsByPosition = Experiment.getWordsByPositionReversed(txt, true, MIN_LINE_LEN, MAX_LINE_LEN);
			else
				wordsByPosition = Experiment.getWordsByPosition(txt, true, MIN_LINE_LEN, MAX_LINE_LEN);
			double[] avgLen = getAverageLength(wordsByPosition);
			if (avgLen.length > maxLen)
				maxLen = avgLen.length;
			len.put(cluster, avgLen);
		}

		// Print results
		System.out.print("Cluster");
		for (int i = 0; i < maxLen; ++i) {
			if (reverse)
				System.out.print(";Length(fromEnd(" + i + "))");
			else
				System.out.print(";Length(fromStart(" + i + "))");
		}
		System.out.println();
		for (Entry<String, double[]> e : len.entrySet()) {
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
	 * Examine the average length of words, based on the position of their line in
	 * the paragraph. The analysis is done cluster by cluster and considers only the
	 * running text in paragraphs.
	 * 
	 * @param clusterParagraphs Maps each cluster into its corresponding text to
	 *                          examine.
	 * @param reverse           If true, does the calculation from the end of the
	 *                          line, instead than from the beginning.
	 */
	private static void examineLengthByLineInParagraph(Map<String, IvtffText> clusterParagraphs, boolean reverse) {

		// Calculate average length in all positions for each cluster
		int maxLen = 0;
		Map<String, double[]> len = new HashMap<>();
		for (String cluster : clusterParagraphs.keySet()) {
			IvtffText txt = Experiment.filterLines(clusterParagraphs.get(cluster), false, false);
			List<Counter<String>> wordsByPosition = Experiment.getWordsByLine(txt, true, MIN_LINE_LEN, MAX_LINE_LEN);
			if (reverse)
				Collections.reverse(wordsByPosition);
			double[] avgLen = getAverageLength(wordsByPosition);
			if (avgLen.length > maxLen)
				maxLen = avgLen.length;
			len.put(cluster, avgLen);
		}

		// Print results
		System.out.print("Cluster");
		for (int i = 0; i < maxLen; ++i) {
			System.out.print(";Length(byLine(" + i + "))");
		}
		System.out.println();
		for (Entry<String, double[]> e : len.entrySet()) {
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
	 * @param wordsByPosition A List of Counters, counting words appearing in
	 *                        specific position (e.g. at a given position in a
	 *                        line).
	 * @return A double[] of same size than wordsByPosition with average word length
	 *         for words in each Counter.
	 */
	private static double[] getAverageLength(List<Counter<String>> wordsByPosition) {
		double[] result = new double[wordsByPosition.size()];
		for (int i = 0; i < wordsByPosition.size(); ++i) {
			int totLen = 0;
			for (Entry<String, Integer> e : wordsByPosition.get(i).entrySet()) {
				totLen += e.getKey().length() * e.getValue();
			}
			result[i] = (double) totLen / wordsByPosition.get(i).getTotalCounted();
		}

		return result;
	}
}
