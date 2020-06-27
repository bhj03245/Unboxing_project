# -*-coding:utf8;-*-

import os
from kivy.uix.image import Image
from kivy.app import App
from kivy.lang import Builder
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.core.text import LabelBase
from kivy.uix.switch import Switch
from kivy.clock import Clock
from kivy.graphics.texture import Texture
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.video import Video
from kivy.uix.camera import Camera

from ccccc import normal_recording
import cv2
import datetime
import time
import sys

LabelBase.register(name="malgun",
                   fn_regular="malgun.ttf")

class KivyCamera(Screen):
    def __init__(self, capture, fps, **kwargs):
        super(KivyCamera, self).__init__(**kwargs)
        self.capture = capture
        Clock.schedule_interval(self.update, 1.0 / fps)

    def update(self, dt):
        ret, frame = self.capture.read()
        if ret:
            buf1 = cv2.flip(frame, 0)
            buf = buf1.tostring()
            image_texture = Texture.create(
                size=(frame.shape[1], frame.shape[0]), colorfmt='bgr')
            image_texture.blit_buffer(buf, colorfmt='bgr', bufferfmt='ubyte')
            self.texture = image_texture



class Main(Screen):
    def cam(self, value):
        self.capture = cv2.VideoCapture(0)
        path = normal_recording()[0]
        out = normal_recording()[1]
        framecnt = 0
        fps = int(self.capture.get(cv2.CAP_PROP_FPS))
        sec = 0

        while True:
            framecnt += 1
            ret, frame = self.capture.read()
            sec = framecnt / fps

            print("%d %d %d" % (fps, framecnt, sec))
            out.write(frame)

            if sec == 10:
                out.release()
        # self.my_camera = KivyCamera(capture=self.capture, fps=30)

    def on_stop(self):
        self.capture.release()

class Menu(Screen):
    pass

    def drowsiness_switch(selfself, switchObject, switchValue):
        if(switchValue):
            print('Switch is On:')
        else:
            print('Switch is OFF')

class Setting(Screen):
    pass

    def drowsiness_switch(selfself, switchObject, switchValue):
        if(switchValue):
            print('Switch is On:')
        else:
            print('Switch is OFF')

class VideoWidget(Screen):
    pass


class Video_list(Screen):
    def video_list(selfself, obj, value):
        if value == 'Norm':
            VideoWidget()
        elif value == 'Impt':
            VideoWidget()
        elif value == 'Park':
            VideoWidget()
        elif value == 'Manl':
            VideoWidget()

class WindowManager(ScreenManager):
    pass

class MyApp(App):
    def build(self):
        return ui

ui = Builder.load_file("UI.kv")

if __name__ == '__main__':
    MyApp().run()
