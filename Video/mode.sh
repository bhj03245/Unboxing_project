#! /bin/bash

value=$(python /home/pi/Desktop/mode.py)
echo $value
norm_check=`ps -ef | grep -v "grep" | grep "sudo python video_ver3.py" | wc -l`
park_check=`ps -ef | grep -v "grep" | grep "sudo python video_park.py" | wc -l`

#if [ "$value" == "NORM" ] && [ "$norm_check" != "0" ] && [ "$park_check" == "0" ]; then
	#ps -ef | grep video_park.py  | awk '{print $2}' | sudo xargs kill -9
	#continue
if [ "$value" == "NORM" ] && [ "$park_check" != "0" ] && [ "$norm_check" == "0" ]; then
	ps -ef | grep video_park.py  | awk '{print $2}' | sudo xargs kill -9
	sudo python /home/pi/Desktop/video_ver3.py
elif [ "$value" == "PARK" ] && [ "$norm_check" != "0" ] && [ "$park_check" == "0" ]; then
	ps -ef | grep video_ver3.py  | awk '{print $2}' | sudo xargs kill -9
	sudo python /home/pi/Desktop/video_park.py 
#elif [ "$value" == "PARK" ] && [ "$park_check" != "0" ] && [ "$norm_check" == "0" ]; then
	#ps -ef | grep video_ver3.py  | awk '{print $2}' | sudo xargs kill -9
	#continue
fi

