import pandas as pd
import matplotlib.pyplot as plt
from shapes import Shape, ShapeReader
from formulas import haversine
import cv2 as cv

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

        mindex = 0
        mindist = haversine(main_lat, main_lon, shapes_duplicated[0][0], shapes_duplicated[0][1])

        for i in range(0, len(shapes_duplicated)):

            dist = haversine(main_lat, main_lon, shapes_duplicated[i][0], shapes_duplicated[i][1])

            if dist < mindist:

                mindist = dist
                mindex = i
        
        pt_two = shapes_duplicated[mindex]

        return pt_one, pt_two


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

            routeX = []
            routeY = []

            img = cv.imread('rtd-data\data-processing\map.png', cv.IMREAD_COLOR)
            plt.imshow(img[:,:,::-1], extent = [-105.2888, -104.6613, 39.5401, 40.0476])

            for bi in rt.bunching_instances:



                routeX.append(bi.longitude)
                routeY.append(bi.latitude)

            plt.scatter(routeX, routeY)
            plt.title(str(item))
            plt.show()
                
            '''
            img = cv.imread('rtd-data\data-processing\map.png', cv.IMREAD_COLOR)
            plt.imshow(img[:,:,::-1], extent = [-105.2888, -104.6613, 39.5401, 40.0476])

            la, lb = rt.locate_two_closest_points(bi)

            totalX = []
            totalY = []

            for ab in rt.shape:

                totalX.append(ab[1])
                totalY.append(ab[0])

            plt.scatter(totalX, totalY)

            plt.scatter(bi.longitude, bi.latitude)
            plt.scatter(la[1], la[0])
            plt.scatter(lb[1], lb[0])
            titleStr = bi.asString() + " route " + item
            plt.title(titleStr)
            plt.show()

            '''
        
    


    

b = BunchingReader("06-19-20")
rt = b.routes['FF1']

la, lb = rt.locate_two_closest_points(rt.bunching_instances[1])