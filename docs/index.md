## Welcome to GitHub Pages

Hi, in these pages I store thoughts, working notes, rants and frustrations about the [Voynich manuscript](https://en.wikipedia.org/wiki/Voynich_manuscript)
as resulting from my work with the [v4j library](https://github.com/mzattera/v4j).

### Terminology

In the below notes, we will try to be consistent with following terminology.

- A **token** in the Voynich is a single sequence of characters, separated by spaces. A **term** represent the set of identical tokens.
In other terms, a token is an instance of a term. For example the below line in the Voynich text:

  ```
  <f1r.15,+P0;m> daiin shckhey ckhor chor shey kol chol chol kor chol
  ```
  
  Contains 10 tokens ("daiin", "shckhey", "ckhor", "chor", "shey", "kol", "chol", "chol", "kor", "chol") which are instances of 
  8 terms ("daiin", "shckhey", "ckhor", "chor", "shey", "kol", "chol" "kor").
  
  When the distinction is not relevant, I might loosely use "word" (often in quotes) to refer to either tokens or terms. 


### Working Notes

- [Note 001 - The Text](./001)

  A short description about how the texts used in the library are obtained; it shows how concordance and majority
  versions of the Voynich transcription are obtained.
  
- [Note 002 - Some Basic Statistics](./002)

  An Excel file with basic page statistics, useful to build pivots.



---

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
