/**
 * 
 */
package org.v4j.applications;

import org.v4j.text.Text;
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
public final class WordEntropy {

	private WordEntropy() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			IvtffText doc = VoynichFactory.getDocument(TranscriptionType.MAJORITY);
			
			for (String cluster : PageHeader.clusters) {
				IvtffText c = doc.filterPages(new PageFilter.Builder().cluster(cluster).build());
				
				System.out.println(cluster + ":\t" + MathUtil.entropy(c.getWords(true)));
				
				getNWordEntropy(c);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	private static void getNWordEntropy(Text c) {
		Counter<String> result = new Counter<>();
		String txt = c.getPlainText();
		
		// Look at pages individually
		
	}

}
