# Welcome to the Repeatable Voynich

Hi, in these pages I store thoughts, working notes, rants and frustrations about the [Voynich manuscript](https://en.wikipedia.org/wiki/Voynich_manuscript)
as resulting from my work with the [v4j library](https://github.com/mzattera/v4j).

One of the aims of these pages is to link each consideration I make in my working notes to a piece of code and a text that can be used to 
reproduce my work and address errors and omissions. In some cases, I also try to reproduce past analysis from other authors or verify common claims about the Voynich
in order to provide some (hopefully) more solid basis for discussions and deciphering attempts.


# The Voynich

Below some links to browse the Voynich online.

* [Voynich Manuscript Voyager](https://www.jasondavies.com/voynich/#outside_front_cover/0.422/0.371/2.30) by Jason Davies.

* Zandbergen's [Voynich MS - Browser](http://www.voynich.nu/folios.html)

* [Voynich Manuscript Project](https://ambertide.github.io/VoynichExplorer/index.html) by Ege Özkan.

* [VIB - Voynich information browser](http://voynich.freie-literatur.de/index.php) by Elias Schwerdtfeger.


# Terminology, Definitions, and Conventions

On this site, I will try to be consistent with following terminology.

- A **transliteration** is a symbol-by-symbol conversion of one script into another. Transliteration is needed to represent the content of the Voynich in a 
format that can be printed or stored in computer files. I might sometimes use the less correct word type **transcription** as a synonym and refer 
to an author of a transliteration as a **transcriber**.

- I refer to the list of symbols used in the target script as the **transliteration alphabet** or simply as the **alphabet**.
Each symbol in the alphabet is referred as a **transliteration character** or simply **character**.

- The word type **glyph** refers to a symbol in the Voynich that appears to constitute a basic unit of text. In principle, a glyph could represent one or more
**Voynich characters** that constitute the **Voynich alphabet**.

-  The question of which glyphs are actual single Voynich characters is still very open and it is at the basis of the different available transliteration alphabets.
Following my analysis in [Note 005](./005), I created a new transliteration alphabet called the [Slot alphabet](alphabet).
  
   Unless stated otherwise, pieces of transliterated Voynich script I quote use the "Basic Eva" as transliteration alphabet and are enclosed in single quotes (e.g. 'qockhey').
More and more frequently, Slot alphabet will be used instead; this will be mention explicitly case by case.

- A **token** in a text is a single sequence of characters, separated by spaces. The list of **word types** (or simply **types**) is the list of tokens without repetitions.
In other words, a token is an instance of a word type. For example; the below line in the Voynich

  ```
  <f1r.15,+P0;m> daiin shckhey ckhor chor shey kol chol chol kor chol
  ```
  
  contains 10 tokens ('daiin', 'shckhey', 'ckhor', 'chor', 'shey', 'kol', 'chol', 'chol', 'kor', 'chol') which are instances of 
  8 word types ('daiin', 'shckhey', 'ckhor', 'chor', 'shey', 'kol', 'chol', 'kor').
  
  When the distinction is not relevant, I might loosely use **word** to refer either to tokens or word types. 


# Working Notes

- [Note 001 - The Text](./001)

  A short description about how the texts used in the library are obtained; it shows how concordance and majority
  versions of the Voynich transliteration are obtained.
  
- [Note 002 - Some Basic Statistics](./002)

  An Excel file with basic page statistics, useful to build pivots.
  
- [Note 003 - Clustering](./003)

  Application of t-SNE visualization and K-Means clustering to the Voynich, showing how page with same illustration type and
  Currier's language also share similar words.
  
  This should be considered when applying statistical analysis methods to the manuscript.

- [Note 004 - On Word Types](./004)

  List of most common Voynichese word types and how they are split across different clusters.

- [Note 005 - Slots and a New Alphabet](./005)

  I show how the structure of Voynich words can be explained by some simple rules, and how these can be used to derive the original Voynich alphabet.

- [Note 006 - Works on Word Structure](./006)

  I list and discuss other approaches to describing the structure of Voynich words, comparing them with my "Slots" concept.
  
- [Note 007 - A Graph View on Word Structure](./007)

  I created a graph showing how characters in words are connected, based on the "Slots" concept.
  
- [Note 008 - Simply the Best Grammar for Voynichese :-)](./008)

  I created a grammar to explain structure of Voynich words, showing it has the best F1 score among all proposed models.
   
- [Note 009 - The Five Languages of the Voynich](./009)

  I used insights provided by the above grammar to show structural differences in words appearing in different sections of the Voynich.
  This suggests: 1) that Currier's languages can be more than 2 and 2) clustering might not be showing a difference in topics.
   
- [Note 010 - Character Distribution Through Clusters](./010)

  I used the Slot alphabet to explore character distribution across clusters in different parts of the page.
   
- [Note 011 - On Length of Tokens](./011)

  Reproducing Vogt's findings on token length across lines, and a minor addition.
   

# Bibliography and Reviews

I will try to put together an annotated [bibliography](./biblio.md) of the things I read.
When appropriate, I will write a small "review" of some of these materials.

- [Review 001 - TILTMAN, John H. (1967): THE VOYNICH MANUSCRIPT "The Most Mysterious Manuscript in the World"](./R001)
 


---

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
