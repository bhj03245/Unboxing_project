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
impt_memory = sysv_ipc.SharedMemory(1218, flags=01000, size=4, mode=0600)
fin_memory = sysv_ipc.SharedMemory(1217, flags=01000, size=2, mode=0600)

class ListQueue:
    def __init__(self):
        self.my_list = list()

    def put(self, element):
        self.my_list.append(element)
        self.my_list = sorted(set(self.my_list), reverse=True)

    def get(self):
        return self.my_list.pop(0)

    def qsize(self):
        return len(self.my_list)

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

def impact():
    t = create_time()
    impt_memory.write("IMPT")
    print("Time : %s" % t)
    while True:
        sec = fin_memory.read()
        print(sec)
        nt = t[:11] + sec
        flag = impt_memory.read()   
        if flag == 'FLAG':
            print("Memory : %s" % flag)
            video_mixing(nt)
            impt_memory.write('    ')
            break
      #  elif 0 <= int(fin) <= 60:
       #     print("memory: %s" % fin)  
        #    video_mixing(t)
         #   break


def video_mixing(t):
    print("Start")
    time = int(t[11:13])
    frame = 30
    file_path = glob.glob("%s*.mp4" % (norm_path))
    fourcc = cv2.VideoWriter_fourcc(*'mp4v')
    new_path = impt_path + 'IMPT_' + t + '.mp4'
    impt_out = cv2.VideoWriter(new_path, fourcc, 30.0, (640, 480))
    files = []
    files.append(sorted(file_path, key=os.path.getctime, reverse=True))

    if time < 10:
        print("############ First ##########")
        imptFrame = time * 30
        start_time = (60 + time) - 10
        end_time = time + 10
        endFrame = end_time * frame
       # print("Impt time : %d" % time)
       # print("Start time : %d" % start_time)
       # print("End time : %d" % end_time)
       # print("End Frame : %d" % endFrame)
        startFrame = int(start_time) * int(frame)
        CurrentFrame = 0
        CurrentSecond = 0

        print("startFrame : %d" % startFrame)

        for i in range(0, 2):
            if i == 2:
                if not os.path.isfile(files[0][i]):
                    break
            print(files[0][i])

            cap = cv2.VideoCapture(files[0][1])
            amount_of_frames = cap.get(cv2.CAP_PROP_FRAME_COUNT)
            cap.set(cv2.CAP_PROP_POS_FRAMES, startFrame)
            middleFrame = amount_of_frames
            while True:
                ret, frame = cap.read()
                if (CurrentFrame > (middleFrame - startFrame)):
                    cap = cv2.VideoCapture(files[0][0])
                    cap.set(cv2.CAP_PROP_POS_FRAMES, imptFrame)

                    while True:
                        ret, frame = cap.read()
                        if (CurrentSecond > (endFrame - 0)):
                            break
                        CurrentSecond += 1
                        impt_out.write(frame)
                    break

                CurrentFrame += 1

                impt_out.write(frame)
                cv2.waitKey(1)

            impt_out.release()
            print(amount_of_frames)
        print("Success!!")
        
    elif 10 <= time <= 50:   
        print("############ Second ##########")
        impt_time = time * frame
        startFrame = impt_time - frame * 10
        CurrentFrame = 0
        endFrame = impt_time + (frame * 10)
        print(impt_time)
        print(startFrame)
        print(endFrame)

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
    
    elif time > 50:
        print("############ Third ##########")
        time_queue = queue.Queue()
        imptFrame = time * 30
        start_time = time - 10
        end_time = time - 50
        startFrame = start_time * frame
        endFrame = end_time * frame

        CurrentFrame = 0
        CurrentSecond = 0

        print("startFrame : %d" % startFrame)

        for i in range(0, 2):
            if i == 2:
                if not os.path.isfile(files[0][i]):
                    break
            print(files[0][i])

            cap = cv2.VideoCapture(files[0][1])
            amount_of_frames = cap.get(cv2.CAP_PROP_FRAME_COUNT)
            cap.set(cv2.CAP_PROP_POS_FRAMES, startFrame)
            middleFrame = amount_of_frames
            while True:
                ret, frame = cap.read()
                if (CurrentFrame > (middleFrame - startFrame)):
                    cap = cv2.VideoCapture(files[0][0])
                    cap.set(cv2.CAP_PROP_POS_FRAMES, startFrame)

                    while True:
                        ret, frame = cap.read()
                        if (CurrentSecond > (endFrame - 0)):
                            break
                        CurrentSecond += 1
                        impt_out.write(frame)
                    break

                CurrentFrame += 1

                impt_out.write(frame)
                cv2.waitKey(1)

            impt_out.release()
            print(amount_of_frames)
        print("Success!!")

if __name__=="__main__":
    if impt_memory.read() != '' or fin_memory.read() != '':
        impt_memory.write('    ')
        fin_memory.write('  ')
	while True:
		check = detect_impact()
		if check == True:
			impact()
		else:
			continue


