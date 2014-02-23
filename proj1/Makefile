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

# run part 1 and part 2
all: part1 part2

part1: clean_p1

part2: clean_p2

clean: clean_p1 clean_p2

clean_p1:
	-rm -rf P1_TRAIN_PATH
	-rm -rf P1_TEST_PATH

clean_p2:
	-rm -rf P2_TRAIN_PATH
	-rm -rf P2_TEST_PATH
