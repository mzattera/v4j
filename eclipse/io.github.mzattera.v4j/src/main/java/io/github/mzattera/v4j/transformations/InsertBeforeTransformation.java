package io.github.mzattera.v4j.transformations;

/**
 * Represents a transformation stating that a character is inserted in
 * words before a given character.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class InsertBeforeTransformation implements Transformation {
	private final char insertedChar;
	private final char beforeChar;
	private final boolean atBeginAndEnd; 

	public InsertBeforeTransformation(char insertedChar, char beforeChar, boolean atBeginAndEnd) {
		this.insertedChar = insertedChar;
		this.beforeChar = beforeChar;
		this.atBeginAndEnd = atBeginAndEnd;
	}

	public char insertion() {
		return insertedChar;
	}

	public char beforeChar() {
		return beforeChar;
	}

	@Override
	public String revert(String w) {

		final int begin = atBeginAndEnd ? 0 : 1;
		final int end = atBeginAndEnd ? w.length() : w.length() - 1;

		for (int i = begin; i < end; ++i) {
			boolean insertMatch = (w.charAt(i) == insertedChar);
			if (insertMatch) {
				if (((i == w.length() - 1) && (beforeChar == '.')) // char was inserted at end of the word
						|| ((i < w.length() - 1) && (beforeChar == w.charAt(i + 1))) // Char was inserted before
																						// specific char
				)
					return w.substring(0, i) + w.substring(i + 1);
			}
		}

		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		InsertBeforeTransformation o2 = (InsertBeforeTransformation) o;
		return (this.insertedChar == o2.insertedChar) && (this.beforeChar == o2.beforeChar);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(insertedChar, beforeChar);
	}

	@Override
	public String toString() {
		if (beforeChar != '.')
			return "Insert '" + insertedChar + "' before '" + beforeChar + "'";
		return "Insert '" + insertedChar + "' at word end";
	}

	@Override
	public String toListElement() {
		return "l.add(new InsertBeforeAnalysis.Transformation('" + insertedChar + "', '" + beforeChar + "'));";
	}
}