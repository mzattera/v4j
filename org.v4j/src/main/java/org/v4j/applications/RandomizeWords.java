/**
 * 
 */
package org.v4j.applications;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.v4j.text.Text;
import org.v4j.text.alphabet.Alphabet;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.PageFilter;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.util.StringUtil;

/**
 * This class taxes a Text and mixes up all of its words. Typically used to have
 * a bottom line reference case in statistical studies.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class RandomizeWords {

	private RandomizeWords() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			IvtffText doc = VoynichFactory.getDocument(TranscriptionType.MAJORITY);
			doc.filterPages(new PageFilter.Builder().cluster("B").build());
			System.out.println(process(doc));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	/**
	 * @return The plain text for given Text, with word randomly shuffled. Word
	 *         count is preserved.
	 */
	public static String process(Text txt) {
		return process (txt.getPlainText(), txt.getAlphabet());
	}

	/**
	 * @param txt A plain text using the given alphabet.
	 * @return The text with words randomly shuffled.
	 */
	public static String process(String txt, Alphabet a) {
		List<String> words = Arrays.asList(StringUtil.splitWords(txt, a));
		Collections.shuffle(words);
		return String.join(a.getSpaceAsString(), words);
	}
}
