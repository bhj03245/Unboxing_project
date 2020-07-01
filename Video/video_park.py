#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys
import cv2
import sys
import time
import RPi.GPIO as gp
import datetime
import threading
import os
import glob
import smbus
import math
import subprocess
import sysv_ipc
import pymysql
from picamera import PiCamera
from picamera.array import PiRGBArray
from park import park_db

gp.setwarnings(False)
gp.setmode(gp.BOARD)

gp.setup(7, gp.OUT)
gp.setup(11, gp.OUT)
gp.setup(12, gp.OUT)

gp.output(7, False)
gp.output(11, False)
gp.output(12, True)

norm_path = '/var/www/html/Upload/UB_video/Normal/'
park_path = '/var/www/html/Upload/UB_video/Parking/'
impt_path = '/var/www/html/Upload/UB_video/Impact/'

fourcc = cv2.VideoWriter_fourcc(*'X264')

#mode_memory = sysv_ipc.SharedMemory(1220)
chk_memory = sysv_ipc.SharedMemory(1219)
impt_memory = sysv_ipc.SharedMemory(1218)
fin_memory = sysv_ipc.SharedMemory(1217) 
mid_memory = sysv_ipc.SharedMemory(1220)

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

    def impact_recording(self):
        __file_name = create_file()
        path = impt_path + "IMPT_" + __file_name
        impt_out = cv2.VideoWriter(path, fourcc, 30.0, (640, 480))
        return path, impt_out

    def show(self, frame):
        cv2.imshow('CAM_Window', frame)

    def user_mode(self):
        mydb = pymysql.connect(
            host='localhost',
            user='pi',
            passwd='myub',
            database='ub_project'
        )
        mycursor = mydb.cursor()

        return mycursor

    def recording(self, record, sec_sum, mode1):
        picam = cv2.VideoCapture(-1)
        if picam.isOpened() == False:
            print('Can\'t open the CAM')
            exit()
	
        cv2.namedWindow('CAM_Window')
        prevTime = 0
        sec_sum = sec_sum
        path = record[0]
        out = record[1]
        framecnt = 0
        fps = int(picam.get(cv2.CAP_PROP_FPS))

        while True:   
            mycursor = self.user_mode()
            sql = "select users_mode from users"
            mycursor.execute(sql)
            mode = mycursor.fetchall()[0][0]

            if mode == 'NORM':
                print(mode)
                #sys.exit()

            # impact : sec > 50 processing 
            if sec_sum == 120:					
                print(sec_sum)
                flag = impt_memory.read()
                chk_memory.write("CHEK")
                impt_memory.write("FLG2")
                sec_sum = 0
                continue

			# Read video
            framecnt += 1
            ret, frame = picam.read()
            sec = framecnt / fps
            rr = (picam.get(cv2.CAP_PROP_POS_FRAMES))
            chk = impt_memory.read()
            print(chk)
            
			# impact : sec < 10 
            if chk == 'IMPT':					
                fin = fin_memory.read()
                fin_memory.write(str('%02d' % sec))
                impt_memory.write('    ')

            print("%d %d %d %d %d" % (fps, framecnt, rr, sec, sec_sum))

			# video display
            matrix = cv2.getRotationMatrix2D((640 / 2, 480 / 2), 270, 1)
            dst = cv2.warpAffine(frame, matrix, (640, 480))
            out.write(dst)
            self.show(dst)
         
			# video saving
            if sec == 60:    
               #picam.release()
                sec_sum += sec
                out.release()
                video = convert(path, path.split('/')[6])
                mid_memory.write("FLAG")
                break 

				# impact : 10 <= sec <= 50 processing
                if 0 <= int(fin_memory.read()) <= 50:
                    flag = impt_memory.read()
                    mid_memory.write("FLAG")
                    break

			# key interrupt : video saving
            if cv2.waitKey(33) >= 0:
                picam.release()
                video = convert(path, path.split('/')[6])  
                break


        if mode == 'PARK':
            record_type = self.parking_recording()
            pass

        elif mode == 'NORM':
            record_type = self.normal_recording()

        pthread = threading.Thread(target=self.recording,args=(self.parking_recording(), sec_sum, mode1))
        pthread.start()



def mode():
    mode = str(park_db())
    print(mode)
    return mode

if __name__=="__main__":
    r = recording()
    p = r.parking_recording()
    mode_park = 'PARK'
    r.recording(p, 0, mode_park)




            


