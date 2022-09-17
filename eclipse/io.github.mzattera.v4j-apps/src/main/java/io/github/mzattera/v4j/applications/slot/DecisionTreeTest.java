package io.github.mzattera.v4j.applications.slot;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffPage;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.PageHeader;
import io.github.mzattera.v4j.text.ivtff.ParseException;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;

/**
 * This verified the decision tree to classify pages, based on features extracted by Extract GrammaticalFeatures.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class DecisionTreeTest {

	/**
	 * Which transcription to use.
	 */
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

	/**
	 * Which transcription type to use.
	 */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.MAJORITY;

	private static final Path OUTPUT_FILE_CLUSTER = Path.of("D:\\ClusterPrediction.csv");

	private DecisionTreeTest() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			testVoynichRules();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("\nCompleted.");
		}
	}

	private static void testVoynichRules() throws IOException, ParseException, URISyntaxException {
		
		// Prints configuration parameters
		System.out.println("Transcription     : " + TRANSCRIPTION);
		System.out.println("Transcription Type: " + TRANSCRIPTION_TYPE);
		System.out.println();

		IvtffText voynich = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, Alphabet.SLOT);

		try (CSVPrinter csvPrinter = new CSVPrinter(Files.newBufferedWriter(OUTPUT_FILE_CLUSTER), CSVFormat.DEFAULT);) {

			csvPrinter.printRecord("Page", "TargetLanguage", "PredictedLanguage", "CorrectLanguage", "TargetCluster",
					"PredictedCluster", "CorrectCluster", "Illustration", "IllustrationAndLanguage", "Quire",
					"Parchment", "Tokens");

			float totPages = 0, correctCluster = 0;
			for (IvtffPage p : voynich.getElements()) {

				PageHeader dsc = p.getDescriptor();
				String predictedCluster = null, predictedLanguage = null;

				if (!dsc.getCluster().equals("?"))
					++totPages;

				if (ExtractGrammaticalFeatures.calculatePercentMatch(p, "-ed-") <= 0.02)  
					predictedLanguage = "A";
				else
					predictedLanguage = "B";
				
				// 92% correct 164 OK, 14 NOK ->      Used in the article for Voynich conference
//				if (ExtractGrammaticalFeatures.calculatePercentMatch(p, "-edy") <= 0.02) { // Language A
//					if (ExtractGrammaticalFeatures.calculatePercentMatch(p, "-eo-") <= 0.14)
//						predictedCluster = "HA";
//					else if (ExtractGrammaticalFeatures.calculatePercentMatch(p, "-t-") <= 0.10)
//						predictedCluster = "PA";
//					else
//						predictedCluster = "HA";
//				} else { // Language B
//					if (ExtractGrammaticalFeatures.calculatePercentMatch(p, "so-") > 0) {
//						if (ExtractGrammaticalFeatures.calculatePercentMatch(p, "-eo-") <= 0.04)
//							predictedCluster = "BB";
//						else
//							predictedCluster = "SB";
//					} else {
//						predictedCluster = "HB";
//					}
//				}

				// Accuracy: 92.134834%
				if (ExtractGrammaticalFeatures.calculatePercentMatch(p, "-ed-") <= 0.02) { // Language A
					if (ExtractGrammaticalFeatures.calculatePercentMatch(p, "-eo-") > 0.14) {
						if (ExtractGrammaticalFeatures.calculatePercentMatch(p, "T-") > 0.04)
							predictedCluster = "HA";
						else
							predictedCluster = "PA";
					} else
						predictedCluster = "HA";
				} else { // Language B
					if (ExtractGrammaticalFeatures.calculatePercentMatch(p, "-so-") > 0.00) {
						if (ExtractGrammaticalFeatures.calculatePercentMatch(p, "-eo-") > 0.04)
							predictedCluster = "SB";
						else
							predictedCluster = "BB";
					} else
						predictedCluster = "HB";
				}

				if (dsc.getCluster().equals(predictedCluster))
					++correctCluster;
				csvPrinter.printRecord(dsc.getId(), dsc.getLanguage(), predictedLanguage,
						dsc.getLanguage().equals(predictedLanguage), dsc.getCluster(), predictedCluster,
						dsc.getCluster().equals(predictedCluster), dsc.getIllustrationType(),
						dsc.getIllustrationAndLanguage(), dsc.getQuire(), dsc.getParchment(),
						p.getWords(true).getTotalCounted());

			} // for each page

			csvPrinter.flush();
			System.out.println("Accuracy: " + correctCluster / totPages * 100 + "%");
		} // try()
	}
}
