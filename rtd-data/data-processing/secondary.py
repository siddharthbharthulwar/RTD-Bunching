import pandas as pd
import matplotlib.pyplot as plt
from shapes import Shape, ShapeReader
from formulas import haversine
import cv2 as cv
import numpy as np
import seaborn as sns

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

        self.table = pd.DataFrame(columns = ['12 AM', '1 AM', '2 AM', '3 AM', '4 AM', '5 AM', '6 AM', '7 AM', '8 AM', '9 AM', '10 AM', '11 AM', '12 PM', '1 PM', '2 PM', '3 PM', '4 PM', '5 PM', '6 PM', '7 PM', '8 PM', '9 PM', '10 PM', '11 PM'])
            
        ls = np.zeros(24).tolist()
        a_series = pd.Series(ls, index = self.table.columns)
        self.table = self.table.append(a_series, ignore_index=True)

    def add_to_table(self, bunchinginstance):

        columnvals = ['12 AM', '1 AM', '2 AM', '3 AM', '4 AM', '5 AM', '6 AM', '7 AM', '8 AM', '9 AM', '10 AM', '11 AM', '12 PM', '1 PM', '2 PM', '3 PM', '4 PM', '5 PM', '6 PM', '7 PM', '8 PM', '9 PM', '10 PM', '11 PM']
        timestampvals = ['00', '01', '02', '03', '04', '05', '06', '07', '08', '09', '10', '11', '12' '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24'] 

        if (len(str(bunchinginstance.timestamp)) == 2):

            column_index = '12 AM' 
        else:

            column_index = columnvals[timestampvals.index(str(bunchinginstance.timestamp)[:2])]
        self.table.iloc[0, self.table.columns.get_loc(column_index)] +=1

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
            
            r = Route(item, self.shapes[item])
            self.routes[item] = r

        for item in temp:

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
        
b = BunchingReader("06-19-20")