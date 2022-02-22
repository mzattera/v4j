/**
 * 
 */
package io.github.mzattera.v4j.util.statemachine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.xwork.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

import io.github.mzattera.v4j.text.alphabet.SlotAlphabet;
import io.github.mzattera.v4j.text.ivtff.ParseException;
import io.github.mzattera.v4j.util.Counter;

/**
 * A directed state machine that outputs text.
 * 
 * This was a quick hack to generate words from models of Voynich words that use
 * a state machine to describe the model.
 * 
 * @author Massimiliano "Maxi" Zattera
 *
 */
public class StateMachine {

	// States for this machine
	private final Map<String, State> states = new HashMap<>();

	// Initial state for this machine
	private State initialState = null;

	/**
	 * Makes given state the initial state for this machine.
	 * 
	 * @return initial state for the machine.
	 */
	public State setInitialState(String stateName) {
		return setInitialState(states.get(stateName));
	}

	/**
	 * Makes given state the initial state for this machine.
	 * 
	 * @return initial state for the machine.
	 */
	public State setInitialState(State s) {
		if (s == null)
			throw new NullPointerException();
		if (!states.containsValue(s))
			throw new IllegalArgumentException();
		initialState = s;
		return initialState;
	}

	/**
	 * @return initial state for the machine.
	 */
	public State getInitialState() {
		return initialState;
	}

	/**
	 * @return true if given state is the initial state for the machine.
	 */
	public boolean isInitialState(String name) {
		return (getState(name) == initialState);
	}

	/**
	 * @return true if given state is the initial state for the machine.
	 */
	public boolean isInitialState(State s) {
		return (s == initialState);
	}

	/**
	 * @return end states for the machine.
	 */
	public Collection<State> getEndStates() {
		List<State> result = new ArrayList<>();
		for (State s : states.values())
			if (s.isEndState())
				result.add(s);
		return result;
	}

	/**
	 * @return true if given state is end state for the machine.
	 */
	public boolean isEndState(String name) {
		return getState(name).isEndState();
	}

	/**
	 * @return true if given state is end state for the machine.
	 */
	public boolean isEndState(State s) {
		return s.isEndState();
	}

	/** Weights associated to edges connecting left state to right state. */
	Map<ImmutablePair<String, String>, Double> weights = new HashMap<>();

	/**
	 * @return weight associated to edge connecting state a to state b.
	 */
	public double getWeight(String a, String b) {
		if (!getState(a).getNextStates().contains(getState(b)))
			return 0.0;

		Double w = weights.get(new ImmutablePair<>(a, b));
		return (w == null) ? 0.0d : w.doubleValue();
	}

	/**
	 * Set weight associated to edge connecting state a to state b.
	 */
	public void setWeight(String a, String b, double d) {
		if (!getState(a).getNextStates().contains(getState(b)))
			throw new IllegalArgumentException("State " + b + " does not follow state " + a);

		if (d == 0.0d)
			weights.remove(new ImmutablePair<>(a, b));
		else
			weights.put(new ImmutablePair<>(a, b), d);
	}

	public StateMachine() {
	}

	/**
	 * Adds a state to this state machine.
	 * 
	 * @return state being added.
	 */
	State addState(State s) {
		if (states.containsKey(s.getName()))
			throw new IllegalArgumentException("State " + s.getName() + " alrteady added.");

		states.put(s.getName(), s);
		s.addTo(this);
		return s;
	}

	/**
	 * Adds a state that emits nothing to this state machine.
	 * 
	 * @return state being added.
	 */
	public State addState(String name) {
		return addState(new State(name));
	}

	/**
	 * Adds a state that emits given token to this state machine.
	 * 
	 * @param token Token to emit when in this state.
	 * @return state being added.
	 */
	public State addState(String name, String token) {
		return addState(new State(name, token));
	}

	/**
	 * Adds a state that emits given tokens to this state machine.
	 * 
	 * @param tokens Tokens to emit when in this state.
	 * @return state being added.
	 */
	public State addState(String name, String[] tokens) {
		return addState(new State(name, tokens));
	}

	/**
	 * Adds a state that emits nothing to this state machine. This can optionally be
	 * flagged as end state for the state machine.
	 * 
	 * @param name
	 * @param endState True of this is an end state for the machine.
	 * @return state being added.
	 */
	public State addState(String name, boolean endState) {
		return addState(new State(name, new String[] {}, endState));
	}

	/**
	 * Adds a state that emits given tokens to this state machine. This can
	 * optionally be flagged as end state for the state machine.
	 * 
	 * @param name
	 * @param token    Token to emit when in this state.
	 * @param endState True of this is an end state for the machine.
	 * @return state being added.
	 */
	public State addState(String name, String[] tokens, boolean endState) {
		return addState(new State(name, tokens, endState));
	}

	/**
	 * Makes b one of the next states of a.
	 * 
	 * @param a
	 * @param b
	 */
	public void addNext(String a, String b) {
		getState(a).addNext(getState(b));
	}

	/**
	 * Makes all states in b next states of a.
	 * 
	 * @param a
	 * @param b
	 */
	public void addNext(String a, String[] b) {
		for (String n : b)
			addNext(a, n);
	}

	/**
	 * Makes b next state of all states in a.
	 * 
	 * @param a
	 * @param b
	 */
	public void addNext(String[] a, String b) {
		for (String n : a)
			addNext(n, b);
	}

	// TODO Add removeNext().

	/**
	 * Removes a state from this state machine.
	 * 
	 * @param name name of State to remove.
	 * @return state being removed.
	 */
	public State removeState(String name) {
		State s = states.get(name);
		if (s == null)
			return null;

		s.removeFrom();
		states.remove(name);
		return s;
	}

	/**
	 * Retrieve a state by name, or null if the state does not exist.
	 */
	public State getState(String name) {
		return states.get(name);
	}

	/**
	 * @return Machine states. You cannot change the machine by altering this Set.
	 */
	public Set<State> getStates() {
		return new HashSet<>(states.values());
	}

	/**
	 * @return all possible words this machine can emit, with number of times they
	 *         were emitted.
	 */
	public Counter<String> emit() {
		return emitAndUpdateWeights(false, (Counter<String>) null);
	}

	/**
	 * @param updateWeights If true, will update edge weights such that each edge
	 *                      weight will reflect the number of words that were
	 *                      generated by that edge.
	 * 
	 * @return all possible words this machine can emit, with number of times they
	 *         were emitted.
	 */
	public Counter<String> emit(boolean updateWeights) {
		return emitAndUpdateWeights(updateWeights, (Counter<String>) null);
	}

	/**
	 * Updates state machine weights such that each edge weight will reflect the
	 * number of words that were generated by that edge.
	 */
	public void updateWeights() {
		emitAndUpdateWeights(true, (Counter<String>) null);
	}

	/**
	 * Updates state machine weights such that each edge weight will reflect the
	 * number of terms that were generated by that edge.
	 * 
	 * @param language If this is is not null, weights will be updated only for
	 *                 terms listed here.
	 */
	public void updateWeights(Set<String> language) {
		emitAndUpdateWeights(true, new Counter<String>(language));
	}

	/**
	 * Updates state machine weights such that each edge weight will reflect the
	 * number of tokens that were generated by that edge.
	 * 
	 * @param language If this is is not null, weights will be updated only for
	 *                 words listed here. For each word in this counter being
	 *                 emitted, the edge will be counted corresponding number of
	 *                 times.
	 */
	public void updateWeights(Counter<String> language) {
		emitAndUpdateWeights(true, language);
	}

	/**
	 * Computes all words this state machine can emit.
	 * 
	 * Optionally updates the machine weights.
	 * 
	 * @param updateWeights If true, will update edge weights such that each edge
	 *                      weight will reflect the number of words that were
	 *                      generated by that edge.
	 * @param language      If this is is not null, weights will be updated only for
	 *                      terms listed here. For each term in this set being
	 *                      emitted, the edge will be counted 1 time.
	 * 
	 * @return all possible words this machine can emit, with number of times they
	 *         were emitted.
	 */
	private Counter<String> emitAndUpdateWeights(boolean updateWeights, Set<String> language) {
		return emitAndUpdateWeights(updateWeights, new Counter<String>(language));
	}

	/**
	 * Computes all words this state machine can emit. optionally updates the
	 * machine weights.
	 * 
	 * @param updateWeights If true, will update edge weights such that each edge
	 *                      weight will reflect the number of words that were
	 *                      generated by that edge.
	 * @param language      If this is is not null, weights will be updated only for
	 *                      words listed here. For each word in this counter being
	 *                      emitted, the edge will be counted corresponding number
	 *                      of times.
	 * 
	 * @return all possible words this machine can emit, with number of times they
	 *         were emitted.
	 */
	private Counter<String> emitAndUpdateWeights(boolean updateWeights, Counter<String> language) {

		Counter<String> result = new Counter<>();
		Map<ImmutablePair<String, String>, Counter<String>> generatedByEdge = new HashMap<>();
		initialState.emit("", new ArrayList<>(), result, generatedByEdge, language);

		if (updateWeights) {
			// Update weights
			weights.clear();
			for (Entry<ImmutablePair<String, String>, Counter<String>> e : generatedByEdge.entrySet()) {
				ImmutablePair<String, String> p = e.getKey();
				Counter<String> termCount = e.getValue();
				setWeight(p.left, p.right, termCount.getTotalCounted());
			}
		}

		return result;
	}

	/** Define what parameter we seek to improve when training the state machine */
	public enum TrainMode {
		PRECISION, RECALL, F1
	};

	/**
	 * "Trains" the state machine.
	 * 
	 * Remove edges as long as that increases the dimension specified in mode.
	 * Notice this goes through a set of local maximums, so it might not find the
	 * absolute best solution.
	 * 
	 * Weights are updated to reflect the number of terms in language that the
	 * machine generates.
	 * 
	 * @param language all and only terms that the state machine should emit.
	 * @param mode     The rule we use to improve machine results.
	 * 
	 * @return Words being generated by the machine, with corresponding count.
	 */
	public Counter<String> train(Set<String> language, TrainMode mode) {

		do {
//			System.out.println(">>>>>");

			////// Compute current situation, that is the words being emitted
			Counter<String> generated = new Counter<>();
			Map<ImmutablePair<String, String>, Counter<String>> generatedByEdge = new HashMap<>();
			initialState.emit("", new ArrayList<>(), generated, generatedByEdge, null);

			// This will store possible edges that can be removed for optimization.
			// We do not consider edges from/to begin or end state if they are not emitting.
			// They must be excluded from optimization as they are just inheriting what
			// happens downstream; not doing so produces much worse results.
			Set<ImmutablePair<String, String>> edges = new HashSet<>();
			for (ImmutablePair<String, String> p : generatedByEdge.keySet()) {
				State s = getState(p.left);
				if (isInitialState(s) && !s.isEmitting())
					continue;
				s = getState(p.right);
				if (isEndState(s) && !s.isEmitting())
					continue;
				edges.add(p);
			}

			// True and false positives
			long tp = 0;
			long fp = 0;
			for (String t : generated.itemSet()) {
				if (language.contains(t))
					++tp;
				else
					++fp;
			}

			double precision = ((double) tp / (tp + fp));
			double recall = ((double) tp / language.size());
			double f1 = 2 * precision * recall / (precision + recall);

//			System.out.println(">>> TP " + tp + " FP " + fp + " P " + precision + " R " + recall + " F1 " + f1);

			////// Try to find a better situation ////////////////////////////

			// Candidate edge to remove and what would be new KPI
			ImmutablePair<String, String> candidate = null;
			double bestPrecision = precision;
			double bestRecall = recall;
			double bestF1 = f1;

			// Find the edge that, if removed, will increase desired parameter the most.
			for (ImmutablePair<String, String> e : edges) {

//				System.out.println(" > " + e.left + " -> " + e.right);

				// how things will change if we remove this edge
				long newTp = tp;
				long newFp = fp;
				for (Entry<String, Integer> entry : generatedByEdge.get(e).entrySet()) { // for all terms generated by
																							// this edge

					String term = entry.getKey();
					int count = entry.getValue();

//					System.out.println("   > " + term + " Gen. " + count + " Tot. " + generated.getCount(term));

					if (generated.getCount(term) == count) {
						// this is the only edge generating this term, if we remove it we will reduce TP
						// or FP
						if (language.contains(term))
							--newTp;
						else
							--newFp;
					}
				}

//				System.out.println("   > " + " NTP: " + newTp + " NFP: " + newFp);

				double newPrecision = ((double) newTp / (newTp + newFp));
				double newRecall = ((double) newTp / language.size());
				double newF1 = 2 * newPrecision * newRecall / (newPrecision + newRecall);

//				System.out.println("    TP " + newTp +" FP " + newFp +" P " + newPrecision + " R " + newRecall + " F1 " + newF1);

				// check if edge is the best candidate for removal so far
				if (((mode == TrainMode.PRECISION) && (newPrecision >= bestPrecision))
						|| ((mode == TrainMode.RECALL) && (newRecall >= bestRecall))
						|| ((mode == TrainMode.F1) && (newF1 >= bestF1))) {
					// current edge is the best one to remove so far
					candidate = e;
					bestPrecision = newPrecision;
					bestRecall = newRecall;
					bestF1 = newF1;
				}
			}

			////// Improve, if possible, and iterate ////////////////////////////
			if (candidate == null)
				break;
//			System.out.println("\tRemoving edge: " + candidate.left + " -> " + candidate.right);
			getState(candidate.left).removeNext(getState(candidate.right));

		} while (true);

		// Cleanup and update weights
		removeUnreachableStates();
		return emitAndUpdateWeights(true, language);
	}

	/**
	 * Removes all edges which weight is less than given threshold. This does not
	 * remove edges from or to initial or end states.
	 * 
	 * @param threshold
	 */
	public void trim(double threshold) {
		for (State s : states.values()) {
			Set<State> next = s.getNextStates();
			for (State n : next) {
				if (getWeight(s.getName(), n.getName()) < threshold) {
					s.removeNext(n);
				}
			}
		}
		removeUnreachableStates();
	}

	/**
	 * Remove unreachable and not emitting states; that is, all those not connected
	 * to an initial or end state.
	 */
	public void removeUnreachableStates() {

		// TODO remove all states below given threshold

		boolean removed = true;
		while (removed) {
			removed = false;

			List<State> tmp = new ArrayList<>(states.values());
			for (State s : tmp) {
				if (((s.getPreviousStates().size() == 0) && (s != initialState))
						|| ((s.getNextStates().size() == 0) && !s.isEndState())) {
					removeState(s.getName());
					removed = true;
				}
			}
		}
	}

	/**
	 * Writes this out as a graph that can be visualized in Gephi.
	 * 
	 * @param out   output folder
	 * @param fName "root" name. Two .TSV files will be generated with the "_nodes"
	 *              and "_edges" suffix.
	 * @throws IOException
	 */
	public void write(String out, String fName) throws IOException {

		// Create CSV file with nodes
		try (CSVPrinter csvPrinter = new CSVPrinter(Files.newBufferedWriter(Path.of(out, fName + "_nodes.tsv")),
				CSVFormat.DEFAULT);) {
			csvPrinter.printRecord("Id", "Label", "Slot", "Letter");
			for (State s : states.values()) {
				String name = s.getName();
				int p = name.indexOf('_');
				String slot = (p == -1 ? "<>" : name.substring(0, p));
				String ch = (p == -1 ? "<>" : name.substring(p + 1));

				csvPrinter.printRecord(name, name, slot, ch);
			}
		}

		// Create CSV file with edges
		try (CSVPrinter csvPrinter = new CSVPrinter(Files.newBufferedWriter(Path.of(out, fName + "_edges.tsv")),
				CSVFormat.DEFAULT);) {
			csvPrinter.printRecord("Source", "Target", "Label", "Type", "Weight");
			for (State a : states.values()) {
				for (State b : a.getNextStates()) {
					double weight = getWeight(a.getName(), b.getName());
					csvPrinter.printRecord(a.getName(), b.getName(), (int) weight, "Directed", weight);
				}
			}
		}
	}

	/**
	 * Saves this state machine as a piece of Java code.
	 */
	public String toJava() {
		StringBuilder r = new StringBuilder();
		r.append("StateMachine m = new StateMachine();\n");

		// Create states
		for (State s : states.values()) {
			String[] tokens = s.getTokens();
			r.append("m.addState(\"");
			r.append(s.getName()).append("\", ");
			r.append("new String[] {\"").append(StringUtils.join(tokens, "\",\"")).append("\"}, ");
			r.append(s.isEndState()).append(");\n");
		}

		r.append("m.setInitialState(\"").append(initialState.getName()).append("\");\n");

		// Creates edges
		for (State s : states.values()) {
			Set<State> next = s.getNextStates();
			if (next.size() == 0)
				continue;

			String[] names = new String[next.size()];
			int i = 0;
			for (State n : next) {
				names[i++] = n.getName();
			}
			r.append("m.addNext(\"");
			r.append(s.getName()).append("\", ");
			r.append("new String[] {\"").append(StringUtils.join(names, "\",\"")).append("\"});\n");
		}

		// Creates weights
		for (Entry<ImmutablePair<String, String>, Double> e : weights.entrySet()) {
			ImmutablePair<String, String> edge = e.getKey();
			double weight = e.getValue();
			r.append("m.setWeight(\"");
			r.append(edge.left).append("\", \"");
			r.append(edge.right).append("\", ");
			r.append(weight).append(");\n");
		}

		return r.toString();
	}

	/**
	 * Saves this state machine as a "formal" grammar (see Note 008).
	 * @throws ParseException 
	 */
	public String toGrammar() throws ParseException {
		StringBuilder r = new StringBuilder();

		// Create states
		String[] sNames = new String[states.size()];
		int c = 0;
		for (State s : states.values()) {
			sNames[c++] = s.getName();
		}
		Arrays.sort(sNames);
		
		for (int i=0; i<sNames.length; ++i) {
			
			State s = states.get(sNames[i]);
			r.append(s.getName()).append(":\n\t");
						
			String[] tokens = s.getTokens();
			for (int j=0; j<tokens.length; ++j) {
				tokens[j] = SlotAlphabet.toEva(tokens[j]);
			}
			Arrays.sort(tokens);
			r.append(StringUtils.join(tokens, ", "));

			Set<State> next = s.getNextStates();
			if (next.size() > 0) {

				String[] names = new String[next.size()];
				c = 0;
				for (State n : next) {
					names[c++] = n.getName();
				}
				Arrays.sort(names);

				r.append(" -> ").append(StringUtils.join(names, ", "));
			}

			r.append("\n");
		}

		return r.toString();
	}

}
