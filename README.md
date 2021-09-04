# v4j

This is a Java library i created to experiment with the [Voynich manuscript](https://en.wikipedia.org/wiki/Voynich_manuscript).

## Packages and Library Overview

The idea of the library is to provide a "document" object; this is a structured or unstructured
text writing one specific "alphabet".

The folder ```eclipse``` contains an eclipse workspace. The (Maven) project ```lib``` holds the actual code for the Java library.

### ```io.github.mattera.v4j.text```

The [```Text```](https://github.com/mzattera/v4j/blob/b18dbf03ee65fdd3635bdc35ed04430a60276df1/eclipse/lib/src/main/java/io/github/mattera/v4j/text/Text.java) class represents the 
simplest possible text.

Please note that texts can have a set of markups (e.g. HTML tags), which is the case for the EVA transcription of the Voynich used in this library.
For this reason, it makes sense to distinguish between the actual text, including markups, that you can retrieve with ```getText()``` as opposed 
to the "normalized" text, where all markup is stripped away and  the spaces are made consistent (e.g. no double spaces); this is the version
of the text you will probably want in most cases, for processing; it can be obtained with ```getPlainText()```.

This class also provides means to get the text alphabet, support structured texts, split text in words, count character occurrences, etc.

[```CompositeText```](https://github.com/mzattera/v4j/blob/f09e3b0b7f1e4729570963e0e9c473df3ecadfc9/eclipse/lib/src/main/java/io/github/mattera/v4j/text/CompositeText.java))
add support for structured texts, where a text is composed of units which, in turn can be composed as well (think of a book made of chapters made or paragraphs).

```filterElements()``` and ```splitElements()``` can be used to select parts of text, or cut the text into parts, based on rules.


