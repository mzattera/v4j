/**
 * 
 */
package io.github.mzattera.v4j.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.mzattera.v4j.util.Counter;
import io.github.mzattera.v4j.util.MathUtil;

/**
 * Tests MathUtil.entropy()
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class MathUtilTest {

	private static final String txt = "in the beginning god created the heavens and the earth and the earth was waste and empty";

	@Test
	@DisplayName("MathUtil.entropy() works")
	public void entropy() {
		String[] w = txt.split(" ");
		Counter<String> c = new Counter<>();

		for (String s : w)
			c.count(s);

		assertEquals(MathUtil.entropy(c), 3.219528282299548);
	}

	@Test
	@DisplayName("MathUtil.shrink() works")
	public void shrink() {
		long[][] A = { { 1, 2, 3, 4, 5, 6, 7 }, { 1, 2, 3, 4, 5, 6, 7 }, { 1, 2, 3, 4, 5, 6, 7 } };
		long[][] R = { { 12, 18, 26 }, { 6, 9, 13 } };
		compare(MathUtil.shrink(A, 2, 3), R);
	}

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
