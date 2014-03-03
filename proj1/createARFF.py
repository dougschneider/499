import sys

if len(sys.argv) < 3:
    raise Exception("must provide -n argument");
numFiles = int(sys.argv[2])

test = False
if len(sys.argv) > 3:
    test = bool(sys.argv[3] == '--test')

imgs = dict()

def handleCSVs(classID, className):
    for i in range(1, numFiles+1):
        with open("%s-%d.csv" % (classID, i), "r") as f:
            first = True
            for line in f:
                if first:
                    first = False
                    continue
                attrs = line.rstrip("\n").split(", ")
                if i == 1:
                    imgs[attrs[1]] = ""
                for j in range(2, len(attrs)):
                    imgs[attrs[1]] += attrs[j] + ","
                if i == numFiles:
                    if test:
                        imgs[attrs[1]] += "?\n"
                    else:
                        imgs[attrs[1]] += ("%s\n" % className)


preamble = "@RELATION plate\n\n"
with open("out.arff", "w+") as out:

    handleCSVs("4", "FOUR")
    handleCSVs("5", "FIVE")
    handleCSVs("o", "OTHER")

    attrs = imgs[imgs.keys()[0]].split(",")
    for i in range(len(attrs)-1):
        preamble += "@ATTRIBUTE " + str(i) + " NUMERIC\n"
    preamble += "@ATTRIBUTE class {FOUR, FIVE, OTHER}\n"
    preamble += "\n@DATA\n"
    out.write(preamble)

    for key, val in imgs.items():
        out.write(val)
