from scipy import ndimage
import os

first = True
with open("p1train.csv", "w+") as f:
    for _, _, filenames in os.walk("training-data"):
        for filename in filenames:
            if filename.endswith(".jpg"):
                s = ""
                title = ""
                num = 1
                for pixel in ndimage.imread("training-data/" + filename).flatten():
                    if first:
                        title += str(num) + ","
                        num += 1
                    s += str(pixel) + ","
                if title != "":
                    title += "class\n"
                    f.write(title)
                    title = ""
                    first = False
                last = filename.split("/")[-1]
                if last[0] == "n" or last[0] == "N":
                    s += "FALSE"
                else:
                    s += "TRUE"
                s += "\n"
                f.write(s)
