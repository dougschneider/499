import sys
import os
import getopt
import random
import shutil
import re
import subprocess


def make_folds(n, src_dir, classes):
    folds = dict()
    for cls in classes:
        # get all data files of this class
        data = get_instances_of_class(cls, src_dir)
        
        # how many of the data points go into each class?
        fold_count = round(len(data) / n)
        
        # randomly choose indexes
        used_images = set()
        for fold_idx in xrange(n):
            this_fold_images = set()
            while len(this_fold_images) < fold_count:
                idx = random.randrange(len(data))
                if idx in used_images:
                    continue
                this_fold_images.add(idx)
                used_images.add(idx)
                
            # make a new fold using the selected images
            if fold_idx not in folds.keys():
                folds[fold_idx] = list()

            for i in this_fold_images:
                folds[fold_idx].append(data[i])

    folds = [v for k,v in folds.items()]
    assert(len(folds) == n)
    return folds


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


def get_command_list(filename):
    """
    Extract list of lines from filename
    """
    commands = list()
    with open(filename, 'r') as fp:
        for line in fp:
            commands.append(line.strip().replace('\n', ''))
    return commands


def clear_dir(folder):
    """
    Delete all files in folder.
    """
    for the_file in os.listdir(folder):
        file_path = os.path.join(folder, the_file)
        try:
            if os.path.isfile(file_path) and the_file != '.gitkeep':
                os.unlink(file_path)
        except Exception, e:
            print e


def cp_files_to_dir(files, src, dst):
    for f in files:
        shutil.copy2(
                "{}/{}".format(src, f),
                "{}/{}".format(dst, f),
                )


def do_n_fold(p, n, preprocess_file, arff_generator_file, learner_cmd,
        accuracy_cmd, src_dir, data_dir, train_dir, test_dir, classes):
    """
    Do n-fold cross-validation to determine the best combination of all
    preprocessing methods in preprocess_file and all the feature extraction
    methods in arff_generator_file.

    Returns a 3-touple containing the string for the best preprocessing method,
    the string for the best arff generation method, and the test accuracy.
    """
    # get list of commands out of each file
    preproc_commands = get_command_list(preprocess_file)
    feature_commands = get_command_list(arff_generator_file)

    # keep track of accuracy for combos
    averages = list()

    # hold out the first fold first, then the second, and so on
    holdout_fold = 0

    # get list of all files in the src_dir
    all_files = get_instances_of_class('.*\.jpg', src_dir)

    for i in xrange(len(preproc_commands)):
        # clear out the temporary data directory
        clear_dir(data_dir)
        
        # copy all the data into the tmp directory
        cp_files_to_dir(all_files, src_dir, data_dir)

        # split data into n folds:
        folds = make_folds(n, data_dir, classes)

        # apply the image preprocessor to the data
        preproc_cmd = preproc_commands[i]
        print "Preprocessor: " + preproc_cmd

        # run preprocessor
        output = subprocess.Popen(
                [preproc_cmd],
                stdout=subprocess.PIPE,
                stderr=subprocess.STDOUT,
                shell=True
            ).communicate()[0]
        print output

        for j in xrange(len(feature_commands)):
            feature_cmd = feature_commands[j]
            accuracies = list()    

            for k in xrange(n):
                # clear test data and training data
                print "Clearing test and train dirs..."
                clear_dir(train_dir)
                clear_dir(test_dir)

                # put holdout fold in test dir
                print "Placing fold {} files in test dir..."\
                        .format(holdout_fold)
                cp_files_to_dir(folds[holdout_fold], data_dir, test_dir)
                
                # put other folds in training dir
                for f in xrange(len(folds)):
                    if f == holdout_fold:
                        continue
                    cp_files_to_dir(folds[f], data_dir, train_dir)
                
                # run feature extractor
                #feature_cmd = feature_cmd.replace("TEST_PATH", test_dir)
                #feature_cmd = feature_cmd.replace("TRAIN_PATH", train_dir)
                print "Feature Extractor: " + feature_cmd
                output = subprocess.Popen(
                        [feature_cmd],
                        stdout=subprocess.PIPE,
                        stderr=subprocess.STDOUT,
                        shell=True
                    ).communicate()[0]
                print output

                # run learning
                print "Running Learner...."
                output = subprocess.Popen(
                        [learner_cmd],
                        stdout=subprocess.PIPE,
                        stderr=subprocess.STDOUT,
                        shell=True
                    ).communicate()[0]
                print output
                # run test
                print "Classifying Test Data...."
                output = subprocess.Popen(
                        [accuracy_cmd],
                        stdout = subprocess.PIPE,
                        stderr = subprocess.STDOUT,
                        shell=True
                    ).communicate()[0]
                print output
                match = re.search(r'\d+\.\d+', output)
                accuracy = float(match.group(0))
                print "Holdout Fold: {}\t, Accuracy: {}".format(k, accuracy)
                accuracies.append(accuracy)

                # change holdout fold
                holdout_fold += 1

                print ""

            # reset holdout fold
            holdout_fold = 0

            # record the average accuracy for the n folds
            averages.append((preproc_commands[i], feature_commands[j], sum(accuracies) / float(len(accuracies))))
    
    # sort out the top accuracy
    averages.sort(key=lambda tup: tup[2])

    return (averages[-1][0], averages[-1][1], averages[-1][2])


if __name__ == "__main__":
    """
    Do n-fold cross-validation to select best pre-processing methods.
    """
    argv = sys.argv[1:]
    usage = "Usage:\n"\
            "{} p n preprocess_methods arff_generator_methods learner_cmd accuracy_cmd src_dir data_dir train_dir test_dir class_1_label class_2_label ... class_n_label\n\n"\
            "p: part 1 or 2\n"\
            "n: number of folds\n"\
            "preprocess_methods: file containing list of command line calls to do any image preprocessing you'd like to try.\n"\
            "arff_generator_methods: file containing list of command line calls to generate arff files.\n"\
            "learner_cmd: command to perform learning.\n"\
            "accuracy_cmd: command that prints out testing accuracy.\n"\
            "src_dir: the directory where the data files are located\n"\
            "data_dir: a temporary directory where this program can store data files\n"\
            "train_dir: the directory where to place training examples\n"\
            "test_dir: the directory where to place testing examples\n"\
            "class_n_label: a regex for the files in the class, in single quites (EG '^n.*' or '^')\n".format(sys.argv[0])

    try:
        opts, args = getopt.getopt(argv, "h?")
    except getopt.GetoptError:
        print usage
        sys.exit(2)
    if len(argv) < 9:
        print usage
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-h' or opt == '-?':
            print usage
            sys.exit()
    
    # pull arguments from argv
    p = int(argv[0])
    n = int(argv[1])
    preprocess_file = argv[2]
    arff_generator_file = argv[3]
    learner_cmd = argv[4].replace("'","")
    accuracy_cmd = argv[5].replace("'","")
    src_dir = argv[6]
    data_dir = argv[7]
    train_dir = argv[8]
    test_dir = argv[9]
    classes = argv[10:]

    best = do_n_fold(p, n, preprocess_file, arff_generator_file, learner_cmd, accuracy_cmd, src_dir, data_dir, train_dir, test_dir, classes)

    print "Best Combo (with {} accuracy)".format(best[2])
    print "Preprocessor: " + best[0]
    print "Feature Extraction: " + best[1]

