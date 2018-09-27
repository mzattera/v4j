package org.v4j.support;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.v4j.text.alphabet.Alphabet;
import org.v4j.text.ivtff.IvtffLine;
import org.v4j.text.ivtff.ParseException;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;

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
			URL url = ClassLoader.getSystemResource(VoynichFactory.TRANSCRIPTION_FOLDER);
			File fIn = new File(new File(url.toURI()), VoynichFactory.ORIGINAL_TRANSCRIPTION_FILE);
			File fOut = new File(new File("I:\\Voynich Mobile\\Git - v4j\\org.v4j\\src\\main\\resources\\Transcriptions"), VoynichFactory.TRANSCRIPTION_FILE);

			doWork(fIn, fOut, "ASCII", Alphabet.EVA);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(-1);
		} finally {
			System.out.println("Completed.");
		}
	}

	/**
	 * Processes an interlinear file and adds a majority and a concordance version.
	 * 
	 * @param fIn
	 *            Input file with interlinear transcription.
	 * @param fOut
	 *            Output file.
	 * @param encoding
	 *            Encoding used in files.
	 * @param a
	 *            Alphabet for the transcription.
	 */
	private static void doWork(File fIn, File fOut, String encoding, Alphabet a) throws IOException, ParseException {

		String groupId = null;
		List<IvtffLine> group = new ArrayList<>(); // group of lines being processed

		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fIn), encoding))) {
			try (BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(fOut), encoding))) {

				String fLine = null;
				IvtffLine line = null;
				int lnum = 0;

				while ((fLine = in.readLine()) != null) {
					++lnum;
					line = null;
					try {
						line = new IvtffLine(fLine, lnum, a);
					} catch (ParseException e) {
						// this is not a line; write e continue
						out.write(fLine);
						out.newLine();
						continue;
					}

					if ((groupId != null) && !groupId.equals(getGroupId(line))) {
						// new group: process current one
						processGroup(group, out);
						group.clear();
					}

					// store current line
					group.add(line);
					groupId = getGroupId(line);
				} // while we have lines in input

				// don't forget last group
				processGroup(group, out);
				
				out.flush();
			}
		}
	}

	/**
	 * 
	 * @param ivtffLine
	 * @return a group Id, which is used to group version of same line form
	 *         different transcribers.
	 */
	public static String getGroupId(IvtffLine line) {
		return line.getDescriptor().getPageId() + "." + line.getDescriptor().getNumber() + "," + line.getDescriptor().getLocus();
	}

	/**
	 * Build majority & concordance version for given rows and writes out the
	 * results.
	 */
	private static void processGroup(List<IvtffLine> group, BufferedWriter out) throws IOException, ParseException {
		if (group.size() == 0)
			return;

		// writes out current lines
		for (IvtffLine line : group) {
			out.write(line.toString());
			out.newLine();
		}

		// merges and writes out result
		out.write(IvtffLine.merge(group, TranscriptionType.MAJORITY).toString());
		out.newLine();
		out.write(IvtffLine.merge(group, TranscriptionType.CONCORDANCE).toString());
		out.newLine();
	}
}