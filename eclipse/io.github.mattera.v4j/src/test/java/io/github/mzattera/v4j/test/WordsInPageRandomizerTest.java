/**
 * 
 */
package io.github.mzattera.v4j.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Random;

import org.junit.jupiter.api.Test;

import io.github.mattera.v4j.experiment.Measurement;
import io.github.mattera.v4j.experiment.TextRandomizationProcess;
import io.github.mattera.v4j.experiment.instance.MeasureUniqueWordPosition;
import io.github.mattera.v4j.experiment.instance.WordsInPageRandomizer;
import io.github.mattera.v4j.text.ivtff.IvtffPage;
import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.ParseException;

/**
 * @author Massimiliano_Zattera
 *
 */
public final class WordsInPageRandomizerTest {

	private static final String txt = "#=IVTFF Eva- 1.5.2018092200\n" + "#\n" + "<f3v> <! $I=H $Q=A $P=F $L=A $H=1>\n"
			+ "<f3v.4,+P0;m>	qodar.??s.eey.kcheol!okal.do.r.chear.een<-><!plant>\n"
			+ "<f3v.5,+P0;m>	.ychear.otchal.char..char.ckhy!<-><!plant>\n"
			+ "<f3v.6,+P0;m>	!or.chear.kor.chodaly.chom<$><!plant>\n";

	private static final long[][] REF_MEAS = {
			{ 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0,
					0, 1, 1, 1 },
			{ 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0 },
			{ 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
					0, 0, 0, 0 } };

	@Test
	public void doTest() throws IOException, ParseException {

		Random rnd = new Random(666);
		Measurement<IvtffPage, long[][]> measurement = new MeasureUniqueWordPosition();
		TextRandomizationProcess<IvtffPage> randomizer = new WordsInPageRandomizer(rnd);

		IvtffText doc = new IvtffText(txt);

		assertEquals (doc.getPlainText(), 
				"qodar.??s.eey.kcheolokal.do.r.chear.een.ychear.otchal.char.char.ckhy.or.chear.kor.chodaly.chom");

		IvtffPage page = doc.getElements().get(0);
		IvtffPage shuf = randomizer.randomize(doc.getElements().get(0));
		assertEquals (shuf.getElements().get(0).getPlainText(), "char.char.ckhy.ychear.otchal");
		assertEquals (shuf.getElements().get(1).getPlainText(), "chear.kor.or.chom.chodaly");
		assertEquals (shuf.getElements().get(2).getPlainText(), "eey.chear.??s.do.een.kcheolokal.qodar.r");

		long[][] m1 = measurement.measure(page);
		compare(m1, REF_MEAS);
	}

	private static void compare(long[][] A, long[][] B) {
		assertEquals (A.length, B.length);
		assertEquals (A[0].length, B[0].length);
		for (int i = 0; i < A.length; ++i) {
			for (int j = 0; j < A[0].length; ++j) {
				assertEquals (A[i][j], B[i][j]);
			}
		}
	}
}
