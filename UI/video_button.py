import kivy
from kivy.app import App
from kivy.uix.button import Button
from kivy.uix.togglebutton import ToggleButton
from kivy.uix.gridlayout import GridLayout
from kivy.graphics import Color, Rectangle
from kivy.uix.camera import Camera
import cv2
import numpy as np

class MyApp(App):
    def build(self):
        layout = GridLayout(rows=2, cols=2, spacing=0, padding=0, row_default_height=150)

        with layout.canvas.before:
            Color(.2,.2,.2,1)
            self.rect = Rectangle(size=(800,600), pos=layout.pos)

        play = Button(text="Play")
        outputControl = ToggleButton(text="LED")
        testButton = Button(text="Test")
        beepButton = Button(text="BEEP!")

        testButton.bind(on_press=print_t)
        play.bind(on_press=print_t)
        layout.add_widget(play)
        layout.add_widget(outputControl)
        layout.add_widget(beepButton)
        layout.add_widget(testButton)
        return layout

def print_t(obj):
    if obj.text == 'Test':
        print("Test!!!!!!!!!!!")

    elif obj.text == 'Play':
        cap = cv2.VideoCapture('IMPT_200531_180221.mp4')
        while(cap.isOpened()):
            ret, frame = cap.read()
            cv2.imshow("image", frame)

            if cv2.waitKey(1) & 0xFF == ord('q'):
                break
        cap.release()
        cv2.destroyAllWindows()

if __name__ == '__main__':
    MyApp().run()

# reference : https://github.com/mrichardson23/rpi-kivy-screen/blob/master/main.py
# http://mattrichardson.com/kivy-gpio-raspberry-pi-touch/index.html