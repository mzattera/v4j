/**
 * 
 */
package io.github.mzattera.v4j.applications.chars;

import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffLine;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;

/**
 * Test claim in CURRIER (1976): In the herbal material and of the biological
 * material there is not one single case of a repeat going over the end of a
 * line to the beginning of the next.
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
			System.out.println("Filter            : " + (FILTER == null ? "<no-filter>" : FILTER)
					+ " running text only (P0 & P1)");
			System.out.println();

			IvtffText doc = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, ALPHABET);
			if (FILTER != null)
				doc = doc.filterPages(FILTER);

			String lastWord = null;
			for (IvtffLine l : doc.getLines()) {
				String[] w = l.splitWords();
				if (w.length == 0)
					continue; // paranoid guard
				if ((lastWord != null) && (w[0].equals(lastWord)))
					System.out.println(l);
				lastWord = w[w.length-1];

			} // for each line

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}

	}
}
