import sys
from scipy import ndimage
import os

def _p1_outputFeatures(fileName, folder, includeClass=True):
    first = True
    with open(fileName, "w+") as f:
        for _, _, filenames in os.walk(folder):
            for filename in filenames:
                if filename.endswith(".jpg"):
                    s = ""
                    title = "@RELATION plate\n\n"
                    num = 1
                    for pixel in ndimage.imread(folder + "/" + filename).flatten():
                        if first:
                            title += "@ATTRIBUTE " + str(num) + " NUMERIC\n"
                            num += 1
                        s += str(pixel) + ","
                    if title != "@RELATION plate\n\n":
                        title += "@ATTRIBUTE class {ALP, NON, NON-ALP}\n\n"
                        title += "@DATA\n"
                        f.write(title)
                        title = ""
                        first = False
                    last = filename.split("/")[-1]
                    if includeClass:
                        if last[0] == "n":
                            s += "NON-ALP"
                        elif last[0] == "N":
                            s += "NON"
                        else:
                            s += "ALP"
                    else:
                        s += "?"
                    s += "\n"
                    f.write(s)


def _p2_outputFeatures(fileName, folder, includeClass=True):
    first = True
    with open(fileName, "w+") as f:
        for _, _, filenames in os.walk(folder):
            for filename in filenames:
                if filename.endswith(".jpg"):
                    s = ""
                    title = "@RELATION plate\n\n"
                    num = 1
                    for pixel in ndimage.imread(folder + "/" + filename).flatten():
                        if first:
                            title += "@ATTRIBUTE " + str(num) + " NUMERIC\n"
                            num += 1
                        s += str(pixel) + ","
                    if title != "@RELATION plate\n\n":
                        title += "@ATTRIBUTE class {FOUR, FIVE, OTHER}\n\n"
                        title += "@DATA\n"
                        f.write(title)
                        title = ""
                        first = False
                    last = filename.split("/")[-1]
                    if includeClass:
                        if last[0] == "a":
                            s += "FOUR"
                        elif last[0] == "b":
                            s += "FIVE"
                        else:
                            s += "OTHER"
                    else:
                        s += "?"
                    s += "\n"
                    f.write(s)


if __name__ == "__main__":
    """
    Generate .arff feature files using the RGB Pixel values of each image.
    """
    usage = "Usage:\n"\
            "{} 1|2 [--include-labels] output_file_name data_dir\n\n"\
            "The first argument is required. Use 1 for part 1, 2 for part 2\n"\
            "Use --include-labels option to include data labels in the arff file\n".format(sys.argv[0])

    data_dir = None
    output_file_name = None
    p = -1

    if len(sys.argv) > 1:
        p = sys.argv[1]
        include_classes = True if sys.argv[2] == '--include-labels' else False
	    output_filename = sys.argv[-2]
        data_dir = sys.argv[-1]

        # monkey-patch in the correct functions
        if p == 1:
            outputFeatures = _p1_outputFeatures
        elif p == 2:
            outputFeatures = _p2_outputFeatures
        else:
            raise Exception(str(p) + " is not a valid value as the first argument.")

        outputFeaturs(output_file_name, data_dir, includeClass=include_classes)
    else:
        print usage
        sys.exit(2)

