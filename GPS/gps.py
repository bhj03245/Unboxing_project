import serial
import RPi.GPIO as GPIO      
import pymysql
from decimal import *
 
delay = 1
 
GPIO.setmode(GPIO.BOARD)    

def find(str, ch):
    for i, ltr in enumerate(str):
        if ltr == ch:
            yield i
 
port = serial.Serial("/dev/ttyUSB0", baudrate=9600, timeout=1)

conn = pymysql.connect(host='localhost', user='pi', password='myub', db='ub_project', charset='utf8')
curs = conn.cursor()


ck=0
fd=''
while ck <= 50:
    rcv = port.read(10)
 
    fd=fd+rcv
    ck=ck+1
 
#print fd
if '$GPRMC' in fd:
    ps=fd.find('$GPRMC')
    dif=len(fd)-ps
        #print dif
    if dif > 50:
        data=fd[ps:(ps+50)]
        #print(data)
        p=list(find(data, ","))
        lat=data[(p[2]+1):p[3]]
        lng=data[(p[4]+1):p[5]]
 
        lat1=lat[2:len(lat)]
        lat1=Decimal(lat1)
        lat1=lat1/60
        lat2=int(lat[0:2])
        lat1=lat2+lat1
 
        lng1=lng[3:len(lng)]
        lng1=Decimal(lng1)
        lng1=lng1/60
        lng2=int(lng[0:3])
        lng1=lng2+lng1
 
        #print(lat1)
        #print(lng1)

try:
    sql = 'UPDATE location SET location_lat=%s, location_lng=%s, location_url=%s WHERE location_num=%s'
    curs.execute(sql, (lat1, lng1, "http://172.30.1.17/Upload/Parkimg/capture_0.jpg", 1))
    conn.commit()
finally:
    conn.close()