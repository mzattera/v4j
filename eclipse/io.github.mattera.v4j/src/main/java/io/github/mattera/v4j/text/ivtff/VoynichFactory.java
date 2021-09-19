/**
 * 
 */
package io.github.mattera.v4j.text.ivtff;

import java.io.IOException;
import java.net.URISyntaxException;

import io.github.mattera.v4j.text.alphabet.Alphabet;
import io.github.mattera.v4j.util.FileUtil;

/**
 * Factory class to read different versions of the Voynich manuscript. The
 * documents are read from IVTFF files in resource folder.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class VoynichFactory {

	/**
	 * Name of folder with transcriptions (inside resource folder), including
	 * trailing separator.
	 */
	public static final String TRANSCRIPTION_FOLDER = "Transcriptions/";

	/**
	 * Name of the original Landini-Stolfi Interlinear file inside
	 * TRANSCRIPTION_FOLDER.
	 */
	public static final String LSI_TRANSCRIPTION_FILE_NAME = "LSI_ivtff_0d.txt";

	/**
	 * Name of the interlinear file including majority & concordance versions inside
	 * TRANSCRIPTION_FOLDER.
	 */
	public static final String AUGMENTED_TRANSCRIPTION_FILE_NAME = "Interlinear_ivtff_1.5.txt";

	/**
	 * Name of the Slot interlinear file including majority & concordance versions
	 * inside TRANSCRIPTION_FOLDER.
	 */
	public static final String AUGMENTED_SLOT_TRANSCRIPTION_FILE_NAME = "Interlinear_slot_ivtff_1.5.txt";

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
		AUGMENTED, // LSI interlinear file augmented with majority and concordance versions.
	}

	/**
	 * Different versions of transcriptions, this applies to interlinear documents.
	 * 
	 * @author Massimiliano "Maxi" Zattera
	 * 
	 */
	public enum TranscriptionType {
		INTERLINEAR, // the full interlinear document, as it is, with all transcribers
		MAJORITY, // return only the majority lines form an interlinear version; that is the
					// letters that appear in most of the available transcriptions.
					// This is equivalent to filtering lines by MAJORITY_TRANSCRIBER.
		CONCORDANCE // return only the concordance lines form an interlinear version; that is
					// characters that exactly match in each of the available transcriptions.
					// This is equivalent to filtering lines by CONCORDANCE_TRANSCRIBER.
	}

	/**
	 * 
	 * @return given transcription of the Voynich.
	 */
	public static IvtffText getDocument(Transcription t) throws IOException, ParseException, URISyntaxException {
		return getDocument(t, TranscriptionType.INTERLINEAR, null);
	}

	/**
	 * 
	 * @return given transcription type for the Transcription.AUGMENTED
	 *         transcription of the Voynich.
	 */
	public static IvtffText getDocument(TranscriptionType type) throws IOException, ParseException, URISyntaxException {
		return getDocument(Transcription.AUGMENTED, type, null);
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
	 * @return given transcription and type of the Voynich, using the given
	 *         alp0habet.
	 */
	public static IvtffText getDocument(Transcription t, TranscriptionType type, Alphabet a)
			throws IOException, ParseException, URISyntaxException {

		switch (t) {
		case LSI:
			if (type != TranscriptionType.INTERLINEAR)
				throw new IllegalArgumentException(
						"Unsupported transcription type " + type + " for transcription " + t);
			if ((a != null) && (a != Alphabet.EVA))
				throw new IllegalArgumentException("Unsupported alphabet " + a + " for transcription " + t);

			return new IvtffText(FileUtil.getResourceFile(TRANSCRIPTION_FOLDER + LSI_TRANSCRIPTION_FILE_NAME));

		case AUGMENTED:
			IvtffText result;

			if ((a == null) || (a == Alphabet.EVA))
				result = new IvtffText(
						FileUtil.getResourceFile(TRANSCRIPTION_FOLDER + AUGMENTED_TRANSCRIPTION_FILE_NAME));
			else if (a == Alphabet.SLOT)
				result = new IvtffText(
						FileUtil.getResourceFile(TRANSCRIPTION_FOLDER + AUGMENTED_SLOT_TRANSCRIPTION_FILE_NAME));
			else
				throw new IllegalArgumentException("Unsupported alphabet " + a + " for transcription " + t);

			switch (type) {
			case MAJORITY:
				return result.filterLines(new LineFilter.Builder().transcriber(MAJORITY_TRANSCRIBER).build());
			case CONCORDANCE:
				return result.filterLines(new LineFilter.Builder().transcriber(CONCORDANCE_TRANSCRIBER).build());
			case INTERLINEAR:
				return result;
			default:
				throw new IllegalArgumentException(
						"Unsupported transcription type " + type + " for transcription " + t);
			}
		default:
			throw new IllegalArgumentException("Unsupported transcription " + t);
		}
	}
}