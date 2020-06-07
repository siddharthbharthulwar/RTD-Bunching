import pandas as pd 
import matplotlib.pyplot as plt 
import matplotlib.animation as animation
import numpy as np


'''

df = pd.read_csv("rtd-data/data/06-06-20.csv")

bg = df.groupby('Epoch').groups


ind = 0
for key in bg:
    print(ind)
    ind+=1
    lats = []
    longs = []
    for index in bg[key]:

        lats.append(df['Latitude'].tolist()[index])
        longs.append(df['Longitude'].tolist()[index])

    plt.scatter(longs, lats)

plt.show()

'''

class AnimatedScatter(object):
    """An animated scatter plot using matplotlib.animations.FuncAnimation."""

    def setup(self):

        df = pd.read_csv("rtd-data/data/06-05-20.csv")

        bg = df.groupby('Epoch').groups

        self.latlist = []
        self.lonlist = []


        ind = 0
        for key in bg:
            print(ind)
            ind+=1
            lats = []
            longs = []
            for index in bg[key]:

                lats.append(df['Latitude'].tolist()[index])
                longs.append(df['Longitude'].tolist()[index])
            
            self.latlist.append(lats)
            self.lonlist.append(longs)


    def __init__(self, numpoints=50):
        self.numpoints = numpoints
        # Setup the figure and axes...
        self.fig, self.ax = plt.subplots()
        # Then setup FuncAnimation.
        self.setup()
        self.ani = animation.FuncAnimation(self.fig, self.update, interval=50, 
                                          init_func=self.setup_plot, blit=True)

    def setup_plot(self):
        """Initial drawing of the scatter plot."""
        x, y = self.lonlist[0], self.latlist[0]
        self.scat = self.ax.scatter(x, y)
        self.ax.axis([-106, -104, 38, 41])
        # For FuncAnimation's sake, we need to return the artist we'll be using
        # Note that it expects a sequence of artists, thus the trailing comma.
        return self.scat,

    def update(self, i):
        """Update the scatter plot."""

        x = self.lonlist[i]
        y = self.latlist[i]

        self.scat = plt.scatter(x, y)

        return self.scat,


if __name__ == '__main__':
    a = AnimatedScatter()
    plt.show()
        

