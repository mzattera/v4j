/**
 * 
 */
package io.github.mzattera.v4j.test;

import java.util.Collection;
import java.util.Random;

import io.github.mattera.v4j.experiment.Measurement;
import io.github.mattera.v4j.experiment.StatisticalTest;
import io.github.mattera.v4j.experiment.TextRandomizationProcess;
import io.github.mattera.v4j.experiment.instance.Chi2GoodnessOfFitTest;
import io.github.mattera.v4j.experiment.instance.Chi2GoodnessOfFitTest2;
import io.github.mattera.v4j.experiment.instance.MeasureUniqueWordPosition;
import io.github.mattera.v4j.experiment.instance.WordsInPageRandomizer;
import io.github.mattera.v4j.text.ivtff.IvtffPage;
import io.github.mattera.v4j.text.ivtff.IvtffText;
import io.github.mattera.v4j.text.ivtff.LineFilter;
import io.github.mattera.v4j.text.ivtff.PageFilter;
import io.github.mattera.v4j.text.ivtff.VoynichFactory;
import io.github.mattera.v4j.text.ivtff.VoynichFactory.TranscriptionType;
import io.github.mattera.v4j.util.MathUtil;

/**
 * @author Massimiliano_Zattera
 *
 */
public class WordsInPageExperimentTest implements RegressionTest {

	private static final int REF_POP_SIZE = 100;

	@Override
	public void doTest() throws Exception {

		Random rnd = new Random(666);
		Measurement<IvtffPage, long[][]> measurement = new MeasureUniqueWordPosition(2, 2);
		StatisticalTest<long[][]> test = new Chi2GoodnessOfFitTest();
		StatisticalTest<long[][]> test2 = new Chi2GoodnessOfFitTest2();
		TextRandomizationProcess<IvtffPage> randomizer = new WordsInPageRandomizer(rnd);
//		StatisticalHypothesisTestExperiment<IvtffPage, long[][]> exp = new RandomizedTextExperiment<IvtffPage, long[][]>(
//				measurement, test, randomizer, REF_POP_SIZE);

		// Keep only BB1 paragraphs
		IvtffText voy = VoynichFactory.getDocument(TranscriptionType.MAJORITY);
		voy = voy.filterPages(new PageFilter.Builder().cluster("BB1").build());
		voy = voy.filterLines(new LineFilter.Builder().genericLocusType("P").build());

		Collection<IvtffPage> p1 = voy.getElements();
		Collection<IvtffPage> p0 = randomizer.randomize(p1, p1.size() * REF_POP_SIZE);
		Collection<IvtffPage> pr = randomizer.randomize(p1, p1.size());

		long[][] m1 = measurement.measure(p1);
		long[][] m0 = measurement.measure(p0);
		long[][] mr = measurement.measure(pr);

		printMeasure(m1, 1, 1);
		printMeasure(m0, 1, 1);
		printMeasure(mr, 1, 1);

		printMeasure(MathUtil.normalize(MathUtil.rectify(m0)));

		System.out.println(test.pValue(m0, m0));
		System.out.println(test.pValue(mr, m0));
		System.out.println(test.pValue(m1, m0));

		System.out.println(test2.pValue(m0, m0));
		System.out.println(test2.pValue(mr, m0));
		System.out.println(test2.pValue(m1, m0));
	}

	private static void printMeasure(long[][] ls, int m, int n) {
		printMeasure(MathUtil.shrink(ls, m, n));
	}

	private static void printMeasure(double[] ls) {
		System.out.println("{");
		for (int i = 0; i < ls.length; ++i) {
			System.out.print(ls[i] + ", ");
		}
		System.out.println("}");
	}

	private static void printMeasure(long[][] ls) {
		System.out.println("{");
		for (int i = 0; i < ls.length; ++i) {
			System.out.print("{");
			for (int j = 0; j < ls[0].length; ++j) {
				System.out.print(ls[i][j] + ", ");
			}
			System.out.println("},");
		}
		System.out.println("}");
	}
/*
	private static void compare(long[][] A, long[][] B) {
		assert (A.length == B.length);
		assert (A[0].length == B[0].length);
		for (int i = 0; i < A.length; ++i) {
			for (int j = 0; j < A[0].length; ++j) {
				assert (A[i][j] == B[i][j]);
			}
		}
	}
	*/
}
