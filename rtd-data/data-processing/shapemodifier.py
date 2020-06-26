import pandas as pd
import csv

df = pd.read_csv('rtd-data/res/trips.txt')
dg = pd.read_csv('rtd-data/res/shapes.txt')
a = df.groupby('shape_id')[['route_id', 'direction_id', 'trip_id']].apply(lambda g: g.values.tolist()).to_dict()

