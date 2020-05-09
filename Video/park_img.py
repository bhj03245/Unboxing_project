import cv2

cap = cv2.VideoCapture('/var/www/html/Upload/UB_video/Normal/NORM_200509_135048.mp4')
rev, image = cap.read()

cnt=0
rev = True
img_path = '/var/www/html/Upload/Parkimg'

while cnt == 0:
	cv2.imwrite(img_path + '/parking_%d.jpg' % cnt, image)
	rev, image = cap.read() 
	print("save parking image")
	cnt += 1

