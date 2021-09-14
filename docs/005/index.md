## Note 005 - Slots and a New Alphabet

_Last updated Sep. 12th, 2021._

_This note refers to [release v.5.0.0](https://github.com/mzattera/v4j/tree/v.5.0.0) of v4j;
**links to classes and files refer to this release**; files might have been changed, deleted or moved in the current master branch.
In addition, some of this note content might have become obsolete in more recent versions of the library._

_Working notes are not providing detailed description of algorithms and classes used; for this, please refer to the 
library code and JavaDoc._

[**<< Home**](..)

---

# Abstract

We show how the structure of Voynich words can be described is simple terms by assuming each term is composed by "slots" that can be filled
accordingly to simple rules, which are described below.

This in turns shed some lights on the definition of Voynichese characters.

Given the nature of this topic, it is impossible to define rules that apply to 100% of cases; after all, syntactical and grammatical exceptions
exists in any modern text as well. However, we will try to make claims that apply to the vast majority of cases. 

# Previous Works

**TODO** _add the core/mantel/crust and the state machine works_.

- This approach is easier to explain and has more implications.

# Methodology

We start our analysis from a concordance version of the Voynich text (see [Note 001](../001)); this is obtained from the 
Landini-Stolfi Interlinear file by merging available interlinear transcriptions for each transcriber. In the merging, characters that are not
read by all authors in the same way are marked as unreadable. This to ensure the terms we will extract from the text are the most accurate.

For reasons explained below, any occurrence of the following EVA characters is also marked as unreadable:

- 'g', 'x', 'v', 'u'.

- 'c' 'h', when they do NOT appear as 'ch', 'sh' or 'cXh', where 'X' is one of the "gallows" ('t', 'k', 'p', 'f').

As a second step, **tokens** are created by splitting the text where a space was detected by at least one of the transcribers.

The list of **terms** is the list of tokens without repetition (this would be the "vocabulary" of the Voynich). These terms have then been analyzed as
explained below.

# Considerations

By looking at the terms in the Voynich, we see their structure (that is, the sequence of Voynich characters used to write them) can be easily described
as follows:

- each term can be considered as composed by 12 "slots"; for convenience we will number them from 0 to 11.

- each slot can be empty or contain (what we think is) a single character in the Voynich alphabet [{1}](#Note1)). 

- the choice of Voynich characters that can occupy a slot is very limited and for 8 out of 12 slot it is as low as 2-3 possible characters.

- each Voynich character can appear in only one or two slots (with exception of EVA 'd' that can appear in three different slots.

The below table summarizes these rules, showing for each of the 12 slots the Voynich characters that can occupy it.

![Slots](images/Slots Table.PNG)

To exemplify this concept, we show how some common terms can be decomposed in slots;

```
daiin

  0     1     2     3     4     5     6     7     8     9    10    11
[ d ] [   ] [   ] [   ] [   ] [   ] [   ] [   ] [ a ] [ii ] [ n ] [   ] 


qokeedy

  0     1     2     3     4     5     6     7     8     9    10    11
[ q ] [ o ] [   ] [ k ] [   ] [   ] [ee ] [ d ] [   ] [   ] [   ] [ y ] 


opchedy

  0     1     2     3     4     5     6     7     8     9    10    11
[   ] [ o ] [   ] [ p ] [ch ] [   ] [ e ] [ d ] [   ] [   ] [   ] [ y ]


chcthor

  0     1     2     3     4     5     6     7     8     9    10    11
[   ] [   ] [   ] [   ] [ch ] [cth] [   ] [   ] [ o ] [   ] [ r ] [   ] 
```

We then see [{2}](#Note2) that tokens can be classified as follows:

- 27'743 tokens (88.6% of total), corresponding to 2'820 different terms (55.2% of total), can be decomposed by using the above rules. We will call these words "**regular**".

- 2'956 tokens (9.4% of total), corresponding to 1'856 different terms (36.4% of total), can be divided in two parts, each composed by at least two Voynich characters, 
where each of these parts is a regular term. We will call these words "**separable**".

  Moreover, we can see that for 1'379 separable terms, that is 74.3% of total separable terms, their constituent parts appear as tokens in the text at least as often the whole
  separable term. For example, the term 'chockhy' appears 18 times in the text; it is a separable term that can be divided in two parts, each one being a regular term, as
  'cho'-'ckhy' which appear in the text 79 and 39 times respectively. We think this is an indication that many separable terms are possibly just two regular words that were written together
  (or the space between them was not read correctly by the transcriber of the text).
  When we need to distinguish these terms from other separable terms, we will call them **verified separable** or simply **verified**.

  **TODO** check the length of the parts and see if only short terms are joined. Check if separable tends to appear in tight spaces. 

- Remaining 618 tokens (2.0% of total), corresponding to 429 different terms (8.4% of total), are marked as "**unstructured**".

  **TODO** Show that vast majority of unstructured words appear only once in the text. This is probably true for separable too.
  

The below table summarizes these findings.

![Distribution of words accordingly to their classification.](images/Summary.PNG)

In short, almost 9 out of 10 tokens in the Voynich text exhibit a "slot" structure. Of the remaining, a fair amount can be decomposed in two parts each corresponding to regular terms
appearing elsewhere in the text. The remaining cases (2 out of 100) are mostly words appearing only once in the text.

**TODO** Char count by slot

**TODO** Decomposition by cluster.


## Alphabet

The definition of the Voynich alphabet, that is of which hand-strokes should be considered a single character in the text, is still open.

However, if we consider the above defined slots as relevant for the structure of terms, we can reasonably assume that the symbols appearing in each slot constitute a single Voynich character.

- Voynich/EVA chars in slot cells constitute a morphological unit (character).

- It is important both fro attacking the cypher and performing statistical analysis to have 1:1 mapping between Voynich and transcription characters.

- This is the only alphabet that uses data-backed evidence in defining the char-set


### Rare Characters ('g', 'x', 'v', 'u')

**TODO** 

===[ g]=================================		
		
REGULAR	g	1
UNSTRUCTURED	ogam	1
UNSTRUCTURED	oetalCg	1
REGULAR	ypCeg	1
REGULAR	org	1
REGULAR	opalg	1
UNSTRUCTURED	oalCeg	1
UNSTRUCTURED	ydaSgarain	1
		
===[ x]=================================		
		
REGULAR	x	7
REGULAR	soSxar	1
UNSTRUCTURED	Colxy	1
REGULAR	Cxar	1
REGULAR	oxar	1
REGULAR	oxor	1
REGULAR	lxor	1
UNSTRUCTURED	axor	1
UNSTRUCTURED	todaSx	1
REGULAR	Sxam	1
UNSTRUCTURED	rokaix	1
REGULAR	xar	3
UNSTRUCTURED	arxor	1
REGULAR	oxy	1
UNSTRUCTURED	kedarxy	1
REGULAR	xoJn	1
SEPARABLE	salxar	1
UNSTRUCTURED	oSox	1
		
===[ v]=================================		
		
UNSTRUCTURED	v	5
UNSTRUCTURED	vo	1
		
===[ u]=================================		


### Gallows

**TODO** Why do we remove 'c' and 'h' left alone.

===[ c]=================================		
		
UNSTRUCTURED	c	1
UNSTRUCTURED	oc	1
UNSTRUCTURED	Ccpar	1
UNSTRUCTURED	ckSy	1
UNSTRUCTURED	ocfSy	1
		
===[ h]=================================		
		
UNSTRUCTURED	theody	1
UNSTRUCTURED	doFhy	1
UNSTRUCTURED	Fhy	1

**TODO** Why stooled gallows are not ligature of stool + gallow

### The Slot Alphabet 

**TODO**

**TODO** Comparison with other alphabets.

**TODO** Create transcription.

# Conclusions 

  **TODO** Can unstructured an separable be scribe errors?
 
 - As far as we know, this attempt if defining an alphabet is the only one backed up by empirical evidence of Voynich word structure.
 
	
---

**Notes**

<a id="Note1">**{1}**</a> Please notice that it is still controversial what a single character is in Voynichese
(that is, what group of hand strokes constitute a single character). For this reason, different transcription alphabets have been developed.

We think this analysis provide some support for defining the alphabet used by the author of the Voynich.

<a id="Note2">**{2}**</a> Class ['Slots']() has been used to perform this analysis. An Excel with its output can be found in the
[analysis folder]().
 
---

[**<< Home**](..)

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
