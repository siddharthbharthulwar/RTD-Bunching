import pandas as pd 
import numpy as np 
import matplotlib.pyplot as plt
from formulas import haversine, midpoint
import itertools
import numpy.ma as ma

class BusPosition:

    def __init__(self, latitude, longitude, routeid):
        
        self.latitude = latitude
        self.longitude = longitude
        self.routeid = routeid

def checklist(busList, threshold):

    bunching_incidences = []

    for a, b in itertools.combinations(busList, 2):

        result = haversine(a.latitude, a.longitude, b.latitude, b.longitude)

        if (result < threshold):

            bunching_incidences.append(midpoint(a.latitude, a.longitude, b.latitude, b.longitude))\

    return bunching_incidences

def generateheatmap(xdata, ydata, bins, maskedBool):

    heatmap, xedges, yedges = np.histogram2d(xdata, ydata, bins = bins)
    extent = [xedges[0], xedges[-1], yedges[0], yedges[-1]]

    if (maskedBool):

        masked = ma.masked_values(heatmap.T, 0)

        plt.imshow(masked, extent = extent, origin = 'lower')
        plt.show()
    else:

        plt.imshow(heatmap.T, extent = extent, origin = 'lower')
        plt.show()



def checkcsv(csvpath):

    df = pd.read_csv(csvpath)
    a = df.groupby('DateTime')[['TripID', 'Latitude', 'Longitude', 'RouteID']].apply(lambda g: g.values.tolist()).to_dict()

    globlats = []
    globlons = []

    for key in a:

        buses = {}

        for i in a[key]:

            buses[i[3]] = []

        for bus in a[key]:

            latitude = bus[1]
            longitude = bus[2]
            routeid = bus[3]

            buses[routeid].append(BusPosition(latitude, longitude, routeid))

        for routekey in buses:
            
            ret = checklist(buses[routekey], 1000)
            lats = []
            lons = []

            for b in ret:

                lats.append(b[0])
                lons.append(b[1])
                globlats.append(b[0])
                globlons.append(b[1])

        buses = None

    generateheatmap(globlons, globlats, 75, False)

checkcsv("rtd-data/data/06-10-20.csv")



        
