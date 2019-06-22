/**
 * 
 */
package org.v4j.applications;

import org.v4j.text.Text;
import org.v4j.text.alphabet.Alphabet;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.PageFilter;
import org.v4j.text.ivtff.PageHeader;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
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
			IvtffText doc = VoynichFactory.getDocument(TranscriptionType.MAJORITY);

			/*
			 * System.out.print("Cluster"); for (int i = 1; i <= N; ++i) {
			 * System.out.print(";" + i); } System.out.println();
			 */
			for (String cluster : PageHeader.clusters) {
				IvtffText c = doc.filterPages(new PageFilter.Builder().cluster(cluster).build());

				System.out.print(cluster);
				for (int i = 1; i <= N; ++i) {
					System.out.print(";" + process(c, i, true));
				}
				System.out.println();

				String txt = RandomizeWords.process(c);
				System.out.print(cluster + "_RND");
				for (int i = 1; i <= N; ++i) {
					System.out.print(";" + process(txt, c.getAlphabet(), i, true));
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
