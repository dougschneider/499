CMPUT 499 Assignment 3
======================

Aaron Krebs
Dough Schneider


Requirements
============

We have not included the required WEKA verions to save space.
WEKA 3.6.10 is required in ../weka-3-6-10
WEKA 3.7.10 is required in ../weka-3-7-10
To use other locations modify the .classpath files for each java project.


Part 1
======

To run part 1, run the PedestriansTrain and PedestriansRun java projects.
These will generate the data files and model, and then run according to the 
model. See the source code for switching between EM and K-Means methods.


Part 2
======

To collect data for part 2, run the NavigateTerrain project. After it starts,
start the python tracker and follow the instructions given by NavigateTerrain
as to when to select the colour to track for the robot and so on. This will
generate p2data.arff.

Generate the model using WEKA with the parameters provided in our report
(3x3 lattice, etc). Our model p2.model is provided.

Generate the images (using the generated model) by running the ProduceMap project.

The robot will follow the path coded by running the FollowRoad project.


Part 3
======

To train the model, run the TravelStraight project. This will produce the p3.agt
agent file. Our p3.agt agent is provided.
To run the model, run TravelStraight with the --test option.


Part 4
======

Using the data generated in parts 1 and 3, run TravelStraightPedestrians to
combine the behaviour of those parts.
