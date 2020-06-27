import pandas as pd
import csv
from shapes import Shape, ShapeReader
import cv2 as cv
import matplotlib.pyplot as plt
import csv
import itertools

df = pd.read_csv('rtd-data/res/trips.txt')
dg = pd.read_csv('rtd-data/res/shapes.txt')
a = df.groupby('route_id')[['route_id', 'shape_id', 'direction_id', 'trip_id']].apply(lambda g: g.values.tolist()).to_dict()

shapereader = ShapeReader()
img = cv.imread('rtd-data\data-processing\map.png', cv.IMREAD_COLOR)


realshapes = []

for item in a:

    if item != '0':

        b = a[item]

        currentshapes = []
        currentshapelens = []

        for thing in b:

            sh = shapereader.shapes[str(thing[1])]
            currentshapes.append(sh)
    
        for i in currentshapes:

            currentshapelens.append(len(i.points))

        maxlen = currentshapelens[0]
        maxindex = 0

        for i in range(0, len(currentshapelens)):

            if (currentshapelens[i] > maxlen):

                maxlen = currentshapelens[i]
                maxindex = i
        
        realshape = currentshapes[maxindex]
        realshape.addRouteID(item)
        realshapes.append(realshape)
        realshape.plot()

pathStr = "rtd-data/data-processing/data/" + "realShapes"  + ".csv"

with open(pathStr, 'w', newline = '') as csvfile:

    filewriter = csv.writer(csvfile, delimiter = ',', quotechar = '|', quoting = csv.QUOTE_MINIMAL)
    filewriter.writerow(['Shape_ID', 'Route_ID', 'Latitude', 'Longitude'])

    for shapeItem in realshapes:

        for pt in shapeItem.points:

            rowlistitem = [str(shapeItem.shapeid), str(shapeItem.routeID), str(pt.latitude), str(pt.longitude)]
            filewriter.writerow(rowlistitem)

