/**
 * 
 */
package io.github.mzattera.v4j.applications.chars;

import java.util.List;
import java.util.Map.Entry;

import io.github.mzattera.v4j.experiment.Experiment;
import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.LineFilter;
import io.github.mzattera.v4j.text.ivtff.PageFilter;
import io.github.mzattera.v4j.text.ivtff.PageHeader;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;

/**
 * Statistics about words starting or containing gallows, in different positions
 * of the text.
 * 
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class CountGallows {

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

	/** Filter to use on pages before analysis */
	public static final ElementFilter<IvtffPage> FILTER = null;

	private final static char[] GALLOWS = { 't', 'k', 'p', 'f', 'T', 'K', 'P', 'F', 'C', 'S' };

	private CountGallows() {
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
			System.out.println("Filter            : " + (FILTER == null ? "<no-filter>" : FILTER)
					+ " running text only (P0 & P1)");
			System.out.println();

			IvtffText doc = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, ALPHABET);
			doc = doc.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);
			if (FILTER != null)
				doc = doc.filterPages(FILTER);

			System.out.println("% of Tokens Containig Gallows");
			process(doc, false);
			System.out.println("\n\n% of Tokens Starting with Gallows");
			process(doc, true);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}

	}

	/**
	 * @param doc
	 * @param first If true count tokens starting with gallows, otherwise count
	 *              tokens containing gallows.
	 */
	private static void process(IvtffText doc, boolean first) {
		// Header
		System.out.print("Locus;Cluster;");
		for (char c : GALLOWS)
			System.out.print(c + ";");
		System.out.println();

		for (String cluster : PageHeader.CLUSTERS) {
			IvtffText clusterText = doc.filterPages(new PageFilter.Builder().cluster(cluster).build());

			// Gets words in different positions;
			// TODO NOTICE WE IGNORE UNREADABLE WORDS!!!!!

			Text[] split = new Experiment.FirstLineInParagraph(false).splitDocument(clusterText);
			IvtffText firstLines = (IvtffText)split[0];
			List<Counter<String>> firstLineWords = Experiment.getWordsByPosition(firstLines, true);

			// First words of paragraphs
			Counter<String> firstWordsOfPar = firstLineWords.get(0);
			System.out.print("First Word of a Paragraph;" + cluster + ";");
			analize(firstWordsOfPar, first);

			// Other words in first line
			Counter<String> wordsInFirstLine = new Counter<>();
			for (int i = 1; i < firstLineWords.size(); ++i) {
				wordsInFirstLine.countAll(firstLineWords.get(i));
			}
			System.out.print("Remaining Words in First Line;" + cluster + ";");
			analize(wordsInFirstLine, first);

			List<Counter<String>> other = Experiment
					.getWordsByPosition((IvtffText)split[1], true);

			// First words in remaining lines
			Counter<String> firstWordsInLine = other.get(0);
			System.out.print("First Word of Other Lines;" + cluster + ";");
			analize(firstWordsInLine, first);

			// All other words
			Counter<String> rest = new Counter<>();
			for (int i = 1; i < other.size(); ++i) {
				rest.countAll(other.get(i));
			}
			System.out.print("Remaining Words;" + cluster + ";");
			analize(rest, first);
		} // for each cluster
	}

	/**
	 * Prints relevant % for given word distribution.
	 * 
	 * @param wordsInFirstLine
	 * @param first            If true count tokens starting with gallows, otherwise
	 *                         count tokens containing gallows.
	 */
	private static void analize(Counter<String> words, boolean first) {

		// How many tokens with given gallows
		Counter<Character> tokens = new Counter<>();

		for (Entry<String, Integer> e : words.entrySet()) {
			for (char c : GALLOWS) {
				if ((first && (e.getKey().charAt(0) == c)) || (!first && (e.getKey().indexOf(c) != -1))) {
					tokens.count(c, e.getValue());
				}
			}
		}

		// Print results
		double t = 0.0;
		for (char c : GALLOWS) {
			double p = (double) tokens.getCount(c) / words.getTotalCounted();
			if (Double.isNaN(p))
				p = 0.0;
			t += p;
			System.out.print(p + ";");
		}
		System.out.println(t);
	}
}
