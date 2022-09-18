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
other lines are in the second group; the character distribution of the two groups is compared using a chi-squared test, if the test shows a statistically significative variation in character distribution,
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

CountCharactersByCluster  -> Character distribution by cluster.

The most evident thing is that, apart for very few exceptions, characters behave very differently in the different clusters. 
If we look to behaviors that appear consistently across clusters, we can see that:

  * 'k' does not appear in first line of pages and in first line of paragraphs (with a slightly less significance for BB cluster).
  * 'S' and 'p' appear with high frequency in first line of paragraphs.
  * 'y', 't', and 'd' tend to appear as first letter in a line; with the exception of cluster HA where 'd' has the opposite behavior.
    'C', 'S', 'o', and 'a' hardly do; with the exception of cluster HA again where 'o' appears with high frequency.
  * 'l' and 'r' tend not to appear as terminal letter of last word in a line.

## 'm'

[TILTMAN (1967)](../biblio.md) already noticed (p.7 point (b.))

 
  


 
# Conclusions

	
---

**Notes**

<a id="Note1">**{1}**</a> Class [`TwoSamplesCharDistributionTest`](https://github.com/mzattera/v4j/blob/v.12.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/java/io/github/mzattera/v4j/applications/chars/TwoSamplesCharDistributionTest.java) was used for this purpose.

<a id="Note2">**{2}**</a> The  file `CharacterDistribution.xlsx` in [this folder](https://github.com/mzattera/v4j/blob/master/resources/analysis/char%20distribution) contains 
detailed results of the analysis.


---

[**<< Home**](..)

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
