/**
 * 
 */
package io.github.mzattera.v4j.applications.chars;

import java.util.Map.Entry;

import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageFilter;
import io.github.mzattera.v4j.text.ivtff.PageHeader;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;

/**
 * Create a scatter plot of char frequency vs % of word types containing it.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class CurrierRepeatTest {

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

	private CurrierRepeatTest() {
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
			System.out.println("Filter            : " + (FILTER == null ? "<no-filter>" : FILTER));
			System.out.println();

			IvtffText doc = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, ALPHABET);
			if (FILTER != null)
				doc = doc.filterPages(FILTER);

			char[] chars = doc.getAlphabet().getRegularChars();

			for (String cluster : PageHeader.CLUSTERS) {
				IvtffText clusterText = doc.filterPages(new PageFilter.Builder().cluster(cluster).build());
				System.out.println("[" + cluster + "]======\n");

				// How many tokens contain a given char?
				Counter<Character> tokens = new Counter<>();

				// How many word types contain a given char?
				Counter<Character> types = new Counter<>();

				// Counts tokens and chars for each character
				Counter<String> words = clusterText.getWords(true);
				for (Entry<String, Integer> e : words.entrySet()) {
					for (char c : chars) {
						if (e.getKey().indexOf(c) != -1) {
							tokens.count(c, e.getValue());
							types.count(c);
						}
					}
				}

				// Print results
				for (char c : chars) {
					System.out.println(c + ";" + (100.0 * tokens.getCount(c) / tokens.getTotalCounted()) + ";"
							+ (100.0 * types.getCount(c) / types.getTotalCounted()) + ";");
				}
				System.out.println();

			} // for each cluster

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}

	}
}
