import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BCM)
gpio_pin = 12
scale = [261, 294, 329, 349, 392, 440, 493, 523]
GPIO.setup(gpio_pin, GPIO.OUT)
list = [7, 4]

try:
    p = GPIO.PWM(gpio_pin, 100)
    p.start(100)
    p.ChangeDutyCycle(90)
    
    for i in range(len(list)):
        p.ChangeFrequency(scale[list[i]])
        time.sleep(1)
    p.stop()
    
finally:
    GPIO.cleanup()
    