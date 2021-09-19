package io.github.mattera.v4j.applications.text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;

import io.github.mattera.v4j.text.alphabet.Alphabet;
import io.github.mattera.v4j.text.ivtff.IvtffLine;
import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.ParseException;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mattera.v4j.util.FileUtil;

/**
 * Processes an interlinear transcription to create concordance and majority
 * versions of it (added as new "artificial" transcribers).
 * 
 * STATUS: Working & with (some) test harness.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public final class BuildConcordanceVersion {

	/// MAKE SURE THIS IS CORRECT BUT DO NOT USE RESOURCE FOLDER AS IT IS READ
	/// ONLY
	private final static String OUTPUT_FOLDER = "D:\\";
	 
	public final static File INPUT_FILE = FileUtil.getResourceFile("Transcriptions/LSI_ivtff_0d_fixed.txt");

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
//			URL url = ClassLoaderUtil.getResource("/tmp.txt", BuildConcordanceVersion.class);
//			File fIn = new File(url.toURI());
			File fOut = new File(OUTPUT_FOLDER, VoynichFactory.AUGMENTED_TRANSCRIPTION_FILE_NAME);

			doWork(INPUT_FILE, fOut, "ASCII", Alphabet.EVA);
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
	 * @param fIn      Input file with interlinear transcription.
	 * @param fOut     Output file.
	 * @param encoding Encoding used in files.
	 * @param a        Alphabet for the transcription.
	 */
	private static void doWork(File fIn, File fOut, String encoding, Alphabet a) throws IOException, ParseException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fIn), encoding))) {
			try (BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(fOut), encoding))) {
				doWork(in, out);
			}
		}
	}

	/**
	 * Processes an interlinear file and adds a majority and a concordance version.
	 * This is made available at package scope, so it is accessible to test classes.
	 * 
	 * @param in  A Reader containing the input document with interlinear
	 *            transcription.
	 * @param out A Writer where output is written.
	 * @param a   Alphabet for the transcription.
	 */
	static void doWork(BufferedReader in, BufferedWriter out) throws IOException, ParseException {

		String groupId = null;
		List<IvtffLine> group = new ArrayList<>(); // group of lines being processed

		try (in; out) {

			String fLine = null;
			IvtffLine line = null;
			int lnum = 0;

			// Parse file header
			fLine = in.readLine();
			if (fLine == null)
				throw new ParseException("Empty input.");
			++lnum;

			Matcher m = IvtffText.FILE_HEADER_PATTERN.matcher(fLine);
			if (!m.matches())
				throw new ParseException("Invalid file header: ", fLine);

			Alphabet a = Alphabet.getAlphabet(m.group(1));
			if (a == null)
				new ParseException("Unsupported alphabeth: " + m.group(1));

			// Write header
			out.write(fLine);
			out.newLine();
			
			DateFormat f = new SimpleDateFormat("yyyyMMdd.HHmm");
			out.write("# Created automatically by " + BuildConcordanceVersion.class.getName() + " on: " + f.format(new Date()));
			out.newLine();
			out.write("#");
			out.newLine();			
			
			while ((fLine = in.readLine()) != null) {
				++lnum;
				line = null;
				try {
					line = new IvtffLine(fLine, lnum, a);
				} catch (ParseException e) {
					// this is not a line

					// process current group and write it, as the non-line will start another group
					// anyway
					processGroup(group, out);
					group.clear();

					// Write current text
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

	/**
	 * 
	 * @param ivtffLine
	 * @return a group Id, which is used to group version of same line form
	 *         different transcribers.
	 */
	public static String getGroupId(IvtffLine line) {
		return line
				.getDescriptor().getPageId() + "." + line.getDescriptor().getNumber() + ","
				+ line.getDescriptor().getLocus();
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
//		IvtffLine ml = IvtffLine.merge(group, TranscriptionType.MAJORITY);
		// String m = ml.getPlainText();
		out.write(IvtffLine.merge(group, TranscriptionType.MAJORITY).toString());
		out.newLine();
//		IvtffLine cl = IvtffLine.merge(group, TranscriptionType.CONCORDANCE);
		out.write(IvtffLine.merge(group, TranscriptionType.CONCORDANCE).toString());
		out.newLine();

		// TODO REMOVE, test only
		/*
		 * String l = null; for (IvtffLine line : group) { if
		 * (line.getPlainText().length() == m.length()) { l = line.getPlainText();
		 * break; } } if ((l == null) || (StringUtil.countMatchingChars(m, l) <
		 * l.length() * 0.5)) { for (IvtffLine line : group) {
		 * System.out.println(line.toString()); } System.out.println(ml.toString());
		 * System.out.println(cl.toString()); }
		 */ }
}