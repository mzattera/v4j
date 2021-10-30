# Note 006 - Works on Word Structure

_Last updated Oct. 23rd, 2021._

_This note refers to [release v.5.0.0](https://github.com/mzattera/v4j/tree/v.5.0.0) of v4j;
**links to classes and files refer to this release**; files might have been changed, deleted or moved in the current master branch.
In addition, some of this note content might have become obsolete in more recent versions of the library._

_Working notes are not providing detailed description of algorithms and classes used; for this, please refer to the 
library code and JavaDoc._

_Please refer to the [home page](..) for a set of definitions that might be relevant for this working note._

[**<< Home**](..)

---


In this page I will list, review, and comment works from different authors about the structure of Voynich words.
When appropriate I will compare their findings with my [slots concept](../005).

I expect these notes to grow and refine over time (as for the others, to be honest).


# John H. Tiltman

Please see my [review](../R001) of [TILTMAN (1967)](../biblio.md). In a nutshell:

  "_(j) Speaking generally, each symbol behaves as if it had its own
place in an "order of precedence" within words; some symbols such as
'o' and 'y' seem to be able to occupy two functionally different places._" 


# Jorge Stolfi

[Describes](https://www.ic.unicamp.br/~stolfi/voynich/97-11-12-pms/) a decomposition of Voynichese words into three parts; prefix, midfix, and suffix.
Based on a classification of EVA characters into soft and hard letters, he then shows how Voynichese words can be decomposed into
a prefix and suffix made entirely of soft letters, and a midfix made entirely of hard letters.

He then refines this concept into a [grammar for Voynichese words](https://www.ic.unicamp.br/~stolfi/voynich/00-06-07-word-grammar/) where words are parsed into three nested layers;
crust, mantle, and core each composed from a specific subset of the Voynichese alphabet.


# Philip Neal

After writing [working note 005](../005), I realized Philip Neal published a [very similar concept](http://philipneal.net/voynichsources/transcription_neva_spaced/);
his point was that this could be the result of using a grille to produce the text, something similar to the more complete approach described in [RUGG (2004)](../biblio.md).

Neal confirmed [{1}](#Note1) that his grid scheme (which pre-dates my notes) is much the same as my slot concept and,
though he only put f103r on his website, he has analyzed every page of the manuscript along the same lines.

He also mentions he knows of at least another researcher coming to similar conclusions independently.

I read of a Philip Neal’s Voynichese word generator in [Voynich Ninja forums](https://www.voynich.ninja/thread-762-post-6281.html?highlight=%5Bqd_%5D%5Baoy_%5D%5Blr_%5D%5BktpfKTPF_%5D%5BCS_%5D%5BeE_%5D%5Bd%5D%5Bao_%5D%5Blrmn_%5D%5By_%5D#pid6281), described as
a regular expression:

```
^[qd_][aoy_][lr_][ktpfKTPF_][CS_][eE_][d][ao_][lrmn_][y_]

(C =ch; S =sh; E =ee; KTPF = the complex gallows)
```

Another version is found in a [post on Cypher Mysteries](http://ciphermysteries.com/2010/11/22/sean-palmers-voynichese-word-generator):

```
^
(d | k | l | p | r | s | t)?
(o | a)?
(l | r)?
(f | k | p | t)?
(sh | ch )?
(e | ee | eee | eeee)?
(d | cfh | ckh | cph | cth)?
(a | o) ?
(m | n | l | in | iin | iiin)?
(y)?
$
```

Neal also proposes a NEVA transcription; in his own words: "_the NEVA transliteration scheme is designed to remedy the problem
that GC's voyn_101 scheme looks nothing like the Voynichese characters it maps on to. I proposed using accents and diacritics to combine the readability of EVA
with the fine distinctions identified by GC. However, this is less of a problem now that we can display voyn_101 in the Voynich font._" [{1}](#Note1).

So NEVA and the Slot alphabet have different objectives, as my proposal aims at deriving an alphabet "grounds up" from the word structure and is further stepping away from the
"fine distinctions" in Glen Claston's Voynich 101.





# Nick Pelling

Proposes a [Markov state machine](http://web.archive.org/web/20110106080456/http://www.ciphermysteries.com/2010/11/22/sean-palmers-voynichese-word-generator)
to generate Voynichese words.

In his page he mentions grammars attributed to Sean Palmer and Philip Neal, which I should investigate and describe here in more detail.


# Elmar Vogt

Created a [grammar](https://voynichthoughts.wordpress.com/grammar/) for existing words in the Voynich Manuscript.  


# Brian Cham

Brian Cham proposes a new pattern in the text of the Voynich Manuscript named the “[Curve-Line System](https://briancham1994.com/2014/12/17/curve-line-system/)” (CLS).
This pattern is fundamentally based on shapes of individual glyphs but also informs the structure of words.

  
	
---

**Notes**

<a id="Note1">**{1}**</a> Personal communication, October 2021.


---

[**<< Home**](..)

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
