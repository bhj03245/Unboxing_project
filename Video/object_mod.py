#!/usr/bin/python
# -*- coding: utf-8 -*-
import cv2
import time
import RPi.GPIO as gp
import datetime
import threading
import os
import smbus
import math
import subprocess
from picamera import PiCamera
from picamera.array import PiRGBArray

power_mgmt_1 = 0x6b
power_mgmt_2 = 0x6c

bus = smbus.SMBus(1)
address = 0x68
bus.write_byte_data(address, power_mgmt_1, 0)

gp.setwarnings(False)
gp.setmode(gp.BOARD)

gp.setup(7, gp.OUT)
gp.setup(11, gp.OUT)
gp.setup(12, gp.OUT)
# i2c = "i2cset -y 1"
# i2c = "i2cset -y 1 0x70 0x00 0x04"
# os.system(i2c)
gp.output(7, False)
gp.output(11, False)
gp.output(12, True)

norm_path = os.getcwd() + '/UB_video/Normal/'
manl_path = os.getcwd() + '/UB_video/Manual/'
park_path = os.getcwd() + '/UB_video/Parking/'
impt_path = os.getcwd() + '/UB_video/Impact/'

fourcc = cv2.VideoWriter_fourcc(*'X264')


def read_byte(adr):
    return bus.read_byte_data(address, adr)


def read_word(adr):
    high = bus.read_byte_data(address, adr)
    low = bus.read_byte_data(address, adr + 1)
    val = (high << 8) + low
    return val


def read_word_2c(adr):
    val = read_word(adr)
    if (val >= 0x8000):
        return -((65535 - val) + 1)
    else:
        return val


def create_time():
    now = datetime.datetime.today().strftime("%y%m%d_%H%M%S")
    return now


def create_file():
    now = create_time()
    file_name = now + '.h264'
    return file_name


def convert(path, file_name):
    dest_file = path.replace('h264','mp4')
    convert_cmd = 'MP4Box -fps 25 -add ' + path + " " + dest_file + "; rm " + path 
    os.system(convert_cmd)
    return dest_file

def timelapse(in_file, out_file, impt_vw, startFrame, endFrame):
    iCurrentFrame = 0
    in_file = in_file
    out_file = out_file
    startFrame = startFrame
    endFrame = endFrame

    vc = cv2.VideoCapture(in_file)

    if vc.isOpened() == False:
        print("Fail to open the video")
        exit()
    vw = impt_vw
    print("In : %s" % in_file)
    print("Out : %s " % out_file)
    print("start Frame : %d" % startFrame)
    print("Video Frame Count : %d" % int(vc.get(cv2.CAP_PROP_FRAME_COUNT)))
    #if((startFrame < 0 or startFrame >= vc.get(cv2.CAP_PROP_FRAME_COUNT)) or
    #	(endFrame < 0 or endFrame >= vc.get(cv2.CAP_PROP_FRAME_COUNT))):

     #   print("Wrong Frame")
     #  exit()

    vc.set(cv2.CAP_PROP_POS_FRAMES, startFrame)
    while True:
        ret, frame = vc.read()
        if (iCurrentFrame > (endFrame - startFrame)):
            break
        iCurrentFrame += 1

        vw.write(frame)
        #cv2.imshow("CAM_Window", frame)
        if cv2.waitKey(1) & 0xFF == 27:
            break

    vc.release()
    vw.release()
    print("Finish")
    #cv2.destroyWindow('CAM_Window')
    convert(out_file, out_file.split('/')[6])

def streaming():
	cmd = 'sh gst-server.sh'
	subprocess.call(cmd, shell=True)

class recording:

    def normal_recording(self):
        __file_name = create_file()
        path = norm_path + "NORM_" + __file_name
        norm_out = cv2.VideoWriter(path, fourcc, 25.0, (640, 480))
        return path, norm_out

    def parking_recording(self):
        __file_name = create_file()
        path = park_path + "PARK_" + __file_name
        park_out = cv2.VideoWriter(path, fourcc, 25.0, (640, 480))
        return path, park_out

    def manual_recording(self):
        __file_name = create_file()
        path = manl_path + "MANL_" + __file_name
        manl_out = cv2.VideoWriter(path, fourcc, 25.0, (640, 480))
        return path, manl_out

    def impact_recording(self):
        __file_name = create_file()
        path = impt_path + "IMPT_" + __file_name
        impt_out = cv2.VideoWriter(path, fourcc, 25.0, (640, 480))
        return path, impt_out

    def gyro(self, video, sec):
        print("IMPACT!!")
        path = self.impact_recording()[0]

        impact_cmd = "ffmpeg -i %s -ss %s -t 20 -vcodec copy -acodec copy %s" % (video, sec, path)
        os.system(impact_cmd)

    # out = self.impact_recording()[1]
    # return path, out
    # break
    def detect_impact(self, check):
        gyro_xout = read_word_2c(0x43)
        gyro_yout = read_word_2c(0x45)
        gyro_zout = read_word_2c(0x47)

        x_scale = gyro_xout / 131.0
        y_scale = gyro_yout / 131.0
        z_scale = gyro_zout / 131.0
		
        #print(y_scale)
        if (1.5 < y_scale):
            check = 1
        return check

    def recording(self, record):
        picam = cv2.VideoCapture(-1)
        if picam.isOpened() == False:
            print('Can\'t open the CAM C')
            exit()
	
        cv2.namedWindow('CAM_Window')
        cnt = 0
        prevTime = 0
   
        path = record[0]
        out = record[1]
        video_time=0
        frame_time = 0
        h = 0

        sthread = threading.Thread(target=streaming)

        while (video_time < 21):
            check = 0
            frame_time += 1

            ret, frame = picam.read()
            curTime = time.time()
            sec = curTime - prevTime
            prevTime = curTime
            fps = int(picam.get(cv2.CAP_PROP_FPS))
            #fps = 1 / (sec)
            cnt += fps          
            #str = "FPS : %0.1f" % fps
            
            # cv2.putText(frame, str, (0, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (0, 255, 0))
            out.write(frame)
            cv2.imshow('CAM_Window', frame)
            check = self.detect_impact(check)
            video_time = frame_time/fps
            rr = (picam.get(cv2.CAP_PROP_POS_FRAMES))
            # print(fr)
            print("%s %s %d" % (video_time, cnt, h-49))                # 1 sec : cnt 900
 
            if check == 1:
              # picam.release()
              # out.release()
               video = convert(path, path.split('/')[6])
               vc = cv2.VideoCapture(video)
               fr = int(vc.get(cv2.CAP_PROP_FRAME_COUNT))
               out_path = self.impact_recording()[0] 
               impt_vw = self.impact_recording()[1]
               tlthread = threading.Thread(target=timelapse, args=(video, out_path, impt_vw, fr-250, fr))
               tlthread.start()
               tlthread.join()
               #timelapse(video, out_path, fr - 250, fr + 250)
               break
            h += 1

            if cv2.waitKey(27) >= 0:
                picam.release()
                sthread.start()
                sthread.join()
				
                break

        print(path.split('/')[6])
        picam.release()
        out.release()
        video = convert(path, path.split('/')[6])
		
        nthread = threading.Thread(target=self.recording, args=(self.normal_recording(),))
        nthread.start()
       # nthread.join()
      
    # cv2.destroyWindow('CAM_Window')


r = recording()
n = r.normal_recording()
#i = r.impact_recording()
#m = r.manual_recording()
#p = r.parking_recording()

r.recording(n)



