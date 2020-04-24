import geocoder

g = geocoder.ip('125.142.56.46')
g = geocoder.ip('me')
print(g.latlng)
print(g.city)
print(g.state)

print(g.lat)
print(g.lng)