License Plate Identification using Machine Learning
===================================================


DATA
====

Data for part 1 is in part-1-data/
Data for part 2 is in part-2-data/

Test and training data is split into balanced parts
using the Makefile. Parameters for splitting and the data
paths can be configured in the Makefile.


PART 1
======

Idenfity whether a plate is class ALP (Alberta License Plate) or NON-ALP.


1. Image data should be in part-1-data/
2. run `make part1`
2. run python p1preproc.py --train
3. Place the images to be classified in a folder called predicting-data
4. run python p1preproc.py
5. run python p1preproc.py --class
6. run the java project P1BuildModel
7. run the java project P1Classify, the classifications are output
8. the output from 7 can be compared to the output from 
   5 (classes.out) for correctness


PART 2
======

Identify whether an ALP is class A (ends in 4), class B (ends in 5), or class OTHER.

