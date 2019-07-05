/**
 * 
 */
package org.v4j;

import org.v4j.util.MathUtil;

/**
 * @author Massimiliano_Zattera
 *
 */
public class Shrink implements RegressionTest {

	@Override
	public void doTest() throws Exception {
		long[][] A = { { 1, 2, 3, 4, 5, 6, 7 }, { 1, 2, 3, 4, 5, 6, 7 } , { 1, 2, 3, 4, 5, 6, 7 } };
		long[][] R = { { 12,18,26 }, { 6,9,13 } };

//		print(A);
//		print(MathUtil.shrink(A, 2, 3));

		compare(MathUtil.shrink(A, 2, 3), R);
	}

//	private static void print(long[][] A) {
//		for (int i = 0; i < A.length; ++i) {
//			for (int j = 0; j < A[0].length; ++j) {
//				System.out.print(A[i][j] + ",\t");
//			}
//			System.out.println();
//		}
//	}

	private static void compare(long[][] A, long[][] B) {
		assert (A.length == B.length);
		assert (A[0].length == B[0].length);
		for (int i = 0; i < A.length; ++i) {
			for (int j = 0; j < A[0].length; ++j) {
				assert(A[i][j] == B[i][j]);
			}
		}
	}
}
