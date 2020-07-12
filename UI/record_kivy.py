import threading
from functools import partial
from threading import Thread

from kivy.clock import Clock
from kivy.uix.image import Image
from kivy.app import App
from kivy.graphics.texture import Texture
from kivy.lang import Builder
from kivy.uix.floatlayout import FloatLayout
from kivy.uix.screenmanager import ScreenManager, Screen
import cv2
import datetime
import os

class Menu(Screen):
    pass

class MainScreen(Screen):
    pass

class WindowManager(ScreenManager):
    pass

class CombineScreen(FloatLayout):
    pass

norm_path = 'D:\\UI\\'
fourcc = cv2.VideoWriter_fourcc(*'mp4v')

class CamApp(App):
    def build(self):
        threading.Thread(target=self.doit, daemon=True).start()
        sm = ScreenManager()
        self.main_screen = MainScreen()
        self.menu = Menu()
        sm.add_widget(self.main_screen)
        sm.add_widget(self.menu)
        return sm

    def create_time(self):
        now = datetime.datetime.today().strftime("%y%m%d_%H%M%S")
        return now

    def create_file(self):
        now = self.create_time()
        file_name = now + '.mp4'
        return file_name

    def normal_recording(self):
        __file_name = self.create_file()
        path = norm_path + "NORM_" + __file_name
        norm_out = cv2.VideoWriter(path, fourcc, 30.0, (640, 480))
        return path, norm_out

    def doit(self):
        # this code is run in a separate thread
        self.do_vid = True  # flag to stop loop

        cam = cv2.VideoCapture(0)
        path = self.normal_recording()[0]
        out = self.normal_recording()[1]
        framecnt = 0
        fps = int(cam.get(cv2.CAP_PROP_FPS))
        sec = 0
        # start processing loop
        while (self.do_vid):
            framecnt += 1
            ret, frame = cam.read()
            sec = framecnt / fps
            print("%d %d %d" % (framecnt, fps, sec))
            Clock.schedule_once(partial(self.display_frame, frame))
            out.write(frame)
            # cv2.imshow('Hidden', frame)
            cv2.waitKey(1)
            if sec == 10:
                out.release()
                break

        nthread = threading.Thread(target=self.doit)
        nthread.start()

    def stop_vid(self):
        # stop the video capture loop
        self.do_vid = False

    def display_frame(self, frame, dt):
        texture = Texture.create(size=(frame.shape[1], frame.shape[0]), colorfmt='bgr')
        texture.blit_buffer(frame.tobytes(order=None), colorfmt='bgr', bufferfmt='ubyte')
        texture.flip_vertical()
        self.main_screen.ids.vid.texture = texture

ui = Builder.load_file("vidui2.kv")

if __name__ == '__main__':
    CamApp().run()