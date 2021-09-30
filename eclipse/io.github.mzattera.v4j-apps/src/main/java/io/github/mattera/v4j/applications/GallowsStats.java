/**
 * 
 */
package io.github.mattera.v4j.applications;

import java.util.List;
import java.util.Map.Entry;

import io.github.mattera.v4j.text.ElementFilter;
import io.github.mattera.v4j.text.alphabet.Alphabet;
import io.github.mattera.v4j.text.ivtff.IvtffLine;
import io.github.mattera.v4j.text.ivtff.IvtffPage;
import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.LineFilter;
import io.github.mattera.v4j.text.ivtff.PageFilter;
import io.github.mattera.v4j.text.ivtff.PageHeader;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mattera.v4j.util.Counter;

/**
 * Do gallows really appear in strange places?
 * 
 * STATUS: Just implemented
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
// TODO write test
public final class GallowsStats {

	/**
	 * Which transcription to use.
	 */
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

	/**
	 * Which transcription type to use.
	 */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.CONCORDANCE;

	/**
	 * Which Alphabet type to use.
	 */
	public static final Alphabet ALPHABET = Alphabet.SLOT;

	/** Filter to use on pages before analysis */
	// Keep only text in paragraphs
	public static final ElementFilter<IvtffLine> FILTER = new LineFilter.Builder().genericLocusType("P").build();

	private GallowsStats() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Prints configuration parameters
			System.out.println("Transcription     : " + TRANSCRIPTION);
			System.out.println("Transcription Type: " + TRANSCRIPTION_TYPE);
			System.out.println("Alphabet          : " + ALPHABET);
			System.out.println("Filter            : " + (FILTER == null ? "<no-filter>" : FILTER));
			System.out.println();

			IvtffText doc = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, ALPHABET);
			if (FILTER != null)
				doc = doc.filterLines(FILTER);

			// Do analysis of each cluster
			System.out.println("Cluster;Line;Char;Count;Frequency");			
			for (String cluster : PageHeader.CLUSTERS) {
				process (cluster, doc.filterPages(new PageFilter.Builder().cluster(cluster).build()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}

	}

	private static void process(String cluster, IvtffText doc) {
		// chars in first line
		Counter<Character> first = new Counter<>();
		// chars in the rest of text
		Counter<Character> others = new Counter<>();
		for (IvtffPage p : doc.getElements()) {
			List<IvtffLine> l = p.getElements();
			first.count(l.get(0).getChars());
			for (int i=1; i<l.size(); ++i)
				others.count(l.get(i).getChars());
		}
		
		for (Entry<Character, Integer> e : first.entrySet()) {
			System.out.println(cluster + ";First;" + e.getKey() + (Character.isUpperCase(e.getKey()) ? "^" : "") + ";" + e.getValue() + ";" + ((double)e.getValue()/first.getTotalCounted()));			
		}
		for (Entry<Character, Integer> e : others.entrySet()) {
			System.out.println(cluster + ";Other;" + e.getKey() + (Character.isUpperCase(e.getKey()) ? "^" : "") + ";" + e.getValue() + ";" + ((double)e.getValue()/others.getTotalCounted()));			
		}
	}
}
