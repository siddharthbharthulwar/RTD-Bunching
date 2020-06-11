import pandas as pd 
import numpy as np 
import matplotlib.pyplot as plt 

df = pd.read_csv("rtd-data/data/06-10-20.csv")

a = df.groupby('DateTime')[['TripID', 'Latitude', 'Longitude', 'RouteID']].apply(lambda g: g.values.tolist()).to_dict()

for key in a:

    lats = []
    lons = []
    routes = []

    for bus in a[key]:

        lats.append(bus[1])
        lons.append(bus[2])
        routes.append(bus[3])

    plt.scatter(lons, lats)

    title = str(key) + " w/ " + str(len(lons)) + " buses"
    plt.title(title)
    plt.show()
