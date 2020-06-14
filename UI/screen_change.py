from kivy.app import App
from kivy.lang import Builder
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.uix.button import Button
from kivy.clock import Clock

# Kivy Formatting
kv_text='''\
<MyScreenManager>:
       LandingScreen:
       InputScreen:

<InputScreen@Screen>:
       name: 'input_sc'
       AnchorLayout:
              id: anchor_1
              Button:
                     text: 'Play'

<LandingScreen@Screen>:
       name: 'landing_sc'
       GridLayout:
              id: grid_1
              cols: 2
              height: 480
              width: 800
              spacing: 25,20
              padding: 25,25
'''
# Screen Manager
class MyScreenManager(ScreenManager):
       pass

# Main screen with button layout
class LandingScreen(Screen):
        def __init__(self, **kwargs):
            super(LandingScreen, self).__init__(**kwargs)
            self.buttons = [] # add references to all buttons here
            Clock.schedule_once(self._finish_init)

       # IDs have to be used here because they cannot be applied until widget initialized
        def _finish_init(self, dt):
            self.ids.grid_1.cols = 2

           # Loop to make 15 different buttons on screen
            for x in range(4):
                self.buttons.append(Button(text='button {}'.format(x)))
                self.ids.grid_1.add_widget(self.buttons[x])
                self.buttons[x].background_normal = 'YOUTUBE.png'
            self.buttons[0].bind(on_release=self.switch_screen)

        def switch_screen(self, *args):
            self.manager.current = 'input_sc'

# Input screen
class InputScreen(Screen):

    def switch_screen(self, *args):
        self.manager.current = 'landing_sc'


class MySubApp(App):
    def build(self):
        return MyScreenManager()

def main():
    Builder.load_string(kv_text)
    app = MySubApp()
    app.run()

if __name__ == '__main__':
    main()