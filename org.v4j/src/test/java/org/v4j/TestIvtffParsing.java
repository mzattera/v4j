/**
 * 
 */
package org.v4j;

import org.v4j.text.ivtff.IvtffText;
import org.v4j.text.ivtff.IvtffLine;
import org.v4j.text.ivtff.IvtffPage;
import org.v4j.text.ivtff.ParseException;

/**
 * Performs minimum compliace test for our IVTFF document parser.
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
public class TestIvtffParsing implements RegressionTest {

	@Override
	public void doTest() throws Exception {

		// * The first line matches the file header definition of Section 6.1, though
		// * without restriction on the 4-character transcription alphabet code

		String in = "#=IVTFF Eva- 1.5" + "\n" + //
				"# Empty file.";
		IvtffText doc = new IvtffText(in);

		try {
			in = "# Empty file.";
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}

		try {
			in = "#=IVTFF xxxxY 1.5.2.4" + "\n" + //
					"# Empty file.";
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}

		// Extension: check is made on the alphabet code and only listed ones are
		// accepted
		try {
			in = "#=IVTFF xxxx 1.5" + "\n" + //
					"# Empty file.";
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}

		try {
			in = "#=IVTFF xxxxY 1.5" + "\n" + //
					"# Empty file.";
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}

		// * The first character of each line can only be one of three characters: #, <
		// or
		// * /

		// implicit in next step and other below
		try {
			in = "Empty file.";
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}

		// * If the first character of a line is < , the line is a page header or the
		// * start of a new locus

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"# Empty file." + "\n" + //
				"<f1v>";
		doc = new IvtffText(in);
		assert (doc.getElement("f1v") != null);

		// Extension: support of page properties
		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"# Empty file." + "\n" + //
				"<f1v>" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>";
		doc = new IvtffText(in);
		IvtffPage p = doc.getElement("f1r");
		assert (p != null);
		assert (p.getDescriptor().getQuire().equals("B"));
		assert (p.getDescriptor().getPageInQuire().equals("L"));
		assert (p.getDescriptor().getType().equals("H"));
		assert (p.getDescriptor().hasSequenceLikeKey());
		assert (p.getDescriptor().getLanguage().equals("A"));
		assert (p.getDescriptor().getHand().equals("1"));
		assert (p.getDescriptor().getExtraneousWriting().equals("M"));

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
		IvtffLine l = p.getElement("f1r.18,@Ro;H");
		assert l.getDescriptor().getPageId().equals("f1r");
		assert l.getDescriptor().getNumber().equals("18");
		assert l.getDescriptor().getLocus().equals("@Ro");
		assert l.getDescriptor().getTranscriber().equals("H");
		assert l.getDescriptor().toString().equals("<f1r.18,@Ro;H>");

		// * If the first character is / then the previous line must have ended with a /

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody.chetey/" + "\n" + //
				"/.yteody.chetey";
		doc = new IvtffText(in);
		p = doc.getElement("f1r");
		l = p.getElement("f1r.18,@Ro;H");
		assert l.getText().equals("yteody.chetey.yteody.chetey");

		try {
			in = "#=IVTFF Eva- 1.5" + "\n" + //
					"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
					"<f1r.18,@Ro;H> yteody.chetey/ " + "\n" + // un allowed space
					"/.yteody.chetey";
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}

		try {
			in = "#=IVTFF Eva- 1.5" + "\n" + //
					"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
					"<f1r.18,@Ro;H> yteody.chetey/" + "\n" + //
					" /.yteody.chetey"; // un allowed space
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}

		// * All in-line free comments <! are closed by a > on the same line in the file

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody<! comment>.chetey";
		doc = new IvtffText(in);
		p = doc.getElement("f1r");
		l = p.getElement("f1r.18,@Ro;H");
		assert l.getNormalizedText().equals("yteody.chetey");

		try {
			in = "#=IVTFF Eva- 1.5" + "\n" + //
					"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
					"<f1r.18,@Ro;H> yteody<! comment>.<chetey";
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}

		// * All other in-line comments consist of 3 or 4 characters, of which the first
		// * is < and the last is >

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteody<12>.chetey";
		doc = new IvtffText(in);
		p = doc.getElement("f1r");
		l = p.getElement("f1r.18,@Ro;H");
		assert l.getNormalizedText().equals("yteody.chetey");

		try {
			in = "#=IVTFF Eva- 1.5" + "\n" + //
					"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
					"<f1r.18,@Ro;H> yteody<123>.<chetey";
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}

		// * All alternative readings [ are closed by a ] on the same line in the file

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> yteod[y:in:iin].chete[y:o]";
		doc = new IvtffText(in);
		p = doc.getElement("f1r");
		l = p.getElement("f1r.18,@Ro;H");
		assert l.getNormalizedText().equals("yteody.chetey");

		try {
			in = "#=IVTFF Eva- 1.5" + "\n" + //
					"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
					"<f1r.18,@Ro;H> yteody[].chetey";
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}

		try {
			in = "#=IVTFF Eva- 1.5" + "\n" + //
					"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
					"<f1r.18,@Ro;H> yteody[y].chetey";
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}

		try {
			in = "#=IVTFF Eva- 1.5" + "\n" + //
					"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
					"<f1r.18,@Ro;H> yteod[y:in:iin].chetey]";
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}

		// * All ligature indications { are close by a } on the same line in the file

		in = "#=IVTFF Eva- 1.5" + "\n" + //
				"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
				"<f1r.18,@Ro;H> y{te}ody.chetey";
		doc = new IvtffText(in);
		p = doc.getElement("f1r");
		l = p.getElement("f1r.18,@Ro;H");
		assert l.getNormalizedText().equals("yteody.chetey");

		try {
			in = "#=IVTFF Eva- 1.5" + "\n" + //
					"<f1r>  <! $I=H $Q=B $P=L $L=A $H=1 $K=Y $X=M>" + "\n" + //
					"<f1r.18,@Ro;H> y{teody.chetey";
			doc = new IvtffText(in);
			throw new Exception("This is unacceptable");
		} catch (ParseException e) {
		}
	}
}
