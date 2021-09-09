## Note 004 - On Terms

_Last updated Sep. 9th, 2021._

_This note refers to [release v.4.0.0](https://github.com/mzattera/v4j/tree/v.4.0.0) of v4j;
**links to classes and files refer to this release** and files might have been changed, deleted or moved in the current master branch.
In addition, some of this note content might have become obsolete in more recent versions of the library._

_Working notes are not providing detailed description of algorithms and classes used; for this, please refer to the 
library code and JavaDoc._

[**<< Home**](..)

---

The class
['MostUsedTerms'](https://github.com/mzattera/v4j/blob/v.4.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/java/io/github/mattera/v4j/applications/MostUsedTerms.java)
finds top 20 most used terms for each cluster defined in [Note 003](../003) and prints out the result in .CSV format.

An Excel file ("`MostUsedTerms.xlsx`") containing this data can be found under the
[analysis folder](https://github.com/mzattera/v4j/tree/master/resources/analysis).

The below table summarizes the results, showing, the relative frequency of terms in each cluster.

![Most used terms](images/Terms.PNG)

As expected from cluster analysis, beside terms that appear frequently in all clusters (such as **chey**, **daiin**, **dar**, and **dy**),
there are terms characteristic of a single cluster; the table below shows them.

![Most used terms](images/Unique.PNG)

It might be interesting to note that:

- Most common terms in Herbal A pages (HA cluster) starts with ch- or sh-; the latter prefix appearing only here,

- Pharmaceutical (PA cluster) common terms end in -ol, which is rare for other clusters. Also they seem to prefer the ok- or qok- prefix.

- Zodiac (ZZ cluster) common terms mostly start with ot-, uncommon for other clusters. Moreover this cluster
features single characters as common terms.

---

[**<< Home**](..)

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.