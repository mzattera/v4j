package io.github.mzattera.v4j.applications.slot;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermClassification;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermDecomposition;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.text.txt.TextString;
import io.github.mzattera.v4j.util.Counter;

/**
 * Studies the "slot" concept and the "Slot" alphabet. Divides words in slots
 * and prints the result as a .CSV text.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class Slots {

	/** Transcription to use */
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

	/** Transcription to use */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.CONCORDANCE;

	/** Filter to use on pages before analysis */
	public static final ElementFilter<IvtffPage> FILTER = null;

	public static void main(String[] args) throws Exception {

		// Get the document to process
		IvtffText doc = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE);
		if (FILTER != null)
			doc = doc.filterPages(FILTER);

		// Replaces text with the "Slot" alphabet
		String txt = SlotAlphabet.fromEva(doc.getPlainText());

		// All readable words, after translating into Slot alphabet.
		Counter<String> allWords = new TextString(txt, Alphabet.SLOT).getWords(true);

		// Classify the terms, based on their decomposition
		Map<String, TermDecomposition> classified = SlotAlphabet.decompose(allWords.itemSet());

		// Verify which SEPARABLE terms are composed of parts actually in the text.
		Map<String, Boolean> verified = verify(classified.values(), allWords);

		// Counts words and tokens by its TermClassification
		Counter<String> wByType = new Counter<>();
		Counter<String> tByType = new Counter<>();
		for (TermDecomposition e : classified.values()) {
			wByType.count(e.classification.name());
			tByType.count(e.classification.name(), allWords.getCount(e.term));
		}
		Counter<Boolean> wVerified = new Counter<>();
		Counter<Boolean> tVerified = new Counter<>();
		for (TermDecomposition e : classified.values()) {
			if ((e.classification == TermClassification.SEPARABLE)) {
				wVerified.count(verified.get(e.term));
				tVerified.count(verified.get(e.term), allWords.getCount(e.term));
			}
		}

		// Prints statistics
		System.out.println("Transcription: " + TRANSCRIPTION + "-" + TRANSCRIPTION_TYPE
				+ (FILTER == null ? " complete" : " with filter:" + FILTER));
		System.out.println(";;;;;;;");
		System.out.println(";;Terms;%;;Tokens;%");
		for (SlotAlphabet.TermClassification v : SlotAlphabet.TermClassification.values()) {
			String n = v.name();
			System.out.print(n + ";;" + wByType.getCount(n) + ";" + (double) wByType.getCount(n) / allWords.size());
			System.out.println(
					";;" + tByType.getCount(n) + ";" + (double) tByType.getCount(n) / allWords.getTotalCounted());

			if (v == TermClassification.SEPARABLE) {
				System.out.print("verified" + ";;" + wVerified.getCount(true) + ";"
						+ (double) wVerified.getCount(true) / wVerified.getTotalCounted());
				System.out.println(";;" + tVerified.getCount(true) + ";"
						+ (double) tVerified.getCount(true) / tVerified.getTotalCounted());
				System.out.print("NOT verified" + ";;" + wVerified.getCount(false) + ";"
						+ (double) wVerified.getCount(false) / wVerified.getTotalCounted());
				System.out.println(";;" + tVerified.getCount(false) + ";"
						+ (double) tVerified.getCount(false) / tVerified.getTotalCounted());
			}
		}
		System.out.println("Total;;" + allWords.size() + ";;;" + allWords.getTotalCounted() + ";");
		System.out.println(";;;;;;;");

		// Prints table of decomposed words
		System.out.println(";;;;;;;;FIRST PART;;;;;;;;;;;;;SECOND PART;;;;;;;");
		System.out.println(
				"Term;Classification;Part 1;Slot 0;Slot 1;Slot 2;Slot 3;Slot 4;Slot 5;Slot 6;Slot 7;Slot 8;Slot 9;Slot 10;Slot 11;Part 2;Slot 0;Slot 1;Slot 2;Slot 3;Slot 4;Slot 5;Slot 6;Slot 7;Slot 8;Slot 9;Slot 10;Slot 11;Count;Verified");
		for (Entry<String, Integer> e : allWords.reversed()) {
			String w = e.getKey();
			int freq = e.getValue();
			System.out.println(w + ";" + toCSV(classified.get(w)) + freq + ";" + verified.getOrDefault(w, false));
		}
	}

	/**
	 * Verifies if each SEPARABLE term is composed of terms actually appearing in
	 * the text.
	 * 
	 * For each term classified as SEPARABLE, this checks whether its two parts
	 * appear in the text at least as frequently as the whole term.
	 * 
	 * @param classification The terms already classified.
	 * @param allTerms       The list of terms in the entire text, with their
	 *                       occurrence count.
	 * 
	 * @return a map where each SEPARABLE term is mapped into a boolean which is
	 *         true it it was verified that the term parts apper in the whole text.
	 */
	public static Map<String, Boolean> verify(Collection<TermDecomposition> classification, Counter<String> allTerms) {
		Map<String, Boolean> result = new HashMap<>();

		for (TermDecomposition d : classification) {
			if (d.classification == TermClassification.SEPARABLE)
				result.put(d.term,
						Math.min(allTerms.getCount(d.part1), allTerms.getCount(d.part2)) >= allTerms.getCount(d.term));
		}

		return result;
	}

	/**
	 * Prints term decomposition in in a way suitable for CSV files.
	 */
	public static String toCSV(TermDecomposition term) {
		StringBuffer result = new StringBuffer(term.classification.name()).append(';');

		result.append(term.part1).append(';');
		for (String s : term.slots1) {
			if (!s.isBlank() && Character.isUpperCase(s.charAt(0)))
				result.append(s).append("^;");
			else
				result.append(s).append(";");
		}

		result.append(term.part2).append(';');
		for (String s : term.slots2) {
			if (!s.isBlank() && Character.isUpperCase(s.charAt(0)))
				result.append(s).append("^;");
			else
				result.append(s).append(";");
		}

		return result.toString();
	}
}
