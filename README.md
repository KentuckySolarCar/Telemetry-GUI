UKY Solar Car Telemetry
=======================

This program is written by [Stephen Parsons](mailto:stephen.parsons@uky.edu).
The underlying functionality to manage the serial connection and the graphical interface borrows heavily from [Eli Bendersky's work](http://eli.thegreenplace.net/2009/08/07/a-live-data-monitor-with-python-pyqt-and-pyserial/).

Getting Started
-----------------------

The program connects to a serial port and then processes the incoming information, displaying it in real time. The following are needed to run the telemetry:

[Python 2.7](https://www.python.org/download/) as source program
[pySerial](http://sourceforge.net/projects/pyserial/files/pyserial/) for serial connection
[PyQt4](http://www.riverbankcomputing.co.uk/software/pyqt/download) for Graphical User Interface
[matplotlib](http://matplotlib.org/downloads.html) for realtime graphs

Each must be installed/configured per machine. This can be a pain. Sometimes it takes a combination of source installations, binary downloads, and package manager installations. Keep trying until it works! It can be done.

Standalone Testing (no connection to car)
-----------------------------------------

First, link two serial ports together so that you can write to one and the program can read from another. On Windows, this is best done with [com0com](http://com0com.sourceforge.net/). On other systems, install ```socat``` and run ```testing/link_sim.py```.
Next, send false data to the serial connection using ```testing/sender_sim.py```.
From this point, use the program as if it were connected to the car.

Usage
-----



License
-----------------------
MIT
