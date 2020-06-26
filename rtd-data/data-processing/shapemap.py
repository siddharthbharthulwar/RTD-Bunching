import matplotlib.pyplot as plt 
import pandas as pd 
import cv2 as cv
import numpy as np


df = pd.read_csv(r"rtd-data\res\shapes.txt")

a = df.groupby('shape_id')[['shape_pt_lat', 'shape_pt_lon', 'shape_pt_sequence', 'shape_dist_traveled']].apply(lambda g: g.values.tolist()).to_dict()
img = cv.imread('rtd-data\data-processing\map.png', cv.IMREAD_COLOR)

de = pd.read_csv(r"rtd-data\res\stops.txt")

dp = pd.read_csv(r"rtd-data\res\trips.txt")

ffshapeids = []

for index, row in dp.iterrows():


    if (row['route_id'] in ['FF1', 'FF2', 'FF3', 'FF4', 'FF5', 'FF6', 'FF7']):

        print(row['route_id'])
        ffshapeids.append(row['shape_id'])

print(ffshapeids)

stoplats = []
stoplons = []
colors = []

for index, row in de.iterrows():

    stoplats.append(row['stop_lat'])
    stoplons.append(row['stop_lon'])

for i in range(0, len(stoplats)):

    colors.append(255)

colors = np.array(colors)

for key in a:

    if key in ffshapeids:

        b = a[key]

        lats = []
        lons = []

        for ls in b:

            lats.append(ls[0])
            lons.append(ls[1])

        plt.imshow(img[:,:,::-1], extent = [-105.2888, -104.6613, 39.5401, 40.0476])
        plt.plot(lons, lats)
        plt.scatter(stoplons, stoplats, c = colors , alpha = 0.5)
        plt.show()