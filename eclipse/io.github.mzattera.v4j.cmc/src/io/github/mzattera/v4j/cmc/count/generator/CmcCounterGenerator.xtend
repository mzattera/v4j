/*
 * generated by Xtext 2.25.0
 */
package io.github.mzattera.v4j.cmc.count.generator

import io.github.mzattera.v4j.cmc.count.cmcCounter.Expansion
import io.github.mzattera.v4j.cmc.count.cmcCounter.Grammar
import io.github.mzattera.v4j.cmc.count.cmcCounter.Rule
import io.github.mzattera.v4j.cmc.count.cmcCounter.RuleOrText
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import java.util.List
import java.util.Map
import java.util.Set
import java.util.regex.Pattern
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext

/**
 * This uses the defined grammar to parse Stolfi's grammar file and count the number of terms the grammar can 
 * generate / recognize.
 * 
 * Location of Stolfi's grammar must be passed as first parameter when invoking io.github.mzattera.v4j.cmc.count.generator.Main.
 *  
 */
class CmcCounterGenerator extends AbstractGenerator {

	// To speed up generation, we cache words produced by each rule
	val Map<String, Set<String>> cache = new HashMap

	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {

		// Get the Rule defining "normal" words.
		val root = resource.allContents.filter(Rule).filter(r|r.name.equals("NormalWord")).next()

////// COUNT
		System.out.println("Total Generated words: " + root.count())

////// PRODUCE
//		val rules = new HashMap<String, Rule> // maps each Rule ID into its definition
//		for (r : resource.allContents.filter(Rule).toIterable()) {
//		System.out.println(r.name + " = " + count(r))
//			if (count(r) <1_200_00) { // We only cache rules for which it makes sense to expand
//				rules.put(r.name, r)
//			}
//		}
//		for (w : produce(root, rules))
//			System.out.println(w)

////// GENERATE (goes out of memory)
//		cache.clear()
//		val Set<String> words = new HashSet
//		root.generate(words)
//		for (String w : words)
//			System.out.println(w)
//		System.out.println(words.size())
	}

	// // RULES FOR COUNTING ////////////////////////////////////////////////////////////////////////////////////
	/**
	 * count() counts the number of words that could be generated by an element
	 */
	def dispatch double count(Grammar g) {
		var result = 0.0d
		for (Rule r : g.rules)
			result += r.count()

		return result
	}

	def dispatch double count(Rule r) {
		var result = 0.0d
		for (Expansion e : r.expansions)
			result += e.count()

		return result
	}

	def dispatch double count(Expansion e) {

		if(e.rules.size === 0) return 1.0d // the single '.' dot rules creates 1 alternative
		var result = 1.0d
		for (RuleOrText r : e.rules)
			result *= r.count()

		return result
	}

	def dispatch double count(RuleOrText r) {
		if (r.rule !== null)
			r.rule.count()
		else
			1.0d
	}

	// // RULES FOR GENERATION by expanding rules as strings //////////////////////////////////////////////////////////////
	/**
	 * @param rules all Rule by name.
	 * 
	 * @return all words produced by given Rule.
	 */
	def Set<String> produce(Rule r, Map<String, Rule> rules) {
		produce(r.productionRules, rules)
	}

	/**
	 * Expand productinRules to produce terms that will be stored in generatedWords.
	 */
	def Set<String> produce(List<String> productionRules, Map<String, Rule> rules) {
		val result = new HashSet<String>

		for (pr : productionRules)
			System.out.println(pr)
		System.out.println()

		// Expands production rules, one at a time
		while (productionRules.size() != 0) {
			val pr = productionRules.remove(0)

			val p = Pattern.compile("\\.([A-Z][a-zA-Z]*)");
			val m = p.matcher(pr);

			if (m.find()) {
				// The production rule lists at least one rule.
				// Replace the rule with its expansions
				val ruleName = m.group(1)
				val rule = rules.get(ruleName)
					val prefix = pr.substring(0, m.start)
					val suffix = pr.substring(m.end)

				if (rule === null) {
					// the rule is not in the list; this means it is TOO big to expand.
					// we just mark it as such
				productionRules.add(prefix + "[" + ruleName + "]" + suffix)
				} else {
					// replace rule with its expansion
					for (replacement : rule.productionRules) { // can cache even more
						productionRules.add(prefix + " " + ruleName + "(" + replacement + ")" + suffix)
					}
				}
			} else {
				// this production rule cannot be further expanded; it is already a word, stop here
				result.add(pr.replaceAll("\\.", ""))
				System.out.println(pr.replaceAll("\\.", ""))
			}
		}

		result
	}

	/**
	 * @return list of expansions for given Rule
	 */
	def List<String> getProductionRules(Rule r) {
		val productionRules = new ArrayList<String>
		for (e : r.expansions) {
			productionRules.add(e.asString())
		}
		productionRules
	}

	/**
	 * @return a string representation of the Expansion (as written in production rules). 
	 * For convenience, the expansion is prefixed by '.'
	 */
	def String asString(Expansion expansion) {
		if(expansion.rules.size == 0) return "."

		val result = new StringBuffer
		for (i : 0 ..< expansion.rules.size) {
			result.append('.')
			val r = expansion.rules.get(i)
			if (r.rule !== null)
				result.append(r.rule.name)
			else
				result.append(r.txt)
		}

		result.toString()
	}

	// // RULES FOR GENERATION by recursively applying rules - GOES OUT OF MEMORY /////////////////////////////////////////
	def Set<String> generate(Grammar g) {
		val Set<String> words = new HashSet
		for (Rule r : g.rules)
			generate(r, words)
		words
	}

	def dispatch Set<String> generate(Rule r, Set<String> words) {
		var Set<String> result = cache.get(r.name)
//		System.out.println(r.name + (result===null?"":" cached!"));
		if(result !== null) return result

		result = new HashSet
		for (Expansion e : r.expansions)
			generate(e, result)

		cache.put(r.name, result)
		System.out.println(r.name + " >>> " + result.size());
		words.addAll(result)
		words
	}

	def dispatch Set<String> generate(
		Expansion e,
		Set<String> words
	) {
		var Set<String> result = new HashSet
		for (RuleOrText r : e.rules)
			result = concat(result, generate(r, new HashSet<String>))
		words.addAll(result)
		words
	}

	def dispatch Set<String> generate(
		RuleOrText r,
		Set<String> words
	) {
		if (r.rule !== null) {
			generate(r.rule, words)
		} else {
			words.add(r.txt)
			words
		}
	}

	def Set<String> concat(Set<String> a, Set<String> b) {
//		System.out.println("> " + a.size() + "x" +b.size()+"="+(a.size()*b.size()))
		if(a.size() === 0) return b
		if(b.size() === 0) return a
		val Set<String> result = new HashSet
		for (String  s1 : a)
			for (String s2 : b)
				result.add(s1 + s2)
		result
	}
}
