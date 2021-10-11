/**
 * 
 */
package io.github.mzattera.v4j.text.txt;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.github.mzattera.v4j.text.CompositeText;
import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.util.FileUtil;

/**
 * This represents a text file. It is composed by lines of text.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class TextFile extends CompositeText<TextLine> {

	private final String id;

	@Override
	public String getId() {
		return id;
	}

	public TextFile(String fileName) throws IOException {
		this(fileName, Alphabet.UTF_16, "UTF-8");
	}

	public TextFile(String fileName, String encoding) throws IOException {
		this(fileName, Alphabet.UTF_16, encoding);
	}

	public TextFile(String fileName, Alphabet a, String encoding) throws IOException {
		this(new File(fileName), a, encoding);
	}

	public TextFile(File file) throws IOException {
		this(file, Alphabet.UTF_16, "UTF-8");
	}

	public TextFile(File file, String encoding) throws IOException {
		this(file, Alphabet.UTF_16, encoding);
	}

	public TextFile(File file, Alphabet a, String encoding) throws IOException {
		super(a);
		id = file.getCanonicalPath();
		
		List<String> lines = FileUtil.read(file, encoding);

		for (int i = 0; i < lines.size(); ++i) {
			addElement(new TextLine(i, lines.get(i)));
		}
	}
}
