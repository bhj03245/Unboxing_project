#!/bin/bash

/home/pi/gst-rtsp-server/examples/test-launch "( rpicamsrc preview=false bitrate=4000000 keyframe-interval=15 ! video/x-h264, framerate=15/1 ! h264parse ! rtph264pay name=pay0 pt=96 )"

