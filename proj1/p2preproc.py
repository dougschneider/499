import sys
from scipy import ndimage
import os
from PIL import Image, ImageFilter

def outputTrainingData():
    outputFeatures("p2train.arff", "transformed-data")

def outputPredictingData():
    outputFeatures("p2predict.arff", "predicting-data", includeClass=False)

def outputFeatures(fileName, folder, includeClass=True):
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

def outputClass(fileName, folder):
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

def transform():
    for _, _, filenames in os.walk("part-2-training-data"):
        for filename in filenames:
            if filename.endswith(".jpg"):
                image = Image.open("part-2-training-data/" + filename)
                image = image.filter(ImageFilter.EDGE_ENHANCE_MORE)
                image.save("transformed-data/" + filename);

if __name__ == "__main__":
    if len(sys.argv) > 1:
        if sys.argv[1] == "--train":
            transform()
            outputTrainingData()
        elif sys.argv[1] == "--class":
            outputClass("classes.out", "predicting-data")
        else:
            raise Exception(sys.argv[1] + " is not a valid argument.")
    else:
        outputPredictingData()
