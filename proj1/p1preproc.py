import sys
from scipy import ndimage
import os

def outputTrainingData():
    outputFeatures("p1train.arff", "part-1-training-data")

def outputPredictingData():
    outputFeatures("p1predict.arff", "predicting-data", includeClass=False)

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
                        title += "@ATTRIBUTE class {ALP, NON-ALP}\n\n"
                        title += "@DATA\n"
                        f.write(title)
                        title = ""
                        first = False
                    last = filename.split("/")[-1]
                    if includeClass:
                        if last[0] == "n" or last[0] == "N":
                            s += "NON-ALP"
                        else:
                            s += "ALP"
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
                    if last[0] == "n" or last[0] == "N":
                        f.write("NON-ALP\n");
                    else:
                        f.write("ALP\n")

if __name__ == "__main__":
    if len(sys.argv) > 1:
        if sys.argv[1] == "--train":
            outputTrainingData()
        elif sys.argv[1] == "--class":
            outputClass("classes.out", "predicting-data")
        else:
            raise Exception(sys.argv[1] + " is not a valid argument.")
    else:
        outputPredictingData()
