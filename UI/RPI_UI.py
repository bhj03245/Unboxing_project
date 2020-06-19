# -*-coding:utf8;-*-

import os
from kivy.app import App
from kivy.lang import Builder
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.uix.switch import Switch


class Main(Screen):
    pass


class Menu(Screen):
    pass

    def drowsiness_switch(selfself, switchObject, switchValue):
        if(switchValue):
            print('Switch is On:')
        else:
            print('Switch is OFF')

class Setting(Screen):
    pass


class Video_list(Screen):
    pass


class WindowManager(ScreenManager):
    pass


class MyApp(App):
    def build(self):
        return ui


ui = Builder.load_file("UI.kv")

if __name__ == '__main__':
    MyApp().run()
