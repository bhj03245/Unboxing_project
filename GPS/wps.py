import geocoder

#g = geocoder.ip('ip')
g = geocoder.ip('me')
print(geocoder.ipinfo('me'))
print(g.latlng)
print(g.city)
print(g.state)

print(g.lat)
print(g.lng)