UKY Solar Car Telemetry
=======================

This program is written by [Stephen Parsons](mailto:stephen.parsons@uky.edu).
The underlying functionality to manage the serial connection and the graphical interface borrows heavily from [Eli Bendersky's work](http://eli.thegreenplace.net/2009/08/07/a-live-data-monitor-with-python-pyqt-and-pyserial/).

Getting Started
---------------

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

Run the telemetry program by double clicking or from a shell with ```python telemetry.py```.

Select a serial port by clicking "Change Port" or with <kbd>Ctrl</kbd>+<kbd>p</kbd>.  
Begin monitoring with "Start Monitor" or <kbd>Ctrl</kbd>+<kbd>m</kbd>.  
Stop monitoring with "Stop Monitor" or <kbd>Ctrl</kbd>+<kbd>t</kbd>.  
You can exit with "Exit" or <kbd>Ctrl</kbd>+<kbd>x</kbd>.

Log files will be automatically written in the ```logs/``` directory.  

Info for each battery is displayed. The bar level indicates the voltage (also noted at the bottom of each battery) and the temperature is displayed as well. Should a battery exceed 30&deg; C, it will turn red. High, low, and average voltages are displayed for each box and for the pack as a whole.

Various stats for the motor controller and the MPPTs are displayed as well as some graphs.

Maintenance
-----------

A large part of the maintenance work will be parsing serial data, storing it, and displaying it. This is functional now but the team may decide to add or change information in the car's output as time goes on. Find ```# Regular Expressions``` in the code and update them as necessary. Below this section you can write update functions to parse the data and store it internally. Changing the display requires writing layouts and widgets and adding them to the existing window. This should be self explanatory after reading the existing interface code.

Todo
----

* Fix tick spacing on graphs and make them look way better in general
* Incorporate additional calculations/data per suggestion of Chris
* Temperature color gradient rather than just turn red at 30&deg; C
* Make sure TelSpec.pdf or similar documentation somewhere stays up to date with the car output (already outdated)
* Add util to take multiple log files and turn them into one
* Investigate packaging as one .exe file with pyInstaller

License
-----------------------
MIT
