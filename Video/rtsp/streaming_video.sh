raspivid -t 0 -w 640 -h 480 -fps 25 -hf -b 2000000 -o - | gst-launch-1.0 -v fdsrc ! h264parse ! gdppay ! tcpserversink host=127.0.0.1 port=5000
