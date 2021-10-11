package io.github.mzattera.v4j.applications.text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;

import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffLine;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.ParseException;
import io.github.mzattera.v4j.text.ivtff.VoynichFactory;
import io.github.mzattera.v4j.util.FileUtil;

/**
 * Processes an interlinear transcription to create concordance and majority
 * versions of it (added as new "artificial" transcribers). In addition, this
 * transliterates the text into Slot alphabet.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public final class BuildSlotVersion {

	/// MAKE SURE THIS IS CORRECT BUT DO NOT USE RESOURCE FOLDER AS IT IS READ
	/// ONLY
	private final static File OUTPUT_FILE = new File("D:\\", VoynichFactory.AUGMENTED_SLOT_TRANSCRIPTION_FILE_NAME);

	public final static File INPUT_FILE = FileUtil
			.getResourceFile(VoynichFactory.TRANSCRIPTION_FOLDER + VoynichFactory.AUGMENTED_TRANSCRIPTION_FILE_NAME);

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {

			// This will contain file translated into Slot alphabet
			StringBuffer slot = new StringBuffer();
			try (BufferedReader in = new BufferedReader(
					new InputStreamReader(new FileInputStream(INPUT_FILE), "ASCII"))) {

				int lnum = 0;
				IvtffLine line = null;
				String fLine = null;

				// Parse file header
				fLine = in.readLine();
				if (fLine == null)
					throw new ParseException("Empty input.");
				++lnum;

				Matcher m = IvtffText.FILE_HEADER_PATTERN.matcher(fLine);
				if (!m.matches())
					throw new ParseException("Invalid file header: ", fLine);

				if (!m.group(1).equals(Alphabet.EVA.getCodeString()))
					throw new ParseException("Unsupported alphabeth: " + m.group(1));
				if (!m.group(2).equals("1.5"))
					throw new ParseException("Unsupported IVTFF format version: " + m.group(2));

				// Write header
				slot.append("#=IVTFF " + Alphabet.SLOT.getCodeString() + " 1.5\n");
				DateFormat f = new SimpleDateFormat("yyyyMMdd.HHmm");
				slot.append("# Transliterated automatically by ").append(BuildSlotVersion.class.getName())
						.append(" on: ").append(f.format(new Date())).append("\n");

				while ((fLine = in.readLine()) != null) {
					++lnum;

					m = IvtffText.LOCUS_IDENTIFIER_PATTERN.matcher(fLine);
					if (!m.find() || (m.start() != 0)) {
						// not a line of text, continue
						slot.append(fLine).append("\n");
						continue;
					}

					// Transcribe into Slot alphabet
					line = new IvtffLine(fLine, lnum, Alphabet.EVA);
					IvtffLine sLine = new IvtffLine(line.getDescriptor(), SlotAlphabet.fromEva(line.getText()),
							Alphabet.SLOT);
					slot.append(sLine).append("\n");
				}
			}

			// Save result
			FileUtil.write(slot.toString(), OUTPUT_FILE.getCanonicalPath());

		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(-1);
		} finally {
			System.out.println("Completed.");
		}
	}
}