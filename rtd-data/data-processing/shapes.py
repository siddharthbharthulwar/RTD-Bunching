import matplotlib.pyplot as plt 
import pandas as pd 
import cv2 as cv

df = pd.read_csv(r"rtd-data\res\shapes.txt")

a = df.groupby('shape_id')[['shape_pt_lat', 'shape_pt_lon', 'shape_pt_sequence', 'shape_dist_traveled']].apply(lambda g: g.values.tolist()).to_dict()
img = cv.imread('rtd-data\data-processing\map.png', cv.IMREAD_COLOR)


for key in a:

    b = a[key]

    lats = []
    lons = []

    for ls in b:

        lats.append(ls[0])
        lons.append(ls[1])

    plt.imshow(img[:,:,::-1], extent = [-105.2888, -104.6613, 39.5401, 40.0476])
    plt.plot(lons, lats)
    plt.show()