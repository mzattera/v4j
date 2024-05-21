/* Copyright (c) 2021-2022 Massimiliano "Maxi" Zattera */

package io.github.mzattera.v4j.applications.slot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermDecomposition;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;

/**
 * Studies the "slot" concept and the "slot" alphabet.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class CountCharsBySlot {

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
//	public static final ElementFilter<IvtffPage> FILTER = new PageFilter.Builder().cluster("HA").build();

	private static final List<String> CHARS = new ArrayList<>();
	static {
		CHARS.add("q");
		CHARS.add("s");
		CHARS.add("d");
		CHARS.add("o");
		CHARS.add("y");
		CHARS.add("l");
		CHARS.add("r");
		CHARS.add("k");
		CHARS.add("t");
		CHARS.add("p");
		CHARS.add("f");
		CHARS.add("C");
		CHARS.add("S");
		CHARS.add("K");
		CHARS.add("T");
		CHARS.add("P");
		CHARS.add("F");
		CHARS.add("e");
		CHARS.add("E");
		CHARS.add("B");
		CHARS.add("a");
		CHARS.add("i");
		CHARS.add("J");
		CHARS.add("U");
		CHARS.add("n");
		CHARS.add("m");
		CHARS.add("=");
	}

	private CountCharsBySlot() {
	}

	public static void main(String[] args) throws Exception {

		// Get the document to process
		IvtffText doc = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, Alphabet.SLOT);
		if (FILTER != null)
			doc = doc.filterPages(FILTER);

		Counter<String> allWords = doc.getWords(true);

		// All terms past classification
		Map<String, TermDecomposition> classified = SlotAlphabet.decompose(allWords.itemSet());

		// Total regular terms and token considered
		int terms = 0, tokens = 0;

		// Prints statistics
		System.out.println("Transcription: " + TRANSCRIPTION + "-" + TRANSCRIPTION_TYPE
				+ (FILTER == null ? " (complete)" : " with filter: " + FILTER));

		// Each counters counts occurrence of tokens with a given character in a slot
		@SuppressWarnings("unchecked")
		Counter<String>[] termSlotCounter = new Counter[SlotAlphabet.SLOTS.size()];
		@SuppressWarnings("unchecked")
		Counter<String>[] tokenSlotCounter = new Counter[SlotAlphabet.SLOTS.size()];
		for (int i = 0; i < termSlotCounter.length; i++) {
			termSlotCounter[i] = new Counter<String>();
			tokenSlotCounter[i] = new Counter<String>();
		}

		// Count all chars in slots
		for (TermDecomposition d : classified.values()) {
			if (d.classification != SlotAlphabet.TermClassification.REGULAR)
				continue;

			++terms;
			tokens += allWords.getCount(d.term);
			for (int i = 0; i < d.slots1.length; ++i) {
				if (d.slots1[i].isEmpty()) {
					termSlotCounter[i].count("=");
					tokenSlotCounter[i].count("=", allWords.getCount(d.term));
				} else {
					termSlotCounter[i].count(d.slots1[i]);
					tokenSlotCounter[i].count(d.slots1[i], allWords.getCount(d.term));
				}
			}
		}

		// Make sure we show all chars (we do in this way so we preserve the order we
		// want).
		for (int i = 0; i < termSlotCounter.length; ++i) {
			for (String ch : termSlotCounter[i].itemSet()) {
				if (!CHARS.contains(ch))
					CHARS.add(ch);
			}
		}

		// Print results
		System.out.println("Total terms considered;;;;;" + terms);
		System.out.println(";;;;;;;");
		System.out.println(";Slot 0;Slot 1;Slot 2;Slot 3;Slot 4;Slot 5;Slot 6;Slot 7;Slot 8;Slot 9;Slot 10;Slot 11");
		for (String ch : CHARS) {
			System.out.print(ch);
			for (int i = 0; i < termSlotCounter.length; i++) {
				int c = termSlotCounter[i].getCount(ch);
				System.out.print(";" + (c > 0 ? c + "" : ""));
			}
			System.out.println();
		}
		System.out.println();

		// Print results
		System.out.println("Total tokens considered;;;;;" + tokens);
		System.out.println(";;;;;;;");
		System.out.println(";Slot 0;Slot 1;Slot 2;Slot 3;Slot 4;Slot 5;Slot 6;Slot 7;Slot 8;Slot 9;Slot 10;Slot 11");
		for (String ch : CHARS) {
			System.out.print(ch);
			for (int i = 0; i < tokenSlotCounter.length; i++) {
				int c = tokenSlotCounter[i].getCount(ch);
				System.out.print(";" + (c > 0 ? c + "" : ""));
			}
			System.out.println();
		}
	}
}
