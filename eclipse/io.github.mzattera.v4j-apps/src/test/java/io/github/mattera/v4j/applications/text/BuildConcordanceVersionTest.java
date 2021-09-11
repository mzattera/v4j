package io.github.mattera.v4j.applications.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.LineFilter;
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
		assertEquals(doc.getText(), "f?????.?kal?a???taiin.shol.shory.???!??s.y?kor.sholdy!<->");
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
		assertEquals(doc.getText(), "fa?hys.ykal.ar!ataiin.shol.shory.ct?!%es.y.kor.sholdy!<->");
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
}
