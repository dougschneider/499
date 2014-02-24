License Plate Identification using Machine Learning
===================================================


DATA
====

Data for part 1 is in part-1-data/
Data for part 2 is in part-2-data/

Test and training data is split into balanced parts
using the Makefile. Parameters for splitting and the data
paths can be configured in the Makefile.


REQUIREMENTS
============
Python requirements listed in requirements.pip.txt
Pillow image library is used, and must be compiled with jpg support (requires libjpeg)
Requires java and javac


PART 1
======

Idenfity whether a plate is class ALP (Alberta License Plate) or NON-ALP.


1. Image data should be in part-1-data/
2. Run `make part1`
3. Training and test accuracies are shown.


PART 2
======

Identify whether an ALP is class A (ends in 4), class B (ends in 5), or class OTHER.

1. Image data should be in part-2-data/
2. Run `make part2`
3. Training and test accuracies are shown.

