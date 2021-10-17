# Welcome to the Repeatable Voynich

Hi, in these pages I store thoughts, working notes, rants and frustrations about the [Voynich manuscript](https://en.wikipedia.org/wiki/Voynich_manuscript)
as resulting from my work with the [v4j library](https://github.com/mzattera/v4j).

One of the aims of these pages is to link each consideration I make in my working notes to a piece of code and a text that can be used to 
reproduce my work and address errors and omissions. In some cases, I also try to reproduce past analysis from other authors or verify common claims about the Voynich
in order to provide some (hopefully) more solid basis for discussions and deciphering attempts.


# Terminology, Definitions, and Conventions

On this site, I will try to be consistent with following terminology.

- A **transliteration** is a symbol-by-symbol conversion of one script into another. Transliteration is needed to represent the content of the Voynich in a 
format that can be printed or stored in computer files. I might sometimes use the less correct term **transcription** as a synonym and refer 
to an author of a transliteration as a **transcriber**.

  - I refer to the list of symbols used in the target script as the **transliteration alphabet** or simply as the **alphabet**.
Each symbol in the alphabet is referred as a **transliteration character** or simply **character**.

- The term **glyph** refers to a symbol in the Voynich that appears to constitute a basic unit of text. In principle, a glyph could represent one or more
 **Voynich characters** that constitute the **Voynich alphabet**.

  The question of which glyphs are actual single Voynich characters is still very open and it is at the basis of the different transliteration alphabets being created.
  
- Unless stated otherwise, pieces of transliterated Voynich script I quote use the "Basic Eva" as transliteration alphabet and are enclosed in single quotes (e.g. 'qockhey').

- A **token** in a text is a single sequence of characters, separated by spaces. The list of **terms** is the list of tokens, without repetitions.
In other words, a token is an instance of a term. For example; the below line in the Voynich

  ```
  <f1r.15,+P0;m> daiin shckhey ckhor chor shey kol chol chol kor chol
  ```
  
  contains 10 tokens ('daiin', 'shckhey', 'ckhor', 'chor', 'shey', 'kol', 'chol', 'chol', 'kor', 'chol') which are instances of 
  8 terms ('daiin', 'shckhey', 'ckhor', 'chor', 'shey', 'kol', 'chol', 'kor').
  
  When the distinction is not relevant, I might loosely use **word** to refer either to tokens or terms. 


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

- [Note 004 - On Terms](./004)

  List of most common Voynichese terms and how they are split across different clusters.

- [Note 005 - Slots and a New Alphabet](./005)

  I show how the structure of Voynich words can be explained by some simple rules, and how these can be used to derive the original Voynich alphabet.


# Bibliography and Reviews

I will try to put together an annotated [bibliography](./biblio.md) of the things I read, with my comments when I see appropriate.


# The Voynich

Below some links to browse the Voynich online.

* [Voynich Manuscript Voyager](https://www.jasondavies.com/voynich/#outside_front_cover/0.422/0.371/2.30) by Jason Davies.

* [Voynich Manuscript Project](https://ambertide.github.io/VoynichExplorer/index.html).



---

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
