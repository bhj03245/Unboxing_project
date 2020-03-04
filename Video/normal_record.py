#!/usr/bin/python

import cv2
import os
import time
import datetime
import threading
import RPi.GPIO as gp
from picamera import PiCamera
from picamera.array import PiRGBArray

gp.setwarnings(False)
gp.setmode(gp.BOARD)

gp.setup(7, gp.OUT)
gp.setup(11, gp.OUT)
gp.setup(12, gp.OUT)

i2c = "i2cset -y 1 0x70 0x00 0x04"
os.system(i2c)
gp.output(7, False)
gp.output(11, False)
gp.output(12, True)

path = os.getcwd() + '/UB_video/Normal/'

picam = PiCamera()
picam.resolution = (640,480)
picam.framerate = 32
rawCapture = PiRGBArray(picam, size=(640,480))

time.sleep(0.1)

def create_time():
    now = datetime.datetime.today().strftime("%Y%m%d_%H%M%S")
    return now

def create_file():
    now = create_time() 
    file_name = now + '.h264'
    return file_name

def normal(picam):
    file_name = create_file()
    picam.start_recording(path + file_name)
    picam.wait_recording(10)
    picam.stop_recording()
    nthread = threading.Thread(target=normal, args=(picam,))
    nthread.start()

def recording():
    now = create_time()
    file_name = create_file()
    picam.start_preview()
    normal(picam)
    
    key = cv2.waitKey(1) & 0xFF
        #rawCapture.truncate(0)
        # if key == ord("q"):     

recording()


#nthread = threading.Thread(target=normal, args=(picam,))
#nthread.start()
#normal(picam)
#picam.stop_recording()
#picam.stop_preview()
#picam.close()


