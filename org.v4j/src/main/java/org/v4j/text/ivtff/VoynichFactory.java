/**
 * 
 */
package org.v4j.text.ivtff;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.v4j.text.alphabet.Alphabet;

/**
 * Factory class to read and write different versions of the Voynich manuscript,
 * or part of it. The documents are read from IVTFF files in resource folder.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
// TODO lots to finalize
public class VoynichFactory {

	/**
	 * Name of folder with transcriptions (inside resource folder)
	 */
	public static final String TRANSCRIPTION_FOLDER = "Transcriptions";

	/**
	 * Name of the original interlinear file (+ corrections) inside
	 * resource folder.
	 */
	public static final String ORIGINAL_TRANSCRIPTION_FILE = "LSI_ivtff_0d_fix.txt";

	private static final String TRANSCRIPTION_FILE_NAME = "Interlinear_ivtff_1.5";

	/**
	 * Name of the interlinear file including majority & concordance
	 * versions inside resource folder.
	 */
	public static final String TRANSCRIPTION_FILE = TRANSCRIPTION_FILE_NAME + ".txt";

	/**
	 * The different transcriptions we have available.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 * 
	 */
	public enum Transcription {
		FSG, // A copy of the FSG transcription, created by Friedman's First Study Group, in
				// the format prepared by Jim Reeds and Jacques Guy. FSG alphabet.
		CD, // The original transcription of D'Imperio and Currier. Currier alphabeth
		// Vnow The updated version of this file, made during the earliest years of the
		// Voynich MS mailing list. Currier >>Link Temporarily unavailable ! (14)
		TT, // The original transcription by Takeshi Takahashi. Eva Alphabeth
		LSI, // The Landini-Stolfi Interlinear file. Eva alphabeth
		GC, // The v101 transcription file. v101 alphabeth.
		ZL // The "Zandbergen" part of the LZ transcription effort. Eva alphabeth.
	}

	/**
	 * Different versions of transcriptions.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 * 
	 */
	// TODO probably this can be a line in the interlinear and removed
	public enum TranscriptionType {
		DEFAULT, // Indicates the default type for the transcription (the original version
					// without any of the below transformations)
		// TODO? rename?
		MAJORITY, // from interlinear, take the letters that appear in most of the transcriptions
		// PROOFREAD, // Same as majority, but at least 2 version of the text must be
		// available
		// TODO verify if this is the right term
		CONCORDANCE // only return characters that exactly match in each transcription
	}

	/**
	 * Different type of contents.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 * 
	 */
	// TODO ...needed?
	public enum ContentType {
		COMPLETE // Complete text
	}

	/**
	 * Returns given transcription.
	 * 
	 * @throws URISyntaxException
	 * @throws ParseException
	 * @throws IOException
	 */
	public static IvtffText getDocument(Transcription tt) throws IOException, ParseException, URISyntaxException {
		return getDocument(tt, TranscriptionType.DEFAULT, ContentType.COMPLETE, null);
	}

	/**
	 * Returns given transcription.
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static IvtffText getDocument(Transcription tt, TranscriptionType v)
			throws IOException, ParseException, URISyntaxException {
		return getDocument(tt, v, ContentType.COMPLETE, null);
	}

	/**
	 * Returns given transcription.
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static IvtffText getDocument(Transcription tt, TranscriptionType v, ContentType c, Alphabet a)
			throws IOException, ParseException, URISyntaxException {
		return new IvtffText(getFile(tt, v, c, a), "ASCII");
	}

	/**
	 * Returns an handler to the file with given transcription.
	 */
	public static File getFile(Transcription tt, TranscriptionType v, ContentType c, Alphabet a)
			throws IOException, ParseException, URISyntaxException {

		URL url = ClassLoader.getSystemResource(getDocumentFileName(tt, v, c, a, ".txt"));
		return new File(url.toURI());
	}

	/**
	 * Returns file name for given transcription of the Voynich text.
	 * 
	 * @param a
	 *            Alphabet used in the document, use null for default alphabet for
	 *            given transcription.
	 */
	private static String getDocumentFileName(Transcription tt, TranscriptionType v, ContentType c, Alphabet a,
			String ext) {

		StringBuffer fName = new StringBuffer(TRANSCRIPTION_FOLDER).append("/");

		if (tt == Transcription.LSI) {
			// TODO spiegare perche' usiamo queta versione e documentare le linee che sono
			// state cambiate
			fName.append(TRANSCRIPTION_FILE_NAME);
			if ((a != null) && (a != Alphabet.EVA))
				fName.append('_').append(a.getCodeString());
		} else
			throw new IllegalArgumentException("Unsupported transcription: " + tt); // TODO add support for all other
																					// transcriptions

		if (v != TranscriptionType.DEFAULT) {
			fName.append('_').append(v.toString());
		}

		if (c != ContentType.COMPLETE) {
			fName.append('_').append(c.toString());
		}

		fName.append(ext);
		return fName.toString();
	}
}
