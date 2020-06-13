import kivy
import subprocess
import os
# import test as t
from kivy.app import App
from kivy.uix.button import Button
from kivy.uix.togglebutton import ToggleButton
from kivy.uix.gridlayout import GridLayout
from kivy.graphics import Color, Rectangle

class MyApp(App):
    def build(self):
        layout = GridLayout(rows=2, cols=2, spacing=0, padding=0, row_default_height=150)

        with layout.canvas.before:
            Color(.2,.2,.2,1)
            self.rect = Rectangle(size=(800,600), pos=layout.pos)

        inputDisplay = Button(text="Input")
        outputControl = ToggleButton(text="LED")
        testButton = Button(text="Test")
        beepButton = Button(text="BEEP!")

        testButton.bind(on_press=print_t)
        layout.add_widget(inputDisplay)
        layout.add_widget(outputControl)
        layout.add_widget(beepButton)
        layout.add_widget(testButton)
        return layout

def print_t(obj):
    if obj.text == 'Test':
        #t.printf()
        cmd = 'sudo python detect_impact_ver2.py'
        os.system(cmd)

if __name__ == '__main__':
    MyApp().run()

# reference : https://github.com/mrichardson23/rpi-kivy-screen/blob/master/main.py
# http://mattrichardson.com/kivy-gpio-raspberry-pi-touch/index.html
