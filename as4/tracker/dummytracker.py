"""
Author: Oscar Ramirez
Modified camshift tracker from opencv sample and tracker_camshift_planar_robot.py
from Camilo P.
"""
import json
import socket

class DummyCamShiftTracker:

    def __init__(self, camera=0, height=640, width=480):

        self.target_x = int(height / 2)
        self.target_y = int(width / 2)
        self.tracker_center_x = 0
        self.tracker_center_y = 0
        self.drag_start = None      # Set to (x,y) when mouse starts drag
        self.track_window = None    # Set to rect when the mouse drag finishes
        self.selection = None
        self.hue = None
        self.quit = False
        self.backproject_mode = False
        self.message = {}

        self.waypoints = list()

        self.count = 0

        print("Keys:\n"
            "    ESC - quit the program\n"
            "    b - switch to/from backprojection view\n"
            "    t - add waypoint\n"
            "    c - clear waypoints\n"
            "To initialize tracking, drag across the object with the mouse\n")

    def run(self):
        HOST, PORT = 'localhost', 5000
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((HOST, PORT))
        while not self.quit:
            self.get_waypoints()
            print self.waypoints
            self.update_message((
                                (0,0),(0,0),0
                                ))
            sock.send(json.dumps(self.message) + "\n")


    def update_message(self, track_box):
        self.message['x'] = float(self.tracker_center_x)
        self.message['y'] = float(self.tracker_center_y)
        self.message['a'] = float(track_box[1][0]) * float(track_box[1][1])
        self.message['theta'] = float(track_box[2])
        self.message['targetx'] = float(self.target_x)
        self.message['targety'] = float(self.target_y)
        self.message['num_waypoints'] = int(len(self.waypoints))
        self.message['waypoints'] = [[float(w[0]), float(w[1])] for w in self.waypoints]

    def get_waypoints(self):
        s = raw_input()
        w = tuple(map(int, s.strip().split(',')))
        self.waypoints.append(w)


if __name__ == "__main__":
    tracker = DummyCamShiftTracker()
    tracker.run()
