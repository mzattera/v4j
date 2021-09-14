/**
 * 
 */
package io.github.mzattera.v4j.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.ParseException;
import io.github.mattera.v4j.util.Counter;

/**
 * @author maxi
 * 
 */
public final class GetWordsTest {

	@Test
	@DisplayName("Text.getWords(boolean) and therefore CountWords work - simple")
	public void doTest() throws Exception {
		String in = "#=IVTFF Eva- 1.5\n" + "<f1r>         <! $I=H $Q=C $P=E $L=A $H=1>\n"
				+ "<f1r.1,@P0;H>       fachyy.ykal,a!!ataiin,shol,sho?y.fachyy.,shory,ct%%%ys.ct%%%ys.y.kor.sh!oldy\n";

		IvtffText doc = new IvtffText(in.toString());

		List<String> words = new ArrayList<String>();
		words.add("fachyy");
		words.add("ykal");
		words.add("aataiin");
		words.add("shol");
		words.add("shory");
		words.add("kor");
		words.add("y");
		words.add("sholdy");

		List<String> wordsUnreadable = new ArrayList<String>();
		wordsUnreadable.addAll(words);
		wordsUnreadable.add("sho?y");
		wordsUnreadable.add("ct???ys");

		// Gets all words
		Counter<String> m = doc.getWords(false);
		assertEquals(m.size(), wordsUnreadable.size());
		for (String w : wordsUnreadable) {
			assertTrue(m.itemSet().contains(w));
			if (w.equals("fachyy") || w.equals("ct???ys")) {
				assertEquals(m.getCount(w), 2);
			} else {
				assertEquals(m.getCount(w), 1);
			}
		}

		// Gets onle readable words
		m = doc.getWords(true);
		assertEquals(m.size(), words.size());
		for (String w : words) {
			assertTrue(m.itemSet().contains(w));
			if (w.equals("fachyy") || w.equals("ct???ys")) {
				assertEquals(m.getCount(w), 2);
			} else {
				assertEquals(m.getCount(w), 1);
			}
		}
	}

	private static final String txt = "#=IVTFF Eva- 1.5.2018092200\n" + "#\n" + "<f3v> <! $I=H $Q=A $P=F $L=A $H=1>\n"
			+ "<f3v.4,+P0;m>	qodar.??s.eey.kcheol!okal.do.r.chear.een<-><!plant>\n"
			+ "<f3v.5,+P0;m>	.ychear.otchal.char..char.ckhy!<-><!plant>\n"
			+ "<f3v.6,+P0;m>	!or.chear.kor.chodaly.chom<$><!plant>\n";

	@Test
	@DisplayName("Text.getWords(boolean) and therefore CountWords work - cooler")
	public void doTest2() throws IOException, ParseException {

		IvtffText doc = new IvtffText(txt);

		assertEquals(doc.getText(),
				"qodar.??s.eey.kcheol!okal.do.r.chear.een<-><!plant>..ychear.otchal.char..char.ckhy!<-><!plant>.!or.chear.kor.chodaly.chom<$><!plant>");
		assertEquals(doc.getPlainText(),
				"qodar.??s.eey.kcheolokal.do.r.chear.een.ychear.otchal.char.char.ckhy.or.chear.kor.chodaly.chom");

		Counter<String> c = doc.getWords(false);
		assertEquals(c.size(), 16);
		assertEquals(c.getCount("char"), 2);
		assertEquals(c.getCount("chear"), 2);
		assertEquals(c.getCount("ckhy"), 1);
		assertEquals(c.getCount("chodaly"), 1);
		assertEquals(c.getCount("chom"), 1);
		assertEquals(c.getCount("do"), 1);
		assertEquals(c.getCount("een"), 1);
		assertEquals(c.getCount("eey"), 1);
		assertEquals(c.getCount("kcheolokal"), 1);
		assertEquals(c.getCount("kor"), 1);
		assertEquals(c.getCount("or"), 1);
		assertEquals(c.getCount("otchal"), 1);
		assertEquals(c.getCount("qodar"), 1);
		assertEquals(c.getCount("r"), 1);
		assertEquals(c.getCount("ychear"), 1);
		assertEquals(c.getCount("??s"), 1);

		c = doc.getWords(true);
		assertEquals(c.size(), 15);
		assertEquals(c.getCount("char"), 2);
		assertEquals(c.getCount("chear"), 2);
		assertEquals(c.getCount("ckhy"), 1);
		assertEquals(c.getCount("chodaly"), 1);
		assertEquals(c.getCount("chom"), 1);
		assertEquals(c.getCount("do"), 1);
		assertEquals(c.getCount("een"), 1);
		assertEquals(c.getCount("eey"), 1);
		assertEquals(c.getCount("kcheolokal"), 1);
		assertEquals(c.getCount("kor"), 1);
		assertEquals(c.getCount("or"), 1);
		assertEquals(c.getCount("otchal"), 1);
		assertEquals(c.getCount("qodar"), 1);
		assertEquals(c.getCount("r"), 1);
		assertEquals(c.getCount("ychear"), 1);
	}
}