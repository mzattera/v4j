/* Copyright (c) 2022 Massimiliano "Maxi" Zattera */

/**
 * 
 */
package io.github.mzattera.v4j.util.statemachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;

import io.github.mzattera.v4j.util.Counter;

/**
 * A state for a state machine.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class State {

	// Machine containing this state
	private StateMachine machine = null;

	/**
	 * @return the machine containing this state.
	 */
	public StateMachine getMachine() {
		return machine;
	}

	private final String name;

	/**
	 * 
	 * @return Unique name for this state.
	 */
	public String getName() {
		return name;
	}

	private final String[] tokens;

	/**
	 * For easier representation, a state can emit a set of tokens. It is a shortcut
	 * instead of define multiple states with same input and output edges. A token
	 * might be empty (meaning the state machine does not emit any text in a given
	 * state). Notice that in this representation, a non-emitting state is a state
	 * with a single token which is the empty string.
	 * 
	 * @return tokens the state machine will emit by hitting this state.
	 */
	public String[] getTokens() {
		return tokens;
	}

	public boolean isEmitting() {
		return (tokens.length > 1) || (tokens[0].length() > 0);
	}

	// TODO Move this into StateMachine?
	private final boolean endState;

	/**
	 * 
	 * @return true if this is an end state for the machine.
	 */
	public boolean isEndState() {
		return endState;
	}

	private final Set<State> nextStates = new HashSet<>();

	/**
	 * 
	 * @return Next states from here. You cannot change the machine by altering this
	 *         Set.
	 */
	public Set<State> getNextStates() {
		return new HashSet<>(nextStates);
	}

	// States that enter into this one.
	private final Set<State> previousStates = new HashSet<>();

	/**
	 * @return Previous states to here. You cannot change the machine by altering
	 *         this Set.
	 */
	public Set<State> getPreviousStates() {
		return new HashSet<>(previousStates);
	}

	/**
	 * State that does emits nothing.
	 * 
	 * @param name
	 */
	State(String name) {
		this(name, "");
	}

	/**
	 * State that emits given token.
	 * 
	 * @param name
	 * @param token Token to emit when in this state.
	 */
	State(String name, String token) {
		this(name, new String[] { token }, false);
	}

	/**
	 * State that emits given tokens.
	 * 
	 * @param name
	 * @param tokens Tokens to emit when in this state.
	 */
	State(String name, String[] tokens) {
		this(name, tokens, false);
	}

	/**
	 * State that emits given tokens. This can optionally be flagged as end state
	 * for the state machine.
	 * 
	 * @param name
	 * @param token    Token to emit when in this state.
	 * @param endState True of this is an end state for the machine.
	 */
	State(String name, String token, boolean endState) {
		this(name, new String[] { token }, endState);
	}

	/**
	 * State that emits given tokens. This can optionally be flagged as end state
	 * for the state machine.
	 *
	 * @param name
	 * @param tokens   Tokens to emit when in this state.
	 * @param endState True of this is an end state for the machine.
	 */
	State(String name, String[] tokens, boolean endState) {
		this.name = name;

		// Bit tricky here because a non-emitting state has still one token which is the
		// empty string.
		Set<String> l = new HashSet<>();
		if (tokens != null) {
			for (String t : tokens)
				if (t != null)
					l.add(t);
		}
		if (l.size() == 0)
			l.add("");
		this.tokens = new String[l.size()];
		l.toArray(this.tokens);

		this.endState = endState;
	}

	/**
	 * "Emits" words from this state.
	 * 
	 * @param prefix What the machine emitted so far, traversing previous states.
	 * @param path List of states traversed so far to emit prefix.
	 * @param words  All words emitted so far; emit() will keep adding recursively to this.
	 * @param wordsByEdge For each edge in this state machine, counts how many time the edge was used do emit given word.
	 * @param language If this is is not null, wordsByedge will be updated only for words listed here.
	 * For each word in this counter being emitted, the edge will be counted corresponding number of times.
	 */
	void emit(String prefix, List<State> path, Counter<String> words,
			Map<ImmutablePair<String, String>, Counter<String>> wordsByEdge,
			Counter<String> language) {

		// This is the path to emit one or more strings
		List<State> newPath = new ArrayList<>(path);
		newPath.add(this);

		if (endState) {

			// These are all the edges traversed
			List<ImmutablePair<String, String>> edges = new ArrayList<>();
			for (int i = 0; i < newPath.size() - 1; ++i) {
				edges.add(new ImmutablePair<>(newPath.get(i).getName(), newPath.get(i + 1).getName()));
			}

			for (String t : tokens) {

				// emit one string
				String term = prefix + t;
				words.count(term);

				// Count edges as generating this term
				for (ImmutablePair<String, String> e : edges) {

					Counter<String> wordCount = wordsByEdge.get(e);
					if (wordCount == null) {
						wordCount = new Counter<String>();
						wordsByEdge.put(e, wordCount);
					}

					if (language == null)
						wordCount.count(term);
					else 
						wordCount.count(term, language.getCount(term));
				}
			}

		} else {

			// Not an end state, continue to next states
			for (State next : nextStates) {
				for (String t : tokens)
					next.emit(prefix + t, newPath, words, wordsByEdge, language);
			}
		}
	}

	/**
	 * Called to connect this state to another in this machine following it. Re-adding an existing
	 * state does nothing.
	 * 
	 * @return state being added.
	 */
	State addNext(State s) {
		if (endState)
			throw new IllegalArgumentException("Cannot add state " + s.name + " to final state " + name + ".");
		if (s == null)
			throw new NullPointerException("Cannot add null state.");
		if (s.machine == null)
			throw new IllegalArgumentException("State " + s.name + " must be assigned to a state machine first.");
		if (!s.machine.equals(machine))
			throw new IllegalArgumentException(
					"Cannot add state " + s.name + " to state " + name + " which is in another state machine.");

//		if (this.name.equals("1_o") && s.name.equals("8_o")) {
//			System.out.println("Hey!");
//		}
		// No need to check duplicates as it is a Set
		nextStates.add(s);
		s.addPrevious(this);
		return s;
	}

	// TODO Add removeNext() notice that, when removing a next state, we should also clear corresponding edges.

	/**
	 * Remove state from list of next states (removes an edge).
	 * 
	 * @return state being removed.
	 */
	State removeNext(State s) {
		if (s == null)
			throw new NullPointerException("Cannot remove null state.");
		if (!nextStates.contains(s))
			throw new IllegalArgumentException();

		machine.setWeight(this.name,  s.name, 0.0d);
		nextStates.remove(s);
		s.removePrevious(this);
		
		return s;
	}

	/**
	 * Adds a state to this state as a previous state (that is, this is the next
	 * state for it). Re-adding an existing state does nothing.
	 * 
	 * @return state being added.
	 */
	private State addPrevious(State s) {
		// No checks needed this is called internally after checks.
		// No need to check duplicates as it is a Set
		previousStates.add(s);
		return s;
	}

	/**
	 * Removes a previous state..
	 * 
	 * @return state being removed.
	 */
	private State removePrevious(State s) {
		// No checks needed this is called internally after checks.
		previousStates.remove(s);
		return s;
	}

	/**
	 * Called to add this state to a machine, this can be done for one machine (use remove()
	 * first if you need to transfer a state).
	 */
	void addTo(StateMachine machine) {
		if (machine == null)
			throw new NullPointerException();
		if ((this.machine != null) && (this.machine != machine))
			throw new IllegalArgumentException("State " + name + " is already assigned to a different state machine.");

		this.machine = machine;
	}

	/**
	 * Called when this state has been removed this state frrm the state machine.
	 * 
	 * @return The state being removed
	 */
	State removeFrom() {
		List<State> tmp = new ArrayList<>(previousStates);
		for (State prev : tmp)
			prev.removeNext(this);
		tmp = new ArrayList<>(nextStates);
		for (State next : tmp)
			removeNext(next);
		machine = null;
		return this;
	}

	/**
	 * Merge this state with another. The two states become a new one; merged states
	 * are removed from the state machine. This method does not work if one state is
	 * reachable from the other.
	 * 
	 * @param other State to merge with this.
	 * 
	 * @return state resulting from the merge.
	 */
	public State merge(State other) {
		if (other == null)
			throw new NullPointerException("Cannot merge null state.");
		if (other.machine == null)
			throw new IllegalArgumentException("State " + other.name + " must be assigned to a state machine first.");
		if (!other.machine.equals(machine))
			throw new IllegalArgumentException(
					"Cannot add state " + other.name + " to state " + name + " which is in another state machine.");
		if (endState != other.endState)
			throw new IllegalArgumentException("Cannot merge states with are only partially end states.");
		if (closure().contains(other) || other.closure().contains(this))
			throw new IllegalArgumentException("Cannot merge states which are connected one another.");

		// Tokens
		Set<String> tks = new HashSet<>();
		tks.addAll(Arrays.asList(tokens));
		tks.addAll(Arrays.asList(other.tokens));
		String[] allTks = new String[tks.size()];
		int i = 0;
		for (String t : tks) {
			allTks[i++] = t;
		}

		State result = new State(name + "|" + other.name, allTks, endState);
		result.machine = machine;

		// Add new state (needed to be able to set weights properly)
		machine.addState(result);
		
		// Merge next states
		for (State n : nextStates) {
			result.addNext(n);
			double oldW = machine.getWeight(this.name, n.name);
			machine.setWeight(this.name, n.name, 0.0d);
			machine.setWeight(result.name, n.name, machine.getWeight(result.name, n.name)+oldW);
		}
		for (State n : other.nextStates) {
			result.addNext(n);
			double oldW = machine.getWeight(other.name, n.name);
			machine.setWeight(other.name, n.name, 0.0d);
			machine.setWeight(result.name, n.name, machine.getWeight(result.name, n.name)+oldW);
		}

		// Merge previous states
		for (State p : previousStates) {
			p.addNext(result);
			double oldW = machine.getWeight(p.name, this.name);
			machine.setWeight(p.name, this.name, 0.0d);
			machine.setWeight(p.name, result.name, machine.getWeight(p.name, result.name)+oldW);
		}
		for (State p : other.previousStates) {
			p.addNext(result);
			double oldW = machine.getWeight(p.name, other.name);
			machine.setWeight(p.name, other.name, 0.0d);
			machine.setWeight(p.name, result.name, machine.getWeight(p.name, result.name)+oldW);
		}

		// Add new state and remove old ones from state machine
		other.removeFrom();
		removeFrom();

		return result;
	}

	/**
	 * Do not use this if your state machine has loops :)
	 * 
	 * @return all states reachable from this one.
	 */
	public Set<State> closure() {
		Set<State> result = new HashSet<>();
		for (State s : nextStates) {
			result.add(s);
			result.addAll(s.closure());
		}
		return result;
	}
}
