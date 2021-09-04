/**
 * 
 */
package io.github.mattera.v4j.support;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mattera.v4j.util.FileUtil;

/**
 * This class takes the XML files for the Bible transcription in different
 * languages and transforms them in .TXT files.
 * 
 * Source files are taken from: http://christos-c.com/bible/
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class BuildBibleTranscription {

	private static final File FOLDER = new File(
			"D:\\Users\\mzatt\\Projects\\Git - v4j\\v4j\\io.github.mattera.v4j\\src\\main\\resources\\Transcriptions\\Bible");

	private final static Pattern VERSE_PATTERN = Pattern
			.compile("<seg id=[\"'][^'\"]*[\"'] type=[\"']verse[\"']>([^<]+)</seg>");

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// We use regex to parse the XML file, for simplicity.
			for (File f : FOLDER.listFiles(new FilenameFilter() { // for each .xml file in FOLDER

				@Override
				public boolean accept(File f, String name) {
					return name.endsWith(".xml");
				}
			})) {

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
				FileUtil.write(txt, f.getCanonicalPath().replaceAll(".xml", ".txt"), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

}
