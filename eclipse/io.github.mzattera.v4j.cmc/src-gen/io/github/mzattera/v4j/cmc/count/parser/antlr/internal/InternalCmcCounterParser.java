/* Copyright (c) 2022 Massimiliano "Maxi" Zattera */

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



import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class InternalCmcCounterParser extends AbstractInternalAntlrParser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "RULE_ID", "RULE_VTEXT", "RULE_DOUBLE", "RULE_COMMENT", "RULE_WS", "':'", "'.'"
    };
    public static final int T__9=9;
    public static final int RULE_ID=4;
    public static final int RULE_VTEXT=5;
    public static final int RULE_WS=8;
    public static final int RULE_COMMENT=7;
    public static final int RULE_DOUBLE=6;
    public static final int EOF=-1;
    public static final int T__10=10;

    // delegates
    // delegators


        public InternalCmcCounterParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public InternalCmcCounterParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return InternalCmcCounterParser.tokenNames; }
    public String getGrammarFileName() { return "InternalCmcCounter.g"; }



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




    // $ANTLR start "entryRuleGrammar"
    // InternalCmcCounter.g:64:1: entryRuleGrammar returns [EObject current=null] : iv_ruleGrammar= ruleGrammar EOF ;
    public final EObject entryRuleGrammar() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleGrammar = null;


        try {
            // InternalCmcCounter.g:64:48: (iv_ruleGrammar= ruleGrammar EOF )
            // InternalCmcCounter.g:65:2: iv_ruleGrammar= ruleGrammar EOF
            {
             newCompositeNode(grammarAccess.getGrammarRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleGrammar=ruleGrammar();

            state._fsp--;

             current =iv_ruleGrammar; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleGrammar"


    // $ANTLR start "ruleGrammar"
    // InternalCmcCounter.g:71:1: ruleGrammar returns [EObject current=null] : ( (lv_rules_0_0= ruleRule ) )* ;
    public final EObject ruleGrammar() throws RecognitionException {
        EObject current = null;

        EObject lv_rules_0_0 = null;



        	enterRule();

        try {
            // InternalCmcCounter.g:77:2: ( ( (lv_rules_0_0= ruleRule ) )* )
            // InternalCmcCounter.g:78:2: ( (lv_rules_0_0= ruleRule ) )*
            {
            // InternalCmcCounter.g:78:2: ( (lv_rules_0_0= ruleRule ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==RULE_ID) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // InternalCmcCounter.g:79:3: (lv_rules_0_0= ruleRule )
            	    {
            	    // InternalCmcCounter.g:79:3: (lv_rules_0_0= ruleRule )
            	    // InternalCmcCounter.g:80:4: lv_rules_0_0= ruleRule
            	    {

            	    				newCompositeNode(grammarAccess.getGrammarAccess().getRulesRuleParserRuleCall_0());
            	    			
            	    pushFollow(FOLLOW_3);
            	    lv_rules_0_0=ruleRule();

            	    state._fsp--;


            	    				if (current==null) {
            	    					current = createModelElementForParent(grammarAccess.getGrammarRule());
            	    				}
            	    				add(
            	    					current,
            	    					"rules",
            	    					lv_rules_0_0,
            	    					"io.github.mzattera.v4j.cmc.count.CmcCounter.Rule");
            	    				afterParserOrEnumRuleCall();
            	    			

            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleGrammar"


    // $ANTLR start "entryRuleRuleOrText"
    // InternalCmcCounter.g:100:1: entryRuleRuleOrText returns [EObject current=null] : iv_ruleRuleOrText= ruleRuleOrText EOF ;
    public final EObject entryRuleRuleOrText() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRuleOrText = null;


        try {
            // InternalCmcCounter.g:100:51: (iv_ruleRuleOrText= ruleRuleOrText EOF )
            // InternalCmcCounter.g:101:2: iv_ruleRuleOrText= ruleRuleOrText EOF
            {
             newCompositeNode(grammarAccess.getRuleOrTextRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRuleOrText=ruleRuleOrText();

            state._fsp--;

             current =iv_ruleRuleOrText; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRuleOrText"


    // $ANTLR start "ruleRuleOrText"
    // InternalCmcCounter.g:107:1: ruleRuleOrText returns [EObject current=null] : ( ( (otherlv_0= RULE_ID ) ) | ( (lv_txt_1_0= RULE_VTEXT ) ) ) ;
    public final EObject ruleRuleOrText() throws RecognitionException {
        EObject current = null;

        Token otherlv_0=null;
        Token lv_txt_1_0=null;


        	enterRule();

        try {
            // InternalCmcCounter.g:113:2: ( ( ( (otherlv_0= RULE_ID ) ) | ( (lv_txt_1_0= RULE_VTEXT ) ) ) )
            // InternalCmcCounter.g:114:2: ( ( (otherlv_0= RULE_ID ) ) | ( (lv_txt_1_0= RULE_VTEXT ) ) )
            {
            // InternalCmcCounter.g:114:2: ( ( (otherlv_0= RULE_ID ) ) | ( (lv_txt_1_0= RULE_VTEXT ) ) )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( (LA2_0==RULE_ID) ) {
                alt2=1;
            }
            else if ( (LA2_0==RULE_VTEXT) ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // InternalCmcCounter.g:115:3: ( (otherlv_0= RULE_ID ) )
                    {
                    // InternalCmcCounter.g:115:3: ( (otherlv_0= RULE_ID ) )
                    // InternalCmcCounter.g:116:4: (otherlv_0= RULE_ID )
                    {
                    // InternalCmcCounter.g:116:4: (otherlv_0= RULE_ID )
                    // InternalCmcCounter.g:117:5: otherlv_0= RULE_ID
                    {

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getRuleOrTextRule());
                    					}
                    				
                    otherlv_0=(Token)match(input,RULE_ID,FOLLOW_2); 

                    					newLeafNode(otherlv_0, grammarAccess.getRuleOrTextAccess().getRuleRuleCrossReference_0_0());
                    				

                    }


                    }


                    }
                    break;
                case 2 :
                    // InternalCmcCounter.g:129:3: ( (lv_txt_1_0= RULE_VTEXT ) )
                    {
                    // InternalCmcCounter.g:129:3: ( (lv_txt_1_0= RULE_VTEXT ) )
                    // InternalCmcCounter.g:130:4: (lv_txt_1_0= RULE_VTEXT )
                    {
                    // InternalCmcCounter.g:130:4: (lv_txt_1_0= RULE_VTEXT )
                    // InternalCmcCounter.g:131:5: lv_txt_1_0= RULE_VTEXT
                    {
                    lv_txt_1_0=(Token)match(input,RULE_VTEXT,FOLLOW_2); 

                    					newLeafNode(lv_txt_1_0, grammarAccess.getRuleOrTextAccess().getTxtVTEXTTerminalRuleCall_1_0());
                    				

                    					if (current==null) {
                    						current = createModelElement(grammarAccess.getRuleOrTextRule());
                    					}
                    					setWithLastConsumed(
                    						current,
                    						"txt",
                    						lv_txt_1_0,
                    						"io.github.mzattera.v4j.cmc.count.CmcCounter.VTEXT");
                    				

                    }


                    }


                    }
                    break;

            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRuleOrText"


    // $ANTLR start "entryRuleRule"
    // InternalCmcCounter.g:151:1: entryRuleRule returns [EObject current=null] : iv_ruleRule= ruleRule EOF ;
    public final EObject entryRuleRule() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleRule = null;


        try {
            // InternalCmcCounter.g:151:45: (iv_ruleRule= ruleRule EOF )
            // InternalCmcCounter.g:152:2: iv_ruleRule= ruleRule EOF
            {
             newCompositeNode(grammarAccess.getRuleRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleRule=ruleRule();

            state._fsp--;

             current =iv_ruleRule; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleRule"


    // $ANTLR start "ruleRule"
    // InternalCmcCounter.g:158:1: ruleRule returns [EObject current=null] : ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( (lv_expansions_2_0= ruleExpansion ) )+ ) ;
    public final EObject ruleRule() throws RecognitionException {
        EObject current = null;

        Token lv_name_0_0=null;
        Token otherlv_1=null;
        EObject lv_expansions_2_0 = null;



        	enterRule();

        try {
            // InternalCmcCounter.g:164:2: ( ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( (lv_expansions_2_0= ruleExpansion ) )+ ) )
            // InternalCmcCounter.g:165:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( (lv_expansions_2_0= ruleExpansion ) )+ )
            {
            // InternalCmcCounter.g:165:2: ( ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( (lv_expansions_2_0= ruleExpansion ) )+ )
            // InternalCmcCounter.g:166:3: ( (lv_name_0_0= RULE_ID ) ) otherlv_1= ':' ( (lv_expansions_2_0= ruleExpansion ) )+
            {
            // InternalCmcCounter.g:166:3: ( (lv_name_0_0= RULE_ID ) )
            // InternalCmcCounter.g:167:4: (lv_name_0_0= RULE_ID )
            {
            // InternalCmcCounter.g:167:4: (lv_name_0_0= RULE_ID )
            // InternalCmcCounter.g:168:5: lv_name_0_0= RULE_ID
            {
            lv_name_0_0=(Token)match(input,RULE_ID,FOLLOW_4); 

            					newLeafNode(lv_name_0_0, grammarAccess.getRuleAccess().getNameIDTerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getRuleRule());
            					}
            					setWithLastConsumed(
            						current,
            						"name",
            						lv_name_0_0,
            						"io.github.mzattera.v4j.cmc.count.CmcCounter.ID");
            				

            }


            }

            otherlv_1=(Token)match(input,9,FOLLOW_5); 

            			newLeafNode(otherlv_1, grammarAccess.getRuleAccess().getColonKeyword_1());
            		
            // InternalCmcCounter.g:188:3: ( (lv_expansions_2_0= ruleExpansion ) )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==RULE_DOUBLE) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // InternalCmcCounter.g:189:4: (lv_expansions_2_0= ruleExpansion )
            	    {
            	    // InternalCmcCounter.g:189:4: (lv_expansions_2_0= ruleExpansion )
            	    // InternalCmcCounter.g:190:5: lv_expansions_2_0= ruleExpansion
            	    {

            	    					newCompositeNode(grammarAccess.getRuleAccess().getExpansionsExpansionParserRuleCall_2_0());
            	    				
            	    pushFollow(FOLLOW_6);
            	    lv_expansions_2_0=ruleExpansion();

            	    state._fsp--;


            	    					if (current==null) {
            	    						current = createModelElementForParent(grammarAccess.getRuleRule());
            	    					}
            	    					add(
            	    						current,
            	    						"expansions",
            	    						lv_expansions_2_0,
            	    						"io.github.mzattera.v4j.cmc.count.CmcCounter.Expansion");
            	    					afterParserOrEnumRuleCall();
            	    				

            	    }


            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleRule"


    // $ANTLR start "entryRuleExpansion"
    // InternalCmcCounter.g:211:1: entryRuleExpansion returns [EObject current=null] : iv_ruleExpansion= ruleExpansion EOF ;
    public final EObject entryRuleExpansion() throws RecognitionException {
        EObject current = null;

        EObject iv_ruleExpansion = null;


        try {
            // InternalCmcCounter.g:211:50: (iv_ruleExpansion= ruleExpansion EOF )
            // InternalCmcCounter.g:212:2: iv_ruleExpansion= ruleExpansion EOF
            {
             newCompositeNode(grammarAccess.getExpansionRule()); 
            pushFollow(FOLLOW_1);
            iv_ruleExpansion=ruleExpansion();

            state._fsp--;

             current =iv_ruleExpansion; 
            match(input,EOF,FOLLOW_2); 

            }

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "entryRuleExpansion"


    // $ANTLR start "ruleExpansion"
    // InternalCmcCounter.g:218:1: ruleExpansion returns [EObject current=null] : ( ( (lv_count_0_0= RULE_DOUBLE ) ) ( (lv_frequency_1_0= RULE_DOUBLE ) ) ( (lv_cumulatedFreq_2_0= RULE_DOUBLE ) ) (otherlv_3= '.' | ( ( (lv_rules_4_0= ruleRuleOrText ) ) (otherlv_5= '.' ( (lv_rules_6_0= ruleRuleOrText ) ) )* ) ) ) ;
    public final EObject ruleExpansion() throws RecognitionException {
        EObject current = null;

        Token lv_count_0_0=null;
        Token lv_frequency_1_0=null;
        Token lv_cumulatedFreq_2_0=null;
        Token otherlv_3=null;
        Token otherlv_5=null;
        EObject lv_rules_4_0 = null;

        EObject lv_rules_6_0 = null;



        	enterRule();

        try {
            // InternalCmcCounter.g:224:2: ( ( ( (lv_count_0_0= RULE_DOUBLE ) ) ( (lv_frequency_1_0= RULE_DOUBLE ) ) ( (lv_cumulatedFreq_2_0= RULE_DOUBLE ) ) (otherlv_3= '.' | ( ( (lv_rules_4_0= ruleRuleOrText ) ) (otherlv_5= '.' ( (lv_rules_6_0= ruleRuleOrText ) ) )* ) ) ) )
            // InternalCmcCounter.g:225:2: ( ( (lv_count_0_0= RULE_DOUBLE ) ) ( (lv_frequency_1_0= RULE_DOUBLE ) ) ( (lv_cumulatedFreq_2_0= RULE_DOUBLE ) ) (otherlv_3= '.' | ( ( (lv_rules_4_0= ruleRuleOrText ) ) (otherlv_5= '.' ( (lv_rules_6_0= ruleRuleOrText ) ) )* ) ) )
            {
            // InternalCmcCounter.g:225:2: ( ( (lv_count_0_0= RULE_DOUBLE ) ) ( (lv_frequency_1_0= RULE_DOUBLE ) ) ( (lv_cumulatedFreq_2_0= RULE_DOUBLE ) ) (otherlv_3= '.' | ( ( (lv_rules_4_0= ruleRuleOrText ) ) (otherlv_5= '.' ( (lv_rules_6_0= ruleRuleOrText ) ) )* ) ) )
            // InternalCmcCounter.g:226:3: ( (lv_count_0_0= RULE_DOUBLE ) ) ( (lv_frequency_1_0= RULE_DOUBLE ) ) ( (lv_cumulatedFreq_2_0= RULE_DOUBLE ) ) (otherlv_3= '.' | ( ( (lv_rules_4_0= ruleRuleOrText ) ) (otherlv_5= '.' ( (lv_rules_6_0= ruleRuleOrText ) ) )* ) )
            {
            // InternalCmcCounter.g:226:3: ( (lv_count_0_0= RULE_DOUBLE ) )
            // InternalCmcCounter.g:227:4: (lv_count_0_0= RULE_DOUBLE )
            {
            // InternalCmcCounter.g:227:4: (lv_count_0_0= RULE_DOUBLE )
            // InternalCmcCounter.g:228:5: lv_count_0_0= RULE_DOUBLE
            {
            lv_count_0_0=(Token)match(input,RULE_DOUBLE,FOLLOW_5); 

            					newLeafNode(lv_count_0_0, grammarAccess.getExpansionAccess().getCountDOUBLETerminalRuleCall_0_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getExpansionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"count",
            						lv_count_0_0,
            						"io.github.mzattera.v4j.cmc.count.CmcCounter.DOUBLE");
            				

            }


            }

            // InternalCmcCounter.g:244:3: ( (lv_frequency_1_0= RULE_DOUBLE ) )
            // InternalCmcCounter.g:245:4: (lv_frequency_1_0= RULE_DOUBLE )
            {
            // InternalCmcCounter.g:245:4: (lv_frequency_1_0= RULE_DOUBLE )
            // InternalCmcCounter.g:246:5: lv_frequency_1_0= RULE_DOUBLE
            {
            lv_frequency_1_0=(Token)match(input,RULE_DOUBLE,FOLLOW_5); 

            					newLeafNode(lv_frequency_1_0, grammarAccess.getExpansionAccess().getFrequencyDOUBLETerminalRuleCall_1_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getExpansionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"frequency",
            						lv_frequency_1_0,
            						"io.github.mzattera.v4j.cmc.count.CmcCounter.DOUBLE");
            				

            }


            }

            // InternalCmcCounter.g:262:3: ( (lv_cumulatedFreq_2_0= RULE_DOUBLE ) )
            // InternalCmcCounter.g:263:4: (lv_cumulatedFreq_2_0= RULE_DOUBLE )
            {
            // InternalCmcCounter.g:263:4: (lv_cumulatedFreq_2_0= RULE_DOUBLE )
            // InternalCmcCounter.g:264:5: lv_cumulatedFreq_2_0= RULE_DOUBLE
            {
            lv_cumulatedFreq_2_0=(Token)match(input,RULE_DOUBLE,FOLLOW_7); 

            					newLeafNode(lv_cumulatedFreq_2_0, grammarAccess.getExpansionAccess().getCumulatedFreqDOUBLETerminalRuleCall_2_0());
            				

            					if (current==null) {
            						current = createModelElement(grammarAccess.getExpansionRule());
            					}
            					setWithLastConsumed(
            						current,
            						"cumulatedFreq",
            						lv_cumulatedFreq_2_0,
            						"io.github.mzattera.v4j.cmc.count.CmcCounter.DOUBLE");
            				

            }


            }

            // InternalCmcCounter.g:280:3: (otherlv_3= '.' | ( ( (lv_rules_4_0= ruleRuleOrText ) ) (otherlv_5= '.' ( (lv_rules_6_0= ruleRuleOrText ) ) )* ) )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==10) ) {
                alt5=1;
            }
            else if ( ((LA5_0>=RULE_ID && LA5_0<=RULE_VTEXT)) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // InternalCmcCounter.g:281:4: otherlv_3= '.'
                    {
                    otherlv_3=(Token)match(input,10,FOLLOW_2); 

                    				newLeafNode(otherlv_3, grammarAccess.getExpansionAccess().getFullStopKeyword_3_0());
                    			

                    }
                    break;
                case 2 :
                    // InternalCmcCounter.g:286:4: ( ( (lv_rules_4_0= ruleRuleOrText ) ) (otherlv_5= '.' ( (lv_rules_6_0= ruleRuleOrText ) ) )* )
                    {
                    // InternalCmcCounter.g:286:4: ( ( (lv_rules_4_0= ruleRuleOrText ) ) (otherlv_5= '.' ( (lv_rules_6_0= ruleRuleOrText ) ) )* )
                    // InternalCmcCounter.g:287:5: ( (lv_rules_4_0= ruleRuleOrText ) ) (otherlv_5= '.' ( (lv_rules_6_0= ruleRuleOrText ) ) )*
                    {
                    // InternalCmcCounter.g:287:5: ( (lv_rules_4_0= ruleRuleOrText ) )
                    // InternalCmcCounter.g:288:6: (lv_rules_4_0= ruleRuleOrText )
                    {
                    // InternalCmcCounter.g:288:6: (lv_rules_4_0= ruleRuleOrText )
                    // InternalCmcCounter.g:289:7: lv_rules_4_0= ruleRuleOrText
                    {

                    							newCompositeNode(grammarAccess.getExpansionAccess().getRulesRuleOrTextParserRuleCall_3_1_0_0());
                    						
                    pushFollow(FOLLOW_8);
                    lv_rules_4_0=ruleRuleOrText();

                    state._fsp--;


                    							if (current==null) {
                    								current = createModelElementForParent(grammarAccess.getExpansionRule());
                    							}
                    							add(
                    								current,
                    								"rules",
                    								lv_rules_4_0,
                    								"io.github.mzattera.v4j.cmc.count.CmcCounter.RuleOrText");
                    							afterParserOrEnumRuleCall();
                    						

                    }


                    }

                    // InternalCmcCounter.g:306:5: (otherlv_5= '.' ( (lv_rules_6_0= ruleRuleOrText ) ) )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( (LA4_0==10) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // InternalCmcCounter.g:307:6: otherlv_5= '.' ( (lv_rules_6_0= ruleRuleOrText ) )
                    	    {
                    	    otherlv_5=(Token)match(input,10,FOLLOW_7); 

                    	    						newLeafNode(otherlv_5, grammarAccess.getExpansionAccess().getFullStopKeyword_3_1_1_0());
                    	    					
                    	    // InternalCmcCounter.g:311:6: ( (lv_rules_6_0= ruleRuleOrText ) )
                    	    // InternalCmcCounter.g:312:7: (lv_rules_6_0= ruleRuleOrText )
                    	    {
                    	    // InternalCmcCounter.g:312:7: (lv_rules_6_0= ruleRuleOrText )
                    	    // InternalCmcCounter.g:313:8: lv_rules_6_0= ruleRuleOrText
                    	    {

                    	    								newCompositeNode(grammarAccess.getExpansionAccess().getRulesRuleOrTextParserRuleCall_3_1_1_1_0());
                    	    							
                    	    pushFollow(FOLLOW_8);
                    	    lv_rules_6_0=ruleRuleOrText();

                    	    state._fsp--;


                    	    								if (current==null) {
                    	    									current = createModelElementForParent(grammarAccess.getExpansionRule());
                    	    								}
                    	    								add(
                    	    									current,
                    	    									"rules",
                    	    									lv_rules_6_0,
                    	    									"io.github.mzattera.v4j.cmc.count.CmcCounter.RuleOrText");
                    	    								afterParserOrEnumRuleCall();
                    	    							

                    	    }


                    	    }


                    	    }
                    	    break;

                    	default :
                    	    break loop4;
                        }
                    } while (true);


                    }


                    }
                    break;

            }


            }


            }


            	leaveRule();

        }

            catch (RecognitionException re) {
                recover(input,re);
                appendSkippedTokens();
            }
        finally {
        }
        return current;
    }
    // $ANTLR end "ruleExpansion"

    // Delegated rules


 

    public static final BitSet FOLLOW_1 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_2 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_3 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_4 = new BitSet(new long[]{0x0000000000000200L});
    public static final BitSet FOLLOW_5 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_6 = new BitSet(new long[]{0x0000000000000042L});
    public static final BitSet FOLLOW_7 = new BitSet(new long[]{0x0000000000000430L});
    public static final BitSet FOLLOW_8 = new BitSet(new long[]{0x0000000000000402L});

}