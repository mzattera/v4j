package io.github.mzattera.v4j.util.statemachine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a model to generate Voyinich words based on ordered "slots" that can contain any between a set of strings.
 * Words are built by choosing strings from the slots and concatenating them, or leaving them empty.
 * This is a faster way to simulate a model than using StateMachine.
 *  
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public final class SlotBasedModel {
	
	/**
	 * "Slots" for words; each element is a list of character combinations admitted
	 * in that slot.
	 */
	private final List<List<String>> slots;

	public SlotBasedModel (List<List<String>> slots) {
		this.slots = new ArrayList<>(slots);
	}

	public SlotBasedModel (String[][] slots) {
		this.slots = new ArrayList<>();
		for (String[] slot : slots) {
			List<String> l = new ArrayList<>();
			for (String s : slot) {
				l.add(s);
			}
			this.slots.add(l);
		}
	}

	/**
	 * @return all possible words this machine can emit from given state.
	 */
	public Set<String> emit() {

		// All terms generated up to length N, with N increasing from 0 up
		Set<String> result = new HashSet<>();
		result.add("");

		for (int i = 0; i < slots.size(); ++i) {

			// Start with all terms of length i
			Set<String> w = new HashSet<>(result);

			// Add to them all chars in slot i (we already covered the case slot i is empty)
			for (String s : result) {
				for (int j = 0; j < slots.get(i).size(); ++j) {
					w.add(s + slots.get(i).get(j));
				}
			}

			// Now store words of length i+1
			result = w;
		}
		
		result.remove("");
		return result;
	}
}
