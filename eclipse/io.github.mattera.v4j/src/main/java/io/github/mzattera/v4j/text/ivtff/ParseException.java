package io.github.mzattera.v4j.text.ivtff;

/**
 * Thrown when an error occurs parsing a file.
 *
 * @author Massimiliano "Maxi" Zattera
 */
public class ParseException extends java.lang.Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of <code>ParseException</code> without detail message.
	 */
	public ParseException() {
		super();
	}

	/**
	 * Constructs an instance of <code>ParseException</code> with the specified
	 * detail message.
	 * 
	 * @param msg
	 *            the detail message.
	 */
	public ParseException(String msg) {
		super(msg);
	}

	/**
	 * Constructs an instance of <code>ParseException</code> with the specified
	 * detail message.
	 * 
	 * @param msg
	 *            the detail message. "param row the row causing the parse error.
	 * @param row
	 *            Row in the input file caousing the error.
	 */
	public ParseException(String msg, String row) {
		super(msg + ": " + row);
	}

	/**
	 * Constructs an instance of <code>ParseException</code> with the specified
	 * detail message.
	 * 
	 * @param msg
	 *            the detail message. "param row the row causing the parse error.
	 * @param row
	 *            Row in the input file caousing the error.
	 * @param rowNumber
	 *            Row number in the input file.
	 */
	public ParseException(String msg, String row, int rowNum) {
		super(((rowNum > 0) ? "Line " + rowNum : "") + ": " + msg + ": " + row);
	}
}
