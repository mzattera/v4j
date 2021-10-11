/**
 * 
 */
package io.github.mzattera.v4j.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.text.ivtff.ParseException;
import io.github.mzattera.v4j.util.Counter;

/**
 * Tests io.github.mattera.v4j.applications.CountNWords.process()
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class CountNWordsTest {

	private static final String txt = "#=IVTFF Eva- 1.5.2018092200\n" + "#\n" + "<f3v> <! $I=H $Q=A $P=F $L=A $H=1>\n"
			+ "<f3v.4,+P0;m>	qodar.??s.eey.kcheol!okal.do.r.chear.een<-><!plant>\n"
			+ "<f3v.5,+P0;m>	.ychear.otchal.char..char.ckhy!<-><!plant>\n"
			+ "<f3v.6,+P0;m>	!or.chear.kor.chodaly.chom<$><!plant>\n";

	@Test
	@DisplayName("CountNWords.process() works")
	public void doTest() {

		IvtffText doc;
		try {
			doc = new IvtffText(txt);
		} catch (IOException | ParseException e1) {
			fail("Cannot create IVTFF document.");
			return;
		}

		assertEquals(doc.getText(),
				"qodar.??s.eey.kcheol!okal.do.r.chear.een<-><!plant>..ychear.otchal.char..char.ckhy!<-><!plant>.!or.chear.kor.chodaly.chom<$><!plant>");
		assertEquals(doc.getPlainText(),
				"qodar.??s.eey.kcheolokal.do.r.chear.een.ychear.otchal.char.char.ckhy.or.chear.kor.chodaly.chom");

		Counter<String> c = io.github.mzattera.v4j.applications.CountNWords.process(doc, 3, false);
		assertEquals(c.size(), 16);
		assertEquals(c.getCount("qodar.??s.eey"), 1);
		assertEquals(c.getCount("??s.eey.kcheolokal"), 1);
		assertEquals(c.getCount("eey.kcheolokal.do"), 1);
		assertEquals(c.getCount("kcheolokal.do.r"), 1);
		assertEquals(c.getCount("do.r.chear"), 1);
		assertEquals(c.getCount("r.chear.een"), 1);
		assertEquals(c.getCount("chear.een.ychear"), 1);
		assertEquals(c.getCount("een.ychear.otchal"), 1);
		assertEquals(c.getCount("ychear.otchal.char"), 1);
		assertEquals(c.getCount("otchal.char.char"), 1);
		assertEquals(c.getCount("char.char.ckhy"), 1);
		assertEquals(c.getCount("char.ckhy.or"), 1);
		assertEquals(c.getCount("ckhy.or.chear"), 1);
		assertEquals(c.getCount("or.chear.kor"), 1);
		assertEquals(c.getCount("chear.kor.chodaly"), 1);
		assertEquals(c.getCount("kor.chodaly.chom"), 1);

		c = io.github.mzattera.v4j.applications.CountNWords.process(doc, 3, true);
		assertEquals(c.size(), 14);
		assertEquals(c.getCount("eey.kcheolokal.do"), 1);
		assertEquals(c.getCount("kcheolokal.do.r"), 1);
		assertEquals(c.getCount("do.r.chear"), 1);
		assertEquals(c.getCount("r.chear.een"), 1);
		assertEquals(c.getCount("chear.een.ychear"), 1);
		assertEquals(c.getCount("een.ychear.otchal"), 1);
		assertEquals(c.getCount("ychear.otchal.char"), 1);
		assertEquals(c.getCount("otchal.char.char"), 1);
		assertEquals(c.getCount("char.char.ckhy"), 1);
		assertEquals(c.getCount("char.ckhy.or"), 1);
		assertEquals(c.getCount("ckhy.or.chear"), 1);
		assertEquals(c.getCount("or.chear.kor"), 1);
		assertEquals(c.getCount("chear.kor.chodaly"), 1);
		assertEquals(c.getCount("kor.chodaly.chom"), 1);
	}
}
