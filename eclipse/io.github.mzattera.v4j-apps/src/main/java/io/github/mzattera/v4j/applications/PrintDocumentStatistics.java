package io.github.mzattera.v4j.applications;
/**
 * 
 */


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageHeader;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mzattera.v4j.util.Counter;

/**
 * This class takes a IVTFF document and prints page statistics in a CSV file.
 * This can be used to create pivots out of it.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class PrintDocumentStatistics {

	private PrintDocumentStatistics() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			IvtffText doc = VoynichFactory.getDocument(TranscriptionType.MAJORITY);
			process(doc, "D:\\VoynichStatsMajority.csv");
			doc = VoynichFactory.getDocument(TranscriptionType.CONCORDANCE);
			process(doc, "D:\\VoynichStatsConcordance.csv");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}

	}

	public static void process(IvtffText doc, String fileName) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
				CSVPrinter csvPrinter = new CSVPrinter(writer,
						CSVFormat.DEFAULT.withHeader("ID", "IllustrationType", "Language", "Cluster", "Hand", "Quire",
								"PageInQuire", "Parchment", "Tokens", "Types", "ClearTokens", "ClearTypes"));) {

			for (IvtffPage p : doc.getElements()) {
				PageHeader ph = p.getDescriptor();
				Counter<String> allWords = p.getWords(false);
				Counter<String> clrWords = p.getWords(true);
				csvPrinter.printRecord(p.getId(), ph.getIllustrationType(), ph.getLanguage(), ph.getCluster(),
						ph.getHand(), ph.getQuire(), ph.getPageInQuire(), ph.getParchment(), allWords.getTotalCounted(),
						allWords.itemSet().size(), clrWords.getTotalCounted(), clrWords.itemSet().size());
			}

			csvPrinter.flush();
		}
	}
}
