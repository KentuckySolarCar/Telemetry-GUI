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


class PlottingDataMonitor(QMainWindow):
    def __init__(self, parent=None):
        super(PlottingDataMonitor, self).__init__(parent)

        if _platform == "linux" or _platform == "linux2":
            # linux
            self.defaultSerialPort = "/dev/ttySy"
        elif _platform == "darwin":
            # OS X
            self.defaultSerialPort = "/dev/ttySy"
        elif _platform == "win32":
            # Windows...
            self.defaultSerialPort = "COM4"

        self.setFont(QFont('Arial Unicode MS', 10))

        self.mppts = []
        for i in range(4):
            self.mppts.append(MPPT(i))

        self.total_battery_voltage = 0.0

        self.BatmanAvgVoltage = 0.0
        self.BatmanMinVoltage = 1.0e10
        self.BatmanAvgCurrent = 0.0
        self.BatmanAvgTemp = 0.0
        self.BatmanWattSec = 0.0
        self.RobinAvgVoltage = 0.0
        self.RobinMinVoltage = 1.0e10
        self.RobinAvgCurrent = 0.0
        self.RobinAvgTemp = 0.0
        self.RobinWattSec = 0.0

        self.graphTimeInterval = 2 #minutes

        # self.arrayCurrent = collections.deque( maxlen = 60*self.graphTimeInterval )
        # self.batteryCurrent = collections.deque( maxlen = 60*self.graphTimeInterval )

        # self.array_power_deque = collections.deque( maxlen = 60*self.graphTimeInterval )
        # self.total_voltage_deque = collections.deque( maxlen = 60*self.graphTimeInterval )
        # self.speed_deque = collections.deque( maxlen = 60*self.graphTimeInterval )
        # self.gross_instant_deque = collections.deque( maxlen = 60*self.graphTimeInterval )
        # self.battery_charge_deque = collections.deque( maxlen = 60*self.graphTimeInterval )

        self.arrayCurrent = []
        self.batteryCurrent = []

        self.array_power_deque = []
        self.total_voltage_deque = []
        self.speed_deque = []
        self.gross_instant_deque = []
        self.battery_charge_deque = []

        self.monitor_active = False
        self.logging_active = False
        self.com_monitor = None
        self.com_data_q = None
        self.com_error_q = None
        self.livefeed = LiveDataFeed()
        self.timer = QTimer()

        self.curTime = time.time()
        self.startTime = time.time()
        self.timeSinceStart = self.curTime - self.startTime

        self.create_menu()
        self.create_main_frame()
        self.create_status_bar()

        self.portname.setText(self.defaultSerialPort)            
        self.set_actions_enable_state()

        self.setWindowTitle('University of Kentucky Solar Car Telemetry')

    def create_main_frame(self):

        # -----------------------------------------------------------
        #   Buttons
        #   TODO: (Ethan) Add a on key pressed event to command promp input
        # -----------------------------------------------------------
        portname_l, self.portname = self.make_data_box('COM Port:')
        
        portname_layout = QHBoxLayout()
        portname_layout.addWidget(portname_l)
        portname_layout.addWidget(self.portname, 0)

        change_port = QPushButton('Change Port')
        change_port.clicked.connect(self.on_select_port)
        portname_layout.addWidget(change_port)

        self.startStop = QPushButton('Start Monitor')
        self.startStop.clicked.connect(self.on_start_stop)
        portname_layout.addWidget(self.startStop)

        self.loggingToggle = QPushButton('Start Logging')
        self.loggingToggle.clicked.connect(self.toggleLogging)
        portname_layout.addWidget(self.loggingToggle)

        self.resetToggle = QPushButton('Reset Calcualtions')
        self.resetToggle.clicked.connect(self.resetCalculations)
        portname_layout.addWidget(self.resetToggle)

        exit = QPushButton('Exit')
        exit.clicked.connect(self.close)
        portname_layout.addWidget(exit)

        portname_layout.addStretch(1)

        portname_groupbox = QGroupBox('Controls')
        portname_groupbox.setLayout(portname_layout)

        # -----------------------------------------------------------
        #   Command Promp
        #   TODO: (Ethan) Add a on key pressed event to command promp input
        # -----------------------------------------------------------
        batteryLayout1 = QGridLayout()
        batteryLayout1.setColumnMinimumWidth(0, 300)

        self.running_log = QTextEdit();
        self.running_log.setEnabled(False)
        batteryLayout1.setRowMinimumHeight(0, 400)
        batteryLayout1.addWidget(self.running_log, 0, 0)

        self.command_promt = QLineEdit()
        batteryLayout1.addWidget(self.command_promt, 1, 0)

        batteryWidget1 = QGroupBox('Log Display')
        batteryWidget1.setAlignment(Qt.AlignHCenter)
        batteryWidget1.setLayout(batteryLayout1)


        # -----------------------------------------------------------
        #   Calculated Values
        #   [name] [value] [reset button]
        #   TODO: (Ethan) Actually update contense where "value" appears
        # -----------------------------------------------------------
        batteryLayout2 = QGridLayout()

        calculationWidget = QGroupBox('Calculations')
        calculationLayout = QGridLayout()

        # Array Power
        self.calc_array_power = QLabel("value")
        calculationLayout.addWidget(QLabel("Array Power"), 0, 0)
        calculationLayout.addWidget(self.calc_array_power, 0, 1)
        # calculationLayout.addWidget(QLabel(""), 0, 2)

        # Gross Watt*Hours
        self.calc_gross_watt_hours = QLabel("value")
        calculationLayout.addWidget(QLabel("Gross Watt*Hours"), 1, 0)
        calculationLayout.addWidget(self.calc_gross_watt_hours, 1, 1)
        # calculationLayout.addWidget(QLabel("B"), 1, 2)

        # 
        self.calc_net_watt_hours = QLabel("value")
        calculationLayout.addWidget(QLabel("Net Watt*Hours"), 2, 0)
        calculationLayout.addWidget(self.calc_net_watt_hours, 2, 1)
        # calculationLayout.addWidget(QLabel("B"), 2, 2)

        # 
        self.calc_average_speed = QLabel("value")
        calculationLayout.addWidget(QLabel("Average Speed"), 3, 0)
        calculationLayout.addWidget(self.calc_average_speed, 3, 1)
        # calculationLayout.addWidget(QLabel("B"), 3, 2)

        # 
        self.calc_average_gross_power = QLabel("value")
        calculationLayout.addWidget(QLabel("Average Gross Power"), 4, 0)
        calculationLayout.addWidget(self.calc_average_gross_power, 4, 1)
        # calculationLayout.addWidget(QLabel("B"), 4, 2)

        # 
        self.calc_average_net_power = QLabel("value")
        calculationLayout.addWidget(QLabel("Average Net Power"), 5, 0)
        calculationLayout.addWidget(self.calc_average_net_power, 5, 1)
        # calculationLayout.addWidget(QLabel("B"), 5, 2)

        # 
        self.calc_gross_average_power = QLabel("value")
        calculationLayout.addWidget(QLabel("Gross Average Power"), 6, 0)
        calculationLayout.addWidget(self.calc_gross_average_power, 6, 1)
        # calculationLayout.addWidget(QLabel(""), 6, 2)

        # 
        self.calc_gross_average_watt = QLabel("value")
        calculationLayout.addWidget(QLabel("Gross Average Watt Hour/Mile"), 7, 0)
        calculationLayout.addWidget(self.calc_gross_average_watt, 7, 1)
        # calculationLayout.addWidget(QLabel(""), 7, 2)

        # 
        self.calc_battery_run_time_remaining = QLabel("value")
        calculationLayout.addWidget(QLabel("Battery Only Run-time Remaining"), 8, 0)
        calculationLayout.addWidget(self.calc_battery_run_time_remaining, 8, 1)
        # calculationLayout.addWidget(QLabel(""), 8, 2)

        # 
        self.calc_battery_range = QLabel("value")
        calculationLayout.addWidget(QLabel("Battery Only Range"), 9, 0)
        calculationLayout.addWidget(self.calc_battery_range, 9, 1)
        # calculationLayout.addWidget(QLabel(""), 9, 2)

        # 
        self.calc_battery_solar_run_time = QLabel("value")
        calculationLayout.addWidget(QLabel("Battery and Solar Run-time"), 10, 0)
        calculationLayout.addWidget(self.calc_battery_solar_run_time, 10, 1)
        # calculationLayout.addWidget(QLabel(""), 10, 2)

        # 
        self.calc_battery_solar_distance = QLabel("value")
        calculationLayout.addWidget(QLabel("Battery and Solar Range"), 11, 0)
        calculationLayout.addWidget(self.calc_battery_solar_distance, 11, 1)
        # calculationLayout.addWidget(QLabel(""), 11, 2)

        # 
        self.calc_battery_charge_remaining = QLabel("value")
        calculationLayout.addWidget(QLabel("Battery Charge Remaining"), 12, 0)
        calculationLayout.addWidget(self.calc_battery_charge_remaining, 12, 1)
        # calculationLayout.addWidget(QLabel(""), 12, 2)

        # 
        self.calc_solar_energy_remaining = QLabel("value")
        calculationLayout.addWidget(QLabel("Solar Energy Remaining in Day"), 13, 0)
        calculationLayout.addWidget(self.calc_solar_energy_remaining, 13, 1)
        # calculationLayout.addWidget(QLabel(""), 13, 2)

        # 
        self.calc_motor_power = QLabel("value")
        calculationLayout.addWidget(QLabel("Motor Power"), 14, 0)
        calculationLayout.addWidget(self.calc_motor_power, 14, 1)
        # calculationLayout.addWidget(QLabel(""), 14, 2)

        calculationWidget.setLayout(calculationLayout);
        batteryLayout2.addWidget(calculationWidget)

        batteryWidget2 = QGroupBox()
        batteryWidget2.setAlignment(Qt.AlignHCenter)
        batteryWidget2.setLayout(batteryLayout2)

        batteryWidget = QWidget()
        batteryWidgetLayout = QHBoxLayout()
        batteryWidgetLayout.addWidget(batteryWidget1)
        batteryWidgetLayout.addWidget(batteryWidget2)
        batteryWidget.setLayout(batteryWidgetLayout)

        # -----------------------------------------------------------
        #   Time
        # -----------------------------------------------------------
        timeWidget = QGroupBox('Time')
        timeLayout = QGridLayout()
        self.currentTime = QLabel(time.strftime("%H:%M:%S", time.localtime(self.curTime)))
        self.timeSince = QLabel(time.strftime("%H:%M:%S", time.gmtime(self.timeSinceStart)))
        timeLayout.addWidget(self.currentTime,0,1)
        timeLayout.addWidget(self.timeSince,1,1)
        timeLayout.addWidget(QLabel('Current time:'),0,0)
        timeLayout.addWidget(QLabel('Run time:'),1,0)
        timeWidget.setLayout(timeLayout)

        # -----------------------------------------------------------
        #   Batteries
        # -----------------------------------------------------------
        batteryStatsWidget = QGroupBox('Batteries')
        batteryStatsLayout = QGridLayout()

        self.BatmanVAvg = QLabel('0.00 V')
        self.BatmanVMax = QLabel('0.00 V (#)')
        self.BatmanVMin = QLabel('0.00 V (#)')
        self.BatmanBC = QLabel('0.00 A')
        self.BatmanTAvg = QLabel('0.00 C')
        self.BatmanTMax = QLabel('0.00 C')
        self.BatmanTMin = QLabel('0.00 C')
        batteryStatsLayout.addWidget(QLabel('Batman'),0,0,1,2, Qt.AlignHCenter)
        batteryStatsLayout.addWidget(QLabel('V_Average:'),1,0)
        batteryStatsLayout.addWidget(self.BatmanVAvg,1,1)
        batteryStatsLayout.addWidget(QLabel('V_Max:'),2,0)
        batteryStatsLayout.addWidget(self.BatmanVMax,2,1)
        batteryStatsLayout.addWidget(QLabel('V_Min:'),3,0)
        batteryStatsLayout.addWidget(self.BatmanVMin,3,1)
        batteryStatsLayout.addWidget(QLabel('Current:'),4,0)
        batteryStatsLayout.addWidget(self.BatmanBC,4,1)
        batteryStatsLayout.addWidget(QLabel('T_Average:'),5,0)
        batteryStatsLayout.addWidget(self.BatmanTAvg,5,1)
        batteryStatsLayout.addWidget(QLabel('T_Max:'),6,0)
        batteryStatsLayout.addWidget(self.BatmanTMax,6,1)
        batteryStatsLayout.addWidget(QLabel('T_Min:'),7,0)
        batteryStatsLayout.addWidget(self.BatmanTMin,7,1)

        self.RobinVAvg = QLabel('0.00 V')
        self.RobinVMax = QLabel('0.00 V (#)')
        self.RobinVMin = QLabel('0.00 V (#)')
        self.RobinBC = QLabel('0.00 A')
        self.RobinTAvg = QLabel('0.00 C')
        self.RobinTMax = QLabel('0.00 C')
        self.RobinTMin = QLabel('0.00 C')
        batteryStatsLayout.addWidget(QLabel('Robin'),0,2,1,2, Qt.AlignHCenter)
        batteryStatsLayout.addWidget(QLabel('V_Average:'),1,2)
        batteryStatsLayout.addWidget(self.RobinVAvg,1,3)
        batteryStatsLayout.addWidget(QLabel('V_Max:'),2,2)
        batteryStatsLayout.addWidget(self.RobinVMax,2,3)
        batteryStatsLayout.addWidget(QLabel('V_Min:'),3,2)
        batteryStatsLayout.addWidget(self.RobinVMin,3,3)
        batteryStatsLayout.addWidget(QLabel('Current:'),4,2)
        batteryStatsLayout.addWidget(self.RobinBC,4,3)
        batteryStatsLayout.addWidget(QLabel('T_Average:'),5,2)
        batteryStatsLayout.addWidget(self.RobinTAvg,5,3)
        batteryStatsLayout.addWidget(QLabel('T_Max:'),6,2)
        batteryStatsLayout.addWidget(self.RobinTMax,6,3)
        batteryStatsLayout.addWidget(QLabel('T_Min:'),7,2)
        batteryStatsLayout.addWidget(self.RobinTMin,7,3)


        batteryStatsWidget.setLayout(batteryStatsLayout)

        # -----------------------------------------------------------
        #   Motor Controller
        # -----------------------------------------------------------
        self.motorControllerWidget = MotorController('Motor Controller')

        # -----------------------------------------------------------
        #   MPPTs
        # -----------------------------------------------------------
        mpptWidget = QGroupBox('MPPTs')
        mpptLayout = QGridLayout()
        mpptLayout.addWidget(QLabel('#'),0,0)
        mpptLayout.addWidget(QLabel('Out Current'),0,1,1,2)
        for i in range(4):
            pass
            # mpptLayout.addWidget(QLabel(str(i)),i+1,0)
            # mpptLayout.addWidget(self.mppts[i],i+1,1)
            # mpptLayout.addWidget(QLabel('A'),i+1,2)
        self.MPPTTotal = QLabel('0.000')
        # mpptLayout.addWidget(QLabel('Total'),5,0)
        # mpptLayout.addWidget(self.MPPTTotal,5,1)
        # mpptLayout.addWidget(QLabel('A'),5,2)
        mpptWidget.setLayout(mpptLayout)

        # -----------------------------------------------------------
        #   Graphs
        # -----------------------------------------------------------
        self.speedGraph = QGroupBox('Speed (mph)')
        self.speedGraph.setFlat(True)
        self.speedGraph.setMinimumWidth(150)
        self.speedFig = plt.figure()
        self.speedCanvas = FigureCanvas(self.speedFig)
        self.speedLayout = QVBoxLayout()
        self.speedLayout.addWidget(self.speedCanvas)
        self.speedGraph.setLayout(self.speedLayout)

        self.grossInstant = QGroupBox('Gross Instant Power')
        self.grossInstant.setFlat(True)
        self.grossInstant.setMinimumWidth(150)
        self.grossInstantFig = plt.figure()
        self.grossInstantCanvas = FigureCanvas(self.grossInstantFig)
        self.grossInstantLayout = QVBoxLayout()
        self.grossInstantLayout.addWidget(self.grossInstantCanvas)
        self.grossInstant.setLayout(self.grossInstantLayout)

        self.arrayPowerGraph = QGroupBox('Array Power')
        self.arrayPowerGraph.setFlat(True)
        self.arrayPowerGraph.setMinimumWidth(150)
        self.arrayPowerFig = plt.figure()
        self.arrayPowerCanvas = FigureCanvas(self.arrayPowerFig)
        self.arrayPowerLayout = QVBoxLayout()
        self.arrayPowerLayout.addWidget(self.arrayPowerCanvas)
        self.arrayPowerGraph.setLayout(self.arrayPowerLayout)

        self.voltageGraph = QGroupBox('Total Battery Voltage (V)')
        self.voltageGraph.setFlat(True)
        self.voltageGraph.setMinimumWidth(150)
        self.voltageFig = plt.figure()
        self.voltageCanvas = FigureCanvas(self.voltageFig)
        self.voltageLayout = QVBoxLayout()
        self.voltageLayout.addWidget(self.voltageCanvas)
        self.voltageGraph.setLayout(self.voltageLayout)

        self.batteryChargeGraph = QGroupBox('Total Battery Voltage (V)')
        self.batteryChargeGraph.setFlat(True)
        self.batteryChargeGraph.setMinimumWidth(150)
        self.batteryChargeFig = plt.figure()
        self.batteryChargeCanvas = FigureCanvas(self.batteryChargeFig)
        self.batteryChargeLayout = QVBoxLayout()
        self.batteryChargeLayout.addWidget(self.batteryChargeCanvas)
        self.batteryChargeGraph.setLayout(self.batteryChargeLayout)

        # -----------------------------------------------------------
        #   Logo
        # -----------------------------------------------------------
        logo = QLabel()
        logo.setGeometry(0,0,100,100)
        logo.setPixmap(QPixmap("img/logo1.png")) 

        # -----------------------------------------------------------
        #   Main Layout
        # -----------------------------------------------------------
        self.main_frame1 = QWidget()
        main_layout1 = QVBoxLayout()
        main_layout1.addWidget(batteryWidget)
        main_layout1.addWidget(portname_groupbox)
        main_layout1.addStretch(1)
        self.main_frame1.setLayout(main_layout1)

        self.main_frame2 = QWidget()
        main_layout2 = QVBoxLayout()
        main_layout2.addWidget(timeWidget)
        main_layout2.addWidget(self.motorControllerWidget)
        main_layout2.addWidget(mpptWidget)
        main_layout2.addWidget(batteryStatsWidget)
        main_layout2.addStretch(1)
        # main_layout2.addWidget(logo)
        self.main_frame2.setLayout(main_layout2)

        self.graphs_frame = QWidget()
        graphs_layout = QVBoxLayout()
        graphs_layout.addWidget(self.speedGraph)
        graphs_layout.addWidget(self.grossInstant)
        # graphs_layout.addWidget(self.mCurrentGraph)
        graphs_layout.addWidget(self.arrayPowerGraph)
        # graphs_layout.addWidget(self.aCurrentGraph)
        graphs_layout.addWidget(self.voltageGraph)
        graphs_layout.addWidget(self.batteryChargeGraph)
        self.graphs_frame.setLayout(graphs_layout)
        
        self.main_frame = QWidget()
        main_layout = QHBoxLayout()
        main_layout.addWidget(self.main_frame1)
        main_layout.addWidget(self.main_frame2)
        main_layout.addWidget(self.graphs_frame)
        self.main_frame.setLayout(main_layout)

        self.setCentralWidget(self.main_frame)
        self.set_actions_enable_state()

        self.secTimer = QTimer()
        self.secTimer.timeout.connect(self.writeLog)
        self.secTimer.timeout.connect(self.updateTime)
        self.secTimer.timeout.connect(self.updateGraphs)
        self.secTimer.timeout.connect(self.performCalculations)
        self.secTimer.start(1000)

    def updateGraphs(self):
        pass
        # Draw the speed over time
        # speedData = self.motorControllerWidget.getSpeeds()
        # times = []
        # speeds = []
        # min_time = 1e30
        # max_time = 0
        # for item in speedData:
        #     times.append( item[0] )
        #     speeds.append( item[1] )
        #     if item[0] < min_time:
        #         min_time = item[0]
        #     if item[0] > max_time:
        #         max_time = item[0]
        # axSpeed = self.speedFig.add_axes([.1, 0, 1, 1])
        # axSpeed.hold(False)
        # axSpeed.plot_date(times, speeds, '')
        # self.speedCanvas.draw()

        # # TODO: (Ethan) Implement graphing of Gross Instance Data
        # gross_instant = [item[1] for item in self.gross_instant_deque]
        # times = [dt.utcfromtimestamp(item[0]) for item in self.gross_instant_deque]
        # axGrossInstant = self.grossInstantFig.add_axes([.1,0,1,1])
        # axGrossInstant.hold(False)
        # axGrossInstant.plot_date(times, gross_instant, '')
        # self.grossInstantCanvas.draw()

        # # TODO: (Ethan) Implement graphing of Array Power
        # array_power = [item[1] for item in self.array_power_deque]
        # times = [dt.utcfromtimestamp(item[0]) for item in self.array_power_deque]
        # axarrayPower = self.arrayPowerFig.add_axes([.1,0,1,1])
        # axarrayPower.hold(False)
        # axarrayPower.plot_date(times, array_power, '')
        # self.arrayPowerCanvas.draw()

        # # TODO: (Ethan) Implement graphing of Battery Charge Remaining
        # battery_charges = [item[1] for item in self.battery_charge_deque]
        # times = [dt.utcfromtimestamp(item[0]) for item in self.battery_charge_deque]
        # axbatteryCharge = self.batteryChargeFig.add_axes([.1,0,1,1])
        # axbatteryCharge.hold(False)
        # axbatteryCharge.plot_date(times, battery_charges, '')
        # self.batteryChargeCanvas.draw()

        # # Draw total votlage over time
        # voltages = [item[1] for item in self.total_voltage_deque]
        # times = [dt.utcfromtimestamp(item[0]) for item in self.total_voltage_deque]
        # axVoltages = self.voltageFig.add_axes([.1,0,1,1])
        # axVoltages.hold(False)
        # axVoltages.plot_date(times, voltages, '')
        # self.voltageCanvas.draw()

    def updateTime(self):
        self.curTime = time.time()
        self.timeSinceStart = self.curTime - self.startTime
        self.currentTime.setText(time.strftime("%H:%M:%S", time.localtime(self.curTime)))
        self.timeSince.setText(time.strftime("%H:%M:%S", time.gmtime(self.timeSinceStart)))
        # print time.strftime("%d.%m.%Y.%H:%M:%S")

    def resetCalculations(self):
        self.array_power_deque = []
        self.total_voltage_deque = []
        self.speed_deque = []
        self.gross_instant_deque = []
        self.battery_charge_deque = []

    def toggleLogging(self):
        if self.logging_active:
            self.logging_active = False
            self.stop_logging()
            self.loggingToggle.setText('Start Logging')
        else:
            self.start_logging()

    def on_start_stop(self):
        if self.monitor_active:
            self.on_stop()
        else:
            self.on_start()

    def make_data_box(self, name):
        label = QLabel(name)
        qle = QLineEdit()
        qle.setEnabled(False)
        qle.setFrame(True)
        return (label, qle)

    def create_menu(self):
        self.file_menu = self.menuBar().addMenu("&File")

        selectport_action = self.create_action("Select COM &Port...",
            shortcut="Ctrl+P", slot=self.on_select_port, tip="Select a COM port")
        self.start_action = self.create_action("&Start monitor",
            shortcut="Ctrl+M", slot=self.on_start, tip="Start the data monitor")
        self.stop_action = self.create_action("&Stop monitor",
            shortcut="Ctrl+T", slot=self.on_stop, tip="Stop the data monitor")
        exit_action = self.create_action("E&xit", slot=self.close, 
            shortcut="Ctrl+X", tip="Exit the application")

        self.start_action.setEnabled(False)
        self.stop_action.setEnabled(False)
        
        self.add_actions(self.file_menu,
            (   selectport_action, self.start_action, self.stop_action,
                None, exit_action))
            
        self.help_menu = self.menuBar().addMenu("&Help")
        about_action = self.create_action("&About", 
            shortcut='F1', slot=self.on_about, 
            tip='About the monitor')
        
        self.add_actions(self.help_menu, (about_action,))

    def create_status_bar(self):
        self.status_text = QLabel('Monitor idle')
        self.statusBar().addWidget(self.status_text, 1)

    def set_actions_enable_state(self):
        if self.portname.text() == '':
            start_enable = stop_enable = False
        else:
            start_enable = not self.monitor_active
            stop_enable = self.monitor_active
        
        self.start_action.setEnabled(start_enable)
        self.stop_action.setEnabled(stop_enable)

    def add_actions(self, target, actions):
        for action in actions:
            if action is None:
                target.addSeparator()
            else:
                target.addAction(action)

    def create_action(  self, text, slot=None, shortcut=None, 
                        icon=None, tip=None, checkable=False, 
                        signal="triggered()"):
        action = QAction(text, self)
        if icon is not None:
            action.setIcon(QIcon(":/%s.png" % icon))
        if shortcut is not None:
            action.setShortcut(shortcut)
        if tip is not None:
            action.setToolTip(tip)
            action.setStatusTip(tip)
        if slot is not None:
            self.connect(action, SIGNAL(signal), slot)
        if checkable:
            action.setCheckable(True)
        return action

    def on_select_port(self):
        # ports = list(enumerate_serial_ports())
        ports = ["/dev/ttyIN7","/dev/ttyUSB0","/dev/ttyUSB1"]
        if len(ports) == 0:
            QMessageBox.critical(self, 'No ports',
                'No serial ports found')
            return
        
        item, ok = QInputDialog.getItem(self, 'Select a port',
                    'Serial port:', ports, 0, False)
        
        if ok and not item.isEmpty():
            self.portname.setText(item)            
            self.set_actions_enable_state()

    def on_start(self):
        """ Start the monitor: com_monitor thread and the update
            timer
        """
        if self.com_monitor is not None or self.portname.text() == '':
            return
        
        self.data_q = Queue.Queue()
        self.error_q = Queue.Queue()
        self.com_monitor = ComMonitorThread(
            self.data_q,
            self.error_q,
            full_port_name(str(self.portname.text())),
            19200)
        self.com_monitor.start()
        
        com_error = get_item_from_queue(self.error_q)
        if com_error is not None:
            QMessageBox.critical(self, 'ComMonitorThread error',
                com_error)
            self.com_monitor = None

        self.monitor_active = True
        self.set_actions_enable_state()
        
        self.timer = QTimer()
        self.connect(self.timer, SIGNAL('timeout()'), self.on_timer)
        
        self.timer.start(0.2)
        
        self.status_text.setText('Monitor running')

        self.startStop.setText('Stop  Monitor')

        self.start_logging()

    def start_logging(self):
        if not os.path.exists('logs'):
            os.makedirs('logs')
        self.logging_active = True
        self.loggingToggle.setText('Stop  Logging')
        fileName = "logs/log" + time.strftime("%d-%m-%Y-%H-%M-%S") + ".csv"
        self.logFile = open(fileName, "a+")
        self.logFile.write("timestamp,speed,motor current,array current,battery current,")
        for i in range(4):
            self.logFile.write("mppt[%d]," %i)
        for i in range(20):
            self.logFile.write("batman[%d]," %i)
        for i in range(20):
            self.logFile.write("robin[%d]," %i)
        self.logFile.write("\n")

    def writeLog(self):
        if self.logging_active:
            pass
            # TODO (Ethan): Implement later
            # self.logFile.write(time.strftime("%d-%m-%Y-%H:%M:%S,"))
            # self.logFile.write(str(self.motorControllerWidget.getSpeed())+",")
            # self.logFile.write(str(self.motorControllerWidget.getCurrent())+",")
            # self.logFile.write(str(self.getArrayCurrent())+",")
            # self.logFile.write(str(self.getBatteryCurrent())+",")
            # for mppt in self.mppts:
            #     self.logFile.write(str(mppt.getOutCurrent())+",")
            # for battery in self.batteries:
            #     # for battery in pack:
            #     self.logFile.write(str(battery.getVoltage())+",")
            # self.logFile.write("\n")

    def stop_logging(self):
        self.logging_active = False
        self.loggingToggle.setText('Start Logging')
        self.logFile.close()

    def on_stop(self):
        """ Stop the monitor
        """
        if self.com_monitor is not None:
            self.com_monitor.join(0.01)
            self.com_monitor = None

        self.monitor_active = False
        self.timer.stop()
        self.set_actions_enable_state()
        
        self.status_text.setText('Monitor idle')

        self.startStop.setText('Start Monitor')

        self.stop_logging()

    def on_about(self):
        msg = __doc__
        QMessageBox.about(self, "About the demo", msg.strip())

    def on_timer(self):
        """ Executed periodically when the monitor update timer
            is fired.
        """
        self.read_serial_data()
        self.update_monitor()

    def read_serial_data(self):
        """ Called periodically by the update timer to read data
            from the serial port.
        """
        qdata = list(get_all_from_queue(self.data_q))
        if len(qdata) > 0:
            data = qdata[-1][0]
            self.livefeed.add_data(data)

    def update_monitor(self):
        """ The the network protocal is json objects sent over
            telemetry with a new line character \r\n as the
            delemeter between messages
            {
                "message_id": "(bat_temp|bat_volt|motor)",
                ...
            }
            Updates the state of the monitor window with new 
            data. The livefeed is used to find out whether new
            data was received since the last update. If not, 
            nothing is updated.
        """
        if self.livefeed.has_new_data:
            data = self.livefeed.read_data().split("\r\n")

            for message in data:
                if( len(message) > 0 ):
                    # print(message)
                    self.running_log.append(message)
                    try:
                        json_obj = json.loads(message)
                        if( json_obj["message_id"] == 'bat_temp' ):
                            self.updateBatteries( json_obj );

                        elif( json_obj["message_id"] == "bat_volt" ):
                            self.updateBatteries( json_obj );

                        elif( json_obj["message_id"] == "motor" ):
                            self.updateMotor( json_obj );
                    except:
                        self.running_log.append("Failed to load json message: " + message)
                        print("Failed to load json message: ",message)

            #  TODO: (Ethan) Update mppts

            # elif MPPTDataRX.match(data):
            #     info = MPPTDataRX.search(data).groups()
            #     self.mppts[int(info[0])].setInVoltage(int(info[1]))
            #     self.mppts[int(info[0])].setInCurrent(int(info[2]))
            #     self.mppts[int(info[0])].setOutVoltage(int(info[3]))
            #     self.updateMPPT()

            # TODO: (Ethan) Update Battery error checking code

            # elif BPSbadRX.match(data):
            #     info = BPSbadRX.search(data).groups()
            #     box = int(info[0])
            #     cell = int(info[1])
            #     print "WARNING BPS TIMEOUT number [%d][%2d]" %(box, cell)
            #     self.batteries[box][cell].setBad()

            # else:
            #     info =  "Could not match input '" + data + "'"
            #     print info

            # print data #debug

    def updateMPPT(self):
        total = 0.0
        for mppt in self.mppts:
            total += mppt.getOutCurrent()
        self.MPPTTotal.setText("%.3f" %total)
        self.arrayCurrent.append((total, time.time()))

    def getArrayCurrent(self):
        return self.arrayCurrent[-1][0] if len(self.arrayCurrent) > 0 else 0

    def getArrayCurrents(self):
        return self.arrayCurrent

    def getBatteryCurrent(self):
        return self.batteryCurrent[-1][0] if len(self.batteryCurrent) > 0 else 0

    def updateMotor(self, json_obj):
        self.motorControllerWidget.setSpeed( float(json_obj["S"]) )
        self.motorControllerWidget.setCurrent( float(json_obj["I"]) )
        self.motorControllerWidget.setEnergy( float(json_obj["M"]) )
        self.motorControllerWidget.setAmpHours( float(json_obj["amp_hours"]) )
        self.motorControllerWidget.setWattSec( float(json_obj["watt_sec"]) )
        self.motorControllerWidget.setOdometer( float(json_obj["odometer"]) )

    #Identifies json object as an identifier for temperature
    #or voltage. From there it identifies it as batman or robin
    #data and updates the "Batteries" section on the GUI.
    def updateBatteries(self, j_object):
    
        if j_object["message_id"] == "bat_temp":
            if j_object["name"] == "0":
                self.BatmanAvgTemp = float(j_object["Tavg"])
                self.BatmanTAvg.setText('%.2f' %self.BatmanAvgTemp)
                self.BatmanTMin.setText('%.2f' %float(j_object["Tmin"]))
                self.BatmanTMax.setText('%.2f' %float(j_object["Tmax"]))
            else:
                self.RobinAvgTemp = float(j_object["Tavg"])
                self.RobinTAvg.setText('%.2f' %self.RobinAvgTemp)
                self.RobinTMin.setText('%.2f' %float(j_object["Tmin"]))
                self.RobinTMax.setText('%.2f' %float(j_object["Tmax"]))
        elif j_object["message_id"] == "bat_volt":
            if j_object["name"] == "0":
                self.BatmanAvgVoltage = float(j_object["Vavg"])
                self.BatmanMinVoltage = float(j_object["Vmin"])
                self.BatmanAvgCurrent = float(j_object["BC"])
                self.BatmanVAvg.setText('%.2f V' %self.BatmanAvgVoltage)
                self.BatmanVMax.setText('%.2f V' %float(j_object["Vmax"]))
                self.BatmanVMin.setText('%.2f V' %float(j_object["Vmin"]))
                self.BatmanBC.setText('%.2f A' %self.BatmanAvgCurrent)
                self.BatmanWattSec = float(j_object["watt_sec"])
            else:
                self.RobinAvgVoltage = float(j_object["Vavg"])
                self.RobinMinVoltage = float(j_object["Vmin"])
                self.RobinAvgCurrent = float(j_object["BC"])
                self.RobinVAvg.setText('%.2f V' %self.RobinAvgVoltage)
                self.RobinVMax.setText('%.2f V' %float(j_object["Vmax"]))
                self.RobinVMin.setText('%.2f V' %float(j_object["Vmin"]))
                self.RobinBC.setText('%.2f A' %self.RobinAvgCurrent)
                self.RobinWattSec = float(j_object["watt_sec"])

    def performCalculations(self):
        loook_up = ((0                  ,0),
        (5.4022988505747378 ,3.1013307984790872),
        (5.8620689655172526 ,3.1279467680608364),
        (7.8544061302682167 ,3.1819391634980985),
        (10.000000000000014 ,3.1963878326996196),
        (11.839080459770145 ,3.2032319391634978),
        (13.984674329501942 ,3.2070342205323192),
        (15.977011494252892 ,3.2176806083650189),
        (17.969348659003842 ,3.2252851711026613),
        (19.961685823754806 ,3.23212927756654  ),
        (21.800766283524922 ,3.238212927756654 ),
        (23.94636015325672  ,3.2450570342205323),
        (26.091954022988517 ,3.2503802281368821),
        (27.931034482758633 ,3.2541825095057035),
        (29.923371647509597 ,3.2595057034220529),
        (32.068965517241395 ,3.2655893536121674),
        (33.908045977011511 ,3.2716730038022814),
        (36.053639846743309 ,3.2792775665399239),
        (38.045977011494273 ,3.2868821292775663),
        (40.038314176245237 ,3.2899239543726235),
        (42.030651340996172 ,3.2906844106463877),
        (44.022988505747136 ,3.2906844106463877),
        (45.862068965517253 ,3.2906844106463877),
        (47.701149425287369 ,3.2899239543726235),
        (50.000000000000014 ,3.2906844106463877),
        (51.992337164750964 ,3.2922053231939161),
        (53.83141762452108  ,3.2922053231939161),
        (55.977011494252878 ,3.2922053231939161),
        (57.969348659003842 ,3.2922053231939161),
        (59.961685823754792 ,3.2929657794676803),
        (61.954022988505756 ,3.2929657794676803),
        (63.946360153256705 ,3.2944866920152092),
        (65.938697318007669 ,3.2960076045627376),
        (68.084291187739467 ,3.2967680608365018),
        (69.923371647509583 ,3.3013307984790874),
        (71.915708812260533 ,3.3104562737642587),
        (73.9080459770115   ,3.3256653992395435),
        (75.900383141762461 ,3.3309885931558934),
        (77.892720306513411 ,3.3317490494296575),
        (80.038314176245223 ,3.3317490494296575),
        (81.877394636015325 ,3.3325095057034222),
        (83.869731800766289 ,3.3332699619771864),
        (85.862068965517238 ,3.3340304182509506),
        (87.8544061302682   ,3.3347908745247148),
        (90.0                 ,3.3347908745247148),
        (91.99233716475095  ,3.3347908745247148),
        (93.83141762452108  ,3.3363117870722432),
        (95.82375478927203  ,3.3385931558935362),
        (97.969348659003828 ,3.367490494296578 ),
        (98.429118773946357 ,3.3986692015209128),
        (100.0 ,3.5))

        odometer = self.motorControllerWidget.getOdometer() / 320 # 3600 - in 2meters so one unit equals 2 meters
        print(odometer)
        if odometer > 0.0000001:
            motor_current = float((self.motorControllerWidget.getCurrents())[-1][0])
            motor_current_time = (self.motorControllerWidget.getCurrents())[1]
            motor_watt_hours = self.motorControllerWidget.getWattSec() / 3600
            battery_watt_hours = (self.BatmanWattSec + self.RobinWattSec) / 3600
            batman_average_voltage = self.BatmanAvgVoltage
            robin_average_voltage = self.RobinAvgVoltage
            total_battery_voltage = batman_average_voltage*20+robin_average_voltage*20
            average_battery_current = (self.BatmanAvgCurrent + self.RobinAvgCurrent)/2
            total_battery_voltage = batman_average_voltage*20+robin_average_voltage*20
            self.total_voltage_deque.append([motor_current_time, total_battery_voltage])
            average_battery_temperature = (self.BatmanAvgTemp + self.RobinAvgTemp)/2

            #ARRAY POWER
            # array_power = ((motor_current-batman_average_voltage)*batman_average_voltage*20)+((motor_current-robin_average_voltage)*robin_average_voltage*20)
            array_power = ((motor_current - average_battery_current ) * total_battery_voltage)
            self.array_power_deque.append([motor_current_time, array_power])

            #BATTERY ONLY RUNTIME (SECONDS)
            battery = Battery(0, 0)
            min_battery_voltage = self.BatmanMinVoltage if (self.BatmanMinVoltage < self.RobinMinVoltage) else self.RobinMinVoltage
            average_current = (self.BatmanAvgCurrent + self.RobinAvgCurrent) / 2
            average_temperature = (self.BatmanAvgTemp + self.RobinAvgTemp) / 2
            state_of_charge = battery.Update( min_battery_voltage, average_current, average_temperature, getCurrentTime() )

            gross_instant_power = total_battery_voltage * motor_current

            self.gross_instant_deque.append( gross_instant_power )

            average_gross_instant = sum( self.gross_instant_deque ) / len( self.gross_instant_deque )

            gross_average_power = sum(self.gross_instant_deque ) / len(self.gross_instant_deque) #this should go to the screen
            avergageeNetPower = gross_average_power - numpy.mean( self.array_power_deque, 1)
            grossInstantPower = self.gross_instant_deque[-1]

            battery_charge_remaining = 0
            last = 0
            for arr in loook_up:
                if( arr[0] > state_of_charge ):
                    break
                battery_charge_remaining += (arr[0] - last)*arr[1]
                last = arr[0]

            #calc_battery_charge_remaining appears to not be defined

            # bad CH  #battery_runtime =  *(battery.updateBatteryRuntime(total_battery_voltage, average_battery_current, average_battery_temperature, motor_current_time)/100)/(average_battery_current)
            battery_runtime = battery_charge_remaining / average_gross_instant*3600 # ? suppose to be gross_power?
            self.calc_battery_run_time_remaining.setText('%.2f W' %battery_runtime)

            # solar_energy_remaining = energyRemainingInDay( getCurrentTime() / 3600000, dateTypeCode)
            solar_energy_remaining = energyRemainingInDay( getCurrentTime() / 3600000, 1)

                    #battery_runtimes is currently stored in hours, convert as approparate (converted in battery_runtime function)
            #BATTERY ONLY RANGE
            battery_range = battery_runtime*numpy.mean( self.speed_deque, 1 )
            self.calc_battery_range.setText('%.2f W' %battery_range)

            
            solar_runtime = (solar_energy_remaining + battery_charge_remaining) / average_gross_instant # ? suppose to be gross_power?

            solar_range = solar_runtime  * numpy.mean( self.speed_deque )

            # BATTERY AND SOLAR RUNTIME

            self.calc_array_power.setText('%.2f W' %array_power)
            self.calc_gross_watt_hours.setText( self.motorControllerWidget.getWattSec() / 3600  );
            self.calc_net_watt_hours.setText( (self.BatmanWattSec + self.RobinWattSec)  / 3600);
            self.calc_average_speed.setText( mean(speed_deque) );
            self.calc_average_gross_power.setText( motor_watt_hours / odometer );
            self.calc_average_net_power.setText( (battery_watt_hours + motor_watt_hours) / odometer );
            # self.calc_gross_average_power.setText(  battery watt hour since last reset button / time since last reset button ); # - since last reset button
            # self.calc_gross_average_watt.setText( ttery watt hour since last reset button  ); #watt hourslast reset button, m
            self.calc_battery_run_time_remaining.setText( battery_runtime );
            self.calc_battery_range.setText( battery_range ); 
            self.calc_battery_solar_run_time.setText( battery_runtime );
            self.calc_battery_solar_distance.setText( solar_range );
            self.calc_battery_charge_remaining.setText( solar_runtime );
            self.calc_solar_energy_remaining.setText( solar_energy_remaining );

        motor_current = float((self.motorControllerWidget.getCurrent())[0])
        struct_time = time.strptime((self.motorControllerWidget.getCurrent())[1], '%T')
        batman_average_voltage = self.BatmanAvgVoltage
        robin_average_voltage = self.RobinAvgVoltage
        average_battery_current = (self.BatmanAvgCurrent + self.RobinAvgCurrent)/2
        total_battery_voltage = batman_average_voltage*20+robin_average_voltage*20
        average_battery_temperature = (self.BatmanAvgTemp + self.RobinAvgTemp)/2

        #ARRAY POWER
        array_power = ((motor_current-batman_average_voltage)*batman_average_voltage*20)+((motor_current-robin_average_voltage)*robin_average_voltage*20)
        self.array_power_list.append([struct_time.hour+struct_time.minute/60+struct_time.second/3600, motor_current])
        self.calc_array_power.setText('%.2f W' %array_power)

        #MOTOR POWER
        motor_power = motor_current*total_battery_voltage
        self.calc_motor_power.setText('%.2f W' %motor_power)

        #BATTERY AND SOLAR RUNTIME (check)
        battery_solar_runtime = battery_runtime+solar_runtime
        self.calc_battery_solar_run_time.setText('%.2f W' %battery_solar_runtime)

        #BATTERY AND SOLAR RANGE (check)
        battery_solar_range = battery_range+solar_range
        self.calc_battery_solar_distance.setText('%.2f W' %battery_solar_range)

    def elevationAngle(self, time):
        solar_noon_hour = 1.5
        latitude = 30.136416
        declination = 21.08
        out_value = arcsine(cos((time-solar_noon_hour)*15)*
                   cos(declination)*
                   cos(latitude)+
                   sin(declination)*
                   sin(latitude))
        return out_value;

    def powerWhileDriving(self, time):
        direct_max_power = 900
        indirect_max_Power = 100
        elev_angle = elevationAngle(datetime.now().time().hour+datetime.now().time().minute/60+datetime.now().time().second/3600)
        return direct_max_power * (abs(cos(90-elev_angle))^1.3)+indirect_max_power*((180-elev_angle)/180)

    def energyRemainingInDay(self, time, dayTypeCode):
        dt = .05
        #Military Time (24 hour time)
        stop_to_charge_time = 17
        stop_charging_in_eve = 20
        start_charge_in_morning = 7
        start_racing_in_morning = 9

        energy = 0

        i = time
        while True:
            if i < stop_charging_in_eve:
                energy += powerWhileDriving(i) * dt
                i += dt
            else:
                break

        if dateTypeCode == 0:
            return energy
        
        i = stop_to_charge_time
        while True:
            if i < stop_charging_in_eve:
                energy += powerWhileDriving(i) * dt
                i += dt
            else:
                break

        if dayTypeCode == 1:
            return energy

        i = start_charge_in_morning
        while True:
            if i < start_racing_in_morning:
                energy += powerWhileDriving(i) * dt
                i += dt

        return energy


def main():
    app = QApplication(sys.argv)
    form = PlottingDataMonitor()
    form.show()
    app.exec_()

if __name__ == "__main__":
    main()
