/* Copyright (c) 2018-2022 Massimiliano "Maxi" Zattera */

package io.github.mzattera.v4j.text.ivtff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.mzattera.v4j.text.CompositeText;
import io.github.mzattera.v4j.text.ElementFilter;
import io.github.mzattera.v4j.text.ElementSplitter;
import io.github.mzattera.v4j.text.alphabet.Alphabet;

/**
 * This class represents one document in IVTFF format (see
 * http://www.voynich.nu/transcr.html).
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public class IvtffText extends CompositeText<IvtffPage> {

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
	 * FGuy G The frogguy alphabet by Jacques Guy
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
	public final static Pattern FILE_HEADER_PATTERN = Pattern.compile("#=IVTFF (.{4}) ([0-9]+\\.[0-9]+)(\\.[0-9]+)?");

	/**
	 * The notation used to identify a page in the Voynich MS is the character f
	 * (for folio) followed by the folio number, followed by r (for recto - the
	 * front) or v (for verso - the reverse).
	 * 
	 * For the foldout folios, fnr1, fnr2, etc, fnv1, fnv2, ...
	 */
	public final static Pattern PAGE_HEADER_PATTERN = Pattern.compile("<f[0-9]{1,3}[rv][0-9]?>|<fRos>");

	/**
	 * Locus identifiers have the following format:
	 * 
	 * < page . num , code >
	 * 
	 * Or : < page . num , code ; T >
	 * 
	 * Whitespace is not allowed inside locus identifiers, but it is used in the
	 * patterns above for clarity. The fields have the following meaning:
	 * 
	 * page The page name, which has to match the most recent page header.
	 * 
	 * num A sequence number, incrementing from 1 for each page. The highest number
	 * that presently occurs is 160.
	 * 
	 * code A 3-character code, which is a 1-character locator followed by a
	 * 2-character locus type
	 * 
	 * T An optional single-character transcriber ID. Only used in interlinear files
	 * that include several parallel transcriptions.
	 */
	public final static Pattern LOCUS_IDENTIFIER_PATTERN = Pattern
			.compile("<(f[0-9]{1,3}[rv][0-9]?|fRos)\\.([0-9]{1,3}[a-z]?),([\\+\\*\\-=&~@/][PLCR].)(;.)?>");

	// unique ID
	private String id;

	@Override
	public String getId() {
		return id;
	}

	private String version;

	/**
	 * @return Transcript version: A.B.x
	 */
	public String getVersion() {
		return version;
	}

	private String majorVersion;

	/**
	 * @return Transcript major version: A.B
	 */
	public String getMajorVersion() {
		return majorVersion;
	}

	/**
	 * 
	 * @return The file header (#=IVTFF...) for this document.
	 */
	public String getFileHeader() {
		return "#=IVTFF " + getAlphabet().getCodeString() + " " + getVersion();
	}

	@Override
	public void setParent(CompositeText<?> parent) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates a new instance of a document.
	 * 
	 * @param in The file from which the document has to be read.
	 */
	public IvtffText(File in) throws IOException, ParseException {
		this(new BufferedReader(new InputStreamReader(new FileInputStream(in), "ASCII")));
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
	 * Creates a new instance of a document from a list of lines. Document metadata
	 * (e.g. ID, alphabet, version, etc.) are copied from the passed document.
	 *
	 * @param doc   document from which metadata is taken.
	 * @param lines list of lines that must go into the new document.
	 * @param ID    ID for newly created document (overwrites existing one).
	 */
	public IvtffText(IvtffText doc, Collection<IvtffLine> lines) {
		this(doc, lines, doc.getId());
	}

	/**
	 * Creates a new instance of a document from a list of lines. Document metadata
	 * (e.g. ID, alphabet, version, etc.) are copied from the passed document.
	 *
	 * @param doc   document from which metadata is taken.
	 * @param lines list of lines that must go into the new document.
	 * @param ID    ID for newly created document (overwrites existing one).
	 */
	public IvtffText(IvtffText doc, Collection<IvtffLine> lines, String ID) {

		super(doc.getAlphabet());
		this.id = ID;
		this.version = doc.version;
		this.majorVersion = doc.majorVersion;

		for (IvtffLine line : lines) {
			IvtffPage docPage = line.getPage();
			IvtffPage myPage = getElement(docPage.getId());
			if (myPage == null) {
				myPage = new IvtffPage(docPage.getDescriptor(), getAlphabet());
				addElement(myPage);
			}

			myPage.addElement(new IvtffLine(line));
		}
	}

	/**
	 * Creates a new instance of a document from a list of pages. Document metadata
	 * (e.g. ID, alphabet, version, etc.) are copied from the passed document.
	 *
	 * @param doc   document from which metadata is taken.
	 * @param pages list of lines that must go into the new document.
	 */
	public static IvtffText fromPages(IvtffText doc, Collection<IvtffPage> pages) {
		return fromPages(doc, pages, doc.getId());
	}

	/**
	 * Creates a new instance of a document from a list of pages. Document metadata
	 * (e.g. ID, alphabet, version, etc.) are copied from the passed document.
	 *
	 * @param doc   document from which metadata is taken.
	 * @param pages list of lines that must go into the new document.
	 * @param ID    ID for newly created document (overwrites existing one).
	 */
	public static IvtffText fromPages(IvtffText doc, Collection<IvtffPage> pages, String ID) {
		List<IvtffLine> lines = new ArrayList<>();
		for (IvtffPage p : pages)
			lines.addAll(p.getElements());

		return new IvtffText(doc, lines, ID);
	}

	/**
	 * Constructor from Reader. Assumes EVA alphabet.
	 * 
	 * @param in A Reader from which the document content is read.
	 */
	private IvtffText(BufferedReader in) throws IOException, ParseException {

		try {
			String row;

			// Parse file header
			row = in.readLine();
			if (row == null)
				throw new ParseException("The input file is empty.");

			Matcher m = FILE_HEADER_PATTERN.matcher(row);
			if (!m.matches())
				throw new ParseException("Invalid file header: ", row);

			this.alphabet = Alphabet.getAlphabet(m.group(1));
			if (this.alphabet == null)
				throw new ParseException("Unsupported alphabeth: " + m.group(1));
			this.majorVersion = m.group(2);
			if (!this.majorVersion.equals("1.5"))
				throw new ParseException("Unsupported IVTFF format version: " + this.majorVersion);
			this.version = (m.group(3) == null) ? this.majorVersion : this.majorVersion + m.group(3);

			IvtffPage currentPage = null;

			int rowNum = 1;
			while ((row = in.readLine()) != null) {
				++rowNum;

				if (row.trim().isEmpty())
					continue; // empty line

				if (row.startsWith("#"))
					continue; // comment

				m = PAGE_HEADER_PATTERN.matcher(row);
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
					currentPage.addElement(line);
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
	 *         different transcriptions, from several authors, and each line may
	 *         appear multiple times, one for each transcription.
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
	 *         clone the lines, so altering the returned lines will make document
	 *         status invalid. // TODO
	 */
	public List<IvtffLine> getLines() {
		List<IvtffLine> ll = new ArrayList<>();

		for (IvtffPage page : elements) {
			ll.addAll(page.getElements());
		}

		return ll;
	}

	/**
	 * Write this Document into one file.
	 */
	public void write(File fOut) throws IOException {
		try (OutputStream os = new FileOutputStream(fOut)) {
			write(os);
		}
	}

	/**
	 * Write this Document into one Stream.
	 */
	public void write(OutputStream os) throws IOException {

		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os, "ASCII"));

		DateFormat f = new SimpleDateFormat("yyyyMMdd.HHmm");
		out.write("#=IVTFF " + getAlphabet().getCodeString() + " " + getVersion());
		out.newLine();
		out.write("# Latest modified on: " + f.format(new Date()));
		out.newLine();
		out.write("#");
		out.newLine();

		for (IvtffPage page : elements) {

			out.write(page.getDescriptor().toString());
			out.newLine();

			for (IvtffLine line : page.getElements()) {
				out.write(line.getDescriptor().toString());
				out.write("\t");
				out.write(line.getText());
				out.newLine();
			}
		}

		out.flush();
	}

	/**
	 * Converts this document in another alphabet and returns result.
	 * 
	 * @throws Exception if conversion cannot be performed.
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
	 * @return a text with all and only pages for which filter.keep() returned true.
	 */
	public IvtffText filterPages(ElementFilter<IvtffPage> filter) {

		List<IvtffLine> toKeep = new ArrayList<>();
		for (IvtffPage page : elements)
			if (filter.keep(page))
				toKeep.addAll(page.getElements());

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

		// TODO change ID of returned documents?
		for (String category : pages.keySet())
			result.put(category, fromPages(this, pages.get(category)));

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
				List<IvtffLine> l = lines.get(category);

				if (l == null) {
					l = new ArrayList<>();
					lines.put(category, l);
				}

				l.addAll(splitted.get(category));
			}
		}

		// TODO change ID of generated documents?
		for (Entry<String, List<IvtffLine>> entry : lines.entrySet()) {
			result.put(entry.getKey(), new IvtffText(this, entry.getValue()));
		}

		return result;
	}

	/**
	 * Splits the running text (P0 and P1 loci) of this document into paragraphs.
	 * Notice other parts of the document are ignored.
	 * 
	 * @return A List of paragraphs from given document.
	 */
	public List<IvtffText> toParagraphs() {
		return IvtffText.toParagraphs(this);
	}

	/**
	 * Splits the running text (P0 and P1 loci) of given document into paragraphs.
	 * Notice other parts of the document are ignored.
	 * 
	 * @param doc The input document.
	 * @return A List of paragraphs from given document.
	 */
	public static List<IvtffText> toParagraphs(IvtffText doc) {
		doc = doc.filterLines(LineFilter.PARAGRAPH_TEXT_FILTER);
		List<IvtffText> paragraphs = new ArrayList<>();
		for (IvtffPage p : doc.getElements()) {
			List<IvtffLine> parLines = new ArrayList<>();
			for (IvtffLine l : p.getElements()) {
				parLines.add(l);
				boolean isParEnd = (l.isLast());
				if (isParEnd) {
					String id = doc.getId() + "_" + l.getId();
					paragraphs.add(new IvtffText(doc, parLines, id));
					parLines.clear();
				}
			}
			if (parLines.size() > 0) { // collect last paragraph
				paragraphs.add(new IvtffText(doc, parLines, doc.getId() + "_" + parLines.get(0).getId()));
			}
		}
		return paragraphs;
	}

	/**
	 * @return A shuffled version of the text, with the same number of pages, number
	 *         of lines per page, number of words per line, but with words shuffled
	 *         at random. This method maintains the original structure of the text,
	 *         and preserves paragraph separators (<$>).
	 */
	public IvtffText shuffledText() {
		return shuffledText(new Random(System.currentTimeMillis()));
	}

	/**
	 * @return A shuffled version of the text, with the same number of pages, number
	 *         of lines per page, number of words per line, but with words shuffled
	 *         at random. This method maintains the original structure of the text,
	 *         and preserves paragraph separators (<$>).
	 */
	public IvtffText shuffledText(Random rnd) {

		// List with shuffled words
		List<String> words = Arrays.asList(splitWords());
		Collections.shuffle(words, rnd);
		int pos = 0;

		char space = getAlphabet().getSpace();
		StringBuffer result = new StringBuffer();
		result.append(getFileHeader()).append('\n');
		for (IvtffPage p : getElements()) {
			result.append(p.getDescriptor()).append('\n');
			for (IvtffLine l : p.getElements()) {
				result.append(l.getDescriptor()).append(' ');
				for (int i = 0; i < l.splitWords().length; ++i) {
					if (i > 0)
						result.append(space);
					result.append(words.get(pos++));
				}
				if (l.getText().endsWith("<$>"))
					result.append("<$>");
				result.append('\n');
			} // for each line
		} // for each page

		if (pos != words.size())
			throw new RuntimeException("PROT! " + pos + " " + words.size());
		try {
			return new IvtffText(result.toString());
		} catch (Exception e) {
			return null; // shall never happen
		}
	}

	/**
	 * Prints the whole document using IVTFF format
	 */
	@Override
	public String toString() {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			write(os);
			return os.toString(java.nio.charset.StandardCharsets.US_ASCII);
		} catch (Exception e) { // should never happen
			return super.toString();
		}
	}
}
