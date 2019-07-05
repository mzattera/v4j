/**
 * 
 */
package org.v4j.experiment.instance;

import java.util.Collections;
import java.util.Random;

import org.v4j.experiment.TextRandomizationProcess;
import org.v4j.text.alphabet.Alphabet;
import org.v4j.text.ivtff.IvtffLine;
import org.v4j.text.ivtff.IvtffPage;
import org.v4j.text.ivtff.ParseException;
import org.v4j.util.StringUtil;

/**
 * Randomly shuffles words in a page. It does so by shuffling words in each
 * line, then shuffling lines. This ensures the size of the resulting text (in
 * terms of number of rows and columns) is the same than the original text.
 * 
 * @author Massimiliano_Zattera
 *
 */
public class WordsInPageRandomizer extends TextRandomizationProcess<IvtffPage> {

	public WordsInPageRandomizer() {
		super();
	}

	public WordsInPageRandomizer(Random rnd) {
		super(rnd);
	}

	@Override
	public IvtffPage randomize(IvtffPage txt) {

		Alphabet a = txt.getAlphabet();
		IvtffPage result = new IvtffPage(txt.getDescriptor(), a);

		// Shuffle text in each line
		for (IvtffLine line : txt.getElements()) {
			IvtffLine l = new IvtffLine(line);
			try {
				l.setText(StringUtil.shuffleWords(line.getPlainText(), a, getRandom()));
			} catch (ParseException e) {
				throw new RuntimeException(); // translates any parse exception that might occur
			}
			result.addElement(l);
		}

		// shuffle lines
		Collections.shuffle(result.getElements(), getRandom());

		return result;
	}

}
