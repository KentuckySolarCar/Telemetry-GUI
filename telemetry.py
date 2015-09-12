"""
UKY Solar Car Telemetry Program
Stephen Parsons (stephen.parsons@uky.edu)

https://github.com/KentuckySolarCar/Telemetry-GUI
"""

import random, sys, os, Queue, re, time, operator, json, math, collections
from datetime import datetime as dt
from sys import platform as _platform

from PyQt4.QtCore import *
from PyQt4.QtGui import *

from matplotlib.backends.backend_qt4agg import FigureCanvasQTAgg as FigureCanvas
from matplotlib.backends.backend_qt4agg import NavigationToolbar2QTAgg as NavigationToolbar
import matplotlib.pyplot as plt

from lib.com_monitor import ComMonitorThread
from lib.livedatafeed import LiveDataFeed

from lib.serialutils import full_port_name, enumerate_serial_ports
from lib.utils import get_all_from_queue, get_item_from_queue


getCurrentTime = lambda: int(round(time.time() * 1000))

def main():
    app = QApplication(sys.argv)
    form = PlottingDataMonitor()
    form.show()
    app.exec_()

if __name__ == "__main__":
    main()
