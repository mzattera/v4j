/**
 * 
 */
package org.v4j;

import io.github.mattera.v4j.text.alphabet.Alphabet;

/**
 * 
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class TextNormalization implements RegressionTest {

	@Override
	public void doTest() throws Exception {

		// test auto-alignment
		String txt = "..ochol.,qol!!,daiin<~>.aii?.{cht}ol{$}..";
		String r = Alphabet.EVA.toPlainText(txt);
		assert r.equals("ochol.qol.daiin.aii?.chtol");
	}
}
