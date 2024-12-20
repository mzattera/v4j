# Note 010 - On character distribution

_Last updated Sep. 18th, 2022._

_This note refers to [release v.13.0.0](https://github.com/mzattera/v4j/tree/v.13.0.0) of v4j;
**links to classes and files refer to this release**; files might have been changed, deleted or moved in the current master branch.
In addition, some of this note content might have become obsolete in more recent versions of the library._

_Working notes are not providing detailed description of algorithms and classes used; for this, please refer to the 
library code and JavaDoc._

_Please refer to the [home page](..) for a set of definitions that might be relevant for this working note._

[**<< Home**](..)

---


[note 008](../008)

# Abstract

It is known since the very beginning of Voynich studies, that the distribution of character across the pages presents some statistical anomalies.
This note looks into it, using for the first time the
[Slot transcription](https://github.com/mzattera/v4j/blob/master/eclipse/io.github.mzattera.v4j/src/main/resources/Transcriptions/Interlinear_slot_ivtff_1.5.txt).

**Unless differently indicated, the rest of this note uses the [Slot alphabet](../alphabet) when quoting Voynich text.** 
Please notice that lowercase letters represent same characters both in EVA and Slot alphabets; uppercase letters used in the Slot alphabet represent groups of
characters in EVA (e.g., 'qoCey' in Slot corresponds to 'qochey' in EVA).

# Methodology

In this note I will show the result of several experiments. In each experiment, the text of the Voynich is split into two groups,
for example the first lines of every paragraph will form the first group, whilst the other lines are in the second group;
the character distribution of the two groups is compared using a chi-squared test;
if the test shows a statistically significant variation, then each character is tested individually (again, using a chi-squared test),
to highlight which characters behave differently in the two groups.
The experiments are done separately for each [cluster](../003); 
a concordance version of the text is used; only text appearing in paragraphs is considered (IVTFF locus type = P0 or P1).

The set of experiments is as follows:

  * First line in page - first lines of pages are compared with the rest of the text.
  * First line in paragraph - first lines of paragraphs are compared with the rest of the text.
  * Last line in paragraph - last lines of paragraphs are compared with the rest of the text.
  * First letter in a line - initial character of first word in a line is compared with initial characters of all other words.
  * Last letter in a line - final character of last word in a line is compared with last characters of all other words.

The results are shown in the below table[{1}](#Note1)[{2}](#Note2):

** FAI UN NUOVO TAGLI INCOLLA PERCHE' LA TABELLA e' STAT RIFATTA**
![Summary table of anomalies in char distribution](images/SummaryTable.PNG)

   * Tests:
     * No significance for middle lines compared with themselves ->dovremmo magari avere sempi delle stesse dimensioni
	 * Quando fai le statistiche dei caratteri fallo anche con una versione col testo mescolato del voynich per vedere cosa succede
	 * Usa dinomial invece di chi squared per i carateri?
	 
# Considerations and Previous Works

** Maybe start with a simple table of char frequency by cluster **
  -> **CountCharactersByCluster  -> Character frequencies by cluster is different**

## Last Letter in a Line

# 'm' as Last Character of a Line

[TILTMAN (1967)](../biblio.md) 'm' appears most commonly at the end of a line, rarely elsewhere (b).

[TILTMAN (1967)](../biblio.md) Paragraphs nearly always begin with gallows, most commonly 'f' or 'p', which also occur frequently in words in the top lines of paragraphs where there is some extra space (c).

[TILTMAN (1967)](../biblio.md) 'y' occurs quite frequently as the initial symbol of a line followed immediately by a combination of symbols which seem to be happy without it in any part of a line away from the beginning (d).

[CURRIER (1976)](../biblio.md)
	In addition to my findings about ‘‘languages’’ and hands, there are two other points
	that I’d like to touch on very briefly. Neither of these has, I think, been discussed by anyone else before.

	The first point is that the line is a functional entity in the manuscript on
	all those pages where the text is presented linearly. There are three things about the lines
	that make me believe the line itself is a functional unit.

	  * The frequency counts of the beginnings and endings of lines are markedly different from the counts of the same characters internally.
		There are, for instance, some characters that may not occur initially in a line.
		There are others whose occurrence as the initial syllable of the first ‘‘word’’ of a line is about one hundredth of the expected.
		This by the way, is based on large samples (the biggest sample is 15,000 ‘‘words’’),
		so that I consider the sample to be big enough so that these statistics are significant.
	  * The ends of the lines contain what seem to be, in many cases, meaningless symbols:
		little groups of letters which don’t occur anywhere else, and just look as if they were
		added to fill out the line to the margin. Although this isn’t always true, it frequently happens.
		There is, for instance, one symbol that, while it does occur elsewhere, occurs at the
		end of the last ‘‘words’’ of lines 85% of the time.
	  * One more fact: I have three computer runs of the herbal material and of the biological material.
		In all of that, which is almost 25,000 ‘‘words,’’ there is not one single case of a repeat going over the end of a line to
		the beginning of the next; not one. This is a large sample, too.

	These three findings have convinced me that the line is a functional entity,
	(what its function is, I don’t know), and that the occurrence of certain symbols is governed by the position of a ‘‘word’’ in a line.

	For instance, there is a particular symbol which almost never occurs as the first letter of a
	‘‘word’’ in a line except when it is followed by the letter that looks like ‘‘o.’’
  
  * Skewed frequencies at beginnings of lines may be illustrated by the two letters ch and Sh.
  If its occurrence as an initial were random, we would expect it to occur one seventh of the time in each word position of a line.
  Actually, it is a very infrequent word initial at the beginning of a line, except when there is an intercalated o. This applies only to 'Language' A.
  Other ‘words’ occur in this position far more frequently than expected, particularly ‘words’ with initial ‘dC,’ ‘qC’ etc.,
  which have the appearance of ‘C’-initial ‘words’ suitably modified for line-initial use
  
  * The 'ligatures' [ cKh cTh cFh cPh ] can never occur as paragraph initial, and almost never line initial.

  * 'p' & 'f' appear 90-95% of the time in the first lines of paragraphs, in some 400 occurrences in one section of the manuscript.


** leggi questo qui sono solo millemila pagine**
[D'IMPERIO (1978b)](../biblio.md)
Pag, 28 4.4.1
The following list of characteristics to be explained by any good cryptanalytic theory summarizes the findings of several researches,
notably the Friedmans and Tiltman; it includes also some observations which I have added from my own study of the text.
1) The basic alphabet of frequently occurring symbols is small (as few as fifteen according to some students and probably no more than twentyfive).
2) THe basic forms are compounded or ligatured to create a large variety of complex symbols.
3) THe symbols are groped into "words" separated by spaces (although some students have expressed doubts about the consistency of spacing).
4) THe number of different words seems surprisingly limited.
5) The words are short...
6) The same word is frequently repeated two, three or more times in immediate succession -> MA DOVE CHE NON TROVO RIPETIZIONI
7) Many words differ from each other by only one or two symbols and such words often occur in immediate succession.
8) Certain symbols occurs characteristically at the beginning, middle, and ends of words, and in certain preferred sequences.
9) Certain symbols appear very rarely and only on certain pages, indicating some special functions or meaning.
10) THere are very few doublets and those involve primarily 'e' and 'i', occasionally also 'y', 'd', 'o'.
11) Very few symbols occur singly in running text; these are primarily 's' and 'y'.
12) "Prefix" like elements are tacked in front of certain words that also occur commonly without them; such prefix elements are 'qo', 'o' and 'y'.
13) the symbol 'q' occurs almost invariably followed by 'o; the resulting compound symbol is rarely seen elsewhere than at the beginning of words.
14) On most herbal folios the first line of the first paragraph begins with a very small set of symbols,
    primarily 't', 'k', 'p', 'f', these are usually immediately followed by 'ch', 'sh', 'o', 'y'.
15) Single words occurring as labels...very rarely begin with the four looped symbols (=gallows?);
    instead they often start with 'o', 'd', 'y' and occasionally 's' and 'ch'.



[D'IMPERIO (1978b)](../biblio.md)
The split gallows seem only to occur on first lines of paragraphs, and in labels. -> NON SO DA DOVE ARRIVA


[D'IMPERIO (1978b)](../biblio.md)
One example (of char statistics) is found in D'Imperio (1978) (see note 4), Fig. 28 on p.106, from several sources but none covering the entire MS text.

[D'IMPERIO (1978b)](../biblio.md)
Pag. 23 similarita' fra i Gallows e il ms. Capelli (individuato da TILTMANN).
Pag. 25 Mss. Nill the original text seems not to show corrections or erasures -> it is probably the beginning of this lore.
Pag. 27 Peterson made an elaborated and complete manual concordance of the text, and studied occurrences of ligatured and compound forms of symbols.
Pag. 29 a list of cypher techniques used in 16th century,


** This MIGHT ** be the definition of Grove words? http://voynich.net/Arch/2004/09/msg00478.html it also provide some rules on Gallows.
Gallows are very problematic because:
		F and P are first line of paragraph dependant (out on the limb - about 95%
of them);
		First Gallows on a page can normally be detached from the first word to
form a relatively normal VMS word;
		Split Gallows are quite rare - but do appear as labels or first words on a
page;
		K and T word patterns are very similar okedy otedy;
		Add the ch versions and we now have 8 Gallows plus Split-Gallows;
		The same first line of paragraph dependancy shows on cph and cfh as well.

	I can't see how f,p, cph(iph), cfh(ifh) can be part of some coding scheme
since they are so dependant on first line of a paragraph (unless the whole
first line is setting up a cipher for the rest of the paragraph).
No...another reference here: http://voynich.net/Arch/2004/09/msg00442.html	(STOLFI TALKING ABOUT GROVE WORDS)	
The graphs are new, but the explanation for those peaks in the
line-sorted graph may be a discovery that John Grove made several
years ago: on many lines, the first word looks like a "normal" word
with an extra gallows attached at the beginning.
IIRC, the evidence that those initial gallows are not really part of
the word includes: (1) many of those words are fairly rare, but if one
removes those "detachable gallows" (as John called them), one often
obtains a relatively common word. (2) words that start with a gallows
letter are more common at the beginning of lines than elsewhere; (3)
those words often have two gallows, which is a fairly rare feature of
Voynichese words.
About item (3): to be precise, in some trenscription of the VMS there
are ~930 tokens (in 35,000, or 2.6%) that do not fit my word grammar
because they violate the three-layer (crust-mantle-core-mantle-crust)
rule. Most of these words occur only once in the text. Of those ~930
tokens, ~210 look like Grove words in that they start with a gallows,
and by removing that gallows one obtains a word that fits my word
grammar. (Most of the remaining ~720 anomalies could be pairs of words
that were run together)
I have not checked whether those ~210 "Grove-like" tokens occur at the
beginning of lines or not. They seem to occur at about the same rate
in most sections, but twice as often in Biological, and hardly at all
in Astro/Cosmo. None of them occur as labels.
The big question is what the Grove Words mean. Apart from
cryptographic devices, they could be separators (like the reversed "P"
sign that scribes used to separate paragraphs). Or they could be tags
indicating "fields" in a "form", e.g.

		
[KNIGHT]
Confirms uneven char distribution but does it for the entire text
It is particularly interesting that lower frequency characters occur more at line-ends,
and higher-frequency ones at the beginnings of lines.
    -> DAVVERO!?!?!? INTERESSANTE DA TESTARE vedi io.github.mzattera.v4j.applications.chars.CharByPositionTest


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


** Here or on a separate note? First words in a line do not have same vocabularly than others (see io.github.mzattera.v4j.applications.line.*).
    It is not different words from same dictionary are chosen; it's that new words not used elsewhere are used. Smae for laswt word
	Would be interesting to see the first and last letters of these words.**

** Here or in a separate note: words in differnt clusters have differnt lenght, but they exhibit the behavior described by VOgt**

** f.76r l'elenco di caratteri sono caratteri che compaiono troppo o troppo poco....quasi tutti


## Removing a character at beginning of words

  * L'abbondanza di p e t in prima riga sembra dovita al fatto che vengono aggiunte arbitrariamente a inizio parola di parole che si trovano
  comunque nel test (Groove words) -> occhio che togliendo un carattere dall'iizio di parola si ottiene omunque spesso un-altra parola valida....quindi sto concetto va ussato con prudenza
  idem per 's' in BOWERN -> check if it is still true for words not in frist or last position in a line ;)
  
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
 
# Key-like sequences

Check those appearing as coulm at line initial compared to our list of prefixes (e.g. f49v).



# Conclusions

The most evident thing is that, apart for very few exceptions, characters behave differently in the different clusters.
We summarize below the main trends, but we invite to refer to the above table for a detailed analysis, case by case.

** Casi piu evidenti q d l o n che si comportano in modo marcatamente opposto in cluster diversi**
 
If we look to behaviors that appear consistently across clusters, we can see that:

  * 'k' does not appear in first line of pages and in first line of paragraphs (with a slightly less significance for BB cluster).
  * 'S' and 'p' appear with high frequency in first line of paragraphs.
  * 'y', 't', and 'd' tend to appear as first letter in a line; with the exception of cluster HA where 'd' has the opposite behavior.
    'C', 'S', 'o', and 'a' hardly do; with the exception of cluster HA again where 'o' appears with high frequency.
  * 'l' and 'r' tend not to appear as terminal letter of last word in a line.

	
---

**Notes**

<a id="Note1">**{1}**</a> Class [`TwoSamplesCharDistributionTest`](https://github.com/mzattera/v4j/blob/v.13.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/java/io/github/mzattera/v4j/applications/chars/TwoSamplesCharDistributionTest.java) was used for this purpose.

<a id="Note2">**{2}**</a> The  file `CharacterDistribution.xlsx` in [this folder](https://github.com/mzattera/v4j/blob/master/resources/analysis/char%20distribution) contains 
detailed results of the analysis.


---

[**<< Home**](..)

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
