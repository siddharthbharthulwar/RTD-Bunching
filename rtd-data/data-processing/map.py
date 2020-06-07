import pandas as pd 
import numpy as np
import matplotlib.pyplot as plt 

df = pd.read_csv("rtd-data/data/06-06-20.csv")

gf = df.sort_values(['Epoch', 'TripID'])


hist = df.hist(column='Epoch', bins = 500)
plt.show()