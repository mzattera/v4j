/**
 * 
 */
package io.github.mzattera.v4j.text.txt;

import java.io.IOException;
import java.net.URISyntaxException;

import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.util.FileUtil;

/**
 * Factory class to read different language versions of the Bible. The documents
 * are read from TXT files in resource folder.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class BibleFactory {

	/**
	 * Languages in which the bible is available.
	 */
	public static final String[] LANGUAGES = { "Italian", "Latin", "German", "French", "English" };

	/**
	 * Name of folder with transcriptions (inside resource folder), including
	 * trailing separator
	 */
	public static final String TRANSCRIPTION_FOLDER = "Transcriptions/Bible/";

	/**
	 * Returns a version of the Bible in given language.
	 * 
	 * @return given transcription type for the MZ transcription
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static TextFile getDocument(String language) throws IOException, URISyntaxException {
		return new TextFile(FileUtil.getResourceFile(TRANSCRIPTION_FOLDER + language + ".txt"), Alphabet.UTF_16,
				"UTF-8");
	}
}