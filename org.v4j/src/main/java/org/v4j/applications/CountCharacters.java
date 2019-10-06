/**
 * 
 */
package org.v4j.applications;

import java.util.Map.Entry;

import org.v4j.text.Text;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.text.txtfile.BibleFactory;
import org.v4j.text.txtfile.TextFile;
import org.v4j.util.Counter;

/**
 * Counts the characters in a document.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class CountCharacters {

	private CountCharacters() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// IvtffText doc = VoynichFactory.getDocument(TranscriptionType.CONCORDANCE);
			// doc.filterPages(new PageFilter.Builder().illustrationType("B").build());
			TextFile doc = BibleFactory.getDocument("Italian");
			
			Counter<Character> c = doc.getChars();
			for (Entry<Character, Integer> e : c.entrySet()) {
				System.out.println(e.getKey() + ";" + e.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}

	}
}
