# -*-coding:utf8;-*-

import os
from kivy.app import App
from kivy.lang import Builder
from kivy.uix.recycleview.views import RecycleDataViewBehavior
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.uix.switch import Switch
from kivy.core.text import LabelBase
from kivy.properties import BooleanProperty, ListProperty
from kivy.uix.behaviors import FocusBehavior
from kivy.uix.recycleview.layout import LayoutSelectionBehavior
from kivy.uix.recycleboxlayout import RecycleBoxLayout
from kivy.uix.button import Button
from kivy.uix.screenmanager import ScreenManager
from kivy.uix.video import Video
from kivy.network.urlrequest import UrlRequest
from kivy.uix.recycleview import RecycleView
from kivy.logger import Logger
import requests
import webbrowser
import pymysql
import glob
import threading
from functools import partial
from kivy.clock import Clock
from kivy.uix.image import Image
from kivy.graphics.texture import Texture
import datetime
import cv2
import RPi.GPIO as gp
import sysv_ipc
import sys

# Init GPIO 
gp.setwarnings(False)
gp.setmode(gp.BOARD)

gp.setup(7, gp.OUT)
gp.setup(11, gp.OUT)
gp.setup(12, gp.OUT)

gp.output(7, False)
gp.output(11, False)
gp.output(12, True)

# Font
LabelBase.register(name='malgun',
                   fn_regular='malgun.ttf')

# Path & Fourcc 
norm_path = '/var/www/html/Upload/UB_video/Normal/'
park_path = '/var/www/html/Upload/UB_video/Parking/'
impt_path = '/var/www/html/Upload/UB_video/Impact/'

fourcc = cv2.VideoWriter_fourcc(*'X264')
nlist = []

# Shared Memory 
chk_memory = sysv_ipc.SharedMemory(1219)
impt_memory = sysv_ipc.SharedMemory(1218)
fin_memory = sysv_ipc.SharedMemory(1217) 
mid_memory = sysv_ipc.SharedMemory(1220)

mode_bool = True
class Main(Screen):
    pass

class Menu(Screen):
    pass

    def drowsiness_switch(selfself, switchObject, switchValue):
        if (switchValue):
            print('Switch is On: ' + SelectableButton().get_source())
        else:
            print('Switch is OFF')


class Setting(Screen):
    pass

    def drowsiness_switch(selfself, switchObject, switchValue):
        if (switchValue):
            print('Switch is On:')
        else:
            print('Switch is OFF')


class Video_list(Screen):
    def run_UrlRequests(self, *args):
        # self.r = UrlRequest("https://www.google.com")
        # r = requests.get("https://www.google.com")
        url = "https://www.google.com"
        webbrowser.open_new(url)


class SelectableRecycleBoxLayout(FocusBehavior, LayoutSelectionBehavior,
                                 RecycleBoxLayout):
    ''' Adds selection and focus behaviour to the view. '''


class SelectableButton(RecycleDataViewBehavior, Button):
    source = ''
    ''' Add selection support to the Label '''
    index = None
    selected = BooleanProperty(False)
    selectable = BooleanProperty(True)

    def refresh_view_attrs(self, rv, index, data):
        ''' Catch and handle the view changes '''
        self.index = index
        return super(SelectableButton, self).refresh_view_attrs(
            rv, index, data)

    def on_touch_down(self, touch):
        ''' Add selection on touch down '''
        if super(SelectableButton, self).on_touch_down(touch):
            return True
        if self.collide_point(*touch.pos) and self.selectable:
            return self.parent.select_with_touch(self.index, touch)

    def apply_selection(self, rv, index, is_selected):
        ''' Respond to the selection of items in the view. '''
        self.selected = is_selected

    def on_release(self, *args):
        App.get_running_app().root.current = "video_widget"

    def on_press(self):
        data = self.text
        self.source = os.path.join(norm_path, data)
        data_index = self.index

    def get_source(self):
        return self.source


class Manual(Screen):
    data_items_manl = ListProperty([])

    def __init__(self, **kwargs):
        super(Manual, self).__init__(**kwargs)
        self.get_board()

    def get_board(self):
        mydb = pymysql.connect(
            host='localhost',
            user='pi',
            passwd='myub',
            database='ub_project',
        )

        mycursor = mydb.cursor()
        sql = "select manl_name from manl"
        mycursor.execute(sql)

        rows = mycursor.fetchall()
        root = 'localhost'
        for row in rows:
            for col in row:
                self.data_items_manl.append(col)


class Normal(Screen):
    data_items_norm = ListProperty([])
    url = ListProperty([])

    def __init__(self, **kwargs):
        super(Normal, self).__init__(**kwargs)
        self.get_board()

    def get_board(self):
        mydb = pymysql.connect(
            host='localhost',
            user='pi',
            passwd='myub',
            database='ub_project',
        )

        mycursor = mydb.cursor()
        sql = "select norm_name from norm"
        mycursor.execute(sql)

        rows = mycursor.fetchall()
        root = 'localhost'
        for row in rows:
            for col in row:
                nlist.append(self.data_items_norm.append(col))


class Impact(Screen):
    data_items_impt = ListProperty([])

    def __init__(self, **kwargs):
        super(Impact, self).__init__(**kwargs)
        self.get_board()

    def get_board(self):
        mydb = pymysql.connect(
            host='localhost',
            user='pi',
            passwd='myub',
            database='ub_project',
        )

        mycursor = mydb.cursor()
        sql = "select impt_name from impt"
        mycursor.execute(sql)

        rows = mycursor.fetchall()
        root = 'localhost'
        for row in rows:
            for col in row:
                self.data_items_impt.append(col)


class Parking(Screen):
    data_items_park = ListProperty([])

    def __init__(self, **kwargs):
        super(Parking, self).__init__(**kwargs)
        self.get_board()

    def get_board(self):
        mydb = pymysql.connect(
            host='localhost',
            user='pi',
            passwd='myub',
            database='ub_project',
        )

        mycursor = mydb.cursor()
        sql = "select park_name from park"
        mycursor.execute(sql)

        rows = mycursor.fetchall()
        root = 'localhost'
        for row in rows:
            for col in row:
                self.data_items_park.append(col)


class VideoWidget(Screen):
    # r_source = source
    # file = []

    # ff = SelectableButton().on_press()
    # file = SelectableButton.data
    # Logger.info('text: ')
    # for i in range(0, len(nlist)):
    #    file.append(nlist[i])
    # source = path + fie

    r_source = '/var/www/html/Upload/UB_video/Normal/NORM_200511_135802.mp4'
    # r_source = os.path.join(path, file)


# class VideoPlayerApp(App):
#   def build(self):
#      return VideoWidget()


class WindowManager(ScreenManager):
    pass


class MyApp(App):
    def mode(self):
        mycursor = self.user_mode()
        sql = "select users_mode from users"
        mycursor.execute(sql)
        mode = mycursor.fetchall()[0][0]
        return mode
        
    def build(self):
        record_type = self.normal_recording()
        sec_sum = 0
        mode1 = self.mode()
        threading.Thread(target=self.recording, args=(record_type, sec_sum, mode1, mode_bool), daemon=True).start()
        sm = ScreenManager()
        self.main = Main()
        self.menu = Menu()
        sm.add_widget(self.main)
        sm.add_widget(self.menu)
        return sm

    def create_time(self):
        now = datetime.datetime.today().strftime("%y%m%d_%H%M%S")
        return now

    def create_file(self):
        now = self.create_time()
        file_name = now + '.h264'
        return file_name

    def convert(self, path, file_name):
        dest_file = path.replace('h264', 'mp4')
        convert_cmd = 'MP4Box -fps 30 -add ' + path + " " + dest_file + "; rm " + path
        os.system(convert_cmd)
        return dest_file

    def normal_recording(self):
        __file_name = self.create_file()
        path = norm_path + "NORM_" + __file_name
        norm_out = cv2.VideoWriter(path, fourcc, 30.0, (640, 480))
        return path, norm_out

    def parking_recording(self):
        __file_name = self.create_file()
        path = park_path + "PARK_" + __file_name
        park_out = cv2.VideoWriter(path, fourcc, 30.0, (640, 480))
        return path, park_out
        
    def impact_recording(self):
        __file_name = create_file()
        path = impt_path + "IMPT_" + __file_name
        impt_out = cv2.VideoWriter(path, fourcc, 30.0, (640, 480))
        return path, impt_out
      
    def user_mode(self):
        mydb = pymysql.connect(
            host='localhost',
            user='pi',
            passwd='myub',
            database='ub_project'
        )
        mycursor = mydb.cursor()
  
        return mycursor
        
    def recording(self, record, sec_sum, mode1, mode_bool):
        # this code is run in a separate thread
        self.do_vid = True  # flag to stop loop

        cam = cv2.VideoCapture(-1)
        if cam.isOpened() == False:
            print('Can\'t open the CAM')
            exit()
            
        path = record[0]
        out = record[1]
        
        sec_sum = sec_sum
        framecnt = 0
        fps = int(cam.get(cv2.CAP_PROP_FPS))
        sec = 0
        
        # start processing loop
        while (self.do_vid):
            # Getting Mode
            mode = self.mode()
            
            # Parking Mode Change
            if mode == 'PARK' and mode_bool == True:
                print(mode)
                mode1 = mode
                mode_bool = False
                break
                
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
            ret, frame = cam.read()
            sec = framecnt / fps
            rr = (cam.get(cv2.CAP_PROP_POS_FRAMES))
            chk = impt_memory.read()
            print(chk)
            
            if chk == 'IMPT':
                fin = fin_memory.read()
                fin_memory.write(str('%02d' %sec))
                impt_memory.write('    ')
                
            print("%s %d %d %d %d" % (mode, rr, framecnt, fps, sec))
            
            matrix = cv2.getRotationMatrix2D((640 / 2, 480 / 2), 270, 1)
            dst = cv2.warpAffine(frame, matrix, (640, 480))
            Clock.schedule_once(partial(self.display_frame, dst))
            out.write(dst)
           
            # cv2.waitKey(1)
            # video saving
            if sec == 60:
                sec_sum += sec
                out.release()
                video = self.convert(path, path.split('/')[6])
                mid_memory.write("FLAG")
                break
                
                # impact : 10 <= sec <= 50 processing
                if 0 <= int(fin_memory.read()) <= 50:
                    flag = impt_memory.read()
                    mid_memory.write("FLAG")
                    break
                    
            # key interrupt : video saving
            if cv2.waitKey(33) >= 0:
                cam.release()
                video = self.convert(path, path.split('/')[6])
                break
                
        if mode1 == 'PARK':
            record_type = self.parking_recording()
        elif mode1 == 'NORM':
            record_type = self.normal_recording()
                
        nthread = threading.Thread(target=self.recording, args=(record_type, sec_sum, mode1, mode_bool))
        nthread.start()

    def stop_vid(self):
        # stop the video capture loop
        self.do_vid = False

    def display_frame(self, frame, dt):
        texture = Texture.create(size=(frame.shape[1], frame.shape[0]), colorfmt='bgr')
        texture.blit_buffer(frame.tobytes(order=None), colorfmt='bgr', bufferfmt='ubyte')
        texture.flip_vertical()
        self.main.ids.vid.texture = texture
    

ui = Builder.load_file("UI.kv")

if __name__ == '__main__':
    MyApp().run()
