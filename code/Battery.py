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

class Battery(QGraphicsView):
    def __init__(self, parent=None):
        super(Battery, self).__init__(parent)
        self.voltage = [0]
        self.temperature = [0]

        # Default values
        self.maxBatteryTemp = 30
        self.maxBatteryVoltage = 3.65
        self.minBatteryVoltage = 2.65
        self.bad = False

        self.setMaximumWidth(52)
        self.setMaximumHeight(92)

        self.scene = QGraphicsScene(0,0,50,90,self)
        self.setScene(self.scene)

        self.setViewportUpdateMode(QGraphicsView.FullViewportUpdate)

    def setVoltage(self, inVoltage):
        self.bad = False
        self.voltage.append(inVoltage/10000.0)
        self.drawBattery()

    def getVoltage(self):
        return self.voltage[-1] if self.voltage else 0

    def setTemperature(self, inTemperature):
        self.bad = False
        self.temperature.append(inTemperature)
        self.drawBattery()

    def setBad(self):
        self.bad = True
        self.drawBattery()

    def getTemperature(self):
        return self.temperature[-1]

    def drawBattery(self):
        self.scene.clear()

        red = QColor(255,0,0)
        white = QColor(255,255,255)
        blue = QColor(0,0,255)

        pen = QPen()
        brush = QBrush()
        brush.setStyle(Qt.SolidPattern)

        if self.temperature[-1] > self.maxBatteryTemp:
            color = red
        else:
            color = blue

        pen.setColor(color)
        brush.setColor(color)

        if self.voltage[-1] > self.maxBatteryVoltage:
            self.scene.addRect(0,0,50,75, pen, brush)

        elif self.voltage[-1] > self.minBatteryVoltage:
            dif = self.voltage[-1] - self.minBatteryVoltage
            div = (self.maxBatteryVoltage - self.minBatteryVoltage) /75.0
            dist = int(dif / div)
            self.scene.addRect(0,75-dist,50,dist,pen,brush)

        vText = QGraphicsTextItem("%0.2f V" %self.voltage[-1])
        vText.setFont(QFont('Arial Unicode MS', 10))
        vText.setPos(0,70)

        self.scene.addItem(vText)

        pen.setColor(color)
        brush.setColor(white)
        self.scene.addRect(5,55,40,15,pen,brush)
        tText = QGraphicsTextItem(u"%d \u2103" %(self.temperature[-1]))
        tText.setFont(QFont('Arial Unicode MS', 10))
        tText.setPos(3,50)
        tText.setDefaultTextColor(color)
        self.scene.addItem(tText)

        if self.bad:
            pen.setColor(red)
            brush.setColor(red)
            self.scene.addRect(5,5,40,40,pen,brush)
            pen.setColor(white)
            brush.setColor(white)
            self.scene.addRect(10,10,30,30,pen,brush)
            bText = QGraphicsTextItem("  ???")
            bText.setFont(QFont('Arial Unicode MS', 10))
            bText.setPos(3,10)
            bText.setDefaultTextColor(red)
            self.scene.addItem(bText)