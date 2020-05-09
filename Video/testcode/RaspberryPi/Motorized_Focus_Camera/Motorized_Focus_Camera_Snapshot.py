
import os
import time
import sys
import threading
import pygame,sys
import picamera
from pygame.locals import *
from time import ctime, sleep
pygame.init()
screen=pygame.display.set_mode((320,240),0,32)
pygame.key.set_repeat(100)
camera = picamera.PiCamera()
def runFocus():
 temp_val = 512
 while True:
   for event in pygame.event.get():
	if event.type ==KEYDOWN:
           print temp_val
	   if event.key == K_UP:
              print 'UP'
	      if temp_val < 1000:
		     temp_val += 10
	      else:
		     temp_val = temp_val
              value = (temp_val<<4) & 0x3ff0
              dat1 = (value>>8)&0x3f
	      dat2 = value & 0xf0
              
              os.system("i2cset -y 0 0x0c %d %d" % (dat1,dat2))
	      camera.capture('Image%d.jpg' %(time.time()))
	      print "Image save OK"
           elif event.key==K_DOWN:
	      print 'DOWN'
	      if temp_val <12 :
                     temp_val = temp_val
              else:
                     temp_val -= 10
              value = (temp_val<<4) & 0x3ff0
              dat1 = (value>>8)&0x3f
              dat2 = value & 0xf0
              os.system("i2cset -y 0 0x0c %d %d" % (dat1,dat2))
	      camera.capture('Image%d.jpg' %(time.time()))
              print "Image save OK"
if __name__ == "__main__":
	

 runFocus()
