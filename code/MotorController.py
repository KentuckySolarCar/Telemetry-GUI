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

class MotorController(QGroupBox):
    def __init__(self, parent=None):
        super(MotorController, self).__init__(parent)
        self.speed = []
        self.current = []
        self.energy = []
        self.amp_sec = 0.0
        self.watt_sec = 0.0
        self.averageSpeed = 0
        self.conversion = 0.223693629
        self.checkFromAv = 0
        self.checkFromE = 0

        self.odometer = 0.0

        self.tSpeed = QLabel('0.00')
        self.tCurrent = QLabel('0.00')
        self.tEnergy = QLabel('0.00')
        self.tAverageSpeed = QLabel('0.00')

        self.resetEnergyButton = QPushButton('Reset')
        self.resetAvSpeedButton = QPushButton('Reset')
        self.resetEnergyButton.clicked.connect(self.resetEnergy)
        self.resetAvSpeedButton.clicked.connect(self.resetAvSpeed)

        motorControllerLayout = QGridLayout()
        motorControllerLayout.setColumnMinimumWidth(1,52)
        motorControllerLayout.addWidget(QLabel('Speed:'),0,0)
        motorControllerLayout.addWidget(self.tSpeed,0,1)
        motorControllerLayout.addWidget(QLabel('mph'),0,2)
        motorControllerLayout.addWidget(QLabel('Current:'),1,0)
        motorControllerLayout.addWidget(self.tCurrent,1,1)
        motorControllerLayout.addWidget(QLabel('A'),1,2)
        motorControllerLayout.addWidget(QLabel('Energy:'),2,0)
        motorControllerLayout.addWidget(self.tEnergy,2,1)
        motorControllerLayout.addWidget(QLabel('Ah'),2,2)
        motorControllerLayout.addWidget(self.resetEnergyButton,2,3)
        motorControllerLayout.addWidget(QLabel('Av. Speed:'),3,0)
        motorControllerLayout.addWidget(self.tAverageSpeed,3,1)
        motorControllerLayout.addWidget(QLabel('mph'),3,2)
        motorControllerLayout.addWidget(self.resetAvSpeedButton,3,3)

        self.setLayout(motorControllerLayout)

    def setSpeed(self, inSpeed):
        # convert to miles per hour
        self.speed.append([inSpeed, time.time()])
        self.tSpeed.setText('%.2f' %self.speed[-1][0])
        self.calcAverageSpeed()

    def setCurrent(self, inCurrent):
        self.current.append([inCurrent, time.time()])
        self.tCurrent.setText('%.2f' %self.current[-1][0])

    def setEnergy(self, energy):
        self.energy.append((energy, time.time()))
        self.calcEnergy()

    def setAmpHours(self, amp_hours):
        self.amp_hours = amp_hours

    def setWattSec(self, watt_sec):
        self.watt_sec = watt_sec

    def setOdometer(self, odo):
        self.odometer = odo

    def calcAverageSpeed(self):
        self.averageSpeed = sum(item[0] for item in self.speed[self.checkFromAv:])/float(len(self.speed[self.checkFromAv:]))
        self.tAverageSpeed.setText('%.2f' %self.averageSpeed)

    def resetEnergy(self):
        if self.energy:
            self.checkFromE = len(self.energy) - 1
            self.calcEnergy()

    def calcEnergy(self):
        if self.energy:
            self.tEnergy.setText(str(self.energy[-1][0] - self.energy[self.checkFromE][0]))
        if len(self.energy) > 1:
            ampSeconds = (self.energy[-1][0] - self.energy[-2][0])*3600.0
            deltaTseconds = self.energy[-1][1] - self.energy[-2][1]
            amps = ampSeconds / deltaTseconds
            self.setCurrent(amps)

    def resetAvSpeed(self):
        self.checkFromAv = len(self.speed) - 1
        self.calcAverageSpeed()

    def getSpeed(self):
        return 0 if not self.speed else self.speed[-1][0]

    def getCurrent(self):
        return 0 if not self.current else self.current[-1]

    def getSpeeds(self):
        return self.speed

    def getCurrents(self):
        return self.current if len(self.current) else [ "0", "0" ]

    def getAmpHours(self):
        return self.amp_hours

    def getWattSec(self):
        return self.watt_sec

    def getOdometer(self):
        return self.odometer




