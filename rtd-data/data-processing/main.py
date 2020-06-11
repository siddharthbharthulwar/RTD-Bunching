import pandas as pd 
import numpy as np 
import matplotlib.pyplot as plt
from formulas import haversine, midpoint
import itertools

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


def checkcsv(csvpath):

    df = pd.read_csv(csvpath)
    a = df.groupby('DateTime')[['TripID', 'Latitude', 'Longitude', 'RouteID']].apply(lambda g: g.values.tolist()).to_dict()

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


            plt.scatter(lons, lats, s = 110)
            for c in buses[routekey]:
                
                plt.scatter(c.longitude, c.latitude)

            plt.title(str(routekey))
            plt.show()

            #print(checklist(buses[routekey], 1000))

        buses = None

checkcsv("rtd-data/data/06-10-20.csv")



        
