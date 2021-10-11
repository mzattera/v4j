/**
 * 
 */
package io.github.mzattera.v4j.applications;

import java.util.Map.Entry;

import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;

/**
 * Counts the words in a document.
 * 
 * STATUS: Works with a test harness (see CountWordsTest)
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class CountWords {

	/**
	 * Which transcription to use.
	 */
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

	/**
	 * Which transcription type to use.
	 */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.CONCORDANCE;

	/** Filter to use on pages before analysis */
	public static final ElementFilter<IvtffPage> FILTER = null;

	/** Get all words or only regular? */
	public static final boolean ONY_REGULAR = true;

	
	private CountWords() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// PRints configuration parameters
			System.out.println("Transcription: " + TRANSCRIPTION);
			System.out.println("Transcription Type: " + TRANSCRIPTION_TYPE);
			System.out.println("Filter: " + (FILTER == null ? " (none)" : FILTER + (ONY_REGULAR ? " only regular words" : " all words (including illegible)")));
			System.out.println();

			IvtffText doc = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE);
			if (FILTER != null)
				doc = doc.filterPages(FILTER);

			Counter<String> c = doc.getWords(ONY_REGULAR);
			System.out.println("Total terms : " + c.size());
			System.out.println("Total tokens: " + c.getTotalCounted());
			System.out.println();
						
			for (Entry<String, Integer> e : c.reversed()) {
				System.out.println(e.getKey() + ";" + e.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}

	}
}
