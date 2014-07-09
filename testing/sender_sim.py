import serial
import random, time, math
from sys import platform as _platform

if _platform == "linux" or _platform == "linux2":
    # linux
    port = "/dev/ttySx"
elif _platform == "darwin":
    # OS X
    port = "/dev/ttySx"
elif _platform == "win32":
    # Windows...
    port = "\\\\.\\COM3"

baud_rate = 19200
ser = serial.Serial(port, baud_rate)
incycle = 0
time_delay = 0.02

def delay():
	time.sleep(time_delay)
	return

while True:

	for i in range(4):
		writestr = ("M["+str(i)+"] "+str(random.randint(0,100))+" "+str(random.randint(0,100))+" "+str(random.randint(0,10)))
		print writestr
		ser.write(writestr)
		delay()

	writestr = ("C = "+str(random.randint(0,100)))
	print writestr
	ser.write(writestr)
	delay()

	writestr = ("S = "+str(random.randint(0,100)))
	print writestr
	ser.write(writestr)
	delay()

	writestr = ("E = "+str(random.randint(0,10000)))
	print writestr
	ser.write(writestr)
	delay()

	for i in range(20):
		writestr = ("V[0][%02d] = "+str(random.randint(2500,3800))) %(i)
		print writestr
		ser.write(writestr)
		delay()

	writestr = ("C = "+str(random.randint(0,100)))
	print writestr
	ser.write(writestr)
	delay()

	writestr = ("S = "+str(random.randint(0,100)))
	print writestr
	ser.write(writestr)
	delay()

	writestr = ("E = "+str(random.randint(0,10000)))
	print writestr
	ser.write(writestr)
	delay()

	for i in range(20):
		writestr = ("V[1][%02d] = "+str(random.randint(2500,3800))) %(i)
		print writestr
		ser.write(writestr)
		delay()

	for i in range(4):
		writestr = ("M["+str(i)+"] "+str(random.randint(0,100))
			+" "+str(random.randint(0,100))+" "+str(random.randint(0,10)))
		print writestr
		ser.write(writestr)
		delay()

	writestr = ("C = "+str(random.randint(0,100)))
	print writestr
	ser.write(writestr)
	delay()

	writestr = ("S = "+str(random.randint(0,100)))
	print writestr
	ser.write(writestr)
	delay()

	writestr = ("E = "+str(random.randint(0,10000)))
	print writestr
	ser.write(writestr)
	delay()

	for i in range(20):
		writestr = ("T[0][%02d] = "+str(random.randint(20,35))) %(i)
		print writestr
		ser.write(writestr)
		delay()

	writestr = ("C = "+str(random.randint(0,100)))
	print writestr
	ser.write(writestr)
	delay()

	writestr = ("S = "+str(random.randint(0,100)))
	print writestr
	ser.write(writestr)
	delay()

	writestr = ("E = "+str(random.randint(0,10000)))
	print writestr
	ser.write(writestr)
	delay()

	for i in range(20):
		writestr = ("T[1][%02d] = "+str(random.randint(20,35))) %(i)
		print writestr
		ser.write(writestr)
		delay()

ser.close()