/**
 * 
 */
package io.github.mattera.v4j.text.txt;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import io.github.mattera.v4j.text.alphabet.Alphabet;

/**
 * Factory class to read different language versions of the Bible. The
 * documents are read from TXT files in resource folder.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class BibleFactory {

	/**
	 * Languages in which the bible is available.
	 */
	public static final String[] LANGUAGES = {"Italian", "Latin", "German", "French"};
	
	/**
	 * Name of folder with transcriptions (inside resource folder)
	 */
	public static final String TRANSCRIPTION_FOLDER = "Transcriptions\\Bible";

	/**
	 * Returns a version of the Bible in given language.
	 * 
	 * @return given transcription type for the MZ transcription
	 * @throws IOException 
	 * @throws URISyntaxException 
	 */
	public static TextFile getDocument(String language) throws IOException, URISyntaxException {
		URL url = ClassLoader.getSystemResource(TRANSCRIPTION_FOLDER + "/" + language+".txt");
		return new TextFile(new File(url.toURI()), Alphabet.UTF_16, "UTF-8");
	}
}