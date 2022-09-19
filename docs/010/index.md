# Note 010 - On character distribution

_Last updated Sep. 18th, 2022._

_This note refers to [release v.12.0.0](https://github.com/mzattera/v4j/tree/v.12.0.0) of v4j;
**links to classes and files refer to this release**; files might have been changed, deleted or moved in the current master branch.
In addition, some of this note content might have become obsolete in more recent versions of the library._

_Working notes are not providing detailed description of algorithms and classes used; for this, please refer to the 
library code and JavaDoc._

_Please refer to the [home page](..) for a set of definitions that might be relevant for this working note._

[**<< Home**](..)

---


[note 008](../008)

# Abstract

It is known since the very beginning of Voynich studies, that the distribution of character across the pages has some statistical anomalies.
This note looks into this, using for the first time the [Slot transcription](https://github.com/mzattera/v4j/blob/master/eclipse/io.github.mzattera.v4j/src/main/resources/Transcriptions/Interlinear_slot_ivtff_1.5.txt).

**Unless differently indicated, the rest of this note uses the Slot alphabet when quoting Voynich text.**


# Methodology

In this note I will show the result of several experiments. In each experiment, the text of the Voynich is split into two groups, for example the initial lines of paragraphs are in one group whilst the 
other lines are in the second group; the character distribution of the two groups is compared using a chi-squared test, if the test shows a statistically sigificant variation in character distribution,
then each character is tested individually, to highlight a difference in distribution among the two groups. The experiments are done separately for each [cluster](../003); 
a concordance version of the text is used; only text appearing in paragraphs is considered (IVTFF locus type = P0 or P1).

The set of experiments is as follows:

  * First line in page - all text appearing in first lines of pages is compared with the rest of the text.
  * First line in paragraph - first lines of paragraphs is compared with the rest of the text.
  * Last line in paragraph - all text appearing in last lines of paragraphs is compared with the rest of the text.
  * First letter in a line - initial character of first word in a line is compared with initial characters of all other words.
  * Last letter in a line - final character of last word in a line is compared with last characters of all other words.

The results are shown in the below table[{1}](#Note1)[{2}](#Note2):

![Summary table of anomalies in char distribution](images/SummaryTable.PNG)

 
# Considerations and Comparison with Previous Works

**CountCharactersByCluster  -> Character distribution by cluster is different**

The most evident thing is that, apart for very few exceptions, characters behave differently in the different clusters.
We summarize below the main trends, but we invite to refer to the above table for a detailed analysis, case by case.

** Casi piu evidenti q d l o n che si comportano in modo marcatamente opposto in cluster diversi**
 
If we look to behaviors that appear consistently across clusters, we can see that:

  * 'k' does not appear in first line of pages and in first line of paragraphs (with a slightly less significance for BB cluster).
  * 'S' and 'p' appear with high frequency in first line of paragraphs.
  * 'y', 't', and 'd' tend to appear as first letter in a line; with the exception of cluster HA where 'd' has the opposite behavior.
    'C', 'S', 'o', and 'a' hardly do; with the exception of cluster HA again where 'o' appears with high frequency.
  * 'l' and 'r' tend not to appear as terminal letter of last word in a line.

[TILTMAN (1967)](../biblio.md) 'm' appears most commonly at the end of a line, rarely elsewhere (b).

[TILTMAN (1967)](../biblio.md) Paragraphs nearly always begin with gallows, most commonly 'f' or 'p', which also occur frequently in words in the top lines of paragraphs where there is some extra space (c).

[TILTMAN (1967)](../biblio.md) 'y' occurs quite frequently as the initial symbol of a line followed immediately by a combination of symbols which seem to be happy without it in any part of a line away from the beginning (d).

[CURRIER (1976)](../biblio.md)

  * In those pages where the text is presented linearly, the line is a functional entity. The following three bullets clarify this general observation
	  * the frequency counts of characters at beginnings and endings of lines are markedly different from elsewhere.
	  The frequency counts of the beginnings and endings of lines are markedly different from the counts of the same characters internally.
	  There are, for instance, some characters that may not occur initially in a line.
	  There are others whose occurrence as the initial syllable of the first ‘‘word’’ of a line is about one hundredth of the expected.
	  * The ends of the lines contain what seem to be, in many cases, meaningless symbols:
	  little groups of letters which don’t occur anywhere else, and just look as if they were added to fill out the line to the margin.
	  Although this isn’t always true, it frequently happens.
	  There is, for instance, one symbol that, while it does occur elsewhere, occurs at the end of the last ‘‘words’’ of lines 85% of the time (????? maybe 'm')
	  * there is not one single case of a repeat going over the end of a line to the beginning of the next
  
  * Skewed frequencies at beginnings of lines may be illustrated by the two letters ch and Sh.
  If its occurrence as an initial were random, we would expect it to occur one seventh of the time in each word position of a line.
  Actually, it is a very infrequent word initial at the beginning of a line, except when there is an intercalated o. This applies only to 'Language' A.
  Other ‘words’ occur in this position far more frequently than expected, particularly ‘words’ with initial ‘dC,’ ‘qC’ etc.,
  which have the appearance of ‘C’-initial ‘words’ suitably modified for line-initial use
  
  * The 'ligatures' [ cKh cTh cFh cPh ] can never occur as paragraph initial, and almost never line initial.

  * 'p' & 'f' appear 90-95% of the time in the first lines of paragraphs, in some 400 occurrences in one section of the manuscript.


** leggi questo qui sono solo millemila pagine**
[D'IMPERIO (1978b)](../biblio.md)
The split gallows seem only to occur on first lines of paragraphs, and in labels.
On most herbal folios, the first paragraph usually starts with t, k, p or f, usually immediately followed by ch, Sh, o, y, aiin or dy.
Labels very rarely start with t, k, p or f . Instead, they often start with o, d, y or sometimes s or ch.

[D'IMPERIO (1978b)](../biblio.md)
One example (of char statistics) is found in D'Imperio (1978) (see note 4), Fig. 28 on p.106, from several sources but none covering the entire MS text.

** Guy THE DISTRIBUTION OF LETTERS c AND o IN THE VOYNICH MANUSCRIPT: EVIDENCE FOR A REAL LANGUAGE? **

** URGENT KNIGHT entrambi **

[BOWERN (2020)](../biblio.md)
The clearest example of this phenomenon is the paragraph, which usually begins with a
gallows character. 85% of the paragraphs in the text begin with one of t, k, f, p. These
“gallows-initial” words are (1) otherwise fairly infrequent; and (2) have the same structure as
normal Voynich words except that they are preceded by a gallows character. John Grove first
hypothesized that gallows-initial words were variants of other words
Furthermore gallows-initial words, when they do appear elsewhere, 
usually begin with k k or f f rather than p p or t t. 

[BOWERN (2020)](../biblio.md)
There is a similar but less robust pattern associated with the beginning of each line. The
first word is somewhat more likely to begin with s- s. This may be another orthographic
variant, but it appears to only occur with words that otherwise begin with o- o or a- a. Thus
aiin aiin, ol ol, and or or are replaced with saiin saiin, sol sol, and sor sor.

[BOWERN (2020)](../biblio.md)
There are also characters which usually appear at the end of the last word of the line,
particularly m. It is plausible that m m and g g are variant forms of the word-final glyphs -iin iin and -y y
However, if this is an orthographic convention, it is not applied in a consistent manner: the forms -iin iin and -y
y are also found line-finally, albeit somewhat less frequently.

** leggi questo qui**
[FIRTH]()
On gallows: https://www.ic.unicamp.br/~stolfi/voynich/mirror/firth/16.txt

[ZANDBERGEN (2021)](../biblio.md)
The first is that the first word in each paragraph typically starts with a character from a very small group,
and this character seems to have been pre-fixed to this word. This character is often written larger than
the other characters on the page, and if this character is removed, a regular Voynich word appears, at
least in most cases. These words play a special role in Stolfi’s word grammar, because they usually don’t
fit the grammar, when the extra character is included.

[ZANDBERGEN (2021)](../biblio.md)
The second feature is a much more serious one. This is that the characters f and p , and also cFh and
cPh , tend to appear only in top lines of paragraphs. Furthermore, this is not a very hard rule – they do
appear elsewhere too, but the predominance in top lines of paragraphs is very strong. T 

[ZANDBERGEN (2021)](../biblio.md)
The third feature is similar to the second, but it is less pronounced, and could be easier to explain. This is
the character m that is a word-final character that predominantly (but again not always) appears at the
ends of lines. In this case, the letter could conceivably be a line final variant form of either r or l , but
there are some issues with that hypothesis. 


**I comportamenti a inizio paragrafo o inizio linea soo piu' marcati che alla fine....Qualcos aqundo inizia ascrivere un paragrafo o una line cambia....**

## Removing a character at beginning of words

  * L'abbondanza di p e t in prima riga sembra dovita al fatto che vengono aggiunte arbitrariamente a inizio parola di parole che si trovano
  comunque nel test (Groove words) -> occhio che togliendo un carattere dall'iizio di parola si ottiene omunque spesso un-altra parola valida....quindi sto concetto va ussato con prudenza
  idem per 's' in BOWERN
  
  * Stolfi: https://www.ic.unicamp.br/~stolfi/EXPORT/projects/voynich/00-06-07-word-grammar/#ref4 :
    The paradigm also provides strong support for John Grove's theory that many ordinary-looking words occur prefixed with a spurious "gallows" letter
	(k t p f in the EVA alphabet). => THIS IS THE CASE IN FIRST LINE



## Pedestals  

  * 'S' in first line of paragraphs 

  * 'C' e 'S' in first line of paragraphs and first letter in line. -> lo aveva visto Currier come inizio riga
  
  * Nobody noticed, maybe because in EVA this is treated as two characters ('sh'), which skews the statistics.
  except for Currier who transcripes this as S Z.

	* Guarda comunque anche le differenze nelle percentuali

# 'a' and 'o'

commento aparte first letter in line.
 
# Conclusions

** 	The distribution is not the same across all clusters. Only some letters behave the same across all document Ancora una volta bisogna  fare analisi separate**

	
---

**Notes**

<a id="Note1">**{1}**</a> Class [`TwoSamplesCharDistributionTest`](https://github.com/mzattera/v4j/blob/v.12.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/java/io/github/mzattera/v4j/applications/chars/TwoSamplesCharDistributionTest.java) was used for this purpose.

<a id="Note2">**{2}**</a> The  file `CharacterDistribution.xlsx` in [this folder](https://github.com/mzattera/v4j/blob/master/resources/analysis/char%20distribution) contains 
detailed results of the analysis.


---

[**<< Home**](..)

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
