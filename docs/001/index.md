## Note 001 - The Text

_Last updated Sep. 19th, 2021._

_This note refers to [release v.5.0.0](https://github.com/mzattera/v4j/tree/v.5.0.0) of v4j;
**links to classes and files refer to this release** and files might have been changed, deleted or moved in the current master branch.
In addition, some of this note content might have become obsolete in more recent versions of the library._

_Working notes are not providing detailed description of algorithms and classes used; for this, please refer to the 
library code and JavaDoc._

_Please refer to the [home page](..) for a set of definitions that might be relevant for this working note._

[**<< Home**](..)

---

### The Voynich Text

As explained in the [v4j README](https://github.com/mzattera/v4j#ivtff), the library provides factory methods to 
obtain an `IvtffText` instance with the Voynich text. At present the library provides means to obtain the
Landini-Stolfi Interlinear file (**LSI**) and an augmented version of it, containing concordance and majority versions of the text.

The corresponding IVTFF files (which are read by the factory) can be found in the
[resource folder](https://github.com/mzattera/v4j/tree/v.5.0.0/eclipse/io.github.mattera.v4j/src/main/resources/Transcriptions)
of the library.

The "augmented" EVA version is created using class
[`BuildConcordanceVersion`](https://github.com/mzattera/v4j/blob/v.5.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/java/io/github/mattera/v4j/applications/text/BuildConcordanceVersion.java);
the input for the class is a slightly modified version of LSI that can be found in the
[v4j-apps resource folder](https://github.com/mzattera/v4j/tree/v.5.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/resources/Transcriptions).
In this version, minor changes are done, that do not change the text content, in order to make sure
all the different versions of the lines align properly, as required by `BuildConcordanceVersion` code.

Class
[`BuildSlotVersion`](https://github.com/mzattera/v4j/blob/v.5.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/java/io/github/mattera/v4j/applications/text/BuildSlotVersion.java);
is then used to transcribe the "augmented" version from EVA into Slot alphabeth.

### The Bible Text

Similarly, class
[`BuildBibleTranscription`](https://github.com/mzattera/v4j/blob/v.5.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/java/io/github/mattera/v4j/applications/text/BuildBibleTranscription.java)
is used to produce .txt version if the Bible from  XML files that can be found in the
[v4j-apps resource folder](https://github.com/mzattera/v4j/tree/v.5.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/resources/Transcriptions).

The corresponding IVTFF files (which are read by the factory) can be found in the 
[resource folder](https://github.com/mzattera/v4j/tree/v.5.0.0/eclipse/io.github.mattera.v4j/src/main/resources/Transcriptions/Bible)
of the library.

---

[**<< Home**](..)

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
