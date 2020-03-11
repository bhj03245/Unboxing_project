import RPi.GPIO as gp
import os
import sys
from threading import Thread
from datetime import datetime

gp.setwarnings(False)
gp.setmode(gp.BOARD)


gp.setup(7, gp.OUT)
gp.setup(11, gp.OUT)
gp.setup(12, gp.OUT)

def main():
	
	#gp.output(7, False)
	#gp.output(11, False)
	#gp.output(12, True)
	#capture(1)
	#i2c = "i2cset -y 1 0x70 0x00 0x04"	# cam A signal
	i2c = "i2cset -y 1 0x70 0x00 0x06"      # cam C signal   
	os.system(i2c)
	gp.output(7, False)
	gp.output(11, True)
	gp.output(12, False)
	
	#t1 = Thread(target = recording, args=(3,))	
	#t2 = Thread(target = capture, args=(1,))
	#t1.start()
	#t2.start()
	#capture(3)
	capture(3)

def recording(cam):
	
	mtime = datetime.today().strftime("%Y%m%d%H%M%S")
	cmd = "raspivid -w 1280 -h 720 -n -t 5000 -o /home/pi/Desktop/UB_video/Normal/norm_%s" % mtime
	  
	#cmd = "raspivid -w 640 -h 480 -fps 25 -t 0 -b 4500000 -o - | gst-launch-1.0 -v fdsrc !  h264parse ! rtph264pay config-interval=1 pt=96 ! udpsink host=127.0.0.1 port=8554"
	
	os.system(cmd)


def capture(self): # video capture and convert
	#cmd = "raspivid -w 1280 -h 720 -t 0"
	mtime = datetime.today().strftime("%Y%m%d%H%M%S")
	cmd = "raspivid -t 20000 -h 720 -w 1280 -fps 25 -vf -b 2000000 -o norm_%s.h264" % mtime 
	conv_cmd = "MP4Box -fps 25 -add norm_%s.h264 norm_%s.mp4; rm norm_%s.h264" % (mtime, mtime, mtime)

	os.system(cmd)	
	os.system(conv_cmd)

if __name__ == "__main__":
	main()

	gp.output(7, False)
	gp.output(11, True)
	gp.output(12, False)
