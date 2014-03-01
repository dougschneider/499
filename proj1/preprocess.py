import sys
from scipy import ndimage
import os
from PIL import Image, ImageFilter, ImageEnhance

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


def _p1_transform(folder):
    for _, _, filenames in os.walk(folder):
        for filename in filenames:
            if filename.endswith(".jpg"):
                full_name = folder + "/" + filename
                image = Image.open(full_name)
                editor = ImageEnhance.Contrast(image)
                image = editor.enhance(1.5)
                image.save(full_name);

def _p2_transform(folder):
    for _, _, filenames in os.walk(folder):
        for filename in filenames:
            if filename.endswith(".jpg"):
                full_name = folder + "/" + filename
                image = Image.open(full_name)
                image = image.filter(ImageFilter.EDGE_ENHANCE_MORE)
                #image = image.convert('L')
                image.save(full_name);


if __name__ == "__main__":
    """
    Parse arguments and split data according to paramaters.
    """
    usage = "Usage:\n"\
            "{} [1|2] [--train|--test] data_dir\n\n"\
            "First parameter is required. Give 1 for part 1, 2 for part 2.\n"\
            "Use --train option to generate arff file with training data\n"\
            "Use --test option to generate arff file with test data (no class labels)\n"\
            "Use --preprocess option to only run preprocessing\n".format(sys.argv[0])

    p = None
    data_dir = None
    if len(sys.argv) > 1:
        p = int(sys.argv[1])
        data_dir = sys.argv[-1]
        
        # monkey-patch in the correct functions
        if p == 1:
            outputFeatures = _p1_outputFeatures
            outputClass = _p1_outputClass
            transform = _p1_transform
        elif p == 2:
            outputFeatures = _p2_outputFeatures
            outputClass = _p2_outputClass
            transform = _p2_transform
        else:
            raise Exception(str(p) + " is not a valid value as the first argument.")

        if sys.argv[2] == "--train":
            transform(data_dir)
            outputClass("p{}_train_classes.out".format(str(p)), data_dir)
            outputFeatures("p{}train.arff".format(str(p)), data_dir)
        elif sys.argv[2] == "--test":
            transform(data_dir)
            outputClass("p{}_test_classes.out".format(str(p)), data_dir)
            outputFeatures("p{}test.arff".format(str(p)), data_dir, includeClass=False)
        elif sys.argv[2] == "--preprocess":
            transform(data_dir)
        else:
            raise Exception(sys.argv[2] + " is not a valid argument.")
    else:
        print usage
        sys.exit(2)

