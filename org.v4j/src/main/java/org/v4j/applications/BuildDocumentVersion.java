package org.v4j.applications;

import java.util.ArrayList;
import java.util.List;

import org.v4j.text.ivtff.IvtffLine;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.ManuscriptFactory;
import org.v4j.text.ivtff.ManuscriptFactory.Transcription;
import org.v4j.text.ivtff.ManuscriptFactory.TranscriptionType;

/**
 * Processes interlinear version to obtain different versions with different
 * degree of concordance between transcribers.
 * 
 * It also creates some text based on Notes004.
 * 
 * It saves the results both as text & HTML.
 * 
 * STATUS: Working & with test harness.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public class BuildDocumentVersion {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		try {
			doWork(ManuscriptFactory.getDocument(Transcription.LSI), TranscriptionType.CONCORDANCE);

//			URL url = ClassLoader.getSystemResource("MS/LSI_ivtff_0d2 - fixed.txt");
//			File inFile = new File(url.toURI());
//			doWork(new IvtffText(inFile), TranscriptionType.CONCORDANCE);

			// TODO write result & do all other transcription types

		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(-1);
		}
	}

	// TODO Comments
	private static IvtffText doWork(IvtffText document, TranscriptionType type) {
		if (!document.isInterlinear())
			throw new IllegalArgumentException("Transcription must ber interlinear.");

		List<IvtffLine> lines = document.getLines();
		if (lines.size() == 0)
			return new IvtffText(document, lines);

		List<IvtffLine> result = new ArrayList<>();
		List<IvtffLine> group = new ArrayList<>(); // group of lines being processed

		int i = 0;
		String groupId = null;
		while (i < lines.size()) {

			if (groupId == null) // start of a new group
				groupId = getGroupId(lines.get(i));

			// Take all lines in current group, that is all lines with same page & number
			while ((i < lines.size()) && getGroupId(lines.get(i)).equals(groupId)) {
				group.add(lines.get(i++));
			}

			result.add(process(group, type));
			group.clear();
			groupId = null;
		} // while we have lines in input

		// TODO
		return null;
	}

	private static IvtffLine process(List<IvtffLine> group, TranscriptionType type) {
		// System.out.println(getGroupId(group.get(0)));
		// for (IvtffLine line : group) {
		// System.out.println("\t" + line);
		// }

		int l = -1;
		for (IvtffLine line : group) {
			// String txt = line.getText().replaceAll("<!@.>", "?");
			String txt = line.getText();
//			 String txt = line.getText().replaceAll("<[^>]*>", "!").replaceAll("!+", "!");

			if (l == -1)
				l = txt.length();
			else if (txt.length() != l)
				System.out.println(line);
		}

		return null;
	}

	/**
	 * 
	 * @param ivtffLine
	 * @return a group Id, which is used to group version of same line form
	 *         different transcribers.
	 */
	private static String getGroupId(IvtffLine line) {
		return line.getDescriptor().getPageId() + "." + line.getDescriptor().getNumber();
	}

}