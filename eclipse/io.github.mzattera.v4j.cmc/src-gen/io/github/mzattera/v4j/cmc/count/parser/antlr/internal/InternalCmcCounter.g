/*
 * generated by Xtext 2.25.0
 */
grammar InternalCmcCounter;

options {
	superClass=AbstractInternalAntlrParser;
}

@lexer::header {
package io.github.mzattera.v4j.cmc.count.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

@parser::header {
package io.github.mzattera.v4j.cmc.count.parser.antlr.internal;

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import io.github.mzattera.v4j.cmc.count.services.CmcCounterGrammarAccess;

}

@parser::members {

 	private CmcCounterGrammarAccess grammarAccess;

    public InternalCmcCounterParser(TokenStream input, CmcCounterGrammarAccess grammarAccess) {
        this(input);
        this.grammarAccess = grammarAccess;
        registerRules(grammarAccess.getGrammar());
    }

    @Override
    protected String getFirstRuleName() {
    	return "Grammar";
   	}

   	@Override
   	protected CmcCounterGrammarAccess getGrammarAccess() {
   		return grammarAccess;
   	}

}

@rulecatch {
    catch (RecognitionException re) {
        recover(input,re);
        appendSkippedTokens();
    }
}

// Entry rule entryRuleGrammar
entryRuleGrammar returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getGrammarRule()); }
	iv_ruleGrammar=ruleGrammar
	{ $current=$iv_ruleGrammar.current; }
	EOF;

// Rule Grammar
ruleGrammar returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			{
				newCompositeNode(grammarAccess.getGrammarAccess().getRulesRuleParserRuleCall_0());
			}
			lv_rules_0_0=ruleRule
			{
				if ($current==null) {
					$current = createModelElementForParent(grammarAccess.getGrammarRule());
				}
				add(
					$current,
					"rules",
					lv_rules_0_0,
					"io.github.mzattera.v4j.cmc.count.CmcCounter.Rule");
				afterParserOrEnumRuleCall();
			}
		)
	)*
;

// Entry rule entryRuleRuleOrText
entryRuleRuleOrText returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getRuleOrTextRule()); }
	iv_ruleRuleOrText=ruleRuleOrText
	{ $current=$iv_ruleRuleOrText.current; }
	EOF;

// Rule RuleOrText
ruleRuleOrText returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			(
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getRuleOrTextRule());
					}
				}
				otherlv_0=RULE_ID
				{
					newLeafNode(otherlv_0, grammarAccess.getRuleOrTextAccess().getRuleRuleCrossReference_0_0());
				}
			)
		)
		    |
		(
			(
				lv_txt_1_0=RULE_VTEXT
				{
					newLeafNode(lv_txt_1_0, grammarAccess.getRuleOrTextAccess().getTxtVTEXTTerminalRuleCall_1_0());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getRuleOrTextRule());
					}
					setWithLastConsumed(
						$current,
						"txt",
						lv_txt_1_0,
						"io.github.mzattera.v4j.cmc.count.CmcCounter.VTEXT");
				}
			)
		)
	)
;

// Entry rule entryRuleRule
entryRuleRule returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getRuleRule()); }
	iv_ruleRule=ruleRule
	{ $current=$iv_ruleRule.current; }
	EOF;

// Rule Rule
ruleRule returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			(
				lv_name_0_0=RULE_ID
				{
					newLeafNode(lv_name_0_0, grammarAccess.getRuleAccess().getNameIDTerminalRuleCall_0_0());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getRuleRule());
					}
					setWithLastConsumed(
						$current,
						"name",
						lv_name_0_0,
						"io.github.mzattera.v4j.cmc.count.CmcCounter.ID");
				}
			)
		)
		otherlv_1=':'
		{
			newLeafNode(otherlv_1, grammarAccess.getRuleAccess().getColonKeyword_1());
		}
		(
			(
				{
					newCompositeNode(grammarAccess.getRuleAccess().getExpansionsExpansionParserRuleCall_2_0());
				}
				lv_expansions_2_0=ruleExpansion
				{
					if ($current==null) {
						$current = createModelElementForParent(grammarAccess.getRuleRule());
					}
					add(
						$current,
						"expansions",
						lv_expansions_2_0,
						"io.github.mzattera.v4j.cmc.count.CmcCounter.Expansion");
					afterParserOrEnumRuleCall();
				}
			)
		)+
	)
;

// Entry rule entryRuleExpansion
entryRuleExpansion returns [EObject current=null]:
	{ newCompositeNode(grammarAccess.getExpansionRule()); }
	iv_ruleExpansion=ruleExpansion
	{ $current=$iv_ruleExpansion.current; }
	EOF;

// Rule Expansion
ruleExpansion returns [EObject current=null]
@init {
	enterRule();
}
@after {
	leaveRule();
}:
	(
		(
			(
				lv_count_0_0=RULE_DOUBLE
				{
					newLeafNode(lv_count_0_0, grammarAccess.getExpansionAccess().getCountDOUBLETerminalRuleCall_0_0());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getExpansionRule());
					}
					setWithLastConsumed(
						$current,
						"count",
						lv_count_0_0,
						"io.github.mzattera.v4j.cmc.count.CmcCounter.DOUBLE");
				}
			)
		)
		(
			(
				lv_frequency_1_0=RULE_DOUBLE
				{
					newLeafNode(lv_frequency_1_0, grammarAccess.getExpansionAccess().getFrequencyDOUBLETerminalRuleCall_1_0());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getExpansionRule());
					}
					setWithLastConsumed(
						$current,
						"frequency",
						lv_frequency_1_0,
						"io.github.mzattera.v4j.cmc.count.CmcCounter.DOUBLE");
				}
			)
		)
		(
			(
				lv_cumulatedFreq_2_0=RULE_DOUBLE
				{
					newLeafNode(lv_cumulatedFreq_2_0, grammarAccess.getExpansionAccess().getCumulatedFreqDOUBLETerminalRuleCall_2_0());
				}
				{
					if ($current==null) {
						$current = createModelElement(grammarAccess.getExpansionRule());
					}
					setWithLastConsumed(
						$current,
						"cumulatedFreq",
						lv_cumulatedFreq_2_0,
						"io.github.mzattera.v4j.cmc.count.CmcCounter.DOUBLE");
				}
			)
		)
		(
			otherlv_3='.'
			{
				newLeafNode(otherlv_3, grammarAccess.getExpansionAccess().getFullStopKeyword_3_0());
			}
			    |
			(
				(
					(
						{
							newCompositeNode(grammarAccess.getExpansionAccess().getRulesRuleOrTextParserRuleCall_3_1_0_0());
						}
						lv_rules_4_0=ruleRuleOrText
						{
							if ($current==null) {
								$current = createModelElementForParent(grammarAccess.getExpansionRule());
							}
							add(
								$current,
								"rules",
								lv_rules_4_0,
								"io.github.mzattera.v4j.cmc.count.CmcCounter.RuleOrText");
							afterParserOrEnumRuleCall();
						}
					)
				)
				(
					otherlv_5='.'
					{
						newLeafNode(otherlv_5, grammarAccess.getExpansionAccess().getFullStopKeyword_3_1_1_0());
					}
					(
						(
							{
								newCompositeNode(grammarAccess.getExpansionAccess().getRulesRuleOrTextParserRuleCall_3_1_1_1_0());
							}
							lv_rules_6_0=ruleRuleOrText
							{
								if ($current==null) {
									$current = createModelElementForParent(grammarAccess.getExpansionRule());
								}
								add(
									$current,
									"rules",
									lv_rules_6_0,
									"io.github.mzattera.v4j.cmc.count.CmcCounter.RuleOrText");
								afterParserOrEnumRuleCall();
							}
						)
					)
				)*
			)
		)
	)
;

RULE_ID : 'A'..'Z' ('a'..'z'|'A'..'Z')*;

RULE_VTEXT : ('a'..'z')+;

RULE_COMMENT : '#' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_DOUBLE : ('0'..'9')+ '.' ('0'..'9')+;

RULE_WS : (' '|'\t'|'\r'|'\n')+;
