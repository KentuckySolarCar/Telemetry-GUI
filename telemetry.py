"""
UKY Solar Car Telemetry Program
Stephen Parsons (stephen.parsons@uky.edu)

https://github.com/KentuckySolarCar/Telemetry-GUI
"""

import random, sys, os, Queue, re, time, operator, json
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

class MotorController(QGroupBox):
    def __init__(self, parent=None):
        super(MotorController, self).__init__(parent)
        self.speed = []
        self.current = []
        self.energy = []
        self.averageSpeed = 0
        self.conversion = 0.223693629
        self.checkFromAv = 0
        self.checkFromE = 0

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
        self.speed.append([inSpeed*self.conversion, time.time()])
        self.tSpeed.setText('%.2f' %self.speed[-1][0])
        self.calcAverageSpeed()

    def setCurrent(self, inCurrent):
        self.current.append((inCurrent, time.time()))
        self.tCurrent.setText('%.2f' %self.current[-1][0])

    def setEnergy(self, energy):
        self.energy.append((energy, time.time()))
        self.calcEnergy()

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
        return 0 if not self.current else self.current[-1][0]

    def getSpeeds(self):
        return self.speed

    def getCurrents(self):
        return self.current


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

        self.batteries = [ Battery(), Battery() ]; # { Batman, Robin }

        # self.batteries = [[],[]]
        # for i in range(20):
        #     self.batteries[0].append(Battery()) # (ET) Batman
        #     self.batteries[1].append(Battery()) # (ET) Robin

        self.mppts = []
        for i in range(4):
            self.mppts.append(MPPT(i))

        self.batteryCurrent = []
        self.arrayCurrent = []
        self.totalVoltage = []

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
        calcualtionLayout = QGridLayout()

        # Array Power
        self.calc_array_power = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Array Power"), 0, 0)
        calcualtionLayout.addWidget(self.calc_array_power, 0, 1)
        # calcualtionLayout.addWidget(QLabel(""), 0, 2)

        # Gross Watt*Hours
        self.calc_gross_watt_hours = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Gross Watt*Hours"), 1, 0)
        calcualtionLayout.addWidget(self.calc_gross_watt_hours, 1, 1)
        # calcualtionLayout.addWidget(QLabel("B"), 1, 2)

        # 
        self.calc_net_watt_hours = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Net Watt*Hours"), 2, 0)
        calcualtionLayout.addWidget(self.calc_net_watt_hours, 2, 1)
        # calcualtionLayout.addWidget(QLabel("B"), 2, 2)

        # 
        self.calc_average_speed = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Average Speed"), 3, 0)
        calcualtionLayout.addWidget(self.calc_average_speed, 3, 1)
        # calcualtionLayout.addWidget(QLabel("B"), 3, 2)

        # 
        self.calc_average_gross_power = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Average Gross Power"), 4, 0)
        calcualtionLayout.addWidget(self.calc_average_gross_power, 4, 1)
        # calcualtionLayout.addWidget(QLabel("B"), 4, 2)

        # 
        self.calc_average_net_power = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Average Net Power"), 5, 0)
        calcualtionLayout.addWidget(self.calc_average_net_power, 5, 1)
        # calcualtionLayout.addWidget(QLabel("B"), 5, 2)

        # 
        self.calc_gross_average_power = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Gross Average Power"), 6, 0)
        calcualtionLayout.addWidget(self.calc_gross_average_power, 6, 1)
        # calcualtionLayout.addWidget(QLabel(""), 6, 2)

        # 
        self.calc_gross_average_watt = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Gross Average Watt Hour/Mile"), 7, 0)
        calcualtionLayout.addWidget(self.calc_gross_average_watt, 7, 1)
        # calcualtionLayout.addWidget(QLabel(""), 7, 2)

        # 
        self.calc_battery_run_time_remaining = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Battery Only Run-time Remaining"), 8, 0)
        calcualtionLayout.addWidget(self.calc_battery_run_time_remaining, 8, 1)
        # calcualtionLayout.addWidget(QLabel(""), 8, 2)

        # 
        self.calc_battery_range = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Battery Only Range"), 9, 0)
        calcualtionLayout.addWidget(self.calc_battery_range, 9, 1)
        # calcualtionLayout.addWidget(QLabel(""), 9, 2)

        # 
        self.calc_battery_solar_run_time = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Battery and Solar Run-time"), 10, 0)
        calcualtionLayout.addWidget(self.calc_battery_solar_run_time, 10, 1)
        # calcualtionLayout.addWidget(QLabel(""), 10, 2)

        # 
        self.calc_battery_solar_distance = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Battery and Solar Range"), 11, 0)
        calcualtionLayout.addWidget(self.calc_battery_solar_distance, 11, 1)
        # calcualtionLayout.addWidget(QLabel(""), 11, 2)

        # 
        self.calc_battery_charge_remaining = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Battery Charge Remaining"), 12, 0)
        calcualtionLayout.addWidget(self.calc_battery_charge_remaining, 12, 1)
        # calcualtionLayout.addWidget(QLabel(""), 12, 2)

        # 
        self.calc_solar_energy_remaining = QLabel("value")
        calcualtionLayout.addWidget(QLabel("Solar Energy Remaining in Day"), 13, 0)
        calcualtionLayout.addWidget(self.calc_solar_energy_remaining, 13, 1)
        # calcualtionLayout.addWidget(QLabel(""), 13, 2)

        calculationWidget.setLayout(calcualtionLayout);
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

        self.tBatteryCurrent = QLabel('0.00 A')
        # self.tBatteryAverage = QLabel('0.00 V')
        # self.tBatteryHigh = QLabel('0.00 V (#)')
        # self.tBatteryLow = QLabel('0.00 V (#)')
        batteryStatsLayout.addWidget(QLabel('Current:'),4,0)
        batteryStatsLayout.addWidget(self.tBatteryCurrent,4,1)
        # batteryStatsLayout.addWidget(QLabel('Average:'),1,4)
        # batteryStatsLayout.addWidget(self.tBatteryAverage,1,5)
        # batteryStatsLayout.addWidget(QLabel('High:'),2,4)
        # batteryStatsLayout.addWidget(self.tBatteryHigh,2,5)
        # batteryStatsLayout.addWidget(QLabel('Low:'),3,4)
        # batteryStatsLayout.addWidget(self.tBatteryLow,3,5)

        self.tBatmanAverage = QLabel('0.00 V')
        self.tBatmanHigh = QLabel('0.00 V (#)')
        self.tBatmanLow = QLabel('0.00 V (#)')
        batteryStatsLayout.addWidget(QLabel('Batman'),0,0,1,2, Qt.AlignHCenter)
        batteryStatsLayout.addWidget(QLabel('Average:'),1,0)
        batteryStatsLayout.addWidget(self.tBatmanAverage,1,1)
        batteryStatsLayout.addWidget(QLabel('High:'),2,0)
        batteryStatsLayout.addWidget(self.tBatmanHigh,2,1)
        batteryStatsLayout.addWidget(QLabel('Low:'),3,0)
        batteryStatsLayout.addWidget(self.tBatmanLow,3,1)

        self.tRobinAverage = QLabel('0.00 V')
        self.tRobinHigh = QLabel('0.00 V (#)')
        self.tRobinLow = QLabel('0.00 V (#)')
        batteryStatsLayout.addWidget(QLabel('Robin'),0,2,1,2, Qt.AlignHCenter)
        batteryStatsLayout.addWidget(QLabel('Average:'),1,2)
        batteryStatsLayout.addWidget(self.tRobinAverage,1,3)
        batteryStatsLayout.addWidget(QLabel('High:'),2,2)
        batteryStatsLayout.addWidget(self.tRobinHigh,2,3)
        batteryStatsLayout.addWidget(QLabel('Low:'),3,2)
        batteryStatsLayout.addWidget(self.tRobinLow,3,3)

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
            mpptLayout.addWidget(QLabel(str(i)),i+1,0)
            mpptLayout.addWidget(self.mppts[i],i+1,1)
            mpptLayout.addWidget(QLabel('A'),i+1,2)
        self.MPPTTotal = QLabel('0.000')
        mpptLayout.addWidget(QLabel('Total'),5,0)
        mpptLayout.addWidget(self.MPPTTotal,5,1)
        mpptLayout.addWidget(QLabel('A'),5,2)
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
        self.grossInstantGraph = plt.figure()
        self.grossInstantCanvas = FigureCanvas(self.grossInstantGraph)
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
        self.secTimer.start(1000)

    def updateGraphs(self):
        speedData = self.motorControllerWidget.getSpeeds()
        speeds = [item[0] for item in speedData]
        times = [dt.utcfromtimestamp(item[1]) for item in speedData]
        axSpeed = self.speedFig.add_axes([.1,0,1,1])
        axSpeed.hold(False)
        axSpeed.plot_date(times, speeds, '')
        self.speedCanvas.draw()

        # TODO: (Ethan) Implement graphing of Gross Instance Data
        # grossInstantData = self.motorControllerWidget.getCurrents()
        # grossInstants = [item[0] for item in grossInstantData]
        # times = [dt.utcfromtimestamp(item[1]) for item in grossInstantData]
        # axGrossInstant = self.mCurrentFig.add_axes([.1,0,1,1])
        # axGrossInstant.hold(False)
        # axGrossInstant.plot_date(times, grossInstants, '')
        # self.grossInstant.draw()

        # TODO: (Ethan) Implement graphing of Array Power
        # arrayPowerData = self.motorControllerWidget.getCurrents()
        # arrayPowers = [item[0] for item in arrayPowerData]
        # times = [dt.utcfromtimestamp(item[1]) for item in arrayPowerData]
        # axarrayPower = self.mCurrentFig.add_axes([.1,0,1,1])
        # axarrayPower.hold(False)
        # axarrayPower.plot_date(times, arrayPowers, '')
        # self.arrayPowerGraph.draw()

        # TODO: (Ethan) Implement graphing of Battery Charge Remaining
        # batteryChargeData = self.motorControllerWidget.getCurrents()
        # batteryCharges = [item[0] for item in batteryChargeData]
        # times = [dt.utcfromtimestamp(item[1]) for item in batteryChargeData]
        # axbatteryCharge = self.mCurrentFig.add_axes([.1,0,1,1])
        # axbatteryCharge.hold(False)
        # axbatteryCharge.plot_date(times, batteryCharges, '')
        # self.batteryChargeGraph.draw()

        voltageData = self.totalVoltage
        voltages = [item[0] for item in voltageData]
        times = [dt.utcfromtimestamp(item[1]) for item in voltageData]
        axVoltages = self.voltageFig.add_axes([.1,0,1,1])
        axVoltages.hold(False)
        axVoltages.plot_date(times, voltages, '')
        self.voltageCanvas.draw()

    def updateTime(self):
        self.curTime = time.time()
        self.timeSinceStart = self.curTime - self.startTime
        self.currentTime.setText(time.strftime("%H:%M:%S", time.localtime(self.curTime)))
        self.timeSince.setText(time.strftime("%H:%M:%S", time.gmtime(self.timeSinceStart)))
        # print time.strftime("%d.%m.%Y.%H:%M:%S")

    def resetCalculations(self):
        pass;

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
        ports = list(enumerate_serial_ports())
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
            self.logFile.write(time.strftime("%d-%m-%Y-%H:%M:%S,"))
            self.logFile.write(str(self.motorControllerWidget.getSpeed())+",")
            self.logFile.write(str(self.motorControllerWidget.getCurrent())+",")
            self.logFile.write(str(self.getArrayCurrent())+",")
            self.logFile.write(str(self.getBatteryCurrent())+",")
            for mppt in self.mppts:
                self.logFile.write(str(mppt.getOutCurrent())+",")
            for battery in self.batteries:
                # for battery in pack:
                self.logFile.write(str(battery.getVoltage())+",")
            self.logFile.write("\n")

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
                print(message)
                json_obj = json.loads(message)
                if( json_obj["message_id"] == 'bat_temp' ):
                    pass

                elif( json_obj["message_id"] == "bat_volt" ):
                    pass

                elif( json_obj["message_id"] == "motor" ):
                    pass

            # TODO: (Ethan) Update REGEX to apply to new message format

            # Regular Expressions
            # with data examples commented above

            # V[1][16] = 33421
            batteryVoltageRX = re.compile("^\s*V\[([0-1])\]\[([0-1][0-9])\]\s\=\s(\d+)\s*$")
            # T[1][15] = 26
            batteryTemperatureRX = re.compile("^\s*T\[([0-1])\]\[([0-1][0-9])\]\s\=\s(\d+)\s*$")
            # C = 0
            batteryCurrentRX = re.compile("^\s*C\s=\s(\d+)\s*$")
            # S = 0
            motorControllerVelocityRX = re.compile("^\s*S\s=\s(\d+)\s*$")
            # E = 0
            motorControllerEnergyRX = re.compile("^\s*E\s=\s(\d+)\s*$")
            # W = 0
            motorControllerBusVoltageRX = re.compile("^\s*W\s=\s(\d+)\s*$")
            # M[2] 22 91 140
            MPPTDataRX = re.compile("^\s*M\[([0-3])\]\s(\d+)\s(\d+)\s(\d+)\s*$")
            # #   BPS TIMEOUT number [1][08]= ??
            BPSbadRX = re.compile("^\s*#\s\s\sBPS\sTIMEOUT\snumber\s\[([0-1])\]\[([0-1][0-9])\]= \?\?\s*$")

            # TODO: (Ethan) Fix parsing of Battery data

            if batteryVoltageRX.match(data):
                pass # TEMP: (Ethan)
            #     info = batteryVoltageRX.search(data).groups()
            #     self.batteries[int(info[0])][int(info[1])].setVoltage(int(info[2]))
            #     self.updateBatteries()

            elif batteryTemperatureRX.match(data):
                pass # TEMP: (Ethan)
            #     info = batteryTemperatureRX.search(data).groups()
            #     self.batteries[int(info[0])][int(info[1])].setTemperature(int(info[2]))

            elif batteryCurrentRX.match(data):
                info = batteryCurrentRX.search(data).groups()
                self.batteryCurrent.append((float(info[0]), time.time()))

            elif motorControllerVelocityRX.match(data):
                info = motorControllerVelocityRX.search(data).groups()
                self.motorControllerWidget.setSpeed(int(info[0]))

            elif motorControllerEnergyRX.match(data):
                info = motorControllerEnergyRX.search(data).groups()
                self.motorControllerWidget.setEnergy(int(info[0]))

            elif motorControllerBusVoltageRX.match(data):
                info = motorControllerBusVoltageRX.search(data).groups()
                busVoltage = int(info[0])

            elif MPPTDataRX.match(data):
                info = MPPTDataRX.search(data).groups()
                self.mppts[int(info[0])].setInVoltage(int(info[1]))
                self.mppts[int(info[0])].setInCurrent(int(info[2]))
                self.mppts[int(info[0])].setOutVoltage(int(info[3]))
                self.updateMPPT()

            # TODO: (Ethan) Update Battery error checking code

            # elif BPSbadRX.match(data):
            #     info = BPSbadRX.search(data).groups()
            #     box = int(info[0])
            #     cell = int(info[1])
            #     print "WARNING BPS TIMEOUT number [%d][%2d]" %(box, cell)
            #     self.batteries[box][cell].setBad()

            else:
                info =  "Could not match input '" + data + "'"
                print info

            # print data #debug

    def updateMPPT(self):
        total = 0.0
        for mppt in self.mppts:
            total += mppt.getOutCurrent()
        self.MPPTTotal.setText("%.3f" %total)
        self.arrayCurrent.append((total, time.time()))

    def getArrayCurrent(self):
        return self.arrayCurrent[-1][0] if self.arrayCurrent else 0

    def getArrayCurrents(self):
        return self.arrayCurrent

    def getBatteryCurrent(self):
        return self.batteryCurrent[-1][0] if self.batteryCurrent else 0

    def updateBatteries(self):
        #find highest, lowest, and average values
        highestBatteryValue = 0.0
        highestBatteryModule = 0
        lowestBatteryValue = 4.0
        lowestBatteryModule = 0
        averageBatteryValue = 0.0
        batterySum = 0.0
        for i in range(2):
            voltage = self.batteries[i].getVoltage()
            batterySum = batterySum + voltage
            if voltage > highestBatteryValue:
                highestBatteryValue = voltage
                highestBatteryModule = (i)
            if voltage < lowestBatteryValue:
                lowestBatteryValue = voltage
                lowestBatteryModule = (i)
        averageBatteryValue = batterySum / 2;

        averageBatmanValue = self.batteries[0].getVoltage()

        averageRobinValue = self.batteries[1].getVoltage()

        self.tBatmanAverage.setText('%.2f V' %averageBatmanValue)
        self.tBatmanHigh.setText('%.2f V (%d)' %(highestBatmanValue, highestBatmanModule))
        self.tBatmanLow.setText('%.2f V (%d)' %(lowestBatmanValue, lowestBatmanModule))

        self.tRobinAverage.setText('%.2f V' %averageRobinValue)
        self.tRobinHigh.setText('%.2f V (%d)' %(highestRobinValue, highestRobinModule))
        self.tRobinLow.setText('%.2f V (%d)' %(lowestRobinValue, lowestRobinModule))

        self.tBatteryCurrent.setText('%.2f A' %(self.batteryCurrent[-1][0] if self.batteryCurrent else 0))
        # self.tBatteryAverage.setText('%.2f V' %averageBatteryValue)
        # self.tBatteryHigh.setText('%.2f V (#%d)' %(highestBatteryValue, highestBatteryModule))
        # self.tBatteryLow.setText('%.2f V (#%d)' %(lowestBatteryValue, lowestBatteryModule))

        total = averageBatmanValue + averageRobinValue
        self.totalVoltage.append((total, time.time()))

def main():
    app = QApplication(sys.argv)
    form = PlottingDataMonitor()
    form.show()
    app.exec_()

if __name__ == "__main__":
    main()
