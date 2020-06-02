#import video_ver3
import os
import glob
import threading
import cv2
import datetime
import smbus
import sysv_ipc

norm_path = os.getcwd() + '/UB_video/Normal/'
impt_path = os.getcwd() + '/UB_video/Impact/'

power_mgmt_1 = 0x6b
bus = smbus.SMBus(1)
address = 0x68
bus.write_byte_data(address, power_mgmt_1, 0)

check = False
memory = sysv_ipc.SharedMemory(1219) 

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

def detect_impact():
    gyro_xout = read_word_2c(0x43)
    gyro_yout = read_word_2c(0x45)
    gyro_zout = read_word_2c(0x47)

    x_scale = gyro_xout / 131.0
    y_scale = gyro_yout / 131.0
    z_scale = gyro_zout / 131.0
		
    #print(y_scale)
    if (2.5 < y_scale):
    	return True

def receive():
	print("Receiving!!")
	return True

def send():
	print("Sending!!")
	return True

def impact():
	t = create_time()
	print("Time : %s" % t)
	while True:
		fin = memory.read()
		if fin == "True":
			print("memory: %s" % fin)	
			video_mixing(t)
			break

def video_mixing(t):
	time = t[11:13]
	frame = 30
	file_path = glob.glob("%s*.mp4" % (norm_path))
	files = []
	files.append(sorted(file_path, key=os.path.getctime, reverse=True))

	impt_time = int(time) * int(frame)
	startFrame = impt_time - frame * 10
	CurrentFrame = 0
	endFrame = impt_time + (frame * 10)
	print(impt_time)
	print(startFrame)
	print(endFrame)
	fourcc = cv2.VideoWriter_fourcc(*'mp4v')
	new_path = impt_path + 'IMPT_' + t + '.mp4'
	
	impt_out = cv2.VideoWriter(new_path, fourcc, 30.0, (640, 480))

	for i in range(0, 1):
		if i == 1:
			if os.path.isfile(files[0][i]) == False: 
				break
		print(files[0][i])
		cap = cv2.VideoCapture(files[0][i])
		amount_of_frames = cap.get(cv2.CAP_PROP_FRAME_COUNT)
		cap.set(cv2.CAP_PROP_POS_FRAMES, startFrame)

		while True:	
			ret, frame = cap.read()
			if (CurrentFrame > (endFrame - startFrame)):
				break
			CurrentFrame += 1

			impt_out.write(frame)
			# cv2.imshow('CAM_Window', frame)
			cv2.waitKey(1)

		impt_out.release()
		print(amount_of_frames)
	print("Success!!")

if __name__=="__main__":
	while True:
		check = detect_impact()
		#print("Detecting...")
		if check == True:
			impact()
		

