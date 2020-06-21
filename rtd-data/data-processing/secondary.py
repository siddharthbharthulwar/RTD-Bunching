import pandas as pd
import matplotlib.pyplot as plt

class BunchingReader:

    def __init__(self):
        
        df = pd.read_csv("rtd-data\data-processing\data\06-19-20.csv")

        print(df)

b = BunchingReader()