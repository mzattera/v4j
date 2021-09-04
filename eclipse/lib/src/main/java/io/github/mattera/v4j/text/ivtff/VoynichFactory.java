/**
 * 
 */
package io.github.mattera.v4j.text.ivtff;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import io.github.mattera.v4j.text.alphabet.Alphabet;

/**
 * Factory class to read different versions of the Voynich manuscript. The
 * documents are read from IVTFF files in resource folder.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class VoynichFactory {

	/**
	 * Name of folder with transcriptions (inside resource folder)
	 */
	public static final String TRANSCRIPTION_FOLDER = "Transcriptions";

	/**
	 * Name of the original interlinear file (+ corrections) inside resource folder.
	 */
	public static final String LSI_TRANSCRIPTION_FILE_NAME = "LSI_ivtff_0d_fix.txt";

	/**
	 * Name of the interlinear file including majority & concordance versions inside
	 * resource folder.
	 */
	public static final String MZ_TRANSCRIPTION_FILE_NAME = "Interlinear_ivtff_1.5.txt";

	/**
	 * Letter used as transcriber in interlinear files for lines that contain the
	 * majority version.
	 */
	public static final String MAJORITY_TRANSCRIBER = "m";

	/**
	 * Letter used as transcriber in interlinear files for lines that contain the
	 * concordance version.
	 */
	public static final String CONCORDANCE_TRANSCRIBER = "c";

	/**
	 * The different transcriptions we have available.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 * 
	 */
	// TODO check if any of these can be merged with LSI
	public enum Transcription {
		FSG, // A copy of the FSG transcription, created by Friedman's First Study Group, in
				// the format prepared by Jim Reeds and Jacques Guy. FSG alphabet.
		CD, // The original transcription of D'Imperio and Currier. Currier alphabeth
		// Vnow The updated version of this file, made during the earliest years of the
		// Voynich MS mailing list. Currier >>Link Temporarily unavailable ! (14)
		TT, // The original transcription by Takeshi Takahashi. Eva Alphabeth
		LSI, // The Landini-Stolfi Interlinear file. Eva alphabeth
		GC, // The v101 transcription file. v101 alphabeth.
		ZL, // The "Zandbergen" part of the LZ transcription effort. Eva alphabeth.
		MZ, // LSI interlinear file augmented with majority and concordance versions.
	}

	/**
	 * Different versions of transcriptions.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 * 
	 */
	public enum TranscriptionType {
		// TODO? rename?
		DEFAULT, // the original document, as it is
		MAJORITY, // return only the majority lines form an interlinear version; that is the
					// letters that appear in most of the transcriptions
		CONCORDANCE // return only the concordance lines form an interlinear version; that is
					// characters that exactly match in each transcription
	}

	/**
	 * 
	 * @return given transcription type for the MZ transcription
	 */
	public static IvtffText getDocument(TranscriptionType type) throws IOException, ParseException, URISyntaxException {
		return getDocument(Transcription.MZ, type, null);
	}

	/**
	 * 
	 * @return given transcription.
	 */
	public static IvtffText getDocument(Transcription t, TranscriptionType type)
			throws IOException, ParseException, URISyntaxException {
		return getDocument(t, type, null);
	}

	/**
	 * 
	 * @return given transcription.
	 */
	public static IvtffText getDocument(Transcription t, TranscriptionType type, Alphabet a)
			throws IOException, ParseException, URISyntaxException {

		IvtffText result = new IvtffText(getFile(t, a), "ASCII");
		switch (type) {
		case MAJORITY:
			return result.filterLines(new LineFilter.Builder().transcriber(MAJORITY_TRANSCRIBER).build());
		case CONCORDANCE:
			return result.filterLines(new LineFilter.Builder().transcriber(CONCORDANCE_TRANSCRIBER).build());
		case DEFAULT:
			return result;
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 
	 * @return an handler to the file with given transcription.
	 */
	public static File getFile(Transcription t, Alphabet a) throws IOException, ParseException, URISyntaxException {

		URL url = ClassLoader.getSystemResource(getDocumentFileName(t, a));
		return new File(url.toURI());
	}

	/**
	 * 
	 * @param a
	 *            Alphabet used in the document, use null for default alphabet for
	 *            given transcription.
	 * 
	 * @return file name for given transcription of the Voynich text.
	 */
	private static String getDocumentFileName(Transcription t, Alphabet a) {

		StringBuffer fName = new StringBuffer(TRANSCRIPTION_FOLDER).append("/");

		if (t == Transcription.LSI) {
			// TODO spiegare perche' usiamo queta versione (fix) e documentare le linee che
			// sono
			// state cambiate
			fName.append(LSI_TRANSCRIPTION_FILE_NAME);
			if ((a != null) && (a != Alphabet.EVA))
				throw new IllegalArgumentException("Unsupported alphabet " + a + " for transcription " + t);
		} else if (t == Transcription.MZ) {
			fName.append(MZ_TRANSCRIPTION_FILE_NAME);
			if ((a != null) && (a != Alphabet.EVA))
				throw new IllegalArgumentException("Unsupported alphabet " + a + " for transcription " + t);
		} else
			throw new IllegalArgumentException("Unsupported transcription: " + t); // TODO add support for all other
																					// transcriptions

		return fName.toString();
	}
}