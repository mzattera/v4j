# Note 007 - A Graph View on Word Structure

_Last updated Oct. 24th, 2021._

_This note refers to [release v.5.0.0](https://github.com/mzattera/v4j/tree/v.5.0.0) of v4j;
**links to classes and files refer to this release**; files might have been changed, deleted or moved in the current master branch.
In addition, some of this note content might have become obsolete in more recent versions of the library._

_Working notes are not providing detailed description of algorithms and classes used; for this, please refer to the 
library code and JavaDoc._

_Please refer to the [home page](..) for a set of definitions that might be relevant for this working note._

[**<< Home**](..)

---


# Abstract


## Methodology

This work builds on my [slot model](../005) for Voynich words. 
** Unless differently noted, this pages uses the Slot alphabet to transliterate Voynich words. **

I created a graph where nodes are charters in their slots; e.g. "1_o" represent character 'o' in slot number 1.
After that I connected node A with node B if there is a regular term in the Voynich where character B follows directly character A;
the connection is a directed graph edge which weight is the number of terms where the characters are connected.
For visualization purposes I remove all edges with a weight less than 10.

Final note, when possible I push 'o' from slot 1 to 8, 'y' from slot 1 to 11, and 'y' from 0 to  7 to 10,
as this improves the graph (and might have implications with word structure as well.

The resulting graph is shown below and commented further.

![Complete word structure graph.](images/Complete.PNG)

# Analysis

Here i analyze char connections slot by slot.

## Slot 0

Characters in slot 0 behave quite different one another.

'q' connects mostly with 'o':

![0_q](images/Complete.PNG)

'd' connects with 'o' and 'y', pedestals or 'd' in slot 7:

![0_d](images/Complete.PNG)

's' connects with 'o', pedestal 'C' or 'a' in slot 8:

![0_s](images/Complete.PNG)

## Slot 1

Characters in slot 0 behave quite different one another.

'q' connects mostly with 'o':

![0_q](images/Complete.PNG)






  
	
---

**Notes**

<a id="Note1">**{1}**</a> Personal communication, October 2021.


---

[**<< Home**](..)

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
