/**
 * 
 */
package io.github.mattera.v4j.applications;

import java.util.Map.Entry;

import io.github.mattera.v4j.text.ElementFilter;
import io.github.mattera.v4j.text.ivtff.IvtffPage;
import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mattera.v4j.util.Counter;

/**
 * Counts the characters in a document.
 * 
 * STATUS: Works with a test harness (see CountCharactersTest)
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class CountCharacters {

	/**
	 * Which transcription to use.
	 */
	public static final Transcription TRANSCRIPTION = Transcription.MZ;

	/**
	 * Which transcription type to use.
	 */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.CONCORDANCE;

	/** Filter to use on pages before analysis */
	public static final ElementFilter<IvtffPage> FILTER = null;

	private CountCharacters() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// PRints configuration parameters
			System.out.println("Transcription: " + TRANSCRIPTION);
			System.out.println("Transcription Type: " + TRANSCRIPTION_TYPE);
			System.out.println("Filter: " + (FILTER == null ? " (none)" : FILTER));
			System.out.println();

			IvtffText doc = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE);
			if (FILTER != null)
				doc = doc.filterPages(FILTER);

			Counter<Character> c = doc.getChars();
			for (Entry<Character, Integer> e : c.reversed()) {
				System.out.println(e.getKey() + ";" + e.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}

	}
}
