raspivid -t 0 -h 720 -w 1280 -fps 25 -b 2000000 -vf -hf -n -o - | gst-launch -v fdsrc ! h264parse ! gdppay ! tcpserversink host=127.0.0.1 port=5000 | /home/pi/gst-rtsp-0.10.8/examples/test-launch "( tcpclientsrc host=127.0.0.1 port=5000 ! gdpdepay ! avdec_h264 ! rtph264pay name=pay0 pt=96 )"

