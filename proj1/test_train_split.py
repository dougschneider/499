import sys
import os
import getopt
import random
import shutil
import re

def get_instances_of_class(cls, folder):
    """
    Get the filename (without path) of all files in cls.
    """
    data = list()
    for _, _, filenames in os.walk(folder):
       for filename in filenames:
           if filename.endswith(".jpg"):
               last = filename.split("/")[-1]
               if re.match(cls, last):
                   data.append(last)
    return data


def split_data(train_split, src_dir, train_dir, test_dir, classes):
    """
    Make a balanced split of the data in src_dir, placing train_split% of each
    class in train_dir, and (100 - train_split)5 of each class in test_dir.
    """
    for cls in classes:
        # get all dat files of this class
        data = get_instances_of_class(cls, src_dir)
        
        # how many of the data points are for training?
        train_count = round(len(data) * train_split / 100)
        
        # randomly choose indexes
        train_indexes = set()
        while len(train_indexes) < train_count:
            train_indexes.add(random.randrange(len(data)))
        
        # move all train_indexes to train_dir, others to test_dir
        COPY = lambda src, dst, filename:\
                shutil.copy2(
                        "{}/{}".format(src, data[i]),
                        "{}/{}".format(dst, data[i])
                        )
                
        for i in range(len(data)):
            if i in train_indexes:
                COPY(src_dir, train_dir, data[i])
            else:
                COPY(src_dir, test_dir, data[i])


if __name__ == "__main__":
    """
    Parse arguments and split data according to paramaters.
    """
    argv = sys.argv[1:]
    usage = "Usage:\n"\
            "{} train_split src_dir train_dir test_dir class_1_label class_2_label ... class_n_label\n\n"\
            "train_split: the portion of the data to use for training (EG for 80:20 train:test split use 80 as arg)\n"\
            "src_dir: the directory where the data files are located\n"\
            "train_dir: the directory where to place training examples\n"\
            "test_dir: the directory where to place testing examples\n"\
            "class_n_label: a regex for the files in the class, in single quites (EG '^n.*' or '^')\n".format(sys.argv[0])

    try:
        opts, args = getopt.getopt(argv, "h?")
    except getopt.GetoptError:
        print usage
        sys.exit(2)
    if len(argv) < 6:
        print usage
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h' or opt == '-?':
            print usage
            sys.exit()
    
    # pull arguments from argv
    train_split = int(argv[0])
    src_dir = argv[1]
    train_dir = argv[2]
    test_dir = argv[3]
    classes = argv[4:]

    split_data(train_split, src_dir, train_dir, test_dir, classes)

