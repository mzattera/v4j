package io.github.mattera.v4j.applications.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mattera.v4j.text.ElementFilter;
import io.github.mattera.v4j.text.ivtff.IvtffLine;
import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.LineFilter;
import io.github.mattera.v4j.text.ivtff.LineSplitter;
import io.github.mattera.v4j.text.ivtff.ParseException;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;

/**
 * @author Massimiliano "Maxi" Zattera
 */
public final class BuildConcordanceVersionTest {

	@Test
	@DisplayName("Test CONCORDANCE")
	public void doTestConcordance() throws Exception {
		String in = "#=IVTFF Eva- 1.5\n" + "<f1r>         <! $I=H $Q=C $P=E $L=A $H=1> \n"
				+ "<f1r.1,@P0;H>       faahys.ykal!ar.ataiin.shol.shory.cth!res.y.kor.sholdy!<->\n"
				+ "<f1r.1,@P0;C>       fachyy.ykal.a!!ataiin,shol,shory,ct%%%ys.y.kor.sholdy!<->\n"
				+ "<f1r.1,@P0;F>       fya!!a.rkal.a!!ytaiin.shol,shory,%k%!%es.y!kor.sholdy!<->\n"
				+ "<f1r.1,@P0;N>       fach!s.tkal.ar!ataiin.shol.shory,cth!%es.y.kor.sholdy!<->";
		IvtffText doc = buildDocument(in, TranscriptionType.CONCORDANCE);
		assertEquals(doc.getText(), "f?????.?kal,a?,?taiin,shol,shory,???!??s.y,kor.sholdy!<->");

		in = "#=IVTFF Eva- 1.5\n" + "<f89v2>         <! $I=H $Q=C $P=E $L=A $H=1> \n"
				+ "<f89v2.22,+P0;H>   ockhody.daiin.y!kam.s<-><!crease>ch!t!y.ch<!fold>y.cthey.dair!air.cho!ol.loy.dair.cheo!daiin<$>\r\n"
				+ "<f89v2.22,+P0;F>   ockhody.daiin.y!kam.s<-><!crease>chcthy.ch<!fold>y.cthey.dair.!?r.cho!ol.loy.dair.cheo.daiin<$>\r\n"
				+ "<f89v2.22,+P0;L>   ockhody.daiin.o!kam.s<-><!crease>!!!!!??ee!!!!!!!.???ee.dair!yir.cho!ol.loy.dair.cheo!daiin<$>\r\n"
				+ "<f89v2.22,+P0;U>   ockhody.daiin.y,kam.s<-><!crease>ch!t!y.ch<!fold>y.cthey.dair,air.cho,ol.loy.dair.cheo,daiin<$>\r\n";
		doc = buildDocument(in, TranscriptionType.CONCORDANCE);
		assertEquals(doc.getText(),
				"ockhody.daiin.?,kam.s.!??!???????,??!!?.???e?.dair,??r.cho,ol.loy.dair.cheo,daiin<$>");

		in = "#=IVTFF Eva- 1.5\n" + "<f1r>         <! $I=H $Q=C $P=E $L=A $H=1> \n"
				+ "<f1r.13,+P0;H>	shoy.ckhey.kodaiin.cphy.cphodaiils.cthey.she.oldain.d!\r\n"
				+ "<f1r.13,+P0;C>	shoy.ckhey.kodaiin.cphy.cphodaiils.cthey.sho.oldain.d!\r\n"
				+ "<f1r.13,+P0;F>	shoy.ckhey.kodaiin.cphy.cph?daiils.cthey.she.oldain.d!\r\n"
				+ "<f1r.13,+P0;N>	shoy.ckhey.kodaiin.cphy.cphodaiils.cthey.she.oldain,d!\r\n"
				+ "<f1r.13,+P0;U>	shoy.ckhey.kodaiin.cphy.cphoda??ls.cthey.shy.oldain.d?\r\n";
		doc = buildDocument(in, TranscriptionType.CONCORDANCE);
		assertEquals(doc.getText(), "shoy.ckhey.kodaiin.cphy.cph?da??ls.cthey.sh?.oldain,d?");

		in = "#=IVTFF Eva- 1.5\n" + "<f17r>         <! $I=H $Q=C $P=E $L=A $H=1> \n"
				+ "<f17r.6,+P0;H>	tsho.qof!cho.qokcheor.chekeg<$>\r\n"
				+ "<f17r.6,+P0;C>	tsho.qof.cho.qokcheor.choto?<$>\r\n"
				+ "<f17r.6,+P0;F>	tsho.qof!cho.qokcheor.chekem<$>\r\n"
				+ "<f17r.6,+P0;U>	tsho.qof,cho.qokcheor.chetom<$>";
		doc = buildDocument(in, TranscriptionType.CONCORDANCE);
		assertEquals(doc.getText(), "tsho.qof,cho.qokcheor.ch????<$>");

		in = "#=IVTFF Eva- 1.5\n" + "<f1r>         <! $I=H $Q=C $P=E $L=A $H=1> \n" + "<f1r.6,+P0;H>	..!!!!\r\n"
				+ "<f1r.6,+P0;C>	.,!!!!\r\n" + "<f1r.6,+P0;F>	,,.!.!\r\n" + "<f1r.6,+P0;N>	.,,.,.\r\n"
				+ "<f1r.6,+P0;U>	,...,,\r\n";
		doc = buildDocument(in, TranscriptionType.CONCORDANCE);
		assertEquals(doc.getText(), ",,,,,,");

		in = "#=IVTFF Eva- 1.5\n" + "<f1r>         <! $I=H $Q=C $P=E $L=A $H=1> \n" + "<f1r.6,+P0;H>	..!!!!%??\r\n"
				+ "<f1r.6,+P0;C>	.%!!!!%??\r\n" + "<f1r.6,+P0;F>	%%.!.!%%?\r\n" + "<f1r.6,+P0;N>	.%%.%.%%%\r\n"
				+ "<f1r.6,+P0;U>	%...%,%%%\r\n";
		doc = buildDocument(in, TranscriptionType.CONCORDANCE);
		assertEquals(doc.getText(), "???,?,%??");
	}

	@Test
	@DisplayName("Test MAJORITY")
	public void doTestMajority() throws Exception {
		String in = "#=IVTFF Eva- 1.5\n" + "<f1r>         <! $I=H $Q=C $P=E $L=A $H=1> \n"
				+ "<f1r.1,@P0;H>       faahys.ykal!ar.ataiin.shol.shory.cth!res.y.kor.sholdy!<->\n"
				+ "<f1r.1,@P0;C>       fachyy.ykal.a!!ataiin,shol,shory,ct%%%ys.y.kor.sholdy!<->\n"
				+ "<f1r.1,@P0;F>       fya!!a.rkal.a!!ytaiin.shol,shory,%k%!%es.y!kor.sholdy!<->\n"
				+ "<f1r.1,@P0;N>       fach!s.tkal.ar!ataiin.shol.shory,cth!%es.y.kor.sholdy!<->";
		IvtffText doc = buildDocument(in, TranscriptionType.MAJORITY);
		assertEquals(doc.getText(), "fa?hys.ykal.ar!ataiin.shol,shory,ct?!%es.y.kor.sholdy!<->");

		in = "#=IVTFF Eva- 1.5\n" + "<f89v2>         <! $I=H $Q=C $P=E $L=A $H=1> \n"
				+ "<f89v2.22,+P0;H>   ockhody.daiin.y!kam.s<-><!crease>ch!t!y.ch<!fold>y.cthey.dair!air.cho!ol.loy.dair.cheo!daiin<$>\r\n"
				+ "<f89v2.22,+P0;F>   ockhody.daiin.y!kam.s<-><!crease>chcthy.ch<!fold>y.cthey.dair.!?r.cho!ol.loy.dair.cheo.daiin<$>\r\n"
				+ "<f89v2.22,+P0;L>   ockhody.daiin.o!kam.s<-><!crease>!!!!!??ee!!!!!!!.???ee.dair!yir.cho!ol.loy.dair.cheo!daiin<$>\r\n"
				+ "<f89v2.22,+P0;U>   ockhody.daiin.y,kam.s<-><!crease>ch!t!y.ch<!fold>y.cthey.dair,air.cho,ol.loy.dair.cheo,daiin<$>\r\n";
		doc = buildDocument(in, TranscriptionType.MAJORITY);
		assertEquals(doc.getText(),
				"ockhody.daiin.y!kam.s.!ch!t!y.ch!!!!!!y.cthey.dair!air.cho!ol.loy.dair.cheo!daiin<$>");

		in = "#=IVTFF Eva- 1.5\n" + "<f1r>         <! $I=H $Q=C $P=E $L=A $H=1> \n"
				+ "<f1r.13,+P0;H>	shoy.ckhey.kodaiin.cphy.cphodaiils.cthey.she.oldain.d!\r\n"
				+ "<f1r.13,+P0;C>	shoy.ckhey.kodaiin.cphy.cphodaiils.cthey.sho.oldain.d!\r\n"
				+ "<f1r.13,+P0;F>	shoy.ckhey.kodaiin.cphy.cph?daiils.cthey.she.oldain.d!\r\n"
				+ "<f1r.13,+P0;N>	shoy.ckhey.kodaiin.cphy.cphodaiils.cthey.she.oldain,d!\r\n"
				+ "<f1r.13,+P0;U>	shoy.ckhey.kodaiin.cphy.cphoda??ls.cthey.shy.oldain.d?\r\n";
		doc = buildDocument(in, TranscriptionType.MAJORITY);
		assertEquals(doc.getText(), "shoy.ckhey.kodaiin.cphy.cphodaiils.cthey.she.oldain.d!");

		in = "#=IVTFF Eva- 1.5\n" + "<f17r>         <! $I=H $Q=C $P=E $L=A $H=1> \n"
				+ "<f17r.6,+P0;H>	tsho.qof!cho.qokcheor.chekeg<$>\r\n"
				+ "<f17r.6,+P0;C>	tsho.qof.cho.qokcheor.choto?<$>\r\n"
				+ "<f17r.6,+P0;F>	tsho.qof!cho.qokcheor.chekem<$>\r\n"
				+ "<f17r.6,+P0;U>	tsho.qof,cho.qokcheor.chetom<$>";
		doc = buildDocument(in, TranscriptionType.MAJORITY);
		assertEquals(doc.getText(), "tsho.qof!cho.qokcheor.che??m<$>");

		in = "#=IVTFF Eva- 1.5\n" + "<f1r>         <! $I=H $Q=C $P=E $L=A $H=1> \n" + "<f1r.6,+P0;H>	..!!!!%??\r\n"
				+ "<f1r.6,+P0;C>	.%!!!!%??\r\n" + "<f1r.6,+P0;F>	%%.!.!%%?\r\n" + "<f1r.6,+P0;N>	.%%.%.%%%\r\n"
				+ "<f1r.6,+P0;U>	%...%,%%%\r\n";
		doc = buildDocument(in, TranscriptionType.MAJORITY);
		assertEquals(doc.getText(), ".%.!%!%%?");
	}

	/**
	 * Builds an IvtffText with content from given String, it then creates a version
	 * of it based on type and returns it.
	 * 
	 * @param text Content of the document.
	 * @param type Type of transcription to build (CONCORDANCE / MAJORITY).
	 * @throws ParseException
	 * @throws IOException
	 */
	private static IvtffText buildDocument(String text, TranscriptionType type) throws IOException, ParseException {
		StringWriter outDoc = new StringWriter();
		try (BufferedReader in = new BufferedReader(new StringReader(text));
				BufferedWriter out = new BufferedWriter(outDoc);) {

			BuildConcordanceVersion.doWork(in, out);
			IvtffText result = new IvtffText(outDoc.toString());

			switch (type) {
			case MAJORITY:
				return result
						.filterLines(new LineFilter.Builder().transcriber(VoynichFactory.MAJORITY_TRANSCRIBER).build());
			case CONCORDANCE:
				return result.filterLines(
						new LineFilter.Builder().transcriber(VoynichFactory.CONCORDANCE_TRANSCRIBER).build());
			case INTERLINEAR:
				return result;
			default:
				throw new IllegalArgumentException("Unsupported transcription type");
			}
		}
	}

	/**
	 * This tests that when , appear in text, they appear only where other spaces are there......more or less.
	 */
	@Test
	@DisplayName("Test Usage of Spaces")
	public void doTestSpaces() {
		try {
			// The document to process, divided in lines
			StringWriter outDoc = new StringWriter();
			try (BufferedReader in = new BufferedReader(new FileReader(BuildConcordanceVersion.INPUT_FILE));
					BufferedWriter out = new BufferedWriter(outDoc);) {

				BuildConcordanceVersion.doWork(in, out);
				IvtffText doc = new IvtffText(outDoc.toString());
				doTest(doc, VoynichFactory.MAJORITY_TRANSCRIBER);
				doTest(doc, VoynichFactory.CONCORDANCE_TRANSCRIBER);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Completed.");
		}
	}

	/**
	 * @param doc
	 * @throws ParseException
	 */
	private static void doTest(IvtffText doc, String transcriptonType) throws ParseException {
		Map<String, IvtffText> groups = doc.splitLines(new LineSplitter.Builder().byPage().byNumber().build());

		for (IvtffText docIvtffText : groups.values()) {
			// Concordance or transcription version of the text to be tested
			IvtffText toTest = docIvtffText.filterLines(new ElementFilter<IvtffLine>() {

				@Override
				public boolean keep(IvtffLine element) {
					// TODO Auto-generated method stub
					return element.getDescriptor().getTranscriber().equals(transcriptonType);
				}
			});

			System.out.println(toTest.getLines().get(0));

			// Other transcriptions of same line
			IvtffText intelinear = docIvtffText.filterLines(new ElementFilter<IvtffLine>() {

				@Override
				public boolean keep(IvtffLine element) {
					// TODO Auto-generated method stub
					return !element.getDescriptor().getTranscriber().equals(VoynichFactory.CONCORDANCE_TRANSCRIBER)
							&& !element.getDescriptor().getTranscriber().equals(VoynichFactory.MAJORITY_TRANSCRIBER);
				}
			});

			// Align lines for comparison; first line is the concordance version
			List<IvtffLine> lines = new ArrayList<>();
			lines.addAll(intelinear.getLines());
			IvtffLine.align(lines); // notice we must align here, to make sure concordance does not influence
									// alignment
			lines.add(0, toTest.getLines().get(0));

			// do the check
			for (int i = 0; i < lines.get(0).getText().length(); ++i) {
				char c = lines.get(0).getText().charAt(i);
				if (c != ',')
					continue;
				for (int j = 1; j < lines.size(); ++j) {
					char c1 = lines.get(j).getText().charAt(i);
					if (!doc.getAlphabet().isWordSeparator(c1) && (c1 != '!')) {
//						System.out.println();
//						for (int k = 1; k < lines.size(); ++k) {
//							System.out.println(lines.get(k));
//						}
//						System.out.println(lines.get(0));
//						System.out.println(lines.get(0).getText().substring(0,i) + " [" + i + "]");
						throw new IllegalArgumentException();
					}
				}
			}

		} // for each group
	}
}
