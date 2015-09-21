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

from lib.com_monitor import ComMonitorThread
from lib.livedatafeed import LiveDataFeed

from lib.serialutils import full_port_name, enumerate_serial_ports
from lib.utils import get_all_from_queue, get_item_from_queue

#from UkMathLib import UkMathLib

getCurrentTime = lambda: int(round(time.time() * 1000))

class MPPT(QLabel):
    def __init__(self, num, parent=None):
        super(MPPT, self).__init__(parent)
        self.inVoltage = [0]
        self.outVoltage = [0]
        self.inCurrent = [0]
        self.outCurrent = [0]
        self.num = num
        self.outCurrent = [0]
        self.setText("%.3f" %self.outCurrent[-1])

    def setInVoltage(self, voltage):
        self.inVoltage.append(voltage)
        self.calcOutCurrent()

    def setOutVoltage(self, voltage):
        self.outVoltage.append(voltage)
        self.calcOutCurrent()

    def setInCurrent(self, current):
        self.inCurrent.append(current / 100.0)
        self.calcOutCurrent()

    def calcOutCurrent(self):
        if not self.outVoltage[-1] == 0:
            self.outCurrent.append(self.inVoltage[-1] * self.inCurrent[-1] / self.outVoltage[-1])
            self.setText("%.3f" %self.outCurrent[-1])

    def getOutCurrent(self):
        return self.outCurrent[-1] if self.outCurrent else 0