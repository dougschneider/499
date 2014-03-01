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


def copy_files(src_path, pairs):
    """
    Make a balanced split of the data in src_dir, placing train_split% of each
    class in train_dir, and (100 - train_split)5 of each class in test_dir.
    """
    for cls, cls_dir in pairs:
        # get all data files of this class
        data = get_instances_of_class(cls, src_dir)
        
        # copy them all into the given directory
        COPY = lambda src, dst, filename:\
                shutil.copy2(
                        "{}/{}".format(src, data[i]),
                        "{}/{}".format(dst, data[i])
                        )
                
        for i in range(len(data)):
            COPY(src_dir, cls_dir, data[i])


if __name__ == "__main__":
    """
    Given a source path, pairs of class regexes and destination paths, copies
    the relevant files to the destination path.
    """
    usage = "Usage:\n"\
            "{} src_dir classs_1_regex "\
            "class_1_path ... class_n_regex class_n_path\n\n"\
            .format(sys.argv[0])

    if len(sys.argv) > 1:
        src_dir = sys.argv[1]
        params = sys.argv[2:]
    else:
        print usage
        sys.exit(2)

    # group regex-dir pairs
    pairs = list()
    for i in xrange(0, len(params), 2):
        pairs.append((params[i], params[i+1]))

    copy_files(src_dir, pairs)

