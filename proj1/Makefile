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

# run part 1 and part 2
all: part1 part2

part1: clean_p1 split_p1

part2: clean_p2 split_p2

split_p1: test_train_split.py
	python $+ $(TRAIN_SPLIT) $(P1_DATA_PATH) $(P1_TRAIN_PATH) $(P1_TEST_PATH) $(P1_NON_ALP) $(P1_ALP_INTERPOLATED) $(P1_ALP)

split_p2: test_train_split.py
	python $+ $(TRAIN_SPLIT) $(P2_DATA_PATH) $(P2_TRAIN_PATH) $(P2_TEST_PATH) $(P2_A) $(P2_B) $(P2_OTHER)

clean: clean_p1 clean_p2

clean_p1:
	-rm -rf $(P1_TRAIN_PATH)/*.jpg
	-rm -rf $(P1_TEST_PATH)/*.jpg

clean_p2:
	-rm -rf $(P2_TRAIN_PATH)/*.jpg
	-rm -rf $(P2_TEST_PATH)/*.jpg
