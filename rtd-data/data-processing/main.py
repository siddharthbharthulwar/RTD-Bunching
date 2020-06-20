import pandas as pd 
import numpy as np 
import matplotlib.pyplot as plt
from formulas import haversine, midpoint
import itertools
import numpy.ma as ma
import math
import cv2 as cv
import csv

class Route:

    def __init__(self, routekey):

        name = routekey[:-1]
        direction = routekey[-1]

        self.name = name
        self.direction = direction

        self.bunchinginstances = []

    def addinstance(self, instance):

        self.bunchinginstances.append(instance)


class BusPosition:

    def __init__(self, latitude, longitude, routeid, directionid):
        
        self.latitude = latitude
        self.longitude = longitude
        self.routeid = routeid
        self.directionid = directionid

class BunchingInstance:

    def __init__(self, routekey, latitude, longitude, timestamp):

        routename = routekey[:-1]
        direction = routekey[-1]

        self.name = routename
        self.direction = direction
        self.latitude = latitude
        self.longitude = longitude
        self.timestamp = timestamp

class DataProcessor:

    def __init__(self, csv):
        
        self.csv = csv
        self.checkcsv()
        self.saveBunchingInstances()
        #self.checkcsv()
    
    def checklist(self, busList, threshold, routekey, timestamp):

        bunching_incidences = []

        for a, b in itertools.combinations(busList, 2):

            result = haversine(a.latitude, a.longitude, b.latitude, b.longitude)

            if (result < threshold):

                location = midpoint(a.latitude, a.longitude, b.latitude, b.longitude)

                bi = BunchingInstance(routekey, location[0], location[1], timestamp)
                bunching_incidences.append(bi)

        return bunching_incidences

    def generateheatmap(self, xdata, ydata, bins, maskedBool):


        img = cv.imread('rtd-data\data-processing\map.png', cv.IMREAD_COLOR)

        heatmap, xedges, yedges = np.histogram2d(xdata, ydata, bins = bins)
        extent = [xedges[0], xedges[-1], yedges[0], yedges[-1]]

        if (maskedBool):

            masked = ma.masked_values(heatmap.T, 0)

            plt.imshow(img[:,:,::-1], extent = [-105.2888, -104.6613, 39.5401, 40.0476])
            plt.imshow(masked, extent = extent, origin = 'lower')
            plt.show()
        else:

            plt.imshow(heatmap.T, extent = extent, origin = 'lower')
            plt.show()

    def saveBunchingInstances(self):

        dateStr = self.csv[14: 22]
        pathStr = "rtd-data/data-processing/data/" + dateStr + ".csv"

        with open(pathStr, 'w') as csvfile:

            filewriter = csv.writer(csvfile, delimiter = ',', quotechar = '|', quoting = csv.QUOTE_MINIMAL)
            filewriter.writerow(['Timestamp', 'RouteName', 'Direction', 'Latitude', 'Longitude'])
            index = 0
            for bi in self.bunchingInstances:

                ls = [str(bi.timestamp), str(bi.name), str(bi.direction), str(bi.latitude), str(bi.longitude)]
                filewriter.writerow(ls)
                print(index)
                index+=1




    def checkcsv(self):

        df = pd.read_csv(self.csv)
        a = df.groupby('DateTime')[['TripID', 'Latitude', 'Longitude', 'RouteID', 'DirectionID']].apply(lambda g: g.values.tolist()).to_dict()

        self.bunchingInstances = []
        self.globlats = []
        self.globlons = []

        for key in a:
            
            buses = {}

            print(key)

            for i in a[key]:

                if not (math.isnan(i[0])):

                    keydir = i[4]
                    keyroute = i[3]

                    keystr = str(keyroute) + "" + str(int(keydir))
                    buses[keystr] = []

            for bus in a[key]:

                if not (math.isnan(i[0])):
                    latitude = bus[1]
                    longitude = bus[2]
                    routeid = bus[3]
                    directionid = bus[4]

                    keystr = str(routeid) + "" +  str(int(directionid))

                    buses[keystr].append(BusPosition(latitude, longitude, routeid, directionid))

            for routekey in buses:

                if not (math.isnan(i[0])):
                
                    ret = self.checklist(buses[routekey], 1000, routekey, key)
                    lats = []
                    lons = []

                    for b in ret:

                        lats.append(b.latitude)
                        lons.append(b.longitude)
                        self.globlats.append(b.latitude)
                        self.globlons.append(b.longitude)
                        bi = BunchingInstance(routekey, b.latitude, b.longitude, key)
                        self.bunchingInstances.append(bi)

            buses = None

        self.generateheatmap(self.globlons, self.globlats, 200, True)


d = DataProcessor("rtd-data/data/06-10-20.csv")
