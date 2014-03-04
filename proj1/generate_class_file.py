import sys
import os


def _p1_outputClass(fileName, folder):
    with open(fileName, "w+") as f:
        for _, _, filenames in os.walk(folder):
            for filename in filenames:
                if filename.endswith(".jpg"):
                    last = filename.split("/")[-1]
                    if last[0] == "n":
                        f.write("NON-ALP\n")
                    elif last[0] == "N":
                        f.write("NON\n")
                    else:
                        f.write("ALP\n")


def _p2_outputClass(fileName, folder):
    with open(fileName, "w+") as f:
        for _, _, filenames in os.walk(folder):
            for filename in filenames:
                if filename.endswith(".jpg"):
                    last = filename.split("/")[-1]
                    if last[0] == "a":
                        f.write("FOUR\n")
                    elif last[0] == "b":
                        f.write("FIVE\n")
                    else:
                        f.write("OTHER\n")


if __name__ == "__main__":
    """
    Print a file containing class labels for a directory.
    """
    usage = "Usage:\n"\
            "{} 1|2 output_file_name data_dir\n\n"\
            "First parameter is required. Give 1 for part 1, 2 for part 2.\n"\
            "output_file_name is the file to which to print the class lavels."\
            "data_dir is the path for which to print \n".format(sys.argv[0])

    data_dir = None
    output_file_name = None
    p = -1

    if len(sys.argv) > 1:
        p = int(sys.argv[1])
        output_file_name = sys.argv[-2]
        data_dir = sys.argv[-1]

        # monkey-patch in the correct functions
        if p == 1:
            outputClass = _p1_outputClass
        elif p == 2:
            outputClass = _p2_outputClass
        else:
            raise Exception(str(p) + " is not a valid value as the first argument.")

        outputClass(output_file_name, data_dir)
    
    else:
        print usage
        sys.exit(2)

