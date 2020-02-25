import RPi.GPIO as gp
import os
import sys
import threading
import time
from threading import Thread
from datetime import datetime


gp.setwarnings(False)
gp.setmode(gp.BOARD)

gp.setup(7, gp.OUT)
gp.setup(11, gp.OUT)
gp.setup(12, gp.OUT)

norm_path = "/home/pi/Desktop/UB_video/Normal/"
park_path = "/home/pi/Desktop/UB_video/Parking/"
manl_path = "/home/pi/Desktop/UB_video/Manual"
impt_path = "/home/pi/Desktop/UB_video/Impact"

def main():
    # gp.output(7, False)
    # gp.output(11, False)
    # gp.output(12, True)
    # capture(1)
    # i2c = "i2cset -y 1 0x70 0x00 0x04"	# cam A signal
    i2c = "i2cset -y 1 0x70 0x00 0x06"  # cam C signal
    os.system(i2c)
    gp.output(7, False)
    gp.output(11, True)
    gp.output(12, False)

    record = recording()

    normal_thread = Thread(target=record.normal_recording())
    park_thread = Thread(target=record.parking_recording())
    manual_thread = Thread(target=record.manual_recording())
    impact_thread = Thread(target=record.impact_recording())

#    normal_thread.start()
#    park_thread.start()


class recording:

    def normal_recording(self):  # video capture and convert
        # cmd = "raspivid -w 1280 -h 720 -t 0"
        mtime = datetime.today().strftime("%Y%m%d%H%M%S")
        raw_nfile = "NORM_" + mtime + ".h264"
        nconv_file = "NORM_" + mtime + ".mp4"
        cmd = "raspivid -t 60000 -h 720 -w 1080 -fps 25 -vf -b 2000000 -o " + norm_path + raw_nfile
        conv_cmd = "MP4Box -fps 25 -add " + norm_path + raw_nfile + " " + norm_path + nconv_file + "; rm " + norm_path + raw_nfile

        os.system(cmd)
        os.system(conv_cmd)
        threading.Timer(0.1, self.normal_recording).start()

    def parking_recording(self):
        mtime = datetime.today().strftime("%Y%m%d%H%M%S")
        raw_pfile = "PARK_" + mtime + ".h264"
        pconv_file = "PARK_" + mtime + ".mp4"
        cmd = "raspivid -t 60000 -h 720 -w 1080 -fps 25 -vf -b 2000000 -o " + park_path + raw_pfile
        conv_cmd = "MP4Box -fps 25 -add " + park_path + raw_pfile + " " + park_path + pconv_file + "; rm " + park_path + raw_pfile

        os.system(cmd)
        os.system(conv_cmd)
        threading.Timer(0.1, self.parking_recording()).start()

    def manual_recording(self):
        mtime = datetime.today().strftime("%Y%m%d%H%M%S")
        raw_mfile = "MANL_" + mtime + ".h264"
        mconv_file = "MANL_" + mtime + ".mp4"
        cmd = "raspivid -t 20000 -h 720 -w 1080 -fps 25 -vf -b 2000000 -o " + manl_path + raw_mfile
        conv_cmd = "MP4Box -fps 25 -add " + manl_path + raw_mfile + " " + manl_path + mconv_file + "; rm " + manl_path + raw_mfile

        os.system(cmd)
        os.system(conv_cmd)
        threading.Timer(0.1, self.manual_recording()).start()

    def impact_recording(self):
        mtime = datetime.today().strftime("%Y%m%d%H%M%S")
        raw_ifile = "IMPT_" + mtime + ".h264"
        iconv_file = "IMPT_" + mtime + ".mp4"
        cmd = "raspivid -t 20000 -h 720 -w 1080 -fps 25 -vf -b 2000000 -o " + impt_path + raw_ifile
        conv_cmd = "MP4Box -fps 25 -add " + impt_path + raw_ifile + " " + impt_path + iconv_file + "; rm " + impt_path + raw_ifile

        os.system(cmd)
        os.system(conv_cmd)
        threading.Timer(0.1, self.impact_recording()).start()

if __name__ == "__main__":
    main()

    gp.output(7, False)
    gp.output(11, True)
    gp.output(12, False)