import pandas as pd 
import numpy as np 
import matplotlib.pyplot as plt 


def findunique(list1): 
      
    # insert the list to the set 
    list_set = set(list1) 
    # convert the set to the list 
    unique_list = (list(list_set)) 
    return unique_list

df = pd.read_csv("rtd-data/data/06-05-20.csv")
unique = findunique(df['TripID'].tolist())

ind = 0

for num in unique: 
    pr = str(ind) + " / " + str(len(unique))
    print(pr)
    lats = []
    lons = []

    for index, row in df.iterrows():

        if (row['TripID'] == num):

            lats.append(row['Latitude'])
            lons.append(row['Longitude'])

    plt.scatter(lats, lons)
    ind += 1

plt.show()

