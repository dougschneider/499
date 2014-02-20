Running part 1:

The images in this part must have a certain naming convention. Any image
that is not an ALP, must begin with either "n" or "N". Any ALP cannot begin
with an "n" or "N".

1. Place the training images in a folder called training-data
2. run python p1preproc.py --train
3. Place the images to be classified in a folder called predicting-data
4. run python p1preproc.py
5. run python p1preproc.py --class
6. run the java project P1BuildModel
7. run the java project P1Classify, the classifications are output
8. the output from 7 can be compared to the output from 
   5 (classes.out) for correctness
