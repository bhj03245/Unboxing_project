import cv2
import datetime
import threading
import os

# Access video object creation
cap = cv2.VideoCapture(0)

width = int(cap.get(cv2.CAP_PROP_FRAME_WIDTH))
height = int(cap.get(cv2.CAP_PROP_FRAME_HEIGHT))
record = False

length = 10 # period : 10 seconds 
os.makedirs(os.getcwd() + '\\video\\')
path = os.getcwd() + '\\video\\'
fourcc = cv2.VideoWriter_fourcc(*'XVID')  # making digital media format code

def normal(video, frame, time_gap):
    video.write(frame)
    if time_gap == length:
        video.release()

def create_time():
    now = datetime.datetime.today().strftime("%Y%m%d_%H%M%S")
    return now

def create_file():
    now = create_time()
    file_name = now + '.avi'
    return file_name

def recording():
    now = create_time()
    file_name = create_file()
    video = cv2.VideoWriter(path + file_name, fourcc, 25.0, (width, height))
    while True:
        # Bring in image from video
        ret, frame = cap.read()

        # Output image to screen
        cv2.imshow('frame', frame)
        key = cv2.waitKey(33)

        now_time = int(now[13:15])
        save_time = int(datetime.datetime.now().strftime("%Y%m%d_%H%M%S")[13:15])

        if now_time >= 50 and now_time > save_time:
            save_time = save_time + 60
            time_gap = abs(save_time - now_time)
            print(time_gap, now_time, save_time)
        else:
            time_gap = abs(save_time - now_time)
            print(time_gap, now_time, save_time)

        nthread = threading.Thread(target=normal, args=(video, frame, time_gap))
        nthread.start()

        if time_gap == length:
            print("Finish")
            recording()
            #break

recording()




