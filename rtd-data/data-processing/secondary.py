import pandas as pd
import matplotlib.pyplot as plt

class BunchingReader:

    def __init__(self, datestr):

        self.datestr = datestr

        path = "rtd-data/data-processing/data/" + datestr + ".csv"
        
        self.df = pd.read_csv(path)

    

b = BunchingReader("06-19-20")