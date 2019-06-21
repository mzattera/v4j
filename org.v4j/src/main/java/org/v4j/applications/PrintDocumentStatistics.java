/**
 * 
 */
package org.v4j.applications;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.v4j.text.ivtff.IvtffPage;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.PageHeader;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.util.Counter;

/**
 * This class takes a IVTFF document and prints page statistics in a CSV file.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class PrintDocumentStatistics {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			IvtffText doc = VoynichFactory.getDocument(TranscriptionType.MAJORITY);
			doWork(doc,
					"D:\\VoynichStatsMajority.csv");
			doc = VoynichFactory.getDocument(TranscriptionType.CONCORDANCE);
			doWork(doc,
					"D:\\VoynichStatsConcordance.csv");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}

	}

	public static void doWork(IvtffText doc, String fileName) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
				CSVPrinter csvPrinter = new CSVPrinter(writer,
						CSVFormat.DEFAULT.withHeader("ID", "IllustrationType", "Language", "Cluster", "Hand", "Quire",
								"PageInQuire", "Parchment", "Words", "Tokens", "ClearWords", "ClearTokens"));) {

			for (IvtffPage p : doc.getElements()) {
				PageHeader ph = p.getDescriptor();
				Counter<String> allWords = p.getWords(false);
				Counter<String> clrWords = p.getWords(true);
				csvPrinter.printRecord(p.getId(), ph.getIllustrationType(), ph.getLanguage(), ph.getCluster(), ph.getHand(),
						ph.getQuire(), ph.getPageInQuire(), ph.getParchment(), allWords.getTotalCounted(),
						allWords.itemSet().size(), clrWords.getTotalCounted(), clrWords.itemSet().size());
			}

			csvPrinter.flush();
		}
	}
}
