## Note 003 - Clustering

_Last updated Sep. 7th, 2021._

_This note refers to [release v.3.0.0](https://github.com/mzattera/v4j/tree/v.3.0.0) of v4j.
Some of the content might not apply to more recent versions of the library._

_Working notes are not providing detailed description of algorithms and classes used, for this, please refer to the 
library code and JavaDoc._

[**<< Home**](..)

---

# Abstract

# Previous Works

# Methodology

Our starting point is the Voynich majority transcription of the text (see [v4j README](https://github.com/mzattera/v4j#ivtff)).

## Embedding and Distance Measure

The text is split into units for analysis, that could be single pages or bigger portions of text (e.g. parchments / bi-folios).
Each unit is embedded as a bag of word where the dimensions are the "readable" words in the Voynich (that is, words with no
"unreadable" characters) and the value for the dimension is the number of times corresponding word appears in the text unit.

Similarity of textual unit is computed as positive angular distance of corresponding embedding; this returns angular distance
between two vectors assumed to have only positive components.

## Outliers

The class [`OutlierDetection`]() is used to look for "outliers", that is textual units which appear very dissimilar to other textual units
the output of the class can be seen [here]().
For single pages, we defined (quite arbitrarily :)) the following outliers, which are removed from the text before further analysis.

- **f65r**:
- **f116v**:
- **f53r**:
- **f27v**:
- **f68r2**:
- **f68r1**:
- **f57v**:
- **f72v1**:

## Preliminary Exploration

The [TensorBoard Embedding Projector](https://projector.tensorflow.org/) has been used to do a preliminary, quick and visual investigation
about clustering Voynich pages. The class [`BuildBoW`]() can be used to generate data suitable for visualization that can be uploaded to the projector;
its output for single pages can be found in [this folder]().
There is also a [pre-populated version of the projector](https://projector.tensorflow.org/?config=https://mzattera.github.io/v4j/003/data/projector_config.json),
that you can use for your own exploration.

The below images have been obtained using the projector with following parameters: T-SNE 2D projection, Perplexity=5, Learning rate=0.01, Supervise=0, Iteration=10'000.
 
![T-SNE visualization of Voynich pages](images/SNE - Pages - ALL.PNG)

#### Courier's Language

Pages tend to form three distinct clusters, which are highly correlated with Courier's languages (A or B).

- A cluster of pages using A language (in blue on top-left of the image), composed mostly by Pharmaceutical and Herbal A pages.
- A cluster of pages using B language (in purple on right of the image), composed mostly by Biological and Stars pages.
- A cluster with Zodiac pages (in red on the bottom-left), for which the language is not provided,
  together with Herbal pages using B language (in purple on the bottom-left).
 
![T-SNE visualization of Voynich Biological pages](images/SNE - Pages - BY_LANGUAGE.PNG)

#### Biological Pages

These pages cluster closely together.
 
![T-SNE visualization of Voynich Biological pages](images/SNE - Pages - BB.PNG)

#### Stars Pages

The stars pages tend to cluster together, nest to the Biological pages (they are all written in Courier's B language).
 
![T-SNE visualization of Voynich Stars pages](images/SNE - Pages - S-.PNG)

#### Herbal A Pages

The herbal pages written with Courier's language B tend to cluster together, well separated from Herbal B pages.
 
![T-SNE visualization of Voynich Herbal A pages](images/SNE - Pages - HA.PNG)

#### Pharmaceutical Pages

Those pages tend to cluster together, next to but separated from Herbal A; to be noticed that all Pharmaceutical pages are written
using Courier's language A and many Herbal A pages appears bounded with Pharmaceutical pages in the same parchment.
 
![T-SNE visualization of Voynich Pharmaceutical pages](images/SNE - Pages - PA.PNG)

#### Herbal B Pages

The herbal pages written with Courier's language B tend to cluster together, well separated from Herbal A pages.
 
![T-SNE visualization of Voynich Herbal B pages](images/SNE - Pages - HB.PNG)

#### Zodiac Pages

The zodiac pages tend to cluster together, next to Herbal B pages.
 
![T-SNE visualization of Voynich Zodiac pages](images/SNE - Pages - Z-.PNG)

#### Astronomical Pages

These pages are grouped in two big parchments; f67 and f68.

- f67r1, f67r2, and f68v2 are in the Zodiac cluster.
- f67v1 & f67v2 (technically a Cosmological page), f68v1, and f68r3 are in the Herbal A cluster.
- f68v3 (technically a Cosmological page) is in the Pharmaceutical cluster. 
- f68r1 and f68r2 are outliers.
 
![T-SNE visualization of Voynich Astronomical pages](images/SNE - Pages - A-.PNG)

#### Cosmological Pages

These pages tend to disperse in the dimension space.

- f69r, f69v , f70r1 and f70r2 (the verso of f70 being classified as Zodiac) are next to the Zodiac cluster.
- f85r2 is in the Language B cluster, together with f85r1 (which contains only Text) and f86v3-6; these are part of the fRos ("Big Rosetta") parchment, which clusters nearby.
- f57v is an outlier.
 
![T-SNE visualization of Voynich Cosmological pages](images/SNE - Pages - C-.PNG)

# Conclusions 


---

[**<< Home**](..)

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
