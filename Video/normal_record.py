#!/usr/bin/python
#-*- coding: utf-8 -*-
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

norm_path = os.getcwd() + '/UB_video/Normal/'
manl_path = os.getcwd() + '/UB_video/Manual/'

picam = PiCamera()
picam.resolution = (640,480)
picam.framerate = 32
rawCapture = PiRGBArray(picam, size=(640,480))

time.sleep(0.1)

def create_time():
    now = datetime.datetime.today().strftime("%y%m%d_%H%M%S")
    return now

def create_file():
    now = create_time() 
    file_name = now + '.h264'
    return file_name

def convert(path, file_name):
    conv_file = file_name.split('.')[0] + '.mp4'
    convert_cmd = 'MP4Box -fps 25 -add ' + path + file_name + " " + path + conv_file + "; rm " + path + file_name
    os.system(convert_cmd)

class recording:
	def normal_recording(self, picam):
		key = int(input("Interrupt : "))
		if key == 1:
			print("Manual ON")
			self.manual_recording(picam)
		
		__file_name = create_file()
		file_name = "NORM_" + __file_name		
		picam.start_recording(norm_path + file_name)
		picam.wait_recording(10)
		picam.stop_recording()
		convert(norm_path, file_name)
		nthread = threading.Thread(target=self.normal_recording, args=(picam,))
		nthread.start()
		print("Finish")
        
	
	def manual_recording(self, picam):
		__file_name = create_file()
		file_name = "MANL_" + __file_name
		picam.start_recording(manl_path + file_name)
		picam.wait_recording(10)
		picam.stop_recording()
		convert(manl_path, file_name)
		nthread = threading.Thread(target=self.manual_recording, args=(picam,))
		nthread.start()
		print("Manual Finish")
  	

	def recording(self):
		frame = picam.start_preview(fullscreen=False, window=(100, 20, 640, 480))
		self.normal_recording(picam)

	    

r = recording()
r.recording()


#nthread = threading.Thread(target=normal, args=(picam,))
#nthread.start()
#normal(picam)
#picam.stop_recording()
#picam.stop_preview()
#picam.close()


