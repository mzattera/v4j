package io.github.mzattera.v4j.applications.transformations;

import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermClassification;
import io.github.mzattera.v4j.text.alphabet.SlotAlphabet.TermDecomposition;

/**
 * Represents a transformation stating that a char at a given slot position has
 * been substituted with another.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class SubstitutionTransformation implements Transformations.Transformation {
	private final int slot; // 0-21, covering separable words
	private final String newChar;
	private final String oldChar;

	public SubstitutionTransformation(int slot, String newChar, String oldChar) {
		this.slot = slot;
		this.newChar = newChar;
		this.oldChar = oldChar;
	}

	public int slot() {
		return slot;
	}

	public String newChar() {
		return newChar;
	}

	public String oldChar() {
		return oldChar;
	}

	@Override
	public String revert(String w) {

		TermDecomposition d = SlotAlphabet.decompose(w);
		if (d.classification == TermClassification.UNSTRUCTURED)
			return null;

		int slotCount = SlotAlphabet.SLOTS.size();
		int part, slot;
		String newChar;
		if (this.slot < slotCount) {
			part = 0;
			slot = this.slot;
			newChar = d.slots1[slot];
		} else {
			part = 1;
			slot = this.slot - slotCount;
			newChar = d.slots2[slot];
		}

		if (!newChar.equals(this.newChar))
			return null;

		String[] newWord = new String[slotCount * 2];
		System.arraycopy(d.slots1, 0, newWord, 0, d.slots1.length);
		System.arraycopy(d.slots2, 0, newWord, d.slots1.length, d.slots2.length);
		newWord[part * slotCount + slot] = this.oldChar;

		return String.join("", newWord);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SubstitutionTransformation o2 = (SubstitutionTransformation) o;
		return (this.slot == o2.slot) && (this.newChar == o2.newChar() && (this.oldChar == o2.oldChar()));
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(slot, newChar, oldChar);
	}

	@Override
	public String toString() {
		return "Substitute char '" + oldChar + "' at slot " + slot + " with '" + newChar + "'";
	}

	@Override
	public String toListElement() {
		return "l.add(new SubstitutionAnalysis.Transformation(" + slot + ", '" + newChar + "', '" + oldChar
				+ "'));";
	}
}