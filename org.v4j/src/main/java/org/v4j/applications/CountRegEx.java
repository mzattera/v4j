/**
 * 
 */
package org.v4j.applications;

import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.PageFilter;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.util.Counter;

/**
 * Counts all the matches of a goiven regex.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class CountRegEx {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			IvtffText doc = VoynichFactory.getDocument(TranscriptionType.CONCORDANCE);
			doc.filterPages(new PageFilter.Builder().illustrationType("B").build());

			Counter<String> c = doWork("."+doc.getPlainText()+".", "(..h)|(c..)");
			for (Entry<String, Integer> e : c.entrySet()) {
				System.out.println(e.getKey() + ";" + e.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}

	}

	private static Counter<String> doWork(String s, String regex) {
		Counter<String> result = new Counter<>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		while (m.matches()) {
			result.count(m.group());
		}
		
		return result;
	}
}
