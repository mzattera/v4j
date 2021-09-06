## Note 001 - The Text

_Last updated Sep. 6th, 2021._

_This note refers to [release v.1.0.0](https://github.com/mzattera/v4j/tree/v.1.0.0) of v4j.
Some of the content might not apply to more recent versions of the library._

_Working notes are not providing detailed description of algorithms and classes sued, for this, please refer to the 
library code and JavaDoc._

### The Voynich Text

As explained in the [v4j README](https://github.com/mzattera/v4j#ivtff), the library provides factory methods to 
obtain an `IvtffText` instance with the Voynich text. At present the library provides means to obtain the
Landini-Stolfi Interlinear file (**LSI**) and an augmented version of it, containing concordance and majority versions of the text.

The corresponding IVTFF files (which are read by the factory) can be found in the
[resource folder](https://github.com/mzattera/v4j/tree/v.1.0.0/eclipse/io.github.mattera.v4j/src/main/resources/Transcriptions)
of the library.

The "augmented" version is created using class
[`BuildConcordanceVersion`](https://github.com/mzattera/v4j/blob/d7b349c08c780214bebe3b515623f54951bb3886/eclipse/io.github.mzattera.v4j-apps/src/main/java/io/github/mattera/v4j/applications/BuildConcordanceVersion.java);
the input for the class is a slightly modified version of LSI that can be found in the
[v4j-apps resource folder](https://github.com/mzattera/v4j/tree/v.1.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/resources/Transcriptions).
In this version, minor changes are done, that do not change the text content, in order to make sure
all the different versions of the lines align properly, as required by `BuildConcordanceVersion` code.

### The Bible Text

Similarly, class
[`BuildBibleTranscription`](https://github.com/mzattera/v4j/blob/d7b349c08c780214bebe3b515623f54951bb3886/eclipse/io.github.mzattera.v4j-apps/src/main/java/io/github/mattera/v4j/applications/BuildBibleTranscription.java)
is used to produce .txt version if the Bible from  XML files that can be found in the
[v4j-apps resource folder](https://github.com/mzattera/v4j/tree/v.1.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/resources/Transcriptions).

The corresponding IVTFF files (which are read by the factory) can be found in the 
[resource folder](https://github.com/mzattera/v4j/tree/v.1.0.0/eclipse/io.github.mattera.v4j/src/main/resources/Transcriptions)
of the library.





