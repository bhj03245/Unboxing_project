#!/usr/bin/python

import smbus

power_mgmt_1 = 0x6b
bus = smbus.SMBus(1)
address = 0x68
bus.write_byte_data(address, power_mgmt_1, 0)

def read_byte(adr):
    return bus.read_byte_data(address, adr)

def read_word(adr):
    high = bus.read_byte_data(address, adr)
    low = bus.read_byte_data(address, adr + 1)
    val = (high << 8) + low
    return val

def read_word_2c(adr):
    val = read_word(adr)
    if (val >= 0x8000):
        return -((65535 - val) + 1)
    else:
        return val

def detect_impact():
    gyro_xout = read_word_2c(0x43)
    gyro_yout = read_word_2c(0x45)
    gyro_zout = read_word_2c(0x47)

    x_scale = gyro_xout / 131.0
    y_scale = gyro_yout / 131.0
    z_scale = gyro_zout / 131.0
        
    if (2.5 < y_scale):
        return 'Impt'
    else:
        return 'Norm'

if __name__=="__main__":
    while True:
    	t = detect_impact()	
	f = open("/var/www/html/apkCtrl/impt_data.txt", 'w')
	f.write(t)
	f.close()
#print(t)
		
		


