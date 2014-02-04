import csv

with open("log.csv", "r") as f:
    csvreader = csv.reader(f)
    with open("processed_log.csv", "w+") as f2:
        for row in csvreader:
            tmp = ""
            tmp += str(int(row[2]) - int(row[1]))
            tmp += ","
            tmp += row[3]
            tmp += "\n"
            f2.write(tmp)
