/* Copyright (c) 2021 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.applications;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map.Entry;

import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.Text;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.text.txt.BibleFactory;
import io.github.mzattera.v4j.text.txt.TextFile;
import io.github.mzattera.v4j.util.Counter;

/**
 * Counts the characters in the Voynich and the Bibles.
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
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

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
			for (Alphabet a : Alphabet.getAlphabets()) {
				processVoynich(a);
				System.out.println();
			}

			for (String language : BibleFactory.LANGUAGES) {
				processModernLanguage(language);
			System.out.println();
		}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}

	}

	private static void processVoynich(Alphabet a) throws Exception {

		IvtffText doc = null;
		try {
			doc = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, a);
		} catch (IllegalArgumentException e) {
			// This specific version of the document this unavailable
			return;
		}
		if (FILTER != null)
			doc = doc.filterPages(FILTER);
		
		// Prints configuration parameters
		System.out.println("Voynich");
		System.out.println("Transcription     : " + TRANSCRIPTION);
		System.out.println("Transcription Type: " + TRANSCRIPTION_TYPE);
		System.out.println("Alphabet          : " + a);
		System.out.println("Filter            : " + (FILTER == null ? "<no-filter>" : FILTER));
		System.out.println();
		
		Counter<Character> c = doc.getChars();
		for (Entry<Character, Integer> e : c.reversed()) {
			System.out.println(e.getKey() + ";" + e.getValue() + ";" + ((double)e.getValue()/c.getTotalCounted()));
		}		
		
	}

	private static void processModernLanguage(String language) throws IOException, URISyntaxException {
		System.out.println("Bible (" + language + ")");

		TextFile doc = BibleFactory.getDocument(language);
		Alphabet a = doc.getAlphabet();

		Counter<Character> c = Text.getChars(a.toUpperCase(doc.getPlainText()), a);
		for (Entry<Character, Integer> e : c.reversed()) {
			System.out.println(e.getKey() + ";" + e.getValue() + ";" + ((double) e.getValue() / c.getTotalCounted()));
		}
	}
}
