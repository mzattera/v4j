/**
 * 
 */
package io.github.mzattera.v4j.text.ivtff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Performs minimum compliance test for our IVTFF document parser.
 * 
 * 
 * Tools reading an IVTFF file shall be able to rely on a minimum conformance to
 * the format definition, which is given by the following rules.
 * 
 * The first line matches the file header definition of Section 6.1, though
 * without restriction on the 4-character transcription alphabet code
 * 
 * The first character of each line can only be one of three characters: #, < or
 * /
 * 
 * If the first character of a line is < , the line is a page header or the
 * start of a new locus
 * 
 * If the first character is / then the previous line must have ended with a /
 * 
 * Page headers must have a valid page name between < and > (see Annex 1) and no
 * whitespace
 * 
 * Locus identifiers have no whitespace, refer exactly to the page in the most
 * recent page header, have an integer number from 1 to 999 between the . and
 * the , and a three-character item between the , and the >
 * 
 * An optional transcriber ID consists of a single character after a semi-colon
 * 
 * All in-line free comments <! are closed by a > on the same line in the file
 * 
 * All other in-line comments consist of 3 or 4 characters, of which the first
 * is < and the last is >
 * 
 * All alternative readings [ are closed by a ] on the same line in the file
 * 
 * All ligature indications { are close by a } on the same line in the file
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class IvtffParsingTest {

	@Test
	@DisplayName("IVTFF parser is compliant")
	public void doTest() throws IOException, ParseException {

		// * The first line matches the file header definition of Section 6.1, though
		// * without restriction on the 4-character transcription alphabet code

		String in = "#=IVTFF Eva- 1.5" + "\n" + //
				"# Empty file.";
		IvtffText doc;
		try {
			doc = new IvtffText(in);
		} catch (IOException | ParseException e1) {
			fail("Cannot create IVTFF document.");
			return;
		}

		in = "# Empty file.";
		createIvtffThrowsException(in);

		in = "#=IVTFF xxxxY 1.5.2.4" + "\n" + //
				"# Empty file.";
		createIvtffThrowsException(in);

		// Extension: check is made on the alphabet code and only listed ones are
		// accepted
		in = "#=IVTFF xxxx 1.5" + "\n" + //
				"# Empty file.";
		createIvtffThrowsException(in);

		in = "#=IVTFF xxxxY 1.5" + "\n" + //
				"# Empty file.";
		createIvtffThrowsException(in);

		// * The first character of each line can only be one of three characters: #, <
		// or
		// * /

		// implicit in next step and other below
		in = "Empty file.";
		createIvtffThrowsException(in);

		// * If the first character of a line is < , the line is a page header or the
		// * start of a new locus

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"# Empty file." + "\n" + //
				"<f1v>";
		doc = new IvtffText(in);
		assertTrue(doc.getElement("f1v") != null);

		// Extension: support of page properties
		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"# Empty file." + "\n" + //
				"<f1v>" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>";
		doc = new IvtffText(in);
		IvtffPage p = doc.getElement("f1r");
		assertTrue(p != null);
		assertEquals(p.getDescriptor().getQuire(), "B");
		assertEquals(p.getDescriptor().getPageInQuire(), "L");
		assertEquals(p.getDescriptor().getIllustrationType(), "H");
		assertTrue(p.getDescriptor().hasSequenceLikeKey());
		assertEquals(p.getDescriptor().getLanguage(), "A");
		assertEquals(p.getDescriptor().getHand(), "1");
		assertEquals(p.getDescriptor().getExtraneousWriting(), "M");

		// * Page headers must have a valid page name between < and > (see Annex 1) and
		// * no whitespace

		// * Locus identifiers have no whitespace, refer exactly to the page in the most
		// * recent page header, have an integer number from 1 to 999 between the . and
		// * the , and a three-character item between the , and the >

		// * An optional transcriber ID consists of a single character after a
		// semi-colon

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"# Empty file." + "\n" + //
				"<f1v>" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody.chetey";
		doc = new IvtffText(in);
		p = doc.getElement("f1r");
		IvtffLine l = p.getElement("f1r.18;H");
		assertEquals(l.getDescriptor().getPageId(), "f1r");
		assertEquals(l.getDescriptor().getNumber(), "18");
		assertEquals(l.getDescriptor().getLocus(), "@Ro");
		assertEquals(l.getDescriptor().getTranscriber(), "H");
		assertEquals(l.getDescriptor().toString(), "<f1r.18,@Ro;H>");

		// * If the first character is / then the previous line must have ended with a /

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody.chetey/" + "\n" + //
				"/.yteody.chetey";
		doc = new IvtffText(in);
		p = doc.getElement("f1r");
		l = p.getElement("f1r.18;H");
		assertEquals(l.getText(), "yteody.chetey.yteody.chetey");

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody.chetey/ " + "\n" + // un allowed space
				"/.yteody.chetey";
		createIvtffThrowsException(in);

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody.chetey/" + "\n" + //
				" /.yteody.chetey"; // un allowed space
		createIvtffThrowsException(in);

		// * All in-line free comments <! are closed by a > on the same line in the file

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody<! comment>.chetey";
		doc = new IvtffText(in);
		p = doc.getElement("f1r");
		l = p.getElement("f1r.18;H");
		assertEquals(l.getPlainText(), "yteody.chetey");

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody<! comment>.<chetey";
		createIvtffThrowsException(in);

		// * All other in-line comments consist of 3 or 4 characters, of which the first
		// * is < and the last is >

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody<12>.chetey";
		doc = new IvtffText(in);
		p = doc.getElement("f1r");
		l = p.getElement("f1r.18;H");
		assertEquals(l.getPlainText(), "yteody.chetey");

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody<123>.<chetey";
		createIvtffThrowsException(in);

		// * All alternative readings [ are closed by a ] on the same line in the file

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteod[y:in:iin].chete[y:o]";
		doc = new IvtffText(in);
		p = doc.getElement("f1r");
		l = p.getElement("f1r.18;H");
		assertEquals(l.getPlainText(), "yteody.chetey");

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody[].chetey";
		createIvtffThrowsException(in);

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody[y].chetey";
		createIvtffThrowsException(in);

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteod[y:in:iin].chetey]";
		createIvtffThrowsException(in);

		// * All ligature indications { are close by a } on the same line in the file

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> y{te}ody.chetey";
		doc = new IvtffText(in);
		p = doc.getElement("f1r");
		l = p.getElement("f1r.18;H");
		assertEquals(l.getPlainText(), "yteody.chetey");

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> y{teody.chetey";
		createIvtffThrowsException(in);
	}

	private static void createIvtffThrowsException(final String text) {
		assertThrows(ParseException.class, () -> new IvtffText(text));
	}
}
