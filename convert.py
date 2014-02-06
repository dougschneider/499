import csv

with open("log.csv", "r") as f:
    csvreader = csv.reader(f)
    with open("processed_log.csv", "w+") as f2:
        f2.write("intensity,class\n")
        for row in csvreader:
            tmp = ""
            tmp += row[3]
            tmp += ","
            if int(row[2]) - int(row[1]) > 0:
                tmp += "TRUE"
            else:
                tmp += "FALSE"
            tmp += "\n"
            f2.write(tmp)
