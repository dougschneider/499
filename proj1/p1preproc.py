import sys
from scipy import ndimage
import os

def outputTrainingData():
    outputFeatures("p1train.csv", "training-data")

def outputPredictingData():
    outputFeatures("p1predict.csv", "predicting-data", includeClass=False)

def outputFeatures(fileName, folder, includeClass=True):
    first = True
    with open(fileName, "w+") as f:
        for _, _, filenames in os.walk(folder):
            for filename in filenames:
                if filename.endswith(".jpg"):
                    s = ""
                    title = ""
                    num = 1
                    for pixel in ndimage.imread(folder + "/" + filename).flatten():
                        if first:
                            title += str(num) + ","
                            num += 1
                        s += str(pixel) + ","
                    if title != "":
                        if includeClass:
                            title += "class\n"
                        else:
                            title = title.rstrip(",")
                        f.write(title)
                        title = ""
                        first = False
                    last = filename.split("/")[-1]
                    if includeClass:
                        if last[0] == "n" or last[0] == "N":
                            s += "FALSE"
                        else:
                            s += "TRUE"
                    else:
                        s = s.rstrip(",")
                    s += "\n"
                    f.write(s)

if __name__ == "__main__":
    if len(sys.argv) > 1:
        if sys.argv[1] == "--train":
            outputTrainingData()
        else:
            raise Exception(sys.argv[1] + " is not a valid argument.")
    else:
        outputPredictingData()
