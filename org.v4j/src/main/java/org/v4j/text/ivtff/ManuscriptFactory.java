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
 * @author mzatt
 *
 */
public class ManuscriptFactory {

	/**
	 * The different transcriptions we have available.
	 * 
	 * @author maxi
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
	 * @author maxi
	 * 
	 */
	// TODO probably this can be a line in the interlinear and removed
	public enum TranscriptionType {
		DEFAULT, // Indicates the default type fro the transcription (the original version
					// without any of the below transformations)
		MAJORITY, // from interlinear, take the letters that appear with highest frequency
		PROOFREAD, // Same as majority, but at least 2 version of the text must be available
		CONCORDANCE // only return characters that match in each transcription
	}

	/**
	 * Different type of contents.
	 * 
	 * @author maxi
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
		URL url = ClassLoader.getSystemResource(getDocumentFileName(tt, v, c, a, ".txt"));
		File inFile = new File(url.toURI());

		return new IvtffText(inFile, "ASCII");
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

		StringBuffer fName = new StringBuffer("MS/");

		if (tt == Transcription.LSI) {
			fName.append("LSI_ivtff_0d");
			if ((a != null) && (a != Alphabet.EVA))
				fName.append(a.getCodeString());
		} else
			throw new IllegalArgumentException("Unsupported transcription: " + tt); // TODO add support for all other
																					// transcriptions

		if (v != TranscriptionType.DEFAULT) {
			fName.append(".").append(v.toString());
		}

		if (c != ContentType.COMPLETE) {
			fName.append(".").append(c.toString());
		}

		fName.append(ext);
		return fName.toString();
	}

	/**
	 * Write this Document as a specific version of the Voynich.
	 * 
	 * @throws Exception
	 */
	public void write(IvtffText doc, Transcription tt, TranscriptionType v, ContentType c) throws Exception {
		URL url = ClassLoader.getSystemResource(getDocumentFileName(tt, v, c, doc.getAlphabet(), ".txt"));
		File outFile = new File(url.toURI());

		doc.write(outFile, "ASCII");
	}

}
