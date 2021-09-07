## Note 003 - Clustering

_Last updated Sep. 7th, 2021._

_This note refers to [release v.3.0.0](https://github.com/mzattera/v4j/tree/v.3.0.0) of v4j.
Some of the content might not apply to more recent versions of the library._

_Working notes are not providing detailed description of algorithms and classes sued, for this, please refer to the 
library code and JavaDoc._


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
about clustering Voynich pages. The class [`BuildBoW`]() can be used to generate data suitable for visualization; its output for single pages can be found
in [this folder](). There is also a
[pre-populated version of the projector](), that you can use for your own exploration.






 

