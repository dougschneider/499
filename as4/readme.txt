The directory structure of our project is laid out in this manner:

AvoidObstacles: This directory contains the java project that runs part 3.

ControllersBase: This directory contains the java project responsible for
                 running PID controller. It is used both by LineFollow and
                 WaypointFollow.

LineFollow: This directory contains the java project for part 1. It uses each
            of the BangBang, P, PD, PI, and PID controllers to follow
            lines of tape.

tracker: This directory contains the code used to interact with the tracker
         camera, it is used by the WaypointFollow project.

WaypointFollow: This directory contains the java project responsible for part 2.
                 To run WaypointFollow, first run the WaypointFollow java code,
                 then the python tracker. Once the tracker is connected, select
                 the object to track, and then position the target circle using
                 right-click. Press 't' to place a waypoint, select and additional
                 waypoint, and press 't' again. The tracker will draw a line, and the
                 robot will follow it. Add additional waypoints by positioning
                 the target circle and pressing 't'.
