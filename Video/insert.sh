#!/bin/bash
ip=`hostname -I`
echo $ip 
eval ./insert_video $ip

