/**
 * 
 */
package io.github.mzattera.v4j.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mattera.v4j.text.alphabet.Alphabet;

/**
 * 
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class TextNormalizationTest {

	@Test
	@DisplayName("Alphabet.EVA.toPlainText() works")
	public void doTest() {

		// test auto-alignment
		String txt = "..ochol.,qol!!,daiin<~>.aii?.{cht}ol{$}..";
		String r = Alphabet.EVA.toPlainText(txt);
		assertEquals(r, "ochol.qol.daiin.aii?.chtol");
	}
}
