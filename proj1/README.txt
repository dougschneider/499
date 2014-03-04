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
Requires java (and javac) version 1.7


EXPECTED DATA LABELS
====================

All data files are expected to have a .jpg extention and be nueric file names.
Class labels should be prefixed to the file name in the formats described below.

The Makefile contains regular expressions for these patterns, should you wish
to use different ones.

Data for part 1 is expected to have the following format:
Non-ALP examples should have prefix "n"
Examples with randomly interpolated pixels should have prefix "Non"
ALP filenames should have no prefix.

Data for part 2 is expected to have the following format:
Examples of plates that end in 4 should have prefix "a"
Examples of plates that end in 5 should have prefix "b"
Other examples should have no prefix.


PART 1
======

Idenfity whether a plate is class ALP (Alberta License Plate) or NON-ALP.


1. Image data should be in part-1-data/
2. Run `make part1_train`
	This will perform n-fold cross-validation of combinations of preprocessors
	listed in 'preprocessors' and feature extraction techniques listed in
	'p1_feature_extractors'. The number of folds can be configured in the
	Makefile.
	This command will display the optimal combination of preprocessing
	and feature selection given the dataset. Only the training partition of
	the data is used for cross-validation.
3. Run `make part1_test`
	This will use the previously selected image preprocessing and feature
	extraction methods and run these on the test data.
4. The training and testing accuracy will be printed.


PART 2
======

Identify whether an ALP is class A (ends in 4), class B (ends in 5), or class OTHER.

1. Image data should be in part-2-data/
2. Run `make part2_train`
	This will perform n-fold cross-validation of combinations of preprocessors
	listed in 'preprocessors' and feature extraction techniques listed in
	'p2_feature_extractors'. The number of folds can be configured in the
	Makefile.
	This command will display the optimal combination of preprocessing
	and feature selection given the dataset. Only the training partition of
	the data is used for cross-validation.
3. Run `make part1_test`
	This will use the previously selected image preprocessing and feature
	extraction methods and run these on the test data.
4. The training and testing accuracy will be printed.

