package io.github.mzattera.v4j.transformations;

/**
 * Represents a transformation stating that given endings are post-fixed to stem
 * anding with a given stemEnding.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class SuffixTransformation implements Transformation {
	private final String ending;
	private final char stemEnding;

	public SuffixTransformation(String endings, char stemInitial) {
		this.ending = endings;
		this.stemEnding = stemInitial;
	}

	public String ending() {
		return ending;
	}

	public char stemEnding() {
		return stemEnding;
	}

	@Override
	public String revert(String w) {

		if (w.length() <= ending.length())
			return null;
		if (w.endsWith(ending)) {
			String stem = w.substring(0, w.length() - ending.length());
			if (stem.charAt(stem.length() - 1) == stemEnding)
				return stem;
		}

		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SuffixTransformation nameData = (SuffixTransformation) o;
		return stemEnding == nameData.stemEnding && java.util.Objects.equals(ending, nameData.ending);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(ending, stemEnding);
	}

	@Override
	public String toString() {
		return "Suffix '-" + ending + "' to '-" + stemEnding + "' endings";
	}

	@Override
	public String toListElement() {
		return "l.add(new SuffixAnalysis.Transformation(\"" + ending + "\", '" + stemEnding + "'));";
	}
}