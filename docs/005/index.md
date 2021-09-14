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

- the choice of Voynich characters that can occupy a slot is very limited and for 8 out of 12 slot is as low as 2-3 possible characters.

- each Voynich character can appear in only one or two slots (with exception of EVA 'd' that can appear in three different slots.

The below table summarizes these rules.

![Slots](images/Slots Table.PNG)

Below, we show how some common terms can be decomposed in slots;

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


**TODO** Decomposition.....

**TODO** Decomposition by cluster.

**TODO** Vast majority of unstructured words appear only once in the text.

## Alphabet

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

### The Slot Alphabet 

**TODO**

**TODO** Comparison with other alphabets.

# Conclusions 

	
---

**Notes**

<a id="Note1">**{1}**</a> Please notice that it is still controversial what a single character is in Voynichese
(that is, what group of hand strokes constitute a single character). For this reason different transcription alphabets have been developed.

We think this analysis provide some support fro defining the alphabet used by the author of the Voynich.
 
---

[**<< Home**](..)

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
