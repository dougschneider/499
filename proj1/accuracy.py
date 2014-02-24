import sys


def compare_files(label, actual, predicted):
    """
    Compare the contents of the two files line-by-line.
    Output the % of lines that differ.
    """
    # pull out all the lines so we can count them
    with open(actual, 'r') as fp:
        actual_lines = [line for line in fp]
    with open(predicted, 'r') as fp:
        predicted_lines = [line for line in fp]

    # if the number of lines are not the same, we have a problem
    if len(actual_lines) != len(predicted_lines):
        print "Files have different numbers of lines."
        sys.exit()

    # lists are of equal length, could use either
    total_lines = len(actual_lines)
    differing_lines = 0
    # check how many lines differ
    for i in xrange(total_lines):
        if actual_lines[i] != predicted_lines[i]:
            #print actual_lines[i] + ":" + predicted_lines[i]
            differing_lines += 1
    
    accuracy = float(total_lines - differing_lines) / float(total_lines) * 100
    print "{} Accuracy: {}%".format(label, accuracy)


if __name__ == "__main__":
    """
    Compare two output files line-by-line.
    """
    usage = "Usage:\n"\
            "{} actual_labels predicted_labels\n\n".format(sys.argv[0])

    if len(sys.argv) > 1:
        label = None
        actual = None
        predicted = None
        label = sys.argv[1]
        actual = sys.argv[2]
        predicted = sys.argv[3]
    else:
        print usage
        sys.exit(2)

    compare_files(label, actual, predicted)

