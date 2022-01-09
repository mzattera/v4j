/**
 * 
 */
package io.github.mzattera.v4j.applications.slot;

import java.util.Map.Entry;

import io.github.mzattera.v4j.applications.CountRegEx;
import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;

/**
 * This class prints occurrences of 'c' and 'h' appearing alone (not in 'ch',
 * 'sh', and gallows).
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class FindStrangeCH {

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Prints configuration parameters
			System.out.println("Transcription     : " + TRANSCRIPTION);
			System.out.println("Transcription Type: " + TRANSCRIPTION_TYPE);
			System.out.println("Filter            : " + (FILTER == null ? "<no-filter>" : FILTER));
			System.out.println();

			IvtffText doc = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, Alphabet.EVA);
			if (FILTER != null)
				doc = doc.filterPages(FILTER);

			// Replaces "valid" occurrences of c and h
			String txt = "." + doc.getPlainText() + ".";
			txt = txt.replaceAll("c([tkpf\\?])h", "C$1H");
			txt = txt.replaceAll("\\?([tkpf\\?])h", "?$1H");
			txt = txt.replaceAll("c([tkpf\\?])\\?", "C$1?");
			txt = txt.replaceAll("ch", "CH");
			txt = txt.replaceAll("sh", "SH");
			txt = txt.replaceAll("c\\?", "C?");
			txt = txt.replaceAll("\\?h", "?H");
			Counter<String> c = CountRegEx.process(txt, "[^\\.]*[ch]+[^\\.]*");

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
