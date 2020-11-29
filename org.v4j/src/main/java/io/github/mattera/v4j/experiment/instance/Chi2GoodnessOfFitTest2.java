/**
 * 
 */
package io.github.mattera.v4j.experiment.instance;

import org.apache.commons.math3.stat.inference.ChiSquareTest;

import io.github.mattera.v4j.experiment.StatisticalTest;
import io.github.mattera.v4j.util.MathUtil;

/**
 * Performs a Chi-squared test comparing two observed frequency
 * to see if they belong to same population.
 * 
 * @author Massimiliano_Zattera
 *
 */
public class Chi2GoodnessOfFitTest2 extends StatisticalTest<long[][]> {

	/*
	 * For testing
	 */
//	public static void main(String[] argv) {
//		long[][] observed = {{388, 322, 314, 316, 344, 316}};
//		long[][] expected = {{333, 333, 333, 333, 333, 333}};
//		long[] aobserved = MathUtil.rectify(observed);
//		double[] dexpected = {333.333, 333.333, 333.333, 333.333, 333.333, 333.333};
//		
//		Chi2GoodnessOfFitTest inst = new Chi2GoodnessOfFitTest();
//		System.out.println(test.chiSquare(dexpected, aobserved));
//		System.out.println(test.chiSquareTest(dexpected, aobserved));
//		System.out.println(inst.rejectNullHypothesis(observed, expected, 0.05));
//		System.out.println(inst.pValue(observed, expected));
//	}

	private static final ChiSquareTest test = new ChiSquareTest();

	@Override
	public double pValue(long[][] observed, long[][] expected) {

		// re-shape measures into linear arrays
		long[] O2 = MathUtil.rectify(expected);
		long[] O = MathUtil.rectify(observed);

		// tests that observed and expected match criteria for chi-squared test
		long tot = 0;
		for (int i = 0; i < O.length; ++i) {
			if (O[i] < 10)
				throw new IllegalArgumentException("For this test to work, each measure must be >=10.");
			tot += O[i];
		}
		if (tot < 200)
			throw new IllegalArgumentException("For this test to work, At least 200 observations are needed.");

		return test.chiSquareTestDataSetsComparison(O2, O);
	}
}
