/**
 * 
 */
package org.v4j.applications;

import org.v4j.text.Text;
import org.v4j.text.alphabet.Alphabet;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.LineFilter;
import org.v4j.text.ivtff.PageFilter;
import org.v4j.text.ivtff.PageHeader;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.text.txtfile.BibleFactory;
import org.v4j.text.txtfile.TextFile;
import org.v4j.util.Counter;
import org.v4j.util.MathUtil;

/**
 * @author Massimiliano_Zattera
 *
 */
public final class BlockWordEntropy {

	private BlockWordEntropy() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			final int N = 10;
			IvtffText voy = VoynichFactory.getDocument(TranscriptionType.MAJORITY);

			// Entropy for Voynich sections
			for (String cluster : PageHeader.clusters) {

				// Get the paragraph text for pages in the current cluster
				IvtffText doc = voy.filterPages(new PageFilter.Builder().cluster(cluster).build());
				doc = doc.filterLines(new LineFilter.Builder().genericLocusType("P").build());

				System.out.print(cluster);
				for (int i = 1; i <= N; ++i) {
					System.out.print(";" + process(doc, i, true));
				}
				System.out.println();

				String rnd = RandomizeWords.process(doc);
				System.out.print(cluster + "_RND");
				for (int i = 1; i <= N; ++i) {
					System.out.print(";" + process(rnd, doc.getAlphabet(), i, true));
				}
				System.out.println();
			}

			// Entropy for bible in different languages
			for (String lang : BibleFactory.LANGUAGES) {
				// Take only first 5000 words in every language, to match the Voynich sections more closely
				TextFile doc = BibleFactory.getDocument(lang);
				String[] w = doc.splitWords();
				StringBuffer txt = new StringBuffer();
				for (int i=0; i<5000; ++i) {
					txt.append(w[i]).append(doc.getAlphabet().getSpace());
				}
								
				System.out.print(lang);
				for (int i = 1; i <= N; ++i) {
					System.out.print(";" + process(txt.toString(), doc.getAlphabet(), i, true));
				}
				System.out.println();

				String rnd = RandomizeWords.process(txt.toString(), doc.getAlphabet());
				System.out.print(lang+"_RND");
				for (int i = 1; i <= N; ++i) {
					System.out.print(";" + process(rnd, doc.getAlphabet(), i, true));
				}
				System.out.println();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	/**
	 * Returns entropy for sequences of n words in given Text plain text.
	 * 
	 * @param regularOnly
	 *            if true, consider only sequences of words that are all readable.
	 * 
	 * @return The count for all sequences of N words in the text.
	 */
	public static double process(Text txt, int n, boolean readableOnly) {
		return process(txt.getPlainText(), txt.getAlphabet(), n, readableOnly);
	}

	/**
	 * Returns entropy for sequences of n words in given text. Notice txt is
	 * supposed to be a plain (normalized) text that uses given Alphabet.
	 * 
	 * 
	 * @param txt
	 *            a plain (normalized) text that uses given Alphabet.
	 * @param a
	 *            Alphabet ued in txt.
	 * @param regularOnly
	 *            if true, consider only sequences of words that are all readable.
	 */
	public static double process(String txt, Alphabet a, int n, boolean readableOnly) {
		Counter<String> c = CountNWords.process(txt, a, n, readableOnly);
		return MathUtil.entropy(c);
	}
}
