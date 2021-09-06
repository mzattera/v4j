/**
 * 
 */
package io.github.mzattera.v4j.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.ParseException;
import io.github.mattera.v4j.util.Counter;

/**
 * Tests Text.getWords()
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class WordCountTest {

	private static final String txt = "#=IVTFF Eva- 1.5.2018092200\n" + 
			"#\n" + 
			"<f3v> <! $I=H $Q=A $P=F $L=A $H=1>\n"
			+ "<f3v.4,+P0;m>	qodar.??s.eey.kcheol!okal.do.r.chear.een<-><!plant>\n"
			+ "<f3v.5,+P0;m>	.ychear.otchal.char..char.ckhy!<-><!plant>\n"
			+ "<f3v.6,+P0;m>	!or.chear.kor.chodaly.chom<$><!plant>\n";

	@Test
	@DisplayName("Tezxt.getWords() works")
	public void doTest() throws IOException, ParseException {

		IvtffText doc = new IvtffText(txt);

		assertEquals (doc.getText(), "qodar.??s.eey.kcheol!okal.do.r.chear.een<-><!plant>..ychear.otchal.char..char.ckhy!<-><!plant>.!or.chear.kor.chodaly.chom<$><!plant>");
		assertEquals (doc.getPlainText(), "qodar.??s.eey.kcheolokal.do.r.chear.een.ychear.otchal.char.char.ckhy.or.chear.kor.chodaly.chom");

		Counter<String> c = doc.getWords(false);
		assertEquals (c.size(), 16);
		assertEquals (c.getCount("char"), 2);
		assertEquals (c.getCount("chear"), 2);
		assertEquals (c.getCount("ckhy"), 1);
		assertEquals (c.getCount("chodaly"), 1);
		assertEquals (c.getCount("chom"), 1);
		assertEquals (c.getCount("do"), 1);
		assertEquals (c.getCount("een"), 1);
		assertEquals (c.getCount("eey"), 1);
		assertEquals (c.getCount("kcheolokal"), 1);
		assertEquals (c.getCount("kor"), 1);
		assertEquals (c.getCount("or"), 1);
		assertEquals (c.getCount("otchal"), 1);
		assertEquals (c.getCount("qodar"), 1);
		assertEquals (c.getCount("r"), 1);
		assertEquals (c.getCount("ychear"), 1);
		assertEquals (c.getCount("??s"), 1);

		c = doc.getWords(true);
		assertEquals (c.size(), 15);
		assertEquals (c.getCount("char"), 2);
		assertEquals (c.getCount("chear"), 2);
		assertEquals (c.getCount("ckhy"), 1);
		assertEquals (c.getCount("chodaly"), 1);
		assertEquals (c.getCount("chom"), 1);
		assertEquals (c.getCount("do"), 1);
		assertEquals (c.getCount("een"), 1);
		assertEquals (c.getCount("eey"), 1);
		assertEquals (c.getCount("kcheolokal"), 1);
		assertEquals (c.getCount("kor"), 1);
		assertEquals (c.getCount("or"), 1);
		assertEquals (c.getCount("otchal"), 1);
		assertEquals (c.getCount("qodar"), 1);
		assertEquals (c.getCount("r"), 1);
		assertEquals (c.getCount("ychear"), 1);
	}
}
