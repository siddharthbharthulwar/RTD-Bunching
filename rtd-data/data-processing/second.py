import pandas as pd 
import matplotlib.pyplot as plt 

df = pd.read_csv("rtd-data/data/06-05-20.csv")
#df.sort(['TripID'])

lats = []
longs = []

for index, row in df.iterrows():

    lats.append(row['Latitude'])
    longs.append(row['Longitude'])

plt.scatter(longs, lats)
plt.show()
    