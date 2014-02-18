UKY Solar Car Telemetry
=======================

This program is written by [Stephen Parsons](stephen.parsons@uky.edu).
The underlying functionality to manage the serial connection and the graphical interface borrows heavily from [Eli Bendersky's work](http://eli.thegreenplace.net/2009/08/07/a-live-data-monitor-with-python-pyqt-and-pyserial/).

Getting Started
-----------------------

The program connects to a serial port and then processes the incoming information. This is handled through [PySerial](http://pyserial.sourceforge.net/). For use with the actual car, you can select the desired port from the program GUI.

There are tools included for testing/developing this program without a connection to the actual car. ```testing/sender_sim.py``` simulates the incoming messages and writes them out to a port. In order for the program to think it is connected to the output of ```sender_sim.py```, it is necessary to link two serial ports together. In Windows this can be done with the program [com0com](http://com0com.sourceforge.net/) (with Windows 8 you will run into driver signature issues and will need to be clever and work around them). On Linux machines the program ```link_sim.py``` will link two ports together (```socat``` must be installed).