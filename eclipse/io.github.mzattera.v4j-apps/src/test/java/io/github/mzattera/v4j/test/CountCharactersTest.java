/* Copyright (c) 2021 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mzattera.v4j.text.alphabet.Alphabet;
import io.github.mzattera.v4j.text.ivtff.IvtffText;
import io.github.mzattera.v4j.util.Counter;

/**
 * Tests that counting chars works.
 * 
 * @author Massimiliano "Maxi" Zattera
 * 
 */
public final class CountCharactersTest {

	private final static char[] FILLER = { '!', '?', '%', ',' };

	@Test
	@DisplayName("Text.getChars() and therefore CountCharacters work")
	public void doTest() throws Exception {
		Alphabet a = Alphabet.EVA;
		char[] c = a.getRegularChars();
		int[] count = new int[c.length];
		Random rnd = new Random(System.currentTimeMillis());
		StringBuffer txt = new StringBuffer();

		txt.append("#=IVTFF Eva- 1.5\n");
		txt.append("<f1r>         <! $I=H $Q=C $P=E $L=A $H=1> \n");
		txt.append("<f1r.1,@P0;H>");

		for (int i = 0; i < 10000;) {
			int l = rnd.nextInt(15);
			for (int j = 0; j < l; ++j) {
				int p = rnd.nextInt(c.length);
				txt.append(c[p]);
				++count[p];
				txt.append(FILLER[rnd.nextInt(FILLER.length)]);
				i += 2;
			}
			txt.append(a.getSpace());
			++i;
		}

		IvtffText doc = new IvtffText(txt.toString());
		Counter<Character> map = doc.getChars();
		for (int i = 0; i < c.length; ++i) {
			assertEquals(count[i], map.getCount(c[i]));
		}
	}
}