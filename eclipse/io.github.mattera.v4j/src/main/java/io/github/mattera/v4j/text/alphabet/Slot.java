package io.github.mattera.v4j.text.alphabet;

/**
 * "Slot" alphabet based on "slot" theory.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class Slot extends Alphabet {

	/**
	 * 
	 */
	public Slot() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getCodeString() {
		return "Slot";
	}

	// TODO: Temporary, needs to be perfected
	private final static char[] regularChars = new char[('z' - 'a' + 1) * 2];
	static {
		int i = 0;
		for (char c = 'a'; c <= 'z'; c++)
			regularChars[i++] = c;
		for (char c = 'A'; c <= 'Z'; c++)
			regularChars[i++] = c;
	}

	// TODO: Temporary, needs to be perfected
	private final static char[] allChars = new char[regularChars.length + 2];
	static {
		int i = 0;
		for (; i < regularChars.length; ++i)
			allChars[i] = regularChars[i++];
		allChars[i++] = '?';
		allChars[i++] = ' ';
	}

	@Override
	public char[] getAllChars() {
		return allChars;
	}

	@Override
	public char[] getRegularChars() {
		return regularChars;
	}

	private static final char[] separatorChars = { ' ' };

	@Override
	public char[] getWordSeparatorChars() {
		return separatorChars;
	}

	private static final char[] unreadableChars = { '?' };

	@Override
	public char[] getUnreadableChars() {
		return unreadableChars;
	}
}
