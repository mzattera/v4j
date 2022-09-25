# Note 011 - In the Beginning...

_Last updated Sep. 18th, 2022._

_This note refers to [release v.13.0.0](https://github.com/mzattera/v4j/tree/v.13.0.0) of v4j;
**links to classes and files refer to this release**; files might have been changed, deleted or moved in the current master branch.
In addition, some of this note content might have become obsolete in more recent versions of the library._

_Working notes are not providing detailed description of algorithms and classes used; for this, please refer to the 
library code and JavaDoc._

_Please refer to the [home page](..) for a set of definitions that might be relevant for this working note._

[**<< Home**](..)

---

# Abstract

Since the early study  (Currier 1976), it has been suggested line in the Voynich are a "unit of information",
based on the fact that some letters tend to appear at beginning or end of a line (see char distribuion).
Vogt investigated the claim, noticing how the first word in a line is unusually longer compared to others (e la seconda piu' corta?)
this work corroborates these findings in particular:

* Shows that words tend to appear in specific positions in the line (e.g.as second, third, fourth word, etc.).
* There is  a set of defined rules to alter the word in first position in a line, by prefixing a letter.
	COSA MIGLIORE => Fai un'analisi vedendo quali lettere compaio piu' di frequent e quali meno e ragruppa di conseguenza. -> lo puoi fare in automatico coi due elenchi di lettere.
	In tutti queswti casi sembra che il prefisso sia il prefisso di qualcosa che inizia con C-S... o meglio r C S k o a (guardando il grafico di lettere che compaiono poco) o forse solo pedestal e gallows
* There is a set of riles to alter the final word of eaqch line....

**Unless differently indicated, the rest of this note uses the Slot alphabet when quoting Voynich text.**


# Methodology

Char distribution shows something happening.
Char length shows something is happening
  -> "last" words might be an artifact depending on uneven distribution of words

Based on the above:
	There must be some unusual words at beginning of a line
	They must be slightly longer than other words
	They must start with one of the preferred characters
	Words with rare characters should appear less
		-> Hyp.: Words in first column are obtained by prefixing regular words starting with rare chars
		with a short prefix starting with a preferred char

Ignore first and last line of paragraphs where something is also happening maybe.

"Strange" words have a uneven distribution tending to appear in first and last (?) position -> Hyp. confirmed
  -> Cyher might have a memory
  -> Show anyhow that it is strange that 30% of words in a line are strange

Based on the above divide strange words in Initial and a stem. Taker longest prefix; see rule in class. -> initial tend to be a frequent char and stem to start with a rare.
	-> with this we cover only ~70% of strange words: how do other look like? -> show % of verified separable 
	-> If an addition rule apply, last word in line should also be longer....check...

Based on the above consider only Initial starting with a frequent char and stem starting with a rare char. 

Redo stats after applying rules. Show pages with: 1) strange words split 2) strange words not split 3) non-strange words split.
	-> "undoing" this is somewhat arbitrary......


![Summary table of anomalies in char distribution](images/SummaryTable.PNG)

 
# Considerations and Comparison with Previous Works

 
# Conclusions

Words prefer to appear in some places -> memory in the cypher?
	
---

**Notes**

<a id="Note1">**{1}**</a> Class [`TwoSamplesCharDistributionTest`](https://github.com/mzattera/v4j/blob/v.13.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/java/io/github/mzattera/v4j/applications/chars/TwoSamplesCharDistributionTest.java) was used for this purpose.

<a id="Note2">**{2}**</a> The  file `CharacterDistribution.xlsx` in [this folder](https://github.com/mzattera/v4j/blob/master/resources/analysis/char%20distribution) contains 
detailed results of the analysis.


---

[**<< Home**](..)

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
