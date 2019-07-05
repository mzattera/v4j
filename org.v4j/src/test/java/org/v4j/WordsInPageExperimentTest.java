/**
 * 
 */
package org.v4j;

import java.util.Random;

import org.v4j.experiment.Measurement;
import org.v4j.experiment.StatisticalTest;
import org.v4j.experiment.TextRandomizationProcess;
import org.v4j.experiment.instance.Chi2GoodnessOfFitTest;
import org.v4j.experiment.instance.MeasureUniqueWordPosition;
import org.v4j.experiment.instance.WordsInPageRandomizer;
import org.v4j.text.ivtff.IvtffPage;
import org.v4j.text.ivtff.IvtffText;

/**
 * @author Massimiliano_Zattera
 *
 */
public class WordsInPageExperimentTest implements RegressionTest {

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

	@Override
	public void doTest() throws Exception {

		Random rnd = new Random(666);
		Measurement<IvtffPage, long[][]> measurement = new MeasureUniqueWordPosition();
		StatisticalTest<long[][]> test = new Chi2GoodnessOfFitTest();
		TextRandomizationProcess<IvtffPage> randomizer = new WordsInPageRandomizer(rnd);

		IvtffText doc = new IvtffText(txt);

		assert (doc.getPlainText().equals(
				"qodar.??s.eey.kcheolokal.do.r.chear.een.ychear.otchal.char.char.ckhy.or.chear.kor.chodaly.chom"));

		IvtffPage page = doc.getElements().get(0);
		IvtffPage shuf = randomizer.randomize(doc.getElements().get(0));
		assert (shuf.getElements().get(0).getPlainText().equals("char.char.ckhy.ychear.otchal"));
		assert (shuf.getElements().get(1).getPlainText().equals("chear.kor.or.chom.chodaly"));
		assert (shuf.getElements().get(2).getPlainText().equals("eey.chear.??s.do.een.kcheolokal.qodar.r"));

		long[][] m1 = measurement.measure(page);
		compare(m1, REF_MEAS);
	}

//	private static void printMeasure(long[][] ls) {
//		System.out.println("{");
//		for (int i = 0; i < ls.length; ++i) {
//			System.out.print("{");
//			for (int j = 0; j < ls[0].length; ++j) {
//				System.out.print(ls[i][j] + ", ");
//			}
//			System.out.println("},");
//		}
//		System.out.println("}");
//	}

	private static void compare(long[][] A, long[][] B) {
		assert (A.length == B.length);
		assert (A[0].length == B[0].length);
		for (int i = 0; i < A.length; ++i) {
			for (int j = 0; j < A[0].length; ++j) {
				assert (A[i][j] == B[i][j]);
			}
		}
	}
}
