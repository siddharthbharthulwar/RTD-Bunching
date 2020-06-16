import pandas as pd 
import numpy as np 
import matplotlib.pyplot as plt
from formulas import haversine, midpoint
import itertools
import numpy.ma as ma
import math
from scipy.ndimage.filters import gaussian_filter
import cv2 as cv

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

def checklist(busList, threshold, routekey, timestamp):

    bunching_incidences = []

    for a, b in itertools.combinations(busList, 2):

        result = haversine(a.latitude, a.longitude, b.latitude, b.longitude)

        if (result < threshold):

            location = midpoint(a.latitude, a.longitude, b.latitude, b.longitude)

            bi = BunchingInstance(routekey, location[0], location[1], timestamp)
            bunching_incidences.append(bi)

    return bunching_incidences

def generateheatmap(xdata, ydata, bins, maskedBool, sigma, alpha):

    img = cv.imread('rtd-data\data-processing\map.png', cv.IMREAD_COLOR)

    heatmap, xedges, yedges = np.histogram2d(xdata, ydata, bins = bins)
    extent = [xedges[0], xedges[-1], yedges[0], yedges[-1]]

    blurred = gaussian_filter(heatmap.T, sigma = sigma)

    if (maskedBool):

        masked = ma.masked_less(blurred, 15)

        plt.imshow(img[:,:,::-1], extent = [-105.2888, -104.6613, 39.5401, 40.0476])
        plt.imshow(masked, extent = extent, origin = 'lower', alpha = 0.4)
        plt.show()
    else:

        plt.imshow(blurred, extent = extent, origin = 'lower')
        plt.show()

def checkcsv(csvpath):

    df = pd.read_csv(csvpath)
    a = df.groupby('DateTime')[['TripID', 'Latitude', 'Longitude', 'RouteID', 'DirectionID']].apply(lambda g: g.values.tolist()).to_dict()

    bunchingFrame = pd.DataFrame(columns =['Time', 'Latitude', 'Longitude', 'Route', 'DirectionID'])

    globlats = []
    globlons = []

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
            
                ret = checklist(buses[routekey], 1000, routekey, key)
                lats = []
                lons = []

                for b in ret:

                    lats.append(b.latitude)
                    lons.append(b.longitude)
                    globlats.append(b.latitude)
                    globlons.append(b.longitude)
                    
                    print(routekey)
                    '''
                    newRow = pd.DataFrame([key, b.latitude, b.longitude, routekey[:-1], routekey[-1]], columns = ['Time', 'Latitude', 'Longitude', 'Route', 'DirectionID'])
                    bunchingFrame.append(newRow)
                    '''

        buses = None


    bunchingFrame.to_csv('bunching.csv')
    generateheatmap(globlons, globlats, 200, True, 3, 0.5)


checkcsv("rtd-data/data/06-08-20.csv")



        
