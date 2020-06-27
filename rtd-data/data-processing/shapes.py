import matplotlib.pyplot as plt 
import pandas as pd 
import cv2 as cv
import numpy as np

class Point:

    def __init__(self, latitude, longitude, index):
        
        self.latitude = latitude
        self.longitude = longitude
        self.index = index

class Shape:

    def __init__(self, shapeid):
        
        self.shapeid = shapeid
        self.points = []
        self.routeID = "nullValue"

    def addRouteID(self, routeID):

        self.routeID = routeID

    def get_y(self):

        ls = []

        for pt in self.points:

            ls.append(pt.latitude)

        return ls

    def get_x(self):

        ls = []

        for pt in self.points:

            ls.append(pt.longitude)

        return ls

    def plot(self):

        img = cv.imread('rtd-data\data-processing\map.png', cv.IMREAD_COLOR)
        plt.imshow(img[:,:,::-1], extent = [-105.2888, -104.6613, 39.5401, 40.0476])
        plt.plot(self.get_x(), self.get_y())

        if not self.routeID == "nullValue":

            plt.title(str(self.routeID))

        plt.show()

class ShapeReader:

    def __init__(self):
        
        df = pd.read_csv(r"rtd-data\res\shapes.txt")

        a = df.groupby('shape_id')[['shape_pt_lat', 'shape_pt_lon', 'shape_pt_sequence', 'shape_dist_traveled']].apply(lambda g: g.values.tolist()).to_dict()

        self.shapes = {}

        for key in a:

            b = a[key]
            shapePoints = []
            sh = Shape(key)

            for it in b:

                pt = Point(it[0], it[1], it[2])
                shapePoints.append(pt)

            sh.points = shapePoints
            self.shapes[str(key)] = sh






