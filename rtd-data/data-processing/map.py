import pandas as pd 
import numpy as np
import matplotlib.pyplot as plt 

df = pd.read_csv("rtd-data/data/06-08-20.csv")

gf = df.sort_values(['DateTime', 'TripID'])

uniquelist = list(set(gf['DateTime'].tolist()))


index = 0
for un in uniquelist:

    ls = gf.loc[gf['DateTime'] == un]

    lats = []
    lons = []
    
    for index, row in ls.iterrows():

        lats.append(row['Latitude'])
        lons.append(row['Longitude'])
    
    fig = plt.figure()
    ax = plt.subplot(111)
    ax.scatter(lons, lats)
    plt.title(str(un))

    filename = "rtd-data/data-processing/images/" + str(index) + ".png"
    fig.savefig(filename, dpi = 96)
    print(index)
    index +=1
'''
hist = df.hist(column='DateTime', bins = 500)
plt.show()
'''