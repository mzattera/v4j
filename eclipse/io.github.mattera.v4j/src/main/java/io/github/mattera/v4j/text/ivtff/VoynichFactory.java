/**
 * 
 */
package io.github.mattera.v4j.text.ivtff;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map.Entry;

import io.github.mattera.v4j.text.alphabet.Alphabet;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mattera.v4j.util.Counter;

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
	public static final String LSI_TRANSCRIPTION_FILE_NAME = "LSI_ivtff_0d.txt";

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
	public enum Transcription {
		/*
		 * FSG, A copy of the FSG transcription, created by Friedman's First Study
		 * Group, in the format prepared by Jim Reeds and Jacques Guy. FSG alphabet.
		 * 
		 * CD, The original transcription of D'Imperio and Currier. Currier alphabet.
		 * 
		 * Vnow The updated version of this file, made during the earliest years of the
		 * Voynich MS mailing list.
		 * 
		 * TT, The original transcription by Takeshi Takahashi. Eva Alphabeth
		 * 
		 * GC, The v101 transcription file. v101 alphabeth.
		 * 
		 * ZL, The "Zandbergen" part of the LZ transcription effort. Eva alphabeth.
		 */
		LSI, // The Landini-Stolfi Interlinear file. Eva alphabeth. This includes FSG, CD and
				// early version of TT (IT).
		MZ // LSI interlinear file augmented with majority and concordance versions.
	}

	/**
	 * Different versions of transcriptions.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 * 
	 */
	public enum TranscriptionType {
		COMPLETE, // the original document, as it is
		MAJORITY, // return only the majority lines form an interlinear version; that is the
					// letters that appear in most of the available transcriptions.
		CONCORDANCE // return only the concordance lines form an interlinear version; that is
					// characters that exactly match in each of the available transcriptions.
	}

	/**
	 * 
	 * @return given transcription of the Voynich.
	 */
	public static IvtffText getDocument(Transcription t) throws IOException, ParseException, URISyntaxException {
		return getDocument(t, TranscriptionType.COMPLETE, null);
	}

	/**
	 * 
	 * @return given transcription type for the Transcription.MZ transcription of the Voynich.
	 */
	public static IvtffText getDocument(TranscriptionType type) throws IOException, ParseException, URISyntaxException {
		return getDocument(Transcription.MZ, type, null);
	}

	/**
	 * 
	 * @return given transcription and type of the Voynich.
	 */
	public static IvtffText getDocument(Transcription t, TranscriptionType type)
			throws IOException, ParseException, URISyntaxException {
		return getDocument(t, type, null);
	}

	/**
	 * 
	 * @return given transcription and type of the Voynich, using the given alp0habet.
	 */
	public static IvtffText getDocument(Transcription t, TranscriptionType type, Alphabet a)
			throws IOException, ParseException, URISyntaxException {

		StringBuffer fName = new StringBuffer(TRANSCRIPTION_FOLDER).append(File.separator);

		switch (t) {
		case LSI:
			if (type != TranscriptionType.COMPLETE)
				throw new IllegalArgumentException(
						"Unsupported transcription type " + type + " for transcription " + t);
			if ((a != null) && (a != Alphabet.EVA))
				throw new IllegalArgumentException("Unsupported alphabet " + a + " for transcription " + t);
			fName.append(LSI_TRANSCRIPTION_FILE_NAME);

			return new IvtffText(getFile(fName), "ASCII");
		case MZ:
			if ((a != null) && (a != Alphabet.EVA))
				throw new IllegalArgumentException("Unsupported alphabet " + a + " for transcription " + t);
			
			fName.append(MZ_TRANSCRIPTION_FILE_NAME);
			IvtffText result = new IvtffText(getFile(fName), "ASCII");
			
			switch (type) {
			case MAJORITY:
				return result.filterLines(new LineFilter.Builder().transcriber(MAJORITY_TRANSCRIBER).build());
			case CONCORDANCE:
				return result.filterLines(new LineFilter.Builder().transcriber(CONCORDANCE_TRANSCRIBER).build());
			case COMPLETE:
				return result;
			default:
				throw new IllegalArgumentException(
						"Unsupported transcription type " + type + " for transcription " + t);
			}
		default:
			throw new IllegalArgumentException("Unsupported transcription " + t);
		}
	}

	/**
	 * 
	 * @return an handler to the file with given name in the resource folder.
	 */
	private static File getFile(StringBuffer fileName) throws IOException, ParseException, URISyntaxException {

		// TODO move into FileUtil and use improved code
		URL url = ClassLoader.getSystemResource(fileName.toString());
		return new File(url.toURI());
	}
}