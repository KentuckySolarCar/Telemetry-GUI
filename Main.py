"""
UKY Solar Car Telemetry Program
Stephen Parsons (stephen.parsons@uky.edu)
https://github.com/KentuckySolarCar/Telemetry-GUI
"""

import random, sys, os, Queue, re, time, operator, json, math, collections, numpy
from datetime import datetime as dt
from sys import platform as _platform

#from lib.Battery import Battery

from PyQt4.QtCore import *
from PyQt4.QtGui import *

from matplotlib.backends.backend_qt4agg import FigureCanvasQTAgg as FigureCanvas
from matplotlib.backends.backend_qt4agg import NavigationToolbar2QTAgg as NavigationToolbar
import matplotlib.pyplot as plt

from code.lib.com_monitor import ComMonitorThread
from code.lib.livedatafeed import LiveDataFeed

from code.lib.serialutils import full_port_name, enumerate_serial_ports
from code.lib.utils import get_all_from_queue, get_item_from_queue

#from UkMathLib import UkMathLib

##################### Changes made by Weilian Song on 9/20/2015 #####################
from code.Battery import *
from code.MotorController import *
from code.MPPT import *
from code.PlottingDataMonitor import *
#####################################################################################

getCurrentTime = lambda: int(round(time.time() * 1000))

def main():
    app = QApplication(sys.argv)
    form = PlottingDataMonitor()
    form.show()
    app.exec_()

if __name__ == "__main__":
    main()
