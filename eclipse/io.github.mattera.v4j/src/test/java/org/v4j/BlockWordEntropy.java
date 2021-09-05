/**
 * 
 */
package org.v4j;

import io.github.mattera.v4j.util.Counter;
import io.github.mattera.v4j.util.MathUtil;

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
		
		assert (MathUtil.entropy(c) == 3.219528282299548);
	}
}
