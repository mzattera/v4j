/**
 * 
 */
package org.v4j;

/**
 * @author Massimiliano "Maxi" Zattera
 * 
 */
public final class RunAllTests {

	/**
	 * Runs all regression tests.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		RegressionTest test;
		
		try {
			// Checks that assertions are enabled
			try {
				assert false : "Assertion test.";
				throw new IllegalStateException ("Assertions are not enabled. Please run this class by using -ea option for JVM.");
			} catch(AssertionError a) {			
			}

			test = new TestIvtffParsing();
			System.out.println("*** " + test.getClass().getName());
			test.doTest();

			test = new StringUtilTest();
			System.out.println("*** " + test.getClass().getName());
			test.doTest();

			System.out.println("*** Test completed successfully");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("*** Test FAILED");
		}
	}

}
