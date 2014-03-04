# Makefile for CMPUT 499 Project 1


################################################################################
# Run Parameters and Class Specifications
################################################################################

# train/test split EG for 80-20 split use 80
TRAIN_SPLIT = 80
N_FOLDS = 5

# regular expressions to match classes for P1
P1_NON_ALP = '^n\d+.*'
P1_ALP_INTERPOLATED = '^Non\d+.*'
P1_ALP = '^\d+.*'

# regular expressions to match classes for P2
P2_A = '^a.*'
P2_B = '^b.*'
P2_OTHER = '^\d+.*'


################################################################################
# Directories
################################################################################

# path to entire dataset
P1_DATA_PATH = part-1-data
P2_DATA_PATH = part-2-data

# path where training data should be copied to
P1_TRAIN_PATH = part-1-train
P2_TRAIN_PATH = part-2-train

# path where testing data should be copied to
P1_TEST_PATH = part-1-test
P2_TEST_PATH = part-2-test

# path where n-fold CV places data
CV_TEST_PATH = cv_test
CV_TRAIN_PATH = cv_train
CV_DATA_PATH = cv_data

# some feature extractors need extra temp space
P24_TEST = p24-test
P25_TEST = p25-test
P2O_TEST = p2o-test
P24_TRAIN = p24-train
P25_TRAIN = p25-train
P2O_TRAIN = p2o-train


################################################################################
# Output File Names
################################################################################

# classifier output filenames
P1_TRAIN_CLASSES = p1_train_classes.out
P2_TRAIN_CLASSES = p2_train_classes.out
P1_TEST_CLASSES = p1_test_classes.out
P2_TEST_CLASSES = p2_test_classes.out
P1_TRAIN_OUT = p1_train_classes_predicted.out
P2_TRAIN_OUT = p2_train_classes_predicted.out
P1_TEST_OUT = p1_test_classes_predicted.out
P2_TEST_OUT = p2_test_classes_predicted.out

# generated arff filenames
P1_TRAIN_ARFF = p1train.arff
P1_TEST_ARFF = p1test.arff
P2_TRAIN_ARFF = p2train.arff
P2_TEST_ARFF = p2test.arff

# generated model filenames
P1_MODEL = p1model.se
P2_MODEL = p2model.se

# classifier generated files
CLASSIFIER_CLASS = Classifier
LEARNER_CLASS = Learner
WEKA_FILES = $(CLASSIFIER_CLASS) $(LEARNER_CLASS)


################################################################################
# Makefile Variables for Cleanup
################################################################################

# temp files that can be cleaned up
P1_OUT_FILES = $(P1_TRAIN_CLASSES) $(P1_TEST_CLASSES) $(P1_TRAIN_OUT) $(P1_TEST_OUT)
P1_MODEL_FILES = $(P1_TEST_ARFF) $(P1_TRAIN_ARFF) $(P1_MODEL)
P2_OUT_FILES = $(P2_TRAIN_CLASSES) $(P2_TEST_CLASSES) $(P2_TRAIN_OUT) $(P2_TEST_OUT)
P2_MODEL_FILES = $(P2_TEST_ARFF) $(P2_TRAIN_ARFF) $(P2_MODEL)

# n-fold CV needs intermediate paths as well. Cleaned between CV iterations
P2_CV_TEST_INTERMEDIATE_FILES = $(P24_TEST)/*.jpg $(P25_TEST)/*.jpg $(P2O_TEST)/*.jpg
P2_CV_TRAIN_INTERMEDIATE_FILES = $(P24_TRAIN)/*.jpg $(P25_TRAIN)/*.jpg $(P2O_TRAIN)/*.jpg


################################################################################
# Script File Names
################################################################################

# generate .arff file using RGB pixel values
PIXEL_ARFF = generate_pixel_arff.py

# do various tranforms on the given images
PREPROCESSOR = preprocess.py

# generate text files with class labels for doing accuracy comparison
CLASS_FILE_GEN = generate_class_file.py

# split data into train and test set
SPLITTER = test_train_split.py

# compare two files of instance labels and print out accuracy
ACCURACY = accuracy.py

# java to build model using Weka
LEARNER_JAVA = P1BuildModel/src/Learner.java

# java to classify using model
CLASSIFIER_JAVA = P1Classify/src/Classifier.java

# java compiler arguments
CLASSPATH = .:../weka-3-6-10/weka.jar

# do n-fold cross-validation
N_FOLD_CV = nfoldcv.py

# copy files into tmp directories, one for each class
SPLIT_CLASSES = split_classes_into_dirs.py

# filenames for preprocessor and and feature extractor parameters
PREPROCESSOR_OPTIONS = preprocessors
P1_FEATURE_EXTRACTORS = feature_extractors_p1
P2_FEATURE_EXTRACTORS = feature_extractors_p2


################################################################################
# Make Commands for Running Part 1 and Part 2
################################################################################

# run part 1 and part 2
all: part1 part2

part1: clean_p1 split_p1 preprocess_p1 p1_learn p1_classify part1_train_acc part1_test_acc

part2: clean_p2 split_p2 preprocess_p2 p2_learn p2_classify part2_train_acc part2_test_acc

part1_nfold: $(N_FOLD_CV) clean split_p1 $(LEARNER_CLASS).class $(CLASSIFIER_CLASS).class
	python nfoldcv.py 1 $(N_FOLDS) $(PREPROCESSOR_OPTIONS) $(P1_FEATURE_EXTRACTORS) 'make part1_cv_class_label_files && make p1_learn' 'make p1_classify && make part1_test_acc' $(P1_TRAIN_PATH) $(CV_DATA_PATH) $(CV_TRAIN_PATH) $(CV_TEST_PATH) $(P1_NON_ALP) $(P1_ALP_INTERPOLATED) $(P1_ALP)

part2_nfold: $(N_FOLD_CV) clean split_p2 $(LEARNER_CLASS).class $(CLASSIFIER_CLASS).class
	python nfoldcv.py 2 $(N_FOLDS) $(PREPROCESSOR_OPTIONS) $(P2_FEATURE_EXTRACTORS) 'make part2_cv_class_label_files && make p2_learn' 'make p2_classify && make part2_test_acc' $(P2_TRAIN_PATH) $(CV_DATA_PATH) $(CV_TRAIN_PATH) $(CV_TEST_PATH) $(P2_A) $(P2_B) $(P2_OTHER)


################################################################################
# Helper Make Commands
################################################################################

# generate class label files
part1_cv_class_label_files: clean_p1_out
	python $(CLASS_FILE_GEN) 1 $(P1_TRAIN_CLASSES) $(CV_TRAIN_PATH)
	python $(CLASS_FILE_GEN) 1 $(P1_TEST_CLASSES) $(CV_TEST_PATH)

part2_cv_class_label_files: clean_p2_out
	python $(CLASS_FILE_GEN) 2 $(P2_TRAIN_CLASSES) $(CV_TRAIN_PATH)
	python $(CLASS_FILE_GEN) 2 $(P2_TEST_CLASSES) $(CV_TEST_PATH)

# print training accuracies
part1_train_acc:
	python $(ACCURACY) Train $(P1_TRAIN_CLASSES) $(P1_TRAIN_OUT)

part2_train_acc:
	python $(ACCURACY) Train $(P2_TRAIN_CLASSES) $(P2_TRAIN_OUT)

# print testing accuracies
part1_test_acc:
	python $(ACCURACY) Test $(P1_TEST_CLASSES) $(P1_TEST_OUT)

part2_test_acc:
	python $(ACCURACY) Test $(P2_TEST_CLASSES) $(P2_TEST_OUT)

# split data into train and test sets
split_p1: $(SPLITTER)
	python $+ $(TRAIN_SPLIT) $(P1_DATA_PATH) $(P1_TRAIN_PATH) $(P1_TEST_PATH) $(P1_NON_ALP) $(P1_ALP_INTERPOLATED) $(P1_ALP)

split_p2: $(SPLITTER)
	python $+ $(TRAIN_SPLIT) $(P2_DATA_PATH) $(P2_TRAIN_PATH) $(P2_TEST_PATH) $(P2_A) $(P2_B) $(P2_OTHER)

# copy files from $(P2_TRAIN_PATH) and $(P2_TEST_PATH) to temporary directores
part2_cp_files_for_extraction: $(SPLIT_CLASSES) clean_part2_intermediate
	python $(SPLIT_CLASSES) $(CV_TRAIN_PATH)/ $(P2_A) $(P24_TRAIN) $(P2_B) $(P25_TRAIN) $(P2_OTHER) $(P2O_TRAIN)
	python $(SPLIT_CLASSES) $(CV_TEST_PATH)/ $(P2_A) $(P24_TEST) $(P2_B) $(P25_TEST) $(P2_OTHER) $(P2O_TEST)

$(LEARNER_CLASS).class: $(LEARNER_JAVA)
	javac -d ./ -classpath $(CLASSPATH) $+

$(CLASSIFIER_CLASS).class: $(CLASSIFIER_JAVA)
	javac -d ./ -classpath $(CLASSPATH) $+

p1_learn: $(LEARNER_CLASS).class
	java -classpath $(CLASSPATH) $(LEARNER_CLASS) $(P1_TRAIN_ARFF) $(P1_MODEL)

p1_classify: $(CLASSIFIER_CLASS).class
	java -classpath $(CLASSPATH) $(CLASSIFIER_CLASS) $(P1_TRAIN_ARFF)  $(P1_MODEL) > $(P1_TRAIN_OUT)
	java -classpath $(CLASSPATH) $(CLASSIFIER_CLASS) $(P1_TEST_ARFF)  $(P1_MODEL) > $(P1_TEST_OUT)

p2_learn: $(LEARNER_CLASS).class
	java -classpath $(CLASSPATH) $(LEARNER_CLASS) $(P2_TRAIN_ARFF) $(P2_MODEL)

p2_classify: $(CLASSIFIER_CLASS).class
	java -classpath $(CLASSPATH) $(CLASSIFIER_CLASS) $(P2_TRAIN_ARFF)  $(P2_MODEL) > $(P2_TRAIN_OUT)
	java -classpath $(CLASSPATH) $(CLASSIFIER_CLASS) $(P2_TEST_ARFF)  $(P2_MODEL) > $(P2_TEST_OUT)


################################################################################
# Image Preprocessor Make Commands
################################################################################

preproc_0: $(PREPROCESSOR)
	python $+ 0 $(CV_DATA_PATH)/

preproc_1: $(PREPROCESSOR)
	python $+ 1 $(CV_DATA_PATH)/

preproc_2: $(PREPROCESSOR)
	python $+ 2 $(CV_DATA_PATH)/


################################################################################
# Feature Extractors for Part 1
################################################################################

# use each pixel RGB as a feature
part1_feat_0: $(PIXEL_ARFF)
	python $+ 1 --include-labels $(P1_TRAIN_ARFF) $(CV_TRAIN_PATH)/
	python $+ 1 $(P1_TEST_ARFF) $(CV_TEST_PATH)/


################################################################################
# Feature Extractors for Part 2
################################################################################

# use each pixel RGB as a feature
part2_feat_0: $(PIXEL_ARFF)
	python $+ 2 --include-labels $(P2_TRAIN_ARFF) $(CV_TRAIN_PATH)/
	python $+ 2 $(P2_TEST_ARFF) $(CV_TEST_PATH)/

part2_feat_1:
	make part2_cp_files_for_extraction
	java -jar JFeatureLib.jar -D CEDD -c classA -d p24-test -o 4-1.csv
	java -jar JFeatureLib.jar -D CEDD -c classA -d p25-test -o 5-1.csv
	java -jar JFeatureLib.jar -D CEDD -c classA -d p2o-test -o o-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p24-test -o 4-2.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p25-test -o 5-2.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p2o-test -o o-2.csv
	python createARFF.py -n 2 --test
	mv out.arff p2test.arff
	java -jar JFeatureLib.jar -D CEDD -c classA -d p24-train -o 4-1.csv
	java -jar JFeatureLib.jar -D CEDD -c classA -d p25-train -o 5-1.csv
	java -jar JFeatureLib.jar -D CEDD -c classA -d p2o-train -o o-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p24-train -o 4-2.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p25-train -o 5-2.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p2o-train -o o-2.csv
	python createARFF.py -n 2
	mv out.arff p2train.arff

part2_feat_2:
	make part2_cp_files_for_extraction
	java -jar JFeatureLib.jar -D CEDD -c classA -d p24-test -o 4-1.csv
	java -jar JFeatureLib.jar -D CEDD -c classA -d p25-test -o 5-1.csv
	java -jar JFeatureLib.jar -D CEDD -c classA -d p2o-test -o o-1.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p24-test -o 4-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p25-test -o 5-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p2o-test -o o-2.csv
	python createARFF.py -n 2 --test
	mv out.arff p2test.arff
	java -jar JFeatureLib.jar -D CEDD -c classA -d p24-train -o 4-1.csv
	java -jar JFeatureLib.jar -D CEDD -c classA -d p25-train -o 5-1.csv
	java -jar JFeatureLib.jar -D CEDD -c classA -d p2o-train -o o-1.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p24-train -o 4-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p25-train -o 5-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p2o-train -o o-2.csv
	python createARFF.py -n 2
	mv out.arff p2train.arff

part2_feat_3:
	make part2_cp_files_for_extraction
	java -jar JFeatureLib.jar -D PHOG -c classA -d p24-test -o 4-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p25-test -o 5-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p2o-test -o o-1.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p24-test -o 4-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p25-test -o 5-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p2o-test -o o-2.csv
	python createARFF.py -n 2 --test
	mv out.arff p2test.arff
	java -jar JFeatureLib.jar -D PHOG -c classA -d p24-train -o 4-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p25-train -o 5-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p2o-train -o o-1.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p24-train -o 4-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p25-train -o 5-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p2o-train -o o-2.csv
	python createARFF.py -n 2
	mv out.arff p2train.arff

part2_feat_4:
	make part2_cp_files_for_extraction
	java -jar JFeatureLib.jar -D PHOG -c classA -d p24-test -o 4-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p25-test -o 5-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p2o-test -o o-1.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p24-test -o 4-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p25-test -o 5-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p2o-test -o o-2.csv
	python createARFF.py -n 2 --test
	mv out.arff p2test.arff
	java -jar JFeatureLib.jar -D PHOG -c classA -d p24-train -o 4-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p25-train -o 5-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p2o-train -o o-1.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p24-train -o 4-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p25-train -o 5-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p2o-train -o o-2.csv
	python createARFF.py -n 2
	mv out.arff p2train.arff

part2_feat_5:
	make part2_cp_files_for_extraction
	java -jar JFeatureLib.jar -D JCD -c classA -d p24-test -o 4-1.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p25-test -o 5-1.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p2o-test -o o-1.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p24-test -o 4-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p25-test -o 5-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p2o-test -o o-2.csv
	python createARFF.py -n 2 --test
	mv out.arff p2test.arff
	java -jar JFeatureLib.jar -D JCD -c classA -d p24-train -o 4-1.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p25-train -o 5-1.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p2o-train -o o-1.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p24-train -o 4-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p25-train -o 5-2.csv
	java -jar JFeatureLib.jar -D FCTH -c classA -d p2o-train -o o-2.csv
	python createARFF.py -n 2
	mv out.arff p2train.arff

part2_feat_6:
	make part2_cp_files_for_extraction
	java -jar JFeatureLib.jar -D PHOG -c classA -d p24-test -o 4-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p25-test -o 5-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p2o-test -o o-1.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p24-test -o 4-2.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p25-test -o 5-2.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p2o-test -o o-2.csv
	python createARFF.py -n 2 --test
	mv out.arff p2test.arff
	java -jar JFeatureLib.jar -D PHOG -c classA -d p24-train -o 4-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p25-train -o 5-1.csv
	java -jar JFeatureLib.jar -D PHOG -c classA -d p2o-train -o o-1.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p24-train -o 4-2.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p25-train -o 5-2.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p2o-train -o o-2.csv
	python createARFF.py -n 2
	mv out.arff p2train.arff

part2_feat_7:
	make part2_cp_files_for_extraction
	java -jar JFeatureLib.jar -D CEDD -c classA -d p24-test -o 4-1.csv
	java -jar JFeatureLib.jar -D CEDD -c classA -d p25-test -o 5-1.csv
	java -jar JFeatureLib.jar -D CEDD -c classA -d p2o-test -o o-1.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p24-test -o 4-2.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p25-test -o 5-2.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p2o-test -o o-2.csv
	python createARFF.py -n 2 --test
	mv out.arff p2test.arff
	java -jar JFeatureLib.jar -D CEDD -c classA -d p24-train -o 4-1.csv
	java -jar JFeatureLib.jar -D CEDD -c classA -d p25-train -o 5-1.csv
	java -jar JFeatureLib.jar -D CEDD -c classA -d p2o-train -o o-1.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p24-train -o 4-2.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p25-train -o 5-2.csv
	java -jar JFeatureLib.jar -D JCD -c classA -d p2o-train -o o-2.csv
	python createARFF.py -n 2
	mv out.arff p2train.arff


################################################################################
# Cleanup Make Commands
################################################################################

clean: clean_p1 clean_p2 clean_part2_intermediate
	-rm -f $(WEKA_FILES)
	-rm -rf $(CV_DATA_PATH)/*.jpg
	-rm -rf $(CV_TRAIN_PATH)/*.jpg
	-rm -rf $(CV_TEST_PATH)/*.jpg

clean_p1: clean_p1_out clean_p1_model
	-rm -rf $(P1_TRAIN_PATH)/*.jpg
	-rm -rf $(P1_TEST_PATH)/*.jpg

clean_p1_out:
	-rm -f $(P1_OUT_FILES)

clean_p1_model:
	-rm -f $(P1_MODEL_FILES)

clean_p2: clean_p2_out clean_p2_model
	-rm -rf $(P2_TRAIN_PATH)/*.jpg
	-rm -rf $(P2_TEST_PATH)/*.jpg

clean_p2_out:
	-rm -f $(P2_OUT_FILES)

clean_p2_model:
	-rm -f $(P2_MODEL_FILES)

clean_part2_intermediate:
	-rm -rf $(P2_CV_TEST_INTERMEDIATE_FILES)
	-rm -rf $(P2_CV_TRAIN_INTERMEDIATE_FILES)
	-rm -f *.csv
	-rm -f jFeatureLib.log

