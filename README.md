# v4j

This is a Java library i created to experiment with the [Voynich manuscript](https://en.wikipedia.org/wiki/Voynich_manuscript).

## Packages and Library Overview

The idea of the library is to provide a "document" object; this is a structured or unstructured
text writing one specific "alphabet".

The folder ```eclipse``` contains an eclipse workspace. The (Maven) project ```lib``` holds the actual code for the Java library.

### ```io.github.mattera.v4j.text```

The [```Text```](https://github.com/mzattera/v4j/blob/b18dbf03ee65fdd3635bdc35ed04430a60276df1/eclipse/lib/src/main/java/io/github/mattera/v4j/text/Text.java) class represents the 
simplest possible text. It has methods to get the text alphabet and to support structured texts

