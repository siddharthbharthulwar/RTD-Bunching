import pandas as pd
import matplotlib.pyplot as plt
from shapes import Shape, ShapeReader
from formulas import haversine
import cv2 as cv
import numpy as np
import seaborn as sns

def compare_timestamps(timestamp1, timestamp2):

    time1 = int("1" + timestamp1)
    time2 = int("1" + timestamp2)

    if (time2 > time1):

        return True

    else:

        return False

def parseInitTimeStamp(timestamp):

    timestamp = str(int(timestamp))

    if (len(timestamp) == 1):

        return "000" + timestamp

    elif (len(timestamp) == 2):

        return "00" + timestamp

    elif (len(timestamp) == 3):

        return "0" + timestamp

    else:

        return timestamp

def getTimestamp(timestamp):

    thresholdList = ["0000", "0030", "0100", "0130", "0200", "0230", "0300", "0330", "0400", "0430", "0500", "0530", "0600", "0630", "0700", "0730", "0800",
    "0830", "0900", "0930", "1000", "1030", "1100", "1130", "1200", "1230", "1300", "1330", "1400", "1430", "1500", "1530", "1600", "1630", "1700", "1730", "1800",
    "1830", "1900", "1930", "2000", "2030", "2100", "2130", "2200", "2230", "2300", "2330"]

    labelList = ["12:00 AM", "12:30 AM", "1:00 AM", "1:30 AM", "2:00 AM", "2:30 AM", "3:00 AM", "3:30 AM", "4:00 AM", "4:30 AM", "5:00 AM", "5:30 AM", "6:00 AM",
    "6:30 AM", "7:00 AM", "7:30 AM", "8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "1:00 PM",
    "1:30 PM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM",
    "9:00 PM","9:30 PM", "10:00 PM", "10:30 PM", "11:00 PM", "11:30 PM"]


    for i in range(0, len(thresholdList)):

        if (compare_timestamps(timestamp, thresholdList[i])):

            threshold = thresholdList[i]
    
    return labelList[thresholdList.index(threshold)]

    
     
class BunchingInstance():

    def __init__(self, timestamp, latitude, Longitude):
        
        self.timestamp = timestamp
        self.latitude = latitude
        self.longitude = Longitude

    def asString(self):

        st = str(self.timestamp) + " @ (" + str(self.latitude) + ", " + str(self.longitude) + ")"
        return st

class Route:

    def __init__(self, routekey, shape):
        
        self.routeid = routekey
        self.shape = shape
        self.bunching_instances = []

    def calculate_percentage_down_route(self, bunching_instance):

        index_one, index_two = self.locate_two_closest_points(bunching_instance)  

    def locate_two_closest_points(self, bunchinginstance):

        main_lat = bunchinginstance.latitude
        main_lon = bunchinginstance.longitude

        shapes_duplicated = self.shape
        mindist = haversine(main_lat, main_lon, shapes_duplicated[0][0], shapes_duplicated[0][1])
        mindex = 0

        for i in range(0, len(shapes_duplicated)):

            dist = haversine(main_lat, main_lon, shapes_duplicated[i][0], shapes_duplicated[i][1])

            if dist < mindist:

                mindist = dist
                mindex = i
        
        pt_one = shapes_duplicated[mindex]
        shapes_duplicated.pop(mindex)
        index_one = mindex
        mindex = 0
        mindist = haversine(main_lat, main_lon, shapes_duplicated[0][0], shapes_duplicated[0][1])

        for i in range(0, len(shapes_duplicated)):

            dist = haversine(main_lat, main_lon, shapes_duplicated[i][0], shapes_duplicated[i][1])

            if dist < mindist:

                mindist = dist
                mindex = i
        
        pt_two = shapes_duplicated[mindex]

        return index_one, mindex

    def initialize_table(self):

        self.table = pd.DataFrame(columns = ["12:00 AM", "12:30 AM", "1:00 AM", "1:30 AM", "2:00 AM", "2:30 AM", "3:00 AM", "3:30 AM", "4:00 AM", "4:30 AM", "5:00 AM", "5:30 AM", "6:00 AM",
    "6:30 AM", "7:00 AM", "7:30 AM", "8:00 AM", "8:30 AM", "9:00 AM", "9:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM", "1:00 PM",
    "1:30 PM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM", "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM", "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM",
    "9:00 PM","9:30 PM", "10:00 PM", "10:30 PM", "11:00 PM", "11:30 PM"])
            
        ls = np.zeros(48).tolist()
        a_series = pd.Series(ls, index = self.table.columns)
        self.table = self.table.append(a_series, ignore_index=True)

    def add_to_table(self, bunchinginstance):
        
        ts = parseInitTimeStamp(bunchinginstance.timestamp)

        self.table.iloc[0, self.table.columns.get_loc(getTimestamp(ts))] +=1

    def show_table(self):

        plt.pcolor(self.table, cmap = 'Blues')
        plt.xlim((0, 24))
        plt.ylim((0, 1))
        plt.title(self.routeid)
        plt.show()


class BunchingReader:


    
    def __init__(self, datestr):

        dd = pd.read_csv("rtd-data/data-processing/data/realShapes.csv")
        self.shapes = dd.groupby('Route_ID')[['Latitude', 'Longitude']].apply(lambda g: g.values.tolist()).to_dict()

        self.datestr = datestr

        path = "rtd-data/data-processing/data/" + datestr + ".csv"
        
        self.df = pd.read_csv(path)
        temp = self.df.groupby('RouteName')[['Timestamp', 'Latitude', 'Longitude']].apply(lambda g: g.values.tolist()).to_dict()
       
        self.routes = {}

        for item in temp:
            
            if (item != 'MALL'):

                r = Route(item, self.shapes[item])
                self.routes[item] = r

        for item in temp:

            if (item != 'MALL'):

                b = temp[item]

                for i in b:
                    
                    bi = BunchingInstance(i[0], i[1], i[2])
                    self.routes[item].bunching_instances.append(bi)

        number_of_instances = 0

        for item in self.routes:

            rt = self.routes[item]
            rt.initialize_table()
            
            routeX = []
            routeY = []

            img = cv.imread('rtd-data\data-processing\map.png', cv.IMREAD_COLOR)
            plt.imshow(img[:,:,::-1], extent = [-105.2888, -104.6613, 39.5401, 40.0476])

            for bi in rt.bunching_instances:

                rt.add_to_table(bi)
            
            rt.show_table()
        
b = BunchingReader("07-01-20")