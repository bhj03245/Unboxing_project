#!/usr/bin/python
# -*- coding: utf-8 -*-
import cv2
import time
import RPi.GPIO as gp
import datetime
import threading
import os
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
park_path = os.getcwd() + '/UB_video/Parking/'


fourcc = cv2.VideoWriter_fourcc('X','2','6','4')


<<<<<<< HEAD
fourcc = cv2.VideoWriter_fourcc('X','2','6','4')


=======
>>>>>>> Video
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
	
	def normal_recording(self):	
		__file_name = create_file()
		path = norm_path + "NORM_" + __file_name
		norm_out = cv2.VideoWriter(path, fourcc, 25.0, (640,480))
		return path, norm_out		
<<<<<<< HEAD
=======

	def parking_recording(self):
		__file_name = create_file()
		path = park_path + "PARK_" + __file_name
		park_out = cv2.VideoWriter(path, fourcc, 25.0, (640,480))
		return path, park_out

	def manual_recording(self):
		__file_name = create_file()
		path = manl_path + "MANL_" + __file_name
		manl_out = cv2.VideoWriter(path, fourcc, 25.0, (640,480))
		return path, manl_out
>>>>>>> Video

	
	def recording(self): 	
		picam = cv2.VideoCapture(0)
		if picam.isOpened() == False:
			print('Can\'t open the CAM C')
			exit()

		cv2.namedWindow('CAM_Window')
		cnt = 0
		prevTime = 0	

<<<<<<< HEAD
		path = self.normal_recording()[0]
		out = self.normal_recording()[1]	
=======
		#path = self.normal_recording()[0]
		#out = self.normal_recording()[1]

		#path = self.parking_recording()[0]
		#out = self.parking_recording()[1]
		
		path = self.manual_recording()[0]
		out = self.manual_recording()[1]
			
>>>>>>> Video
		while(cnt < 5000):
			ret, frame = picam.read()
			curTime = time.time()
			sec = curTime - prevTime
			prevTime = curTime
			fps = 1/(sec)		
			cnt = cnt + fps
			#str = "FPS : %0.1f" % fps
			#cv2.putText(frame, str, (0,30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0))
			#print(cnt)	
			out.write(frame)
			cv2.imshow('CAM_Window', frame)
			#out.write(frame) 
	
			if cv2.waitKey(10) >= 0:
				break

		print(path.split('/')[6])
		picam.release()
		out.release()
<<<<<<< HEAD
		convert(norm_path, path.split('/')[6])
=======
		#convert(park_path, path.split('/')[6])		
		#convert(norm_path, path.split('/')[6])
		convert(manl_path, path.split('/')[6])		
>>>>>>> Video
		nthread = threading.Thread(target=self.recording, args=())
		nthread.start()
		
		#cv2.destroyWindow('CAM_Window')

r = recording()
r.recording()
