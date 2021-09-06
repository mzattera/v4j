# v4j

This is a Java library i created to experiment with the [Voynich manuscript](https://en.wikipedia.org/wiki/Voynich_manuscript).

The folder `eclipse` contains an eclipse workspace. The (Maven) project `io.github.mattera.v4j` holds the actual code for the Java library.
The library content is described below. The (Maven) project `io.github.mattera.v4j-apps` contains classes I created to experiment with the 
Voynich manuscript; here you cna find examples about how to use the library.

**_Note:_** _In this document we use the terms "transcription" and "transliteration" interchangeably, though the latter is more correct._

## Packages and Library Overview - Project `io.github.mattera.v4j`

The idea of the library is to provide a "document" object; this is a (possibly) structured text written in one specific "alphabet".
I tried to document the classes using JavaDoc; please refer to the Java source for more details: the below is just a quick intro.


### `io.github.mattera.v4j.text.alphabet`

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
		
The `Alphabet` class is abstract; to provide an actual implementation simply extend this class and provide methods that 
list characters accordingly to their category.

The `Alphabet` class provides some static fields to access already defined alphabets:

- `Alphabet.EVA` is the EVA alphabet.

- `Alphabet.UTF_16` is the UTF-16 char-set used in Java. This is the alphabet to be used to process "normal" (as non-Voynich) text files and strings.
	

### `io.github.mattera.v4j.text`

The `Text` class represents the simplest possible text.

Please note that texts can have a set of markups and/or metadata (e.g. HTML tags); this is the case for the EVA transcriptions of the Voynich used in this library.
For this reason, it makes sense to distinguish between the actual text, including metadata, that you can retrieve with ```getText()``` as opposed 
to the "normalized" text, where all markup is stripped away and  the spaces are made consistent (e.g. no double spaces); this is the version
of the text you will probably want in most cases for processing; it can be obtained with ```getPlainText()```.

This class also provides means to get the text alphabet, split text in words, count character occurrences, etc.

`CompositeText` adds support for structured texts, where a text is composed of units which, in turn, can be structured as well
(think of a book made of chapters made or paragraphs). `filterElements()` and `splitElements()` can be used to select parts of text,
or cut the text into parts, based on rules.

### Getting the Voynich Text - `io.github.mattera.v4j.text.ivtff`

The main class in this package is `IvtffText` that represents a text in IVTFF (Intermediate Voynich Transliteration File Format) format,
as described on [René Zandbergen's website](http://www.voynich.nu/transcr.html). This website provides extensive information about the IVTFF format,
please make sure you understand how the format works as its structure is reflected in the Java classes in this package.

`VoynichFactory` class provides methods to get a copy of the Voynich text as `IvtffText`.
As described on the René Zandbergen's website, there are different transcriptions of the Voynich,
created by different authors (or "transcribers") using different alphabets. This library at the moment can provide two transcriptions (as defined by `IvtffText.Transcription`):

- **`LSI`**: The Landini-Stolfi Interlinear file, a file that uses EVA alphabet and merges transcriptions from several authors in an "interlinear" format,
where multiple versions of each line in the manuscript are provided, one per author (or transcriber).

- **`MZ`**: This is an augmented version of the LSI transcription where two "artificial" authors were created, each corresponding to one of two `IvtffText.TranscriptionType`.

  - **`CONCORDANCE`**: each line of this transcription is created by merging readings from all available transcribers. Only characters that appears to be read 
  in the same way by all authors are considered; other characters (read differently by one ore more transcribers) are marked as unreadable.

  - **`MAJORITY`**: Each character in each line is chosen with a "majority" vote, based on available transcriptions from different authors.
  For example, if two authors read one character as "a" and a third author reads the character as "o", the majority version will show the character as "a"
  (whilst the concordance version will show a "?" instead).

  - **`INTERLINEAR`**: If this type is used, the full (interlinear) transcription is returned.

There are several `VoynichFactory.getDocument(...)` methods to return available transcriptions. Please notice that not all combination of 
transcription, transcription type and alphabet are available.

`IvtffText` is composed of `IvtffPage`s, each having a descriptor, a `PageHeader` instance that can be obtained by using `getDescriptor()`,
which contains IVTFF metadata for the page, such as "language" (A or B), illustration type, position of page in the text (quire, bifolio), etc.

In turn an `IvtffPage`is composed of `IvtffLine`s, each having a descriptor, a `LocusIdentifier` instance that can be obtained by using `getDescriptor()`,
which contains IVTFF metadata for the line, namely the locus identifier and the transcriber. Please notice that for interlinear texts several 
copies of the same line exists with different transcribers.

In addition to inherited methods `filterElements()` and `splitElements()`, the methods `filterPages()`, `filterLines()`, `splitPages()`, and `splitLines()`
can be used to create IVTFF documents by filtering and/or splitting content of an existing document. Again, please refer to JavaDoc fro more details.

```Java
/* Get all biological pages (MAJORITY transcription) */

IvtffText doc = VoynichFactory.getDocument(TranscriptionType.MAJORITY);
doc = doc.filterPages(new PageFilter.Builder().illustrationType("B").build());
```

### Testing (under src/test/java) - `io.github.mattera.v4j.test

Here you can find regression tests implemented with JUnit.






