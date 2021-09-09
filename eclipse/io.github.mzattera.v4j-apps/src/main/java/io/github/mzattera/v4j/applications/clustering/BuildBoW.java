/**
 * 
 */
package io.github.mzattera.v4j.applications.clustering;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import io.github.mattera.v4j.text.ivtff.IvtffPage;
import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.PageHeader;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;
import io.github.mattera.v4j.util.clustering.BagOfWords;

/**
 * This class builds Bag Of Words out of the Voynich and generates some TSV
 * files that can be used for visualisation on https://projector.tensorflow.org/
 * 
 * This class can work at page or parchment (bifolio)
 * level, this behavior is configurable (see ClusteringConfiguration class).
 * 
 * @author Massimiliano_Zattera
 *
 */
public final class BuildBoW {

	private BuildBoW() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ClusteringConfiguration.print();

			// The document to process
			IvtffText doc = VoynichFactory.getDocument(ClusteringConfiguration.TRANSCRIPTION, ClusteringConfiguration.TRANSCRIPTION_TYPE);

			// Remove outlier pages 
			doc = doc.filterPages(ClusteringConfiguration.OUTLIER_FILTER);

			// Create BoW for the elements in the document, split accordingly to
			// DOCUMENT_SPLITTER.
			// Our embedding dimensions for the BoW will be the words in the text.
			List<BagOfWords> bow = BagOfWords.toBoW(doc.splitPages(ClusteringConfiguration.DOCUMENT_SPLITTER).values(),
					BagOfWords.buildDimensions(doc), ClusteringConfiguration.BOW_MODE);

			//////////////////////////////////////////////////////

			saveDimensions(bow);
			saveMetadata(bow);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	private static void saveDimensions(List<BagOfWords> bow) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ClusteringConfiguration.OUTPUT_FOLDER, "vectors.tsv"));
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withDelimiter('\t'));) {

			for (BagOfWords b : bow) {
				for (double d : b.getPoint())
					csvPrinter.print(d);
				csvPrinter.println();
			}

			csvPrinter.flush();
		}
	}

	private static void saveMetadata(List<BagOfWords> bow) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ClusteringConfiguration.OUTPUT_FOLDER, "metadata.tsv"));
				CSVPrinter csvPrinter = new CSVPrinter(writer,
						CSVFormat.DEFAULT.withDelimiter('\t').withHeader("ID", "IllustrationType", "Language",
								"Cluster", "Hand", "Quire", "Parchment", "Illustration + Language"));) {

			for (BagOfWords b : bow) {
				IvtffText txt = (IvtffText) b.getText();
				PageHeader ph = txt.getElements().get(0).getDescriptor();
				

				// TODO this is really ad hoc....should be made more generic
				String id = null;
				String illustration = null;
				String language = null;
				if (ClusteringConfiguration.DOCUMENT_SPLITTER == ClusteringConfiguration.SPLIT_BY_PARCHMENTS) {
					id = ph.getParchment() + "";
					illustration = "T";
					for (IvtffPage page : txt.getElements()) {
						illustration = page.getDescriptor().getIllustrationType();
						if (!illustration.equals("T")) break;
					}
					language = "?";
					for (IvtffPage page : txt.getElements()) {
						language = page.getDescriptor().getLanguage();
						if (!language.equals("?")) break;
					}
				} else {
					id = ph.getId();
					illustration = ph.getIllustrationType();
					language = ph.getLanguage();
				}
				
				csvPrinter.printRecord(id, illustration, language, ph.getCluster(),
						ph.getHand(), ph.getQuire(), ph.getParchment(), illustration + language);
			}

			csvPrinter.flush();
		}
	}

}
