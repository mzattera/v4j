package io.github.mattera.v4j.applications.slot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.mattera.v4j.text.ElementFilter;
import io.github.mattera.v4j.text.alphabet.Alphabet;
import io.github.mattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mattera.v4j.text.ivtff.IvtffLine;
import io.github.mattera.v4j.text.ivtff.IvtffPage;
import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.ParseException;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.Transcription;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;

/**
 * Funny check needed as regression test.
 * Creating the SLOT transcription, using fromEva(getText()).getPLainText() produced different results than 
 * performing conversion on the fly calling frmEva(getPlainText()).
 * This checks they match
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class FromEva {

	/**
	 * Which transcription to use.
	 */
	public static final Transcription TRANSCRIPTION = Transcription.AUGMENTED;

	/**
	 * Which transcription type to use.
	 */
	public static final TranscriptionType TRANSCRIPTION_TYPE = TranscriptionType.CONCORDANCE;

	/** Filter to use on pages before analysis */
	public static final ElementFilter<IvtffPage> FILTER = null;

	private FromEva() {
	}

	@Test
	public void doTest() throws IOException, ParseException, URISyntaxException {

		IvtffText slot = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, Alphabet.SLOT);
		if (Slots.FILTER != null)
			slot = slot.filterPages(Slots.FILTER);
		List<IvtffLine> sLines = slot.getLines();

		IvtffText eva = VoynichFactory.getDocument(TRANSCRIPTION, TRANSCRIPTION_TYPE, Alphabet.EVA);
		if (FILTER != null)
			eva = eva.filterPages(FILTER);
		List<IvtffLine> eLines = eva.getLines();

		for (int i = 0; i < sLines.size(); ++i) {
			String s = sLines.get(i).getPlainText();
			String e = SlotAlphabet.fromEva(eLines.get(i).getPlainText());

			assertEquals(s,e);
		}
	}
}
