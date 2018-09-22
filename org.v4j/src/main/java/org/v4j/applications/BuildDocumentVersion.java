package org.v4j.applications;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.v4j.text.ivtff.IvtffLine;
import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.ParseException;
import org.v4j.text.ivtff.VoynichFactory;
import org.v4j.text.ivtff.VoynichFactory.Transcription;
import org.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import org.v4j.util.StringUtil;

/**
 * Processes interlinear version to obtain different versions with different
 * degree of concordance between transcribers.
 * 
 * It also creates some text based on Notes004.
 * 
 * It saves the results both as text & HTML.
 * 
 * STATUS: Working & with test harness.
 * 
 * @author Massimiliano "Maxi" Zattera
 */
public class BuildDocumentVersion {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		try {
			doWork(VoynichFactory.getDocument(Transcription.LSI), TranscriptionType.CONCORDANCE);

			// URL url = ClassLoader.getSystemResource("MS/LSI_ivtff_0d2 - fixed.txt");
			// File inFile = new File(url.toURI());
			// doWork(new IvtffText(inFile), TranscriptionType.CONCORDANCE);

			// TODO write result & do all other transcription types

		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.exit(-1);
		}
	}

	// TODO Comments
	private static IvtffText doWork(IvtffText document, TranscriptionType type) throws ParseException {
		if (!document.isInterlinear())
			throw new IllegalArgumentException("Transcription must ber interlinear.");

		List<IvtffLine> lines = document.getLines();
		if (lines.size() == 0)
			return new IvtffText(document, lines);

		List<IvtffLine> result = new ArrayList<>();
		List<IvtffLine> group = new ArrayList<>(); // group of lines being processed

		int i = 0;
		String groupId = null;
		while (i < lines.size()) {

			if (groupId == null) // start of a new group
				groupId = getGroupId(lines.get(i));

			// Take all lines in current group, that is all lines with same page & number
			while ((i < lines.size()) && getGroupId(lines.get(i)).equals(groupId)) {
				group.add(lines.get(i++));
			}

			result.add(process(group, type));
			group.clear();
			groupId = null;
		} // while we have lines in input

		// TODO
		return null;
	}

	private static IvtffLine process(List<IvtffLine> group, TranscriptionType type) throws ParseException {

		if (group.size() == 0)
			throw new IllegalArgumentException("Cannot process an empty group of lines.");

		List<IvtffLine> backup = new ArrayList<>();
		for (IvtffLine l : group)
			backup.add(new IvtffLine(l));

		if (!alignText(group)) {
			for (IvtffLine line : backup)
				System.out.println(line);
			System.out.println();
		} else {
			// System.out.println(group.get(0).getDescriptor().toString());
		}

		return null;
	}

	private static final String GROVE_PATTERN_STRING = "(\\s*(<!(Grove|was|illegible|gallow|merged|higher|missing|swapped|label|star|from|From|gap|first|second|anomalous|on |para )[^>]*>|<\\->))+\\z";
	private static final Pattern GROVE_PATTERN = Pattern.compile(GROVE_PATTERN_STRING);

	/**
	 * Changes text of given lines such that thez are aligned and readz to be
	 * merged.
	 * 
	 * @return true if the lines could be aligned.
	 */
	// TODO make PRIVATE; fro test onlz
	public static boolean alignText(List<IvtffLine> group) throws ParseException {

		// Check if lines are already aligned
		String longest = group.get(0).getText();
		int maxLen = longest.length(); // target length of text for lines in this group
		boolean aligned = true; // true if text is aligned for all lines
		boolean allEndWithComment = true; // do all line end with comments?
		for (IvtffLine line : group) {
			String txt = line.getText();

			if (txt.length() != maxLen) {
				aligned = false;
				if (txt.length() > maxLen) {
					longest = txt;
					maxLen = longest.length();
				}
			}

			Matcher m = GROVE_PATTERN.matcher(txt);
			if (!m.find())
				allEndWithComment = false;
		}

		if (aligned)
			return true;

		// Not aligned.
		// Check if they could be aligned by removing inline comments or extra '!' at
		// the end
		// of lines:
		//
		// <f72r3.28,&Lz;H> yfary
		// <f72r3.28,&Lz;V> ypary <!Grove's #3>
		// <f72r3.28,&Lz;U> ypary
		//
		// <f69r.6,&L0;H> ar.odain.chtaly
		// <f69r.6,&L0;U> ar.odair.chtaly
		// <f69r.6,&L0;V> ar.odair.chtaly<->
		if (!allEndWithComment) {

			longest = null;
			maxLen = -1; // target length of text for lines in this group
			aligned = true;
			for (IvtffLine line : group) {

				// remove inline comments at end of the line...
				String txt = line.getText().replaceAll(GROVE_PATTERN_STRING, "");
				line.setText(txt);

				if (longest == null) {
					longest = txt;
					maxLen = txt.length();
				} else if (txt.length() != maxLen) {
					aligned = false;
					if (txt.length() > maxLen) {
						longest = txt;
						maxLen = longest.length();
					}
				}
			}
		}

		if (aligned)
			return true;

		// not aligned, let's do the alignment
		aligned = true;
		for (IvtffLine line : group) {

			if (line.getText().length() == maxLen)
				continue;

			String txt = StringUtil.align(line.getText(), longest);
			line.setText(txt);

			if (txt.length() != maxLen)
				aligned = false;
		}

		return aligned;
	}

	/**
	 * 
	 * @param ivtffLine
	 * @return a group Id, which is used to group version of same line form
	 *         different transcribers.
	 */
	private static String getGroupId(IvtffLine line) {
		return line.getDescriptor().getPageId() + "." + line.getDescriptor().getNumber();
	}

}