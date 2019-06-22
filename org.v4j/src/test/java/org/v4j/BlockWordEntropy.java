/**
 * 
 */
package org.v4j;

import org.v4j.text.ivtff.IvtffText;
import org.v4j.util.Counter;
import org.v4j.util.MathUtil;

/**
 * Tests MathUtil.entropy()
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class BlockWordEntropy implements RegressionTest {

	private static final String txt = "in the beginning god created the heavens and the earth and the earth was waste and empty";

	@Override
	public void doTest() throws Exception {

		String[] w = txt.split(" ");
		Counter<String> c = new Counter<>();
		
		for (String s:w)
			c.count(s);
		
		System.out.println("Entropy: " + MathUtil.entropy(c));
	}
}
