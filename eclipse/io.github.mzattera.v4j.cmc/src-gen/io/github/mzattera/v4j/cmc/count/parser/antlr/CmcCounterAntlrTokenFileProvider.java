/*
 * generated by Xtext 2.28.0
 */
package io.github.mzattera.v4j.cmc.count.parser.antlr;

import java.io.InputStream;
import org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider;

public class CmcCounterAntlrTokenFileProvider implements IAntlrTokenFileProvider {

	@Override
	public InputStream getAntlrTokenFile() {
		ClassLoader classLoader = getClass().getClassLoader();
		return classLoader.getResourceAsStream("io/github/mzattera/v4j/cmc/count/parser/antlr/internal/InternalCmcCounter.tokens");
	}
}
