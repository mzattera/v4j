package org.v4j.text.ivtff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.v4j.text.ElementFilter;
import org.v4j.text.ElementSplitter;
import org.v4j.text.Text;
import org.v4j.text.alphabet.Alphabet;

/**
 * This class represents one document in IVTFF format (see
 * http://www.voynich.nu/transcr.html).
 * 
 * @author Massimiliano "Maxi" Zattera
 */
// TODO Rename to IVTFFDocument
public class IvtffText extends Text<IvtffPage> {

	// unique ID
	private String id;

	@Override
	public String getId() {
		return id;
	}

	// Transcript version: A.B.x
	private String version;

	/**
	 * 
	 * @return Transcript version: A.B.x
	 */
	public String getVersion() {
		return version;
	}

	// Major version: A.B
	private String majorVersion;

	/**
	 * 
	 * @return Transcript major version: A.B
	 */
	public String getMajorVersion() {
		return majorVersion;
	}

	@Override
	public Text<?> getParent() {
		return null;
	}

	@Override
	public void setParent(Text<?> parent) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates a new instance of a document.
	 * 
	 * @param in
	 *            The file from which the document has to be read.
	 */
	public IvtffText(File in) throws IOException, ParseException {
		this(in, "ASCII");
	}

	/**
	 * Creates a new instance of a document.
	 * 
	 * @param in
	 *            The file from which the document has to be read.
	 * @param encoding
	 *            File encoding.
	 */
	public IvtffText(File in, String encoding) throws IOException, ParseException {

		this(new BufferedReader(new InputStreamReader(new FileInputStream(in), encoding)));
		this.id = in.getName();
	}

	/**
	 * Build a document by parsing content of given string.
	 */
	public IvtffText(String content) throws IOException, ParseException {

		this(new BufferedReader(new StringReader(content)));
		this.id = "FROM_STRING";
	}

	/**
	 * Copy constructor (kinda). Creates a new instance of a document from a list of
	 * lines contained in given document. Notice that lines are copied into the new
	 * document; they are not shared.
	 *
	 * @param doc
	 *            document where the lines come from.
	 * @param lines
	 *            list of lines that must go into the new document, they must belong
	 *            to doc.
	 */
	public IvtffText(IvtffText doc, List<IvtffLine> lines) {

		super(doc.getAlphabet());
		this.id = doc.getId();
		this.version = doc.version;
		this.majorVersion = doc.majorVersion;
		this.setParent(null);

		for (IvtffLine line : lines) {
			IvtffPage docPage = line.getPage();
			IvtffPage myPage = getElement(docPage.getId());
			if (myPage == null) {
				myPage = new IvtffPage(docPage.getDescriptor(), getAlphabet());
				addElement(myPage);
			}

			myPage.addLine(new IvtffLine(line));
		}
	}

	/**
	 * The file header is the first line in the file. It must have at least 12
	 * characters, but may be longer. The first 8 characters must be the sequence
	 * #=IVTFF followed by one <space>. The four characters after this are used to
	 * identify the transcription alphabet that is used in this file. Following this
	 * code may be an indication of the version of the IVTFF format definition. The
	 * format definition document may have a version number consisting of three
	 * parts (A.B.x), which refers to format definition A.B regardless of the values
	 * of x. The format indicator in the file only have meaning for versions 1.5 or
	 * higher.
	 * 
	 * For the identification of the transcription alphabet, the following cases
	 * have been pre-defined:
	 * 
	 * FSG- F The alphabet agreed by Friedman and his team
	 * 
	 * Curr C The alphabet used by Prescott Currier
	 * 
	 * FGuy G The ‘frogguy’ alphabet by Jacques Guy
	 * 
	 * Eva- E Eva (either basic or extended Eva)
	 * 
	 * v101 V The voynich-101 alphabet by Glen Claston
	 * 
	 * Additional codes may be added by users.
	 * 
	 * The single-character code is intended to allow the use of several different
	 * transcription alphabets in one file. It can be used in a dedicated in-line
	 * comment as described in Table 10. This is not yet used in any transcription
	 * file. AND IS UNSUPPORTED
	 */
	private final static Pattern fileHeader = Pattern.compile("#=IVTFF (.{4}) ([0-9]+\\.[0-9]+)(\\.[0-9]+)?");

	/**
	 * The notation used to identify a page in the Voynich MS is the character f
	 * (for folio) followed by the folio number, followed by r (for recto - the
	 * front) or v (for verso - the reverse).
	 * 
	 * For the foldout folios, fnr1, fnr2, etc, fnv1, fnv2, ...
	 */
	private final static Pattern pageHeader = Pattern.compile("<f[0-9]{1,3}[rv][0-9]?>|<fRos>");

	/**
	 * Creates a new instance of Document.
	 * 
	 * @param in
	 *            A reader from which the file is read.
	 * @param voynich
	 *            If true, then the file must have line locators.
	 * @param alphabet
	 *            The alphabet for the document
	 */
	private IvtffText(BufferedReader in) throws IOException, ParseException {

		try {
			String row;

			// Parse file header
			row = in.readLine();
			if (row == null)
				throw new ParseException("The input file is empty.");

			Matcher m = fileHeader.matcher(row);
			if (!m.matches())
				throw new ParseException("Invalid file header: ", row);
			if (m.group(1).equals("Eva-")) {
				setAlphabet(Alphabet.EVA);
			} else {
				throw new ParseException("Unsupported alphabeth: " + m.group(1));
			}

			IvtffPage currentPage = null;

			int rowNum = 1;
			while ((row = in.readLine()) != null) {
				++rowNum;

				if (row.trim().isEmpty())
					continue; // empty line

				if (row.startsWith("#"))
					continue; // comment

				m = pageHeader.matcher(row);
				if (m.find() && (m.start() == 0)) {
					// page header found
					PageHeader pageDescriptor = new PageHeader(row, rowNum);
					IvtffPage page = new IvtffPage(pageDescriptor, getAlphabet());
					addElement(page);
					currentPage = page;

				} else {
					// regular row

					if (currentPage == null)
						throw new ParseException("Data block starting without page header", row, rowNum);

					// take care of lines broken in multiple file lines
					while (row.endsWith("/")) {
						String next = in.readLine();
						if (next == null)
							throw new ParseException("Multi-line text truncated", row, rowNum);
						++rowNum;

						if (!next.startsWith("/"))
							throw new ParseException("Invalid continuation of multi-line text", row, rowNum);

						row = row.substring(0, row.length() - 1) + next.substring(1);
					}

					IvtffLine line = new IvtffLine(row, rowNum, getAlphabet());
					currentPage.addLine(line);
				}
			}
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (Exception e) {
				}
		}
	}

	/**
	 * 
	 * @return true if this is intelinear text. An interlinear text is composed from
	 *         different transcriptions and each line may appear multiple times, one
	 *         for each transcription.
	 */
	public boolean isInterlinear() {
		List<IvtffLine> lines = getLines();
		if (lines.size() == 0)
			return false;

		String firstAuthor = lines.get(0).getDescriptor().getTranscriber();
		for (IvtffLine line : lines)
			if (!line.getDescriptor().getTranscriber().equals(firstAuthor))
				return true;

		return false;
	}

	/**
	 * @return lines in this document. Notice that for performance reasons we do not
	 *         clone the list, so altering the list will impact the document.
	 */
	public List<IvtffLine> getLines() {
		List<IvtffLine> ll = new ArrayList<>();

		for (IvtffPage page : elements) {
			ll.addAll(page.getLines());
		}

		return ll;
	}

	/**
	 * Write this Document as a specific HTML version of the Voynich.
	 * 
	 * @throws Exception
	 */
	// public void writeHtml(TranscriptionType v, ContentType c) throws Exception {
	// writeHtml(getDocumentFileName(v, c, getAlphabet(),
	// Configuration.HTML_ROOT_FOLDER, ".html"));
	// }

	/**
	 * Write this Document into one file.
	 */
	public void write(File fOut) throws IOException {
		write(fOut, "ASCII");
	}

	/**
	 * Write this Document as HTML into given file. //
	 */
	// public void writeHtml(String fileName) throws IOException {
	//
	// HtmlConverter cvt = new HtmlConverter();
	// FileUtil.write(cvt.toHtml(this), fileName);
	// }

	/**
	 * Write Document into one output stream.
	 */
	public void write(File fOut, String encoding) throws IOException {

		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fOut), encoding));

			DateFormat f = new SimpleDateFormat("yyyyMMdd.HHmm");
			out.write("#=IVTFF " + getAlphabet().getCodeString() + " " + getVersion());
			out.newLine();
			out.write("# Latest modified on: " + f.format(new Date()));
			out.newLine();
			out.newLine();

			for (IvtffPage page : elements) {

				out.write(page.getDescriptor().toString());
				out.newLine();

				for (IvtffLine line : page.getLines()) {
					out.write(line.getDescriptor().toString());
					out.write("\t");
					out.write(line.getText());
					out.newLine();
				}
			}

			out.flush();
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (Exception e) {
				}
		}
	}

	/**
	 * Converts this document in another alphabet and returns result.
	 * 
	 * @throws Exception
	 *             if conversion cannot be performed.
	 */
	// public Document convert(Alphabet a) throws Exception {
	// if (a.getCode() == this.getAlphabet().getCode()) {
	// // basically clone this
	// return new Document(_pageIDs, _lines, _alphabet);
	// }
	//
	// switch (this.getAlphabet().getCode()) {
	// case EVA_SCRIPT:
	// switch (a.getCode()) {
	// case ZAZZY:
	// Line[] l = new Line[_lines.length];
	// for (int i = 0; i < _lines.length; ++i) {
	// l[i] = new Line(_lines[i].getID(),
	// Alphabet.evaScript2Zazzy(_lines[i].getText()));
	// }
	// return new Document(_pageIDs, l, Alphabet.getZazzyAlphabet());
	// default:
	// throw new Exception("Cannot convert a document form " +
	// this.getAlphabet().getCode().toString()
	// + " into " + a.getCode().toString());
	// }
	// case ZAZZY:
	// switch (a.getCode()) {
	// case EVA_SCRIPT:
	// Line[] l = new Line[_lines.length];
	// for (int i = 0; i < _lines.length; ++i) {
	// l[i] = new Line(_lines[i].getID(),
	// Alphabet.zazzy2evaScript(_lines[i].getText()));
	// }
	// return new Document(_pageIDs, l, Alphabet.getEvaScriptAlphabet());
	// default:
	// throw new Exception("Cannot convert a document form " +
	// this.getAlphabet().getCode().toString()
	// + " into " + a.getCode().toString());
	// }
	// default:
	// throw new Exception("Cannot convert a document form " +
	// this.getAlphabet().getCode().toString() + " into "
	// + a.getCode().toString());
	// }
	// }

	/**
	 * 
	 * @param filter
	 * @return a text with all and only lines for which filter.keep() returned true.
	 */
	public IvtffText filterPages(ElementFilter<IvtffPage> filter) {

		List<IvtffLine> toKeep = new ArrayList<>();
		for (IvtffPage page : elements)
			if (filter.keep(page))
				toKeep.addAll(page.getLines());

		return new IvtffText(this, toKeep);
	}

	/**
	 * 
	 * @param filter
	 * @return a text with all and only lines for which filter.keep() returned true.
	 */
	public IvtffText filterLines(ElementFilter<IvtffLine> filter) {

		List<IvtffLine> toKeep = new ArrayList<>();
		for (IvtffPage page : elements)
			toKeep.addAll(page.filterElements(filter));

		return new IvtffText(this, toKeep);
	}

	/**
	 * 
	 * @param splitter
	 * @return a map that maps each category (as defined by splitter) into a text
	 *         with pages in that category.
	 */
	public Map<String, IvtffText> splitPages(ElementSplitter<IvtffPage> splitter) {

		Map<String, IvtffText> result = new HashMap<>();
		Map<String, List<IvtffPage>> pages = splitElements(splitter);
		List<IvtffLine> lines = new ArrayList<>();

		for (String category : pages.keySet()) {
			lines.clear();

			for (IvtffPage page : pages.get(category))
				lines.addAll(page.getLines());

			result.put(category, new IvtffText(this, lines));
		}

		return result;
	}

	/**
	 * 
	 * @param splitter
	 * @return a map that maps each category (as defined by splitter) into a text
	 *         with lines in that category.
	 */
	public Map<String, IvtffText> splitLines(ElementSplitter<IvtffLine> splitter) {

		Map<String, IvtffText> result = new HashMap<>();
		Map<String, List<IvtffLine>> lines = new HashMap<>();

		for (IvtffPage page : elements) {
			Map<String, List<IvtffLine>> splitted = page.splitElements(splitter);
			for (String category : splitted.keySet()) {
				List<IvtffLine> l = splitted.get(category);

				if (l == null) {
					l = new ArrayList<>();
					lines.put(category, l);
				}

				l.addAll(splitted.get(category));
			}
		}

		for (Entry<String, List<IvtffLine>> entry : lines.entrySet()) {
			result.put(entry.getKey(), new IvtffText(this, entry.getValue()));
		}

		return result;
	}
}
