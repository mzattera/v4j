# v4j

This is a Java library i created to experiment with the [Voynich manuscript](https://en.wikipedia.org/wiki/Voynich_manuscript).

The outcomes of my experiments are tracked on the [project pages](https://mzattera.github.io/v4j/).

- The folder `eclipse` contains an [Eclipse](https://www.eclipse.org/ide/) workspace.

  - The (Maven) project `io.github.mzattera.v4j` holds the actual code for the Java library.
The library content is described below.

  - The (Maven) project `io.github.mzattera.v4j-apps` contains classes I created to experiment with the 
Voynich manuscript; here you can find examples about how to use the library.

  - The project `io.github.mzattera.v4j.cmc` is a [Xtext](https://www.eclipse.org/Xtext/) creating when writing [Note 006](https://mzattera.github.io/v4j/006/). 
Please notice you will need to install Xtext inside Eclipse in order for this project to work.

- The folder `KNIME` contains several sub-folders, each corresponding to a different [KNIME](https://www.knime.com/) project.

**_Note:_** Plase check the [project pages](https://mzattera.github.io/v4j/) for some terminology that is relevant here.


## Packages and Library Overview - Project `io.github.mzattera.v4j`

The idea of the library is to provide a "document" object; this is a (possibly) structured text written in one specific "alphabet".
I tried to document the classes using JavaDoc; please refer to the Java source for more details: the below is just a quick intro.

<a id="alphabet"></a>

### `io.github.mzattera.v4j.text.alphabet`

Each text processed by the library is written using a specific alphabet, that is a set of characters used to write the text.
These characters are divided into several categories, that are relevant when processing a text. The `Alphabet` class provides
methods to inspect character types.

- A character is "invalid" if it does not belong to the alphabet; that is, the character should not appear in any
text written using this alphabet.

- Valid characters are divided into "regular" characters, that form words in the text and "special" characters that are used, for example,
as word separators, markup, etc.

- Special characters include "word separators", which separate words in the text. As there might be more than one word separator (e.g. 
punctuation) one of them is chosen as the default "space" character (returned by `getSpace()` and `getSpaceAsString()`).

  Special characters also include "unreadable" characters that are used (e.g. in the EVA alphabet) to mark illegible characters in the original text.
		
The `Alphabet` class is abstract; to provide an actual implementation, simply extend this class and provide methods that 
list characters accordingly to their category.

The `Alphabet` class provides some static fields to access already defined alphabets:

- `Alphabet.EVA` is the Basic EVA alphabet.

- `Alphabet.UTF_16` is the UTF-16 char-set used in Java. This is the alphabet to be used to process "normal" (as non-Voynich) text files and strings.

- `Alphabet.SLOT` is the Slot alphabet as defined in [this working note](https://mzattera.github.io/v4j/005/).
	

### `io.github.mzattera.v4j.text`

The `Text` class represents the simplest possible text.

Please note that texts can have a set of markups and/or metadata (e.g. HTML tags); this is the case for the EVA transcriptions of the Voynich used in this library.
For this reason, it makes sense to distinguish between the actual text, including metadata, that you can retrieve with ```getText()``` as opposed 
to the "normalized" text, where all markup is stripped away and  the spaces are made consistent (e.g. no double spaces); this is the version
of the text you will probably want in most cases for processing; it can be obtained with ```getPlainText()```.

This class also provides means to get the text alphabet, split text in words, count character occurrences, etc.

`CompositeText` adds support for structured texts, where a text is composed of units which, in turn, can be structured as well
(think of a book made of chapters made or paragraphs). `filterElements()` and `splitElements()` can be used to select parts of text,
or cut the text into parts, based on rules.

<a id="ivtff"></a>

### Getting the Voynich Text - `io.github.mzattera.v4j.text.ivtff`

The main class in this package is `IvtffText` that represents a text in IVTFF (Intermediate Voynich Transliteration File Format) format,
as described on [René Zandbergen's website](http://www.voynich.nu/transcr.html). This website provides extensive information about the IVTFF format,
please make sure you understand how the format works as its structure is reflected in the Java classes in this package.

**Currently, v4j supports IVTFF version described in issue 1.5.1, 23/09/2017 of the format definition**; this is not the latest version of IVTFF.

`VoynichFactory` class provides methods to get a copy of the Voynich text as `IvtffText`.
As described on the René Zandbergen's website, there are different transcriptions of the Voynich,
created by different authors (or "transcribers") using different alphabets. This library at the moment can provide two transcriptions (as defined by `IvtffText.Transliteration`):

- **`LSI`**: The Landini-Stolfi Interlinear file, a file that uses EVA alphabet and merges transcriptions from several authors in an "interlinear" format,
where multiple versions of each line in the manuscript are provided, one per author (or transcriber).

- **`AUGMENTED`**: This is an "augmented" version of the LSI transliteration where two "artificial" transcribers were created, 
each corresponding to one of `IvtffText.TranscriptionType` values; `IvtffText.TranscriptionType` can be used in factory methods described below to 
get one of these transcriptions. This transliteration is available both in EVA and Slot alphabet.

  - **`CONCORDANCE`**: each line of this transliteration is created by merging readings from all available transcribers. Only characters that appears to be read 
  in the same way by all authors are considered; other characters (read differently by one ore more transcribers) are marked as unreadable.

  - **`MAJORITY`**: Each character in each line is chosen with a "majority" vote, based on available transcriptions from different authors.
  For example, if two authors read one character as "a" and a third author reads the character as "o", the majority version will show the character as "a"
  (whilst the concordance version will show a "?" instead).

  - **`INTERLINEAR`**: If this `IvtffText.TranscriptionType` is used, the full (interlinear) transliteration is returned.
  
Please notice that, for each of the above transcriptions, the contribution of an individual transcriber can be obtained by using the `filterLines()` and 
`splitLines()` methods described below.

There are several `VoynichFactory.getDocument(...)` methods to return available transcriptions. Please notice that not all combination of 
transliteration, transliteration type and alphabet are available.

`IvtffText` is composed of `IvtffPage`s, each having a descriptor, a `PageHeader` instance that can be obtained by using `getDescriptor()`,
which contains IVTFF metadata for the page, such as "language" (A or B), illustration type (Biological, Herbal, etc.), position of page in the text (quire, bifolio), etc.
Please notice `PageHeader` also provides the parchment (or bifolio) for each of the Voynich pages, even if this information is not provided 
in IVTFF files.

In turn an `IvtffPage`is composed of `IvtffLine`s, each having a descriptor, a `LocusIdentifier` instance that can be obtained by using `getDescriptor()`,
which contains IVTFF metadata for the line, namely the locus identifier and the transcriber. Please notice that for interlinear texts several 
copies of the same line exists with different transcribers.

In addition to inherited methods `filterElements()` and `splitElements()`, the methods `filterPages()`, `filterLines()`, `splitPages()`, and `splitLines()`
can be used to create IVTFF documents by filtering and/or splitting content of an existing document. Again, please refer to JavaDoc for more details.
Also notice that, based on [working note 003](https://mzattera.github.io/v4j/003/), `PageHeader` exposes a cluster for each page in the manuscript;
this information can be used to filter or split the manuscripts into clusters.

Same code snippets can be found below.

```Java
/* Get a document containing all and only biological pages (MAJORITY transliteration) */

IvtffText doc = VoynichFactory.getDocument(TranscriptionType.MAJORITY);
doc = doc.filterPages(new PageFilter.Builder().illustrationType("B").build());
```

```Java
/*
Split the manuscript into clusters (see https://mzattera.github.io/v4j/003/).

clusterMap will match a key like "Cluster=<cluster_name>" into a IvfttText 
with pages in that cluster.

See PageHeader.CLUSTERS for the list of possible <cluster_name> values.
*/

IvtffText = VoynichFactory.getDocument(TranscriptionType.CONCORDANCE);
Map<String, IvtffText> clusterMap = doc.splitPages(new PageSplitter.Builder().byCluster().build());
```

```Java
/*
Print all lines transcribed by Currier.
*/

IvtffText doc = VoynichFactory.getDocument(TranscriptionType.INTERLINEAR);
doc = doc.filterLines(new LineFilter.Builder().transcriber("C").build());
for (IvtffLine l:doc.getLines()) {
	System.out.println(l);
}
```

### Other Texts - `io.github.mzattera.v4j.text.txt`

`TextString` represent a Java string as a `Text` document, whilst `TextFile` represents a text file
composed by numbered `TextLine`s. These classes allow processing normal (e.g. contemporary plain English)
texts within the vj4 library.

Sometimes it is useful to compare Voynich statistics with those from a known text. For this reason
`BibleFactory` provide methods to return the text of the Bible in different languages as `TextFile`
instances.

### Term Decomposition and Analysis

`SlotAlphabet` allows you to decompose words in slots, as described in this [working note](https://mzattera.github.io/v4j/005/).
Class `SlotAlphabet.TermDecomposition` allows you to access term components, including the term prefix, root and suffix fro which
a formal definition is provided.

### Clustering

v4j provides tools for k-means and hierarchical agglomerative clustering using Apache commons-math3 and `org.opencompare.hac`
libraries respectively.

The class `io.github.mzattera.v4j.util.BagOfWords` can be used to build a Bag of Words out of some text(s).
The class can build a BoW where dimensions can be (see `BagOfWordsMode`):

- The count of occurrences for corresponding word.
- The relative frequency of corresponding word in the text.
- 1 or 0, depending whether corresponding word is in the text or not (one-hot encoding).
- TF-IDF frequency for corresponding word; this is supported only when BoW are built from a set of documents.

Notice this class is `Clusterable`, thus can be used with the Apache clustering API where subclasses of `Clusterer<T extends Clusterable>`
are used to cluster set of `Clusterable` instances.

#### K-Means Clustering - `io.github.mzattera.v4j.util.clustering`

Below an example of how BoW instances can be clustered:

```Java
// Distance measure for clustering
DistanceMeasure distance = new PositiveAngularDistance();

// Minimum number of clusters to create
int minSize = 4;

// Maximum number of clusters to create
int maxSize = 8;

// For each possible cluster size, perform this number of trials and return the
// best solution.
int numTrials = 50;

// This is the "evaluator" I use to determine the best trial (how good
// the clustering is)
ClusterEvaluator<BagOfWords> eval = new SilhouetteEvaluator<>(distance);

// Create an Experiment out of BoW for the elements in the document, split
// accordingly to
// DOCUMENT_SPLITTER.
// Our embedding dimensions for the BoW will be the words in the text.
BagOfWordsExperiment experiment = new BagOfWordsExperiment(
		BagOfWords.toBoW(doc.splitPages(ClusteringConfiguration.DOCUMENT_SPLITTER).values(),
				BagOfWords.buildDimensions(doc), ClusteringConfiguration.BOW_MODE));

// Creates a Clusterer and perform the clustering
// Notice that any clusterer in the Apache API could be used instead
Clusterer<BagOfWords> clusterer = new MultiSizeClusterer<>(minSize, maxSize, numTrials, distance, eval);
List<? extends Cluster<BagOfWords>> clusters = clusterer.cluster(experiment.getItems());

...

// Access each cluster like this
clusters.get(i);

// The BoW in the cluster will be these
clusters.get(i).getPoints();

...
```


### Useful Stuff - `io.github.mzattera.v4j.util`

This package contains some "utility" classes to deal with files, math, etc.

Please take a look what is in here before implementing anythign from scratch.


#### `io.github.mzattera.v4j.util.statemachine`

This package provides classes to represent state machines, which were used initially to produce by [Note 007](https://mzattera.github.io/v4j/007/).

It is not meant to replace other, more sophisticated, packages for implementation of state machines; is has been created as a quick hack to provide required
analysis capabilities, focusing on generation of Voynich terms.


### `io.github.mzattera.v4j.cmc`

This is a [Xtext](https://www.eclipse.org/Xtext/) project created for [Note 006](https://mzattera.github.io/v4j/006/); please refer to it for more details.


### Testing

Both projects `io.github.mzattera.v4j` and `io.github.mzattera.v4j-apps` contain JUnit tests.






