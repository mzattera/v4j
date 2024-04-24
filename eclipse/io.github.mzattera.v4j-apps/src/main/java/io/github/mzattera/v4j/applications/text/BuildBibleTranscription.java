/**
 * 
 */
package io.github.mzattera.v4j.applications.text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mzattera.v4j.util.FileUtil;
import io.github.mzattera.v4j.util.ResourceUtil;

/**
 * This class takes the XML files for the Bible transcription in different
 * languages and transforms them in .TXT files.
 * 
 * Source files are taken from: http://christos-c.com/bible/
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class BuildBibleTranscription {


	/// MAKE SURE THIS IS CORRECT BUT DO NOT USE RESOURCE FOLDER AS IT IS READ
	/// ONLY
	private final static String OUTPUT_FOLDER = "D:\\";

	private final static Pattern VERSE_PATTERN = Pattern
			.compile("<seg id=[\"'][^'\"]*[\"'] type=[\"']verse[\"']>([^<]+)</seg>");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			convert(ResourceUtil.getResourceFile("Transcriptions/Bible/English.xml"));
			convert(ResourceUtil.getResourceFile("Transcriptions/Bible/French.xml"));
			convert(ResourceUtil.getResourceFile("Transcriptions/Bible/German.xml"));
			convert(ResourceUtil.getResourceFile("Transcriptions/Bible/Italian.xml"));
			convert(ResourceUtil.getResourceFile("Transcriptions/Bible/Latin.xml"));
			convert(ResourceUtil.getResourceFile("Transcriptions/Bible/Spanish.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	private static void convert(File f) throws IOException {

		// read the text in the file
		List<String> lines = FileUtil.read(f, "UTF-8");
		String xml = String.join("\n", lines);

		// find verses in the XML
		Matcher m = VERSE_PATTERN.matcher(xml);
		List<String> txt = new ArrayList<>();
		while (m.find()) {
			String s = m.group(1).trim();
			if (s.length() > 0)
				txt.add(s);
		}

		// write the result as a simple file
		File out = new File (OUTPUT_FOLDER, f.getName().replace(".xml", ".txt"));
		FileUtil.write(txt, out.getCanonicalPath(), "UTF-8");
	}
}