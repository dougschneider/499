License Plate Identification using Machine Learning
===================================================

CMPUT 499 WINTER 2014


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
Pillow image library is used, and must be compiled with jpg support (requires
libjpeg8 and libjpeg8-dev).
Requires java (and javac) version 1.7.

localsetup.sh will install the dependancies and create a python virtualenv.
However, we were having trouble installing scipy through pip, so using the system
package manager for scipy and numpy is likely easier.


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

The steps below are combined into `make part1`.

1. Image data should be in part-1-data/

2. Run `make split_p1`
	This will split the data into randomly selected balanced partitions.
	The training portion is placed in part-1-train/ and the test portion
	in part-1-test/. The sizes of the partitions can be configured in the
	Makefile.
	Skip this step and manually place the train and test set in their
	appropriate folders if you would like to specify test and train
	partitions manually.

3. Run `make part1_preprocessor_select'
	This will perform n-fold cross-validation of combinations of preprocessors
	listed in 'preprocessors' and feature extraction techniques listed in
	'p1_feature_extractors'. The number of folds can be configured in the
	Makefile.
	This command will display the optimal combination of preprocessing
	and feature selection given the dataset. Only the training partition of
	the data is used for cross-validation. The selected preprocessor and
	feature extraction are also written to file for use in later steps.

4. Run `make part1_preprocess`
	This applies the selected preprocessing to the training and testing
	partitions. It also generates the .arff file used to build the models
	using the selected feature extraction method.

5. Run `make part1_train`
	This will build the model.
	
6. Run `make part1_test`
	This will use the previously built model and classify the training and
	testing data. The accuracy of classifying the test and train data
	will be printed.


PART 2
======

Identify whether an ALP is class A (ends in 4), class B (ends in 5), or class OTHER.

The steps below are combined into `make part2`.

1. Image data should be in part-2-data/

2. Run `make split_p2`
	This will split the data into randomly selected balanced partitions.
	The training portion is placed in part-2-train/ and the test portion
	in part-2-test/. The sizes of the partitions can be configured in the
	Makefile.
	Skip this step and manually place the train and test set in their
	appropriate folders if you would like to specify test and train
	partitions manually.

3. Run `make part2_preprocessor_select'
	This will perform n-fold cross-validation of combinations of preprocessors
	listed in 'preprocessors' and feature extraction techniques listed in
	'p2_feature_extractors'. The number of folds can be configured in the
	Makefile.
	This command will display the optimal combination of preprocessing
	and feature selection given the dataset. Only the training partition of
	the data is used for cross-validation. The selected preprocessor and
	feature extraction are also written to file for use in later steps.

4. Run `make part2_preprocess`
	This applies the selected preprocessing to the training and testing
	partitions. It also generates the .arff file used to build the models
	using the selected feature extraction method.

5. Run `make part2_train`
	This will build the model.
	
6. Run `make part2_test`
	This will use the previously built model and classify the training and
	testing data. The accuracy of classifying the test and train data
	will be printed.

