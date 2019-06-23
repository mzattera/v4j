/**
 * 
 */
package org.v4j.applications;

import java.util.List;
import java.util.Map.Entry;

import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.PageFilter;
import org.v4j.text.ivtff.PageHeader;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.util.Counter;

/**
 * Shows for each cluster the list of most frequent words.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class MostUsedWords {

	private MostUsedWords() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			IvtffText voy = VoynichFactory.getDocument(TranscriptionType.MAJORITY);
			System.out.println("Cluster;Term;Count;Rel. Freq.");

			// Entropy for Voynich sections
			for (String cluster : PageHeader.CLUSTERS) {
				IvtffText doc = voy.filterPages(new PageFilter.Builder().cluster(cluster).build());

				Counter<String> count = doc.getWords(true);
				List<Entry<String, Integer>> words = count.sorted();
				int m = Math.max(words.size() - 20, 0);
				for (int i = words.size() - 1; i > m; --i) {
					System.out.println(cluster + ";" + words.get(i).getKey() + ";" + words.get(i).getValue() + ";"
							+ ((double) words.get(i).getValue() / count.getTotalCounted()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}
}
