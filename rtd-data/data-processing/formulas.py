import math

def haversine(latitude1, longitude1, latitude2, longitude2):
    radius = 20924640
    lat1 = float(latitude1)
    lon1 = float(longitude1)
    lat2 = float(latitude2)
    lon2 = float(longitude2)
    lon1, lat1, lon2, lat2 = map(math.radians, [lon1, lat1, lon2, lat2])
    dlon = lon2 - lon1
    dlat = lat2 - lat1
    a = math.sin(dlat/2)**2 + math.cos(lat1) * math.cos(lat2) * math.sin(dlon/2)**2
    c = 2 * math.atan2(math.sqrt(a), math.sqrt(1-a))
    distance = radius * c

    return distance

def midpoint(latitude1, longitude1, latitude2, longitude2):

    midlat = (latitude1 + latitude2) / 2
    midlon = (longitude1 + longitude2) / 2

    return (midlat, midlon)
