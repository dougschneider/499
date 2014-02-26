# Makefile for CMPUT 499 Project 1

# path to entire dataset
P1_DATA_PATH = part-1-data
P2_DATA_PATH = part-2-data

# path where training data should be copied to
P1_TRAIN_PATH = part-1-train
P2_TRAIN_PATH = part-2-train

# path where testing data should be copied to
P1_TEST_PATH = part-1-test
P2_TEST_PATH = part-2-test

# classifier output filenames
P1_TRAIN_CLASSES = p1_train_classes.out
P2_TRAIN_CLASSES = p2_train_classes.out
P1_TEST_CLASSES = p1_test_classes.out
P2_TEST_CLASSES = p2_test_classes.out
P1_TRAIN_OUT = p1_train_classes_predicted.out
P2_TRAIN_OUT = p2_train_classes_predicted.out
P1_TEST_OUT = p1_test_classes_predicted.out
P2_TEST_OUT = p2_test_classes_predicted.out

# train/test split EG for 80-20 split use 80
TRAIN_SPLIT = 80

# regular expressions to match classes for P1
P1_NON_ALP = '^n\d+.*'
P1_ALP_INTERPOLATED = '^Non\d+.*'
P1_ALP = '^\d+.*'

# regular expressions to match classes for P2
P2_A = '^a.*'
P2_B = '^b.*'
P2_OTHER = '^\d+.*'

# generated arff filenames
P1_TRAIN_ARFF = p1train.arff
P1_TEST_ARFF = p1test.arff
P2_TRAIN_ARFF = p2train.arff
P2_TEST_ARFF = p2test.arff

# generated model filenames
P1_MODEL = p1model.se
P2_MODEL = p2model.se

# classifier generated files
WEKA_FILES = Classifier.class Learner.class

# intermediate files that can be cleaned up
P1_TMP_FILES = $(P1_TRAIN_CLASSES) $(P1_TEST_CLASSES) $(P1_TRAIN_ARFF) $(P1_TEST_ARFF) $(P1_MODEL) $(P1_TRAIN_OUT) $(P1_TEST_OUT)
P2_TMP_FILES = $(P2_TRAIN_CLASSES) $(P2_TEST_CLASSES) $(P2_TRAIN_ARFF) $(P2_TEST_ARFF) $(P2_MODEL) $(P2_TRAIN_OUT) $(P2_TEST_OUT)

# filenames for utilities
PREPROCESSOR = preprocess.py
SPLITTER = test_train_split.py
ACCURACY = accuracy.py

# java compiler arguments
CLASSPATH = .:../weka-3-6-10/weka.jar

# run part 1 and part 2
all: part1 part2

part1: clean_p1 split_p1 preprocess_p1 p1_learn p1_classify
	python $(ACCURACY) Train $(P1_TRAIN_CLASSES) $(P1_TRAIN_OUT)
	python $(ACCURACY) Test $(P1_TEST_CLASSES) $(P1_TEST_OUT)

part2: clean_p2 split_p2 preprocess_p2 p2_learn p2_classify
	python $(ACCURACY) Train $(P2_TRAIN_CLASSES) $(P2_TRAIN_OUT)
	python $(ACCURACY) Test $(P2_TEST_CLASSES) $(P2_TEST_OUT)

split_p1: $(SPLITTER)
	python $+ $(TRAIN_SPLIT) $(P1_DATA_PATH) $(P1_TRAIN_PATH) $(P1_TEST_PATH) $(P1_NON_ALP) $(P1_ALP_INTERPOLATED) $(P1_ALP)

split_p2: $(SPLITTER)
	python $+ $(TRAIN_SPLIT) $(P2_DATA_PATH) $(P2_TRAIN_PATH) $(P2_TEST_PATH) $(P2_A) $(P2_B) $(P2_OTHER)

preprocess_p1: $(PREPROCESSOR)
	python $+ 1 --train $(P1_TRAIN_PATH)
	python $+ 1 --test $(P1_TEST_PATH)

preprocess_p2: $(PREPROCESSOR)
	python $+ 2 --train $(P2_TRAIN_PATH)
	python $+ 2 --test $(P2_TEST_PATH)

Learner: P1BuildModel/src/Learner.java
	javac -d ./ -classpath $(CLASSPATH) $+

Classifier: P1Classify/src/Classifier.java
	javac -d ./ -classpath $(CLASSPATH) $+

p1_learn: Learner
	java -classpath $(CLASSPATH) $+ $(P1_TRAIN_ARFF) $(P1_MODEL)

p1_classify: Classifier
	java -classpath $(CLASSPATH) $+ $(P1_TRAIN_ARFF)  $(P1_MODEL) > $(P1_TRAIN_OUT)
	java -classpath $(CLASSPATH) $+ $(P1_TEST_ARFF)  $(P1_MODEL) > $(P1_TEST_OUT)

p2_learn: Learner
	java -classpath $(CLASSPATH) $+ $(P2_TRAIN_ARFF) $(P2_MODEL)

p2_classify: Classifier
	java -classpath $(CLASSPATH) $+ $(P2_TRAIN_ARFF)  $(P2_MODEL) > $(P2_TRAIN_OUT)
	java -classpath $(CLASSPATH) $+ $(P2_TEST_ARFF)  $(P2_MODEL) > $(P2_TEST_OUT)

clean: clean_p1 clean_p2
	-rm -f $(WEKA_FILES)

clean_p1:
	-rm -rf $(P1_TRAIN_PATH)/*.jpg
	-rm -rf $(P1_TEST_PATH)/*.jpg
	-rm -f $(P1_TMP_FILES)

clean_p2:
	-rm -rf $(P2_TRAIN_PATH)/*.jpg
	-rm -rf $(P2_TEST_PATH)/*.jpg
	-rm -f $(P2_TMP_FILES)

