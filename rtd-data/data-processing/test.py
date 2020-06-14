import pandas as pd 
import matplotlib.pyplot as plt 
import matplotlib.animation as animation
import numpy as np

df = pd.read_csv("rtd-data/data/06-13-20.csv")

indices = []
epochs = []

for index, row in df.iterrows():

    indices.append(index)
    print(index)
    epochs.append(row['DateTime'])

plt.plot(indices, epochs)
plt.show()
