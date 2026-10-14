package io.github.mzattera.v4j.transformations;

/**
 * Represents a transformation stating that given initials are pre-fixed to
 * stems staring with a given character (stemInitial).
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class PrefixTransformation implements Transformations.Transformation {
	private final String initials;
	private final char stemInitial;

	public PrefixTransformation(String initials, char stemInitial) {
		this.initials = initials;
		this.stemInitial = stemInitial;
	}

	public String initials() {
		return initials;
	}

	public char stemInitial() {
		return stemInitial;
	}

	@Override
	public String revert(String w) {

		if (w.length() <= initials.length())
			return null;
		if (!w.startsWith(initials))
			return null;

		String stem = w.substring(initials.length());
		if (stem.charAt(0) != stemInitial)
			return null;

		return stem;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		PrefixTransformation nameData = (PrefixTransformation) o;
		return stemInitial == nameData.stemInitial && java.util.Objects.equals(initials, nameData.initials);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(initials, stemInitial);
	}

	@Override
	public String toString() {
		return "Prefix '" + initials + "-' to '" + stemInitial + "-' initials";
	}

	@Override
	public String toListElement() {
		return "l.add(new PrefixAnalysis.Transformation(\"" + initials + "\", '" + stemInitial + "'));";
	}
}