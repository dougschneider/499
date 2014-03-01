import sys

test = False
if len(sys.argv) > 1:
    test = bool(sys.argv[1] == '--test')

preamble = "@RELATION plate\n\n"
first = True
with open("out.arff", "w+") as out:
    imgs = dict()
    with open("4-1.csv", "r") as f:
        for line in f:
            if first:
                first = False
                continue
            attrs = line.rstrip("\n").split(", ")
            imgs[attrs[1]] = ""
            for i in range(2, len(attrs)):
                imgs[attrs[1]] += attrs[i] + ","

    first = True
    with open("4-2.csv", "r") as f:
        for line in f:
            if first:
                first = False
                continue
            attrs = line.rstrip("\n").split(", ")
            for i in range(2, len(attrs)):
                imgs[attrs[1]] += attrs[i] + ","
            if test:
                imgs[attrs[1]] += "?\n"
            else:
                imgs[attrs[1]] += "FOUR\n"

    attrs = imgs[imgs.keys()[0]].split(",")
    for i in range(len(attrs)-1):
        preamble += "@ATTRIBUTE " + str(i) + " NUMERIC\n"
    preamble += "@ATTRIBUTE class {FOUR, FIVE, OTHER}\n"
    preamble += "\n@DATA\n"
    out.write(preamble)

    first = True
    with open("5-1.csv", "r") as f:
        for line in f:
            if first:
                first = False
                continue
            attrs = line.rstrip("\n").split(", ")
            imgs[attrs[1]] = ""
            for i in range(2, len(attrs)):
                imgs[attrs[1]] += attrs[i] + ","

    first = True
    with open("5-2.csv", "r") as f:
        for line in f:
            if first:
                first = False
                continue
            attrs = line.rstrip("\n").split(", ")
            for i in range(2, len(attrs)):
                imgs[attrs[1]] += attrs[i] + ","
            if test:
                imgs[attrs[1]] += "?\n"
            else:
                imgs[attrs[1]] += "FIVE\n"

    first = True
    with open("o-1.csv", "r") as f:
        for line in f:
            if first:
                first = False
                continue
            attrs = line.rstrip("\n").split(", ")
            imgs[attrs[1]] = ""
            for i in range(2, len(attrs)):
                imgs[attrs[1]] += attrs[i] + ","

    first = True
    with open("o-2.csv", "r") as f:
        for line in f:
            if first:
                first = False
                continue
            attrs = line.rstrip("\n").split(", ")
            for i in range(2, len(attrs)):
                imgs[attrs[1]] += attrs[i] + ","
            if test:
                imgs[attrs[1]] += "?\n"
            else:
                imgs[attrs[1]] += "OTHER\n"

    for key, val in imgs.items():
        out.write(val)
