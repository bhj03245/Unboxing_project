import cv2
import glob
import os

pvideo_path = '/var/www/html/Upload/UB_video/Normal'
files_path = os.path.join(pvideo_path, '*.mp4')
files = sorted(glob.iglob(files_path), key=os.path.getctime, reverse=True)
print files[0]

cap = cv2.VideoCapture(files[0])
rev, image = cap.read()

cnt=0
rev = True
img_path = '/var/www/html/Upload/Parkimg'

while cnt == 0:
	cv2.imwrite(img_path + '/capture_%d.jpg' % cnt, image)
	rev, image = cap.read() 
	print("save parking image")
	cnt += 1

