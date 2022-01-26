# Note 008 - The Best Grammar for Voynichese (as far as I know)

_Last updated Jan. 25th, 2021._

_This note refers to [release v.9.0.0](https://github.com/mzattera/v4j/tree/v.9.0.0) of v4j;
**links to classes and files refer to this release**; files might have been changed, deleted or moved in the current master branch.
In addition, some of this note content might have become obsolete in more recent versions of the library._

_Working notes are not providing detailed description of algorithms and classes used; for this, please refer to the 
library code and JavaDoc._

_Please refer to the [home page](..) for a set of definitions that might be relevant for this working note._

[**<< Home**](..)

---


# Abstract

Following the work described in [Note 005](../005) and [007](../007), I created a grammar for words in the Voynich.
This grammar covers more than 2/3 of the terms in the concordance version of the manuscript. Moreover, it
is the best grammar created so far accordingly to the F-Score, a standard measure widely adopted to quantify performance of classifiers.

In this note I use the terms "model", "gaph", "grammar", "state machine", and "classifier" more or less interchangeably to indicate a representation of the inner structure of Voynich words
as it is typically possible to move from one representation to an equivalent one (e.g. from a formal grammar to the equivalent state machine, to the graph depicting the machine,
 and all can be viewed as classifiers, as explained below).

 
# Methodology

I wrote some code [{1}](#Note1) that, starting from the list of terms in the concordance version of the Voynich (see [Note 001](../001)):

  - Decompose these terms accordingly to the slot model (see [Note 005](../005)).
  
  - Creates a graph connecting character in these terms; edges below a given weight (that is, that are not used by a high enough number of terms).
This is similar to what has been described in [007](../007) [{2}](#Note2).

  - Removes edges in order to increase the F-Score for the corresponding classifier (see below).
  
  - Merges some nodes deemed equivalent (e.g. all sequences of 'e' are made into a single node).
  
  - Perform further optimizations to increase F-Score and remove edges with a given weight.
  
The resulting state machine is described by the below grammar; in this notation:

```
S:
	a,b, -> X,Y
```

means that when the machine is in state S, it can emit any of the two string a or b and then enters in any of the two states X or Y. If we start from the initial state (`<BEGIN>`)
and follow the evolution of the state machine through all possible paths ending with the final state (`<END>`) we will generate all the terms this model defines as terms in the Voynich.
Of course, this list does not match exactly the list of terms in the Voynich 8that is, the Voynich vocabulary); how good this match is, it is discussed in the below sections of this note.

```
<BEGIN>:
	 -> 0_d, 0_q, 0_s, 1_o, 1_y, 2_l, 2_r, 3_t|3_p|3_k|3_f,
	    4_C, 4_S, 5_T|5_P|5_K|5_F, 6_e|6_E|6_B, 7_d, 8_a, 8_o

0_q:
	q -> 1_o, 8_o
0_d:
	d ->           4_C, 4_S, 6_e|6_E|6_B
0_s:
	s ->           4_C, 4_S

1_o:
	o -> 3_t|3_p|3_k|3_f, 4_C,      6_e|6_E|6_B, 7_d, 8_a
1_y:
	y -> 3_t|3_p|3_k|3_f, 4_C, 4_S,              7_d, 8_a

2_l:
	l -> 3_t|3_p|3_k|3_f, 4_C, 4_S, 8_a, 8_o
2_r:
	r ->                  4_C,      8_a, 8_o

3_t|3_p|3_k|3_f:
	f, k, p, t -> 4_C, 6_e|6_E|6_B, 8_a, 8_o, 11_y, <END>

4_C:
	ch -> 6_e|6_E|6_B, 7_s, 8_a, 8_o, 10_d, 11_y, <END>
4_S:
	sh -> 6_e|6_E|6_B,           8_o, 10_d
	
5_T|5_P|5_K|5_F:
	cfh, ckh, cph, cth -> 6_e|6_E|6_B, 8_a, 8_o, 10_d

6_e|6_E|6_B:
	e, ee, eee -> 7_s, 8_o, 10_d, 11_y, <END>

7_d:
	d -> 8_a, 8_o
7_s:
	s -> <END>

8_a:
	a -> 9_i|9_J,       10_l, 10_m, 10_n, 10_r, <END>
8_o:
	o ->          10_d, 10_l,             10_r, <END>

9_i|9_J:
	i, ii -> 10_n, 10_r

10_d:
	d -> 11_y, <END>
10_l:
	l -> 11_y, <END>
10_m:
	m ->       <END>
10_n:
	n ->       <END>
10_r:
	r ->       <END>

11_y:
	y -> <END>

<END>:
```

The state machine is also depicted below [{3}](#Note3) (`<BEGIN>` and `<END>` state omitted for clarity).
Nodes are named after the letter they represent (as explained in [Note 007](../007)), weights on edges show how many tokens in the Voynich contain any given sequence of characters.

![State Machine](images/Graph.PNG)


# Comparison with other works - Classifiers 

See [^5].

| Model 	| Generated terms 	| True Positives 	| Generated Tokens 	| Precision 	| Recall 	| F-Score |
| :--- 	| ---: 	| ---: 	| ---: 	| ---: 	| ---: 	| ---: |
| BEST 	| 3,221	| 1,194	| 67.856%	| 0.371	| 0.232	| 0.286 |
| ROE 	| 84	| 83	| 11.506%	| 0.988	| 0.016	| 0.032 |
| NEAL_1 	| 174,818	| 1,782	| 66.013%	| 0.010	| 0.347	| 0.020 |
| NEAL_2 	| 1,311,345	| 1,049	| 45.248%	| 0.001	| 0.204	| 0.002 |
| VOGT 	| 3,331	| 202	| 20.333%	| 0.061	| 0.090	| 0.073 |
| VOGT 	| 3,806	| 261	| 20.810%	| 0.069	| 0.051	| 0.058 |
| CMC 	| 143,124,560,075,240,080,000	| 4,527	| 97.813%	| 0.000	| 0.881	| 0.000 |
| SLOT 	| 4,643,467	| 2,617	| 86.447%	| 0.001	| 0.509	| 0.001 |

# Considerations


# Conclusions



[^5]: Class ... has been used for this.
	
---

**Notes**

<a id="Note1">**{1}**</a> Class [`BuildSlotStateMachine`](https://github.com/mzattera/v4j/blob/v.9.0.0/eclipse/io.github.mzattera.v4j-apps/src/main/java/io/github/mzattera/v4j/applications/slot/BuildSlotStateMachine.java) was used for
this purpose. It provides means to generate and evaluate state machines and output thm indifferent formats, including a grammar and Java code.

<a id="Note2">**{2}**</a> In this case, best result were obtained by setting minimum weight for edges to 5.

<a id="Note3">**{3}**</a> A version of this graph that can be visualized using [Gephi](https://gephi.org/) (`StateMachine.gephi`) is stored [here](https://github.com/mzattera/v4j/tree/master/resources/analysis/slots).

---

[**<< Home**](..)

Copyright Massimiliano Zattera.

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
