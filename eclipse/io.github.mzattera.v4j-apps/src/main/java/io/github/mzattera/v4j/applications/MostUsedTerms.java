/**
 * 
 */
package io.github.mzattera.v4j.applications;

import java.util.List;
import java.util.Map.Entry;

import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageFilter;
import io.github.mzattera.v4j.text.ivtff.PageHeader;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;

/**
 * Shows for each cluster the list of most frequent words.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class MostUsedTerms {

	private final static Transcription TRANSCRIPTION = Transcription.AUGMENTED;
	private final static TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.MAJORITY;
	
	private MostUsedTerms() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			System.out.println("Using transcription     : " + TRANSCRIPTION);
			System.out.println("Using transcription type: " + TRANSCRIPTION_TYPE);
			
			IvtffText voy = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE);
			System.out.println("\nCluster;Term;Count;Rel. Freq.");

			for (String cluster : PageHeader.CLUSTERS) {
				IvtffText doc = voy.filterPages(new PageFilter.Builder().cluster(cluster).build());

				Counter<String> readableTokens = doc.getWords(true);
				int totTokens = doc.getWords(false).getTotalCounted();
				List<Entry<String, Integer>> words = readableTokens.reversed();
				int m = Math.min(words.size(), 20);
				for (int i = 0; i < m; ++i) {
					System.out.println(cluster + ";" + words.get(i).getKey() + ";" + words.get(i).getValue() + ";"
							+ ((double) words.get(i).getValue() / totTokens));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}
}
