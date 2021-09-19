package io.github.mattera.v4j.applications.slot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.mattera.v4j.text.alphabet.Alphabet;
import io.github.mattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mattera.v4j.text.alphabet.SlotAlphabet.TermDecomposition;
import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;
import io.github.mattera.v4j.text.txt.TextString;
import io.github.mattera.v4j.util.Counter;

/**
 * Studies the "slot" concept and the "slot" alphabet.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class CountCharsBySlot {

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

	public static void main(String[] args) throws Exception {

		// Get the document to process
		IvtffText doc = VoynichFactory.getDocument(Slots.TRANSCRIPTION, Slots.TRANSCRIPTION_TYPE);
		if (Slots.FILTER != null)
			doc = doc.filterPages(Slots.FILTER);

		// Replaces text with the "slot" alphabet (tentative)
		String txt = SlotAlphabet.fromEva(doc.getPlainText());

		// All words after replacement, with no unreadable characters.
		// NOTE The default alphabet uses only letters as "plain" characters, so don't
		// replace letters with e.g. numbers or this line won't work.
		Counter<String> allWords = new TextString(txt, Alphabet.SLOT).getWords(true);

		// All terms past classification
		Map<String, TermDecomposition> classified = SlotAlphabet.decompose(allWords.itemSet());

		// Total regular terms (considered)
		int tot = 0;

		// Prints statistics
		System.out.println("Transcription: " + Slots.TRANSCRIPTION + "-" + Slots.TRANSCRIPTION_TYPE
				+ (Slots.FILTER == null ? " (complete)" : " with filter: " + Slots.FILTER));

		// Each counters counts occurrence of tokens with a given character in a slot
		@SuppressWarnings("unchecked")
		Counter<String>[] slotCounter = new Counter[SlotAlphabet.SLOTS.size()];
		for (int i = 0; i < slotCounter.length; i++)
			slotCounter[i] = new Counter<String>();

		// Count all chars in slots
		for (TermDecomposition d : classified.values()) {
			if (d.classification != SlotAlphabet.TermClassification.REGULAR)
				continue;

			++tot;
			for (int i = 0; i < d.slots1.length; ++i) {
				if (d.slots1[i].isEmpty())
					slotCounter[i].count("=");
				else
					slotCounter[i].count(d.slots1[i]);
			}
		}

		// Make sure we show all chars (we do in this way so we preserve the order we
		// want).
		for (int i = 0; i < slotCounter.length; ++i) {
			for (String ch : slotCounter[i].itemSet()) {
				if (!CHARS.contains(ch))
					CHARS.add(ch);
			}
		}

		// Print results
		System.out.println("Total terms considered;;;;;" + tot);
		System.out.println(";;;;;;;");
		System.out.println(";Slot 0;Slot 1;Slot 2;Slot 3;Slot 4;Slot 5;Slot 6;Slot 7;Slot 8;Slot 9;Slot 10;Slot 11");
		for (String ch : CHARS) {
			System.out.print(ch);
			for (int i = 0; i < slotCounter.length; i++) {
				int c = slotCounter[i].getCount(ch);
				System.out.print(";" + (c > 0 ? c + "" : ""));
			}
			System.out.println();
		}
	}
}
