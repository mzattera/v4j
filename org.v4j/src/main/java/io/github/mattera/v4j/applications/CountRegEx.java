package io.github.mattera.v4j.applications;

import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mattera.v4j.util.Counter;

/**
 * Counts all the matches of a given regular expression (regex). It then prints
 * a list of the matches found together with their count.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class CountRegEx {

	// The RegEx to look for.
	private final static String REGEX = "c([^tpfk]h|[tpfk][^h]|[^tpfkh])";
//	private final static String REGEX = "c([^tpfk]h|[^tpfkh]|[tpfk][^h])";
//	private final static String REGEX = "[^tpfkcs\\?]h|.\\?h";
//	private final static String REGEX = ".c.";

	private CountRegEx() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println(REGEX);
			System.out.println();

			// Get the text to process
			IvtffText doc = VoynichFactory.getDocument(TranscriptionType.MAJORITY);
//			doc = doc.filterPages(new ElementFilter<IvtffPage>() {
//
//				@Override
//				public boolean keep(IvtffPage element) {
//					// This filter returns all pages which belong to the "main" clusters.
//					return (element.getDescriptor().getCluster().length() == 3);
//				}
//			});


			Counter<String> c = process("." + doc.getPlainText() + ".", REGEX);

			for (Entry<String, Integer> e : c.reversed()) {
				System.out.println(e.getKey() + ";" + e.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}

	}

	public static Counter<String> process(String s, String regex) {
		Counter<String> result = new Counter<>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(s);
		while (m.find()) {
			result.count(m.group());
		}

		return result;
	}
}
