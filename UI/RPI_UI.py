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
import requests
import webbrowser
import pymysql

LabelBase.register(name='malgun',
                   fn_regular='malgun.ttf')
date = ''
source = ''


class Main(Screen):
    pass


class Menu(Screen):
    pass

    def drowsiness_switch(selfself, switchObject, switchValue):
        if (switchValue):
            print('Switch is On:')
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
        date = self.text


class Manual(Screen):
    data_items = ListProperty([])

    def __init__(self, **kwargs):
        super(Manual, self).__init__(**kwargs)
        self.get_board()

    def get_board(self):
        mydb = pymysql.connect(
            host='localhost',
            user='spring',
            passwd='passwd',
            database='springdb',
        )

        mycursor = mydb.cursor()
        sql = "select b_date from tk_board"
        mycursor.execute(sql)

        rows = mycursor.fetchall()
        root = 'localhost'
        for row in rows:
            for col in row:
                self.data_items.append(col)


class Normal(Screen):
    data_items = ListProperty([])
    url = ListProperty([])

    def __init__(self, **kwargs):
        super(Normal, self).__init__(**kwargs)
        self.get_board()

    def get_board(self):
        mydb = pymysql.connect(
            host='localhost',
            user='spring',
            passwd='passwd',
            database='springdb',
        )

        mycursor = mydb.cursor()
        sql = "select b_date from tk_board"
        mycursor.execute(sql)

        rows = mycursor.fetchall()
        root = 'localhost'
        for row in rows:
            for col in row:
                self.data_items.append(col)


class Impact(Screen):
    data_items = ListProperty([])

    def __init__(self, **kwargs):
        super(Impact, self).__init__(**kwargs)
        self.get_board()

    def get_board(self):
        mydb = pymysql.connect(
            host='localhost',
            user='spring',
            passwd='passwd',
            database='springdb',
        )

        mycursor = mydb.cursor()
        sql = "select b_date from tk_board"
        mycursor.execute(sql)

        rows = mycursor.fetchall()
        root = 'localhost'
        for row in rows:
            for col in row:
                self.data_items.append(col)


class Parking(Screen):
    data_items = ListProperty([])

    def __init__(self, **kwargs):
        super(Parking, self).__init__(**kwargs)
        self.get_board()

    def get_board(self):
        mydb = pymysql.connect(
            host='localhost',
            user='spring',
            passwd='passwd',
            database='springdb',
        )

        mycursor = mydb.cursor()
        sql = "select b_date from tk_board"
        mycursor.execute(sql)

        rows = mycursor.fetchall()
        root = 'localhost'
        for row in rows:
            for col in row:
                self.data_items.append(col)


class VideoWidget(Screen):
    path = r'/var/www/html/Upload?UB_video/Normal/'
    file = date
    source = path + file


class VideoPlayerApp(App):
    def build(self):
        return VideoWidget()


class WindowManager(ScreenManager):
    pass


class MyApp(App):
    def build(self):
        return ui


ui = Builder.load_file("UI.kv")

if __name__ == '__main__':
    MyApp().run()
