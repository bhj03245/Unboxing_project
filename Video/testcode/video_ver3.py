#!/usr/bin/python
# -*- coding: utf-8 -*-
import cv2
import time
import RPi.GPIO as gp
import datetime
import threading
import os
import glob
import smbus
import math
import subprocess

from picamera import PiCamera
from picamera.array import PiRGBArray

gp.setwarnings(False)
gp.setmode(gp.BOARD)

gp.setup(7, gp.OUT)
gp.setup(11, gp.OUT)
gp.setup(12, gp.OUT)

gp.output(7, False)
gp.output(11, False)
gp.output(12, True)

norm_path = os.getcwd() + '/UB_video/Normal/'
manl_path = os.getcwd() + '/UB_video/Manual/'
park_path = os.getcwd() + '/UB_video/Parking/'
impt_path = os.getcwd() + '/UB_video/Impact/'

fourcc = cv2.VideoWriter_fourcc(*'X264')

def create_time():
    now = datetime.datetime.today().strftime("%y%m%d_%H%M%S")
    return now

def create_file():
    now = create_time()
    file_name = now + '.h264'
    return file_name


def convert(path, file_name):
    dest_file = path.replace('h264','mp4')
    convert_cmd = 'MP4Box -fps 30 -add ' + path + " " + dest_file + "; rm " + path 
    os.system(convert_cmd)
    return dest_file

def streaming():
	cmd = 'sh gst-server.sh'
	subprocess.call(cmd, shell=True)

class recording:

    def normal_recording(self):
        __file_name = create_file()
        path = norm_path + "NORM_" + __file_name
        norm_out = cv2.VideoWriter(path, fourcc, 30.0, (640, 480))
        return path, norm_out

    def parking_recording(self):
        __file_name = create_file()
        path = park_path + "PARK_" + __file_name
        park_out = cv2.VideoWriter(path, fourcc, 30.0, (640, 480))
        return path, park_out

    def manual_recording(self):
        __file_name = create_file()
        path = manl_path + "MANL_" + __file_name
        manl_out = cv2.VideoWriter(path, fourcc, 30.0, (640, 480))
        return path, manl_out

    def impact_recording(self):
        __file_name = create_file()
        path = impt_path + "IMPT_" + __file_name
        impt_out = cv2.VideoWriter(path, fourcc, 30.0, (640, 480))
        return path, impt_out

    def show(self, frame):
        cv2.imshow('CAM_Window', frame)

    def recording(self, record):
        picam = cv2.VideoCapture(-1)
        if picam.isOpened() == False:
            print('Can\'t open the CAM C')
            exit()
	
        cv2.namedWindow('CAM_Window')
        prevTime = 0
   
        path = record[0]
        out = record[1]
        framecnt = 0

        fps = int(picam.get(cv2.CAP_PROP_FPS))

        while True:
            check = 0
            framecnt += 1
            ret, frame = picam.read()
            sec = framecnt / fps
            rr = (picam.get(cv2.CAP_PROP_POS_FRAMES))
            print("%d %d %d %d" % (fps, framecnt, rr, sec))

            out.write(frame)
            self.show(frame)
            #cv2.imshow('CAM_Window', frame)

            if sec == 60: 
                #picam.release()
                out.release()
                video = convert(path, path.split('/')[6])
                break

            if cv2.waitKey(33) >= 0:
                picam.release()
                video = convert(path, path.split('/')[6])  
                break

		
        nthread = threading.Thread(target=self.recording, args=(self.normal_recording(),))
        nthread.start()


        #cv2.destroyWindow('CAM_Window')


r = recording()
n = r.normal_recording()
#i = r.impact_recording()
#m = r.manual_recording()
#p = r.parking_recording()

r.recording(n)


