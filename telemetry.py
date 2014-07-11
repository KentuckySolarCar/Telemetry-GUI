"""
UKY Solar Car Telemetry Program
Stephen Parsons (stephen.parsons@uky.edu)
"""

import random, sys, os, Queue, re, time, operator
from sys import platform as _platform

from PyQt4.QtCore import *
from PyQt4.QtGui import *

# from matplotlib.backends.backend_qt4agg import FigureCanvasQTAgg as FigureCanvas
# from matplotlib.backends.backend_qt4agg import NavigationToolbar2QTAgg as NavigationToolbar
# import matplotlib.pyplot as plt

from lib.com_monitor import ComMonitorThread
from lib.livedatafeed import LiveDataFeed

from lib.serialutils import full_port_name, enumerate_serial_ports
from lib.utils import get_all_from_queue, get_item_from_queue

print "hello"

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
        self.speed.append(inSpeed*self.conversion)
        self.tSpeed.setText('%.3f' %self.speed[-1])
        self.calcAverageSpeed()

    def setCurrent(self):
        pass

    def setEnergy(self, energy):
        self.energy.append(energy)
        self.calcEnergy()

    def calcAverageSpeed(self):
        self.averageSpeed = sum(self.speed[self.checkFromAv:])/float(len(self.speed[self.checkFromAv:]))
        self.tAverageSpeed.setText('%.3f' %self.averageSpeed)

    def resetEnergy(self):
        self.checkFromE = len(self.energy) - 1
        self.calcEnergy()

    def calcEnergy(self):
        self.tEnergy.setText(str(self.energy[-1] - self.energy[self.checkFromE]))

    def resetAvSpeed(self):
        self.checkFromAv = len(self.speed) - 1
        self.calcAverageSpeed()


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
        return self.outCurrent[-1]

class Battery(QGraphicsView):
    def __init__(self, parent=None):
        super(Battery, self).__init__(parent)
        self.voltage = [0]
        self.temperature = [0]

        # Default values
        self.maxBatteryTemp = 30
        self.maxBatteryVoltage = 3.65
        self.minBatteryVoltage = 2.65

        self.setMaximumWidth(52)
        self.setMaximumHeight(102)

        self.scene = QGraphicsScene(0,0,50,100,self)
        self.setScene(self.scene)

        self.setViewportUpdateMode(QGraphicsView.FullViewportUpdate)

    def setVoltage(self, inVoltage):
        self.voltage.append(inVoltage/10000.0)
        self.drawBattery()

    def getVoltage(self):
        return self.voltage[-1]

    def setTemperature(self, inTemperature):
        self.temperature.append(inTemperature)
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
            self.scene.addRect(0,0,50,85, pen, brush)

        elif self.voltage[-1] > self.minBatteryVoltage:
            dif = self.voltage[-1] - self.minBatteryVoltage
            div = (self.maxBatteryVoltage - self.minBatteryVoltage) / 85.0
            dist = int(dif / div)
            self.scene.addRect(0,85-dist,50,dist,pen,brush)

        vText = QGraphicsTextItem("%0.2f V" %self.voltage[-1])
        vText.setFont(QFont('Arial Unicode MS', 10))
        vText.setPos(0,80)

        self.scene.addItem(vText)

        pen.setColor(color)
        brush.setColor(white)
        self.scene.addRect(5,65,40,15,pen,brush)
        tText = QGraphicsTextItem(u"%d \u2103" %(self.temperature[-1]))
        tText.setFont(QFont('Arial Unicode MS', 10))
        tText.setPos(3,60)
        tText.setDefaultTextColor(color)
        self.scene.addItem(tText)


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

        self.batteries = [[],[]]
        for i in range(20):
            self.batteries[0].append(Battery())
            self.batteries[1].append(Battery())

        self.mppts = []
        for i in range(4):
            self.mppts.append(MPPT(i))

        self.batteryCurrent = 0
        self.BatmanCurrent = 0
        self.RobinCurrent = 0

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

        exit = QPushButton('Exit')
        exit.clicked.connect(self.close)
        portname_layout.addWidget(exit)

        portname_layout.addStretch(1)

        portname_groupbox = QGroupBox('Controls')
        portname_groupbox.setLayout(portname_layout)

        batteryLayout1 = QGridLayout()
        for i in range(5):
            batteryLayout1.setColumnMinimumWidth(i,52)
        for i in range(0,8,2):
            batteryLayout1.setRowMinimumHeight(i,102)
            batteryLayout1.setRowMinimumHeight(i+1,15)
        counter = 0
        for i in range(4):
            for j in range(5):
                batteryLayout1.addWidget(self.batteries[0][counter],2*i,j)
                tempLabel = QLabel(str(counter))
                tempLabel.setAlignment(Qt.AlignHCenter)
                batteryLayout1.addWidget(tempLabel,2*i+1,j)
                counter += 1

        batteryLayout2 = QGridLayout()
        for i in range(5):
            batteryLayout2.setColumnMinimumWidth(i,52)
        for i in range(0,8,2):
            batteryLayout2.setRowMinimumHeight(i,102)
            batteryLayout2.setRowMinimumHeight(i+1,15)
        counter = 0
        for i in range(4):
            for j in range(5):
                batteryLayout2.addWidget(self.batteries[1][counter],2*i,j)
                tempLabel = QLabel(str(counter+20))
                tempLabel.setAlignment(Qt.AlignHCenter)
                batteryLayout2.addWidget(tempLabel,2*i+1,j)
                counter += 1

        batteryWidget1 = QGroupBox('Batman')
        batteryWidget1.setAlignment(Qt.AlignHCenter)
        batteryWidget1.setLayout(batteryLayout1)

        batteryWidget2 = QGroupBox('Robin')
        batteryWidget2.setAlignment(Qt.AlignHCenter)
        batteryWidget2.setLayout(batteryLayout2)

        batteryWidget = QWidget()
        batteryWidgetLayout = QHBoxLayout()
        batteryWidgetLayout.addWidget(batteryWidget1)
        batteryWidgetLayout.addWidget(batteryWidget2)
        batteryWidget.setLayout(batteryWidgetLayout)

        #Time
        timeWidget = QGroupBox('Time')
        timeLayout = QGridLayout()
        self.currentTime = QLabel(time.strftime("%H:%M:%S", time.localtime(self.curTime)))
        self.timeSince = QLabel(time.strftime("%H:%M:%S", time.gmtime(self.timeSinceStart)))
        timeLayout.addWidget(self.currentTime,0,1)
        timeLayout.addWidget(self.timeSince,1,1)
        timeLayout.addWidget(QLabel('Current time:'),0,0)
        timeLayout.addWidget(QLabel('Run time:'),1,0)
        timeWidget.setLayout(timeLayout)

        #Batteries
        batteryStatsWidget = QGroupBox('Batteries')
        batteryStatsLayout = QGridLayout()

        self.tBatmanCurrent = QLabel('0.00')
        self.tBatmanAverage = QLabel('0.00')
        self.tBatmanValueHigh = QLabel('0.00')
        self.tBatmanModuleHigh = QLabel('(#)')
        self.tBatmanValueLow = QLabel('0.00')
        self.tBatmanModuleLow = QLabel('(#)')
        self.tBatmanCurrent.setText('%.2f' %self.BatmanCurrent)
        batteryStatsLayout.addWidget(QLabel('Batman'),0,0)
        batteryStatsLayout.addWidget(QLabel('Average:'),1,0)
        batteryStatsLayout.addWidget(self.tBatmanAverage,1,1)
        batteryStatsLayout.addWidget(QLabel('V'),1,2)
        batteryStatsLayout.addWidget(QLabel('High:'),2,0)
        batteryStatsLayout.addWidget(self.tBatmanValueHigh,2,1)
        batteryStatsLayout.addWidget(QLabel('V'),2,2)
        batteryStatsLayout.addWidget(self.tBatmanModuleHigh,2,3)
        batteryStatsLayout.addWidget(QLabel('Low:'),3,0)
        batteryStatsLayout.addWidget(self.tBatmanValueLow,3,1)
        batteryStatsLayout.addWidget(QLabel('V'),3,2)
        batteryStatsLayout.addWidget(self.tBatmanModuleLow,3,3)

        self.tRobinCurrent = QLabel('0.00')
        self.tRobinAverage = QLabel('0.00')
        self.tRobinValueHigh = QLabel('0.00')
        self.tRobinModuleHigh = QLabel('(#)')
        self.tRobinValueLow = QLabel('0.00')
        self.tRobinModuleLow = QLabel('(#)')
        self.tRobinCurrent.setText('%.2f' %self.RobinCurrent)
        batteryStatsLayout.addWidget(QLabel('Robin'),0,4)
        batteryStatsLayout.addWidget(QLabel('Average:'),1,4)
        batteryStatsLayout.addWidget(self.tRobinAverage,1,5)
        batteryStatsLayout.addWidget(QLabel('V'),1,6)
        batteryStatsLayout.addWidget(QLabel('High:'),2,4)
        batteryStatsLayout.addWidget(self.tRobinValueHigh,2,5)
        batteryStatsLayout.addWidget(QLabel('V'),2,6)
        batteryStatsLayout.addWidget(self.tRobinModuleHigh,2,7)
        batteryStatsLayout.addWidget(QLabel('Low:'),3,4)
        batteryStatsLayout.addWidget(self.tRobinValueLow,3,5)
        batteryStatsLayout.addWidget(QLabel('V'),3,6)
        batteryStatsLayout.addWidget(self.tRobinModuleLow,3,7)

        self.tBatteryCurrent = QLabel('0.00')
        self.tBatteryAverage = QLabel('0.00')
        self.tBatteryValueHigh = QLabel('0.00')
        self.tBatteryModuleHigh = QLabel('(#)')
        self.tBatteryValueLow = QLabel('0.00')
        self.tBatteryModuleLow = QLabel('(#)')
        self.tBatteryCurrent.setText('%.2f' %self.batteryCurrent)
        batteryStatsLayout.addWidget(QLabel('Total Current:'),0,8)
        batteryStatsLayout.addWidget(self.tBatteryCurrent,0,9)
        batteryStatsLayout.addWidget(QLabel('A'),0,10)
        batteryStatsLayout.addWidget(QLabel('Average:'),1,8)
        batteryStatsLayout.addWidget(self.tBatteryAverage,1,9)
        batteryStatsLayout.addWidget(QLabel('V'),1,10)
        batteryStatsLayout.addWidget(QLabel('High:'),2,8)
        batteryStatsLayout.addWidget(self.tBatteryValueHigh,2,9)
        batteryStatsLayout.addWidget(QLabel('V'),2,10)
        batteryStatsLayout.addWidget(self.tBatteryModuleHigh,2,11)
        batteryStatsLayout.addWidget(QLabel('Low:'),3,8)
        batteryStatsLayout.addWidget(self.tBatteryValueLow,3,9)
        batteryStatsLayout.addWidget(QLabel('V'),3,10)
        batteryStatsLayout.addWidget(self.tBatteryModuleLow,3,11)

        batteryStatsWidget.setLayout(batteryStatsLayout)

        #Motor Controller
        self.motorControllerWidget = MotorController('MotorController')

        #MPPTs
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

        #Main Layout
        self.main_frame1 = QWidget()
        main_layout1 = QVBoxLayout()
        main_layout1.addWidget(batteryWidget)
        main_layout1.addStretch(1)
        main_layout1.addWidget(batteryStatsWidget)
        main_layout1.addWidget(portname_groupbox)
        self.main_frame1.setLayout(main_layout1)

        self.main_frame2 = QWidget()
        main_layout2 = QVBoxLayout()
        main_layout2.addWidget(timeWidget)
        main_layout2.addWidget(self.motorControllerWidget)
        main_layout2.addWidget(mpptWidget)
        main_layout2.addStretch(1)
        self.main_frame2.setLayout(main_layout2)
        
        self.main_frame = QWidget()
        main_layout = QHBoxLayout()
        main_layout.addWidget(self.main_frame1)
        main_layout.addWidget(self.main_frame2)
        self.main_frame.setLayout(main_layout)

        self.setCentralWidget(self.main_frame)
        self.set_actions_enable_state()

        self.secTimer = QTimer()
        self.secTimer.timeout.connect(self.writeLog)
        self.secTimer.timeout.connect(self.updateTime)
        self.secTimer.start(1000)

    def updateTime(self):
        self.curTime = time.time()
        self.timeSinceStart = self.curTime - self.startTime
        self.currentTime.setText(time.strftime("%H:%M:%S", time.localtime(self.curTime)))
        self.timeSince.setText(time.strftime("%H:%M:%S", time.gmtime(self.timeSinceStart)))
        # print time.strftime("%d.%m.%Y.%H:%M:%S")

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
        self.logFile.write("timestamp, speed, motor current, array current, battery current, mppt voltages\n")

    def writeLog(self):
        if self.logging_active:
            self.logFile.write(time.strftime("%d-%m-%Y-%H:%M:%S,"))
            # self.logFile.write((self.motorControllerWidget.speed[-1] if self.speed.len() > 0 else "") + ",")
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
        """ Updates the state of the monitor window with new 
            data. The livefeed is used to find out whether new
            data was received since the last update. If not, 
            nothing is updated.
        """
        if self.livefeed.has_new_data:
            data = self.livefeed.read_data().strip("\r\n")

            # batteryVoltageRX = re.compile("^V\[([0-1])\]\[([0-1][0-9]|20)\]\s\=\s(\d+)$")
            # batteryTemperatureRX = re.compile("^T\[([0-1])\]\[([0-1][0-9]|20)\]\s\=\s(\d+)$")
            # These next two lines do not accept 20 as a battery number, the above do
            batteryVoltageRX = re.compile("^\s*V\[([0-1])\]\[([0-1][0-9])\]\s\=\s(\d+)\s*$", re.MULTILINE)
            batteryTemperatureRX = re.compile("^\s*T\[([0-1])\]\[([0-1][0-9])\]\s\=\s(\d+)\s*$", re.MULTILINE)

            batteryCurrentRX = re.compile("^\s*C\s=\s(\d+)\s*$", re.MULTILINE)
            motorControllerVelocityRX = re.compile("^\s*S\s=\s(\d+)\s*$", re.MULTILINE)
            motorControllerEnergyRX = re.compile("^\s*E\s=\s(\d+)\s*$", re.MULTILINE)
            MPPTDataRX = re.compile("^\s*M\[([0-3])\]\s(\d+)\s(\d+)\s(\d+)\s*$", re.MULTILINE)

            if batteryVoltageRX.match(data):
                info = batteryVoltageRX.search(data).groups()
                self.batteries[int(info[0])][int(info[1])].setVoltage(int(info[2]))
                self.updateBatteries()

            elif batteryTemperatureRX.match(data):
                info = batteryTemperatureRX.search(data).groups()
                self.batteries[int(info[0])][int(info[1])].setTemperature(int(info[2]))

            elif batteryCurrentRX.match(data):
                info = batteryCurrentRX.search(data).groups()
                self.batteryCurrent = float(info[0])

            elif motorControllerVelocityRX.match(data):
                info = motorControllerVelocityRX.search(data).groups()
                self.motorControllerWidget.setSpeed(int(info[0]))

            elif motorControllerEnergyRX.match(data):
                info = motorControllerEnergyRX.search(data).groups()
                self.motorControllerWidget.setEnergy(int(info[0]))

            elif MPPTDataRX.match(data):
                info = MPPTDataRX.search(data).groups()
                self.mppts[int(info[0])].setInVoltage(int(info[1]))
                self.mppts[int(info[0])].setInCurrent(int(info[2]))
                self.mppts[int(info[0])].setOutVoltage(int(info[3]))
                self.updateMPPT()

            else:
                info =  "Could not match input '" + data + "'"
                print info

            # print info # debug

    def updateMPPT(self):
        total = 0.0
        for mppt in self.mppts:
            total += mppt.getOutCurrent()
        self.MPPTTotal.setText("%.3f" %total)

    def updateBatteries(self):
        #find highest, lowest, and average values
        highestBatteryValue = 0.0
        highestBatteryModule = 0
        lowestBatteryValue = 4.0
        lowestBatteryModule = 0
        averageBatteryValue = 0.0
        batterySum = 0.0
        for i in range(2):            
            for j in range(20):
                voltage = self.batteries[i][j].getVoltage()
                batterySum = batterySum + voltage
                if voltage > highestBatteryValue:
                    highestBatteryValue = voltage
                    highestBatteryModule = (20*i + j)
                if voltage < lowestBatteryValue:
                    lowestBatteryValue = voltage
                    lowestBatteryModule = (20*i + j)
        averageBatteryValue = batterySum / 40;

        highestBatmanModule, highestBatmanValue = max(enumerate(batman.getVoltage() for batman in self.batteries[0]), key=operator.itemgetter(1))
        lowestBatmanModule, lowestBatmanValue = min(enumerate(batman.getVoltage() for batman in self.batteries[0]), key=operator.itemgetter(1))
        averageBatmanValue = sum(batman.getVoltage() for batman in self.batteries[0]) / 20.0

        highestRobinModule, highestRobinValue = max(enumerate(robin.getVoltage() for robin in self.batteries[1]), key=operator.itemgetter(1))
        highestRobinModule += 20
        lowestRobinModule, lowestRobinValue = min(enumerate(robin.getVoltage() for robin in self.batteries[1]), key=operator.itemgetter(1))
        lowestRobinModule += 20
        averageRobinValue = sum(robin.getVoltage() for robin in self.batteries[1]) / 20.0

        self.tBatmanCurrent.setText('%.2f' %self.BatmanCurrent)
        self.tBatmanAverage.setText('%.2f' %averageBatmanValue)
        self.tBatmanValueHigh.setText('%.2f' %highestBatmanValue)
        self.tBatmanModuleHigh.setText('(#%d)' %highestBatmanModule)
        self.tBatmanValueLow.setText('%.2f' %lowestBatmanValue)
        self.tBatmanModuleLow.setText('(#%d)' %lowestBatmanModule)

        self.tRobinCurrent.setText('%.2f' %self.RobinCurrent)
        self.tRobinAverage.setText('%.2f' %averageRobinValue)
        self.tRobinValueHigh.setText('%.2f' %highestRobinValue)
        self.tRobinModuleHigh.setText('(#%d)' %highestRobinModule)
        self.tRobinValueLow.setText('%.2f' %lowestRobinValue)
        self.tRobinModuleLow.setText('(#%d)' %lowestRobinModule)

        self.tBatteryCurrent.setText('%.2f' %self.batteryCurrent)
        self.tBatteryAverage.setText('%.2f' %averageBatteryValue)
        self.tBatteryValueHigh.setText('%.2f' %highestBatteryValue)
        self.tBatteryModuleHigh.setText('(#%d)' %highestBatteryModule)
        self.tBatteryValueLow.setText('%.2f' %lowestBatteryValue)
        self.tBatteryModuleLow.setText('(#%d)' %lowestBatteryModule)

def main():
    app = QApplication(sys.argv)
    form = PlottingDataMonitor()
    form.show()
    app.exec_()

if __name__ == "__main__":
    main()
=======
"""
UKY Solar Car Telemetry Program
Stephen Parsons (stephen.parsons@uky.edu)
https://github.com/KentuckySolarCar/Telemetry-GUI
"""

import random, sys, os, Queue, re, time, operator
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
        self.speed.append(inSpeed*self.conversion)
        self.tSpeed.setText('%.3f' %self.speed[-1])
        self.calcAverageSpeed()

    def setCurrent(self):
        pass

    def setEnergy(self, energy):
        self.energy.append(energy)
        self.calcEnergy()

    def calcAverageSpeed(self):
        self.averageSpeed = sum(self.speed[self.checkFromAv:])/float(len(self.speed[self.checkFromAv:]))
        self.tAverageSpeed.setText('%.3f' %self.averageSpeed)

    def resetEnergy(self):
        self.checkFromE = len(self.energy) - 1
        self.calcEnergy()

    def calcEnergy(self):
        self.tEnergy.setText(str(self.energy[-1] - self.energy[self.checkFromE]))

    def resetAvSpeed(self):
        self.checkFromAv = len(self.speed) - 1
        self.calcAverageSpeed()


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
        return self.outCurrent[-1]

class Battery(QGraphicsView):
    def __init__(self, parent=None):
        super(Battery, self).__init__(parent)
        self.voltage = [0]
        self.temperature = [0]

        # Default values
        self.maxBatteryTemp = 30
        self.maxBatteryVoltage = 3.65
        self.minBatteryVoltage = 2.65

        self.setMaximumWidth(52)
        self.setMaximumHeight(102)

        self.scene = QGraphicsScene(0,0,50,100,self)
        self.setScene(self.scene)

        self.setViewportUpdateMode(QGraphicsView.FullViewportUpdate)

    def setVoltage(self, inVoltage):
        self.voltage.append(inVoltage/10000.0)
        self.drawBattery()

    def getVoltage(self):
        return self.voltage[-1]

    def setTemperature(self, inTemperature):
        self.temperature.append(inTemperature)
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
            self.scene.addRect(0,0,50,85, pen, brush)

        elif self.voltage[-1] > self.minBatteryVoltage:
            dif = self.voltage[-1] - self.minBatteryVoltage
            div = (self.maxBatteryVoltage - self.minBatteryVoltage) / 85.0
            dist = int(dif / div)
            self.scene.addRect(0,85-dist,50,dist,pen,brush)

        vText = QGraphicsTextItem("%0.2f V" %self.voltage[-1])
        vText.setFont(QFont('Arial Unicode MS', 10))
        vText.setPos(0,80)

        self.scene.addItem(vText)

        pen.setColor(color)
        brush.setColor(white)
        self.scene.addRect(5,65,40,15,pen,brush)
        tText = QGraphicsTextItem(u"%d \u2103" %(self.temperature[-1]))
        tText.setFont(QFont('Arial Unicode MS', 10))
        tText.setPos(3,60)
        tText.setDefaultTextColor(color)
        self.scene.addItem(tText)


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

        self.batteries = [[],[]]
        for i in range(20):
            self.batteries[0].append(Battery())
            self.batteries[1].append(Battery())

        self.mppts = []
        for i in range(4):
            self.mppts.append(MPPT(i))

        self.batteryCurrent = 0
        self.BatmanCurrent = 0
        self.RobinCurrent = 0

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

        exit = QPushButton('Exit')
        exit.clicked.connect(self.close)
        portname_layout.addWidget(exit)

        portname_layout.addStretch(1)

        portname_groupbox = QGroupBox('Controls')
        portname_groupbox.setLayout(portname_layout)

        batteryLayout1 = QGridLayout()
        for i in range(5):
            batteryLayout1.setColumnMinimumWidth(i,52)
        for i in range(0,8,2):
            batteryLayout1.setRowMinimumHeight(i,102)
            batteryLayout1.setRowMinimumHeight(i+1,15)
        counter = 0
        for i in range(4):
            for j in range(5):
                batteryLayout1.addWidget(self.batteries[0][counter],2*i,j)
                tempLabel = QLabel(str(counter))
                tempLabel.setAlignment(Qt.AlignHCenter)
                batteryLayout1.addWidget(tempLabel,2*i+1,j)
                counter += 1

        batteryLayout2 = QGridLayout()
        for i in range(5):
            batteryLayout2.setColumnMinimumWidth(i,52)
        for i in range(0,8,2):
            batteryLayout2.setRowMinimumHeight(i,102)
            batteryLayout2.setRowMinimumHeight(i+1,15)
        counter = 0
        for i in range(4):
            for j in range(5):
                batteryLayout2.addWidget(self.batteries[1][counter],2*i,j)
                tempLabel = QLabel(str(counter+20))
                tempLabel.setAlignment(Qt.AlignHCenter)
                batteryLayout2.addWidget(tempLabel,2*i+1,j)
                counter += 1

        batteryWidget1 = QGroupBox('Batman')
        batteryWidget1.setAlignment(Qt.AlignHCenter)
        batteryWidget1.setLayout(batteryLayout1)

        batteryWidget2 = QGroupBox('Robin')
        batteryWidget2.setAlignment(Qt.AlignHCenter)
        batteryWidget2.setLayout(batteryLayout2)

        batteryWidget = QWidget()
        batteryWidgetLayout = QHBoxLayout()
        batteryWidgetLayout.addWidget(batteryWidget1)
        batteryWidgetLayout.addWidget(batteryWidget2)
        batteryWidget.setLayout(batteryWidgetLayout)

        #Time
        timeWidget = QGroupBox('Time')
        timeLayout = QGridLayout()
        self.currentTime = QLabel(time.strftime("%H:%M:%S", time.localtime(self.curTime)))
        self.timeSince = QLabel(time.strftime("%H:%M:%S", time.gmtime(self.timeSinceStart)))
        timeLayout.addWidget(self.currentTime,0,1)
        timeLayout.addWidget(self.timeSince,1,1)
        timeLayout.addWidget(QLabel('Current time:'),0,0)
        timeLayout.addWidget(QLabel('Run time:'),1,0)
        timeWidget.setLayout(timeLayout)

        #Batteries
        batteryStatsWidget = QGroupBox('Batteries')
        batteryStatsLayout = QGridLayout()

        self.tBatmanCurrent = QLabel('0.00')
        self.tBatmanAverage = QLabel('0.00')
        self.tBatmanValueHigh = QLabel('0.00')
        self.tBatmanModuleHigh = QLabel('(#)')
        self.tBatmanValueLow = QLabel('0.00')
        self.tBatmanModuleLow = QLabel('(#)')
        self.tBatmanCurrent.setText('%.2f' %self.BatmanCurrent)
        batteryStatsLayout.addWidget(QLabel('Batman'),0,0)
        batteryStatsLayout.addWidget(QLabel('Average:'),1,0)
        batteryStatsLayout.addWidget(self.tBatmanAverage,1,1)
        batteryStatsLayout.addWidget(QLabel('V'),1,2)
        batteryStatsLayout.addWidget(QLabel('High:'),2,0)
        batteryStatsLayout.addWidget(self.tBatmanValueHigh,2,1)
        batteryStatsLayout.addWidget(QLabel('V'),2,2)
        batteryStatsLayout.addWidget(self.tBatmanModuleHigh,2,3)
        batteryStatsLayout.addWidget(QLabel('Low:'),3,0)
        batteryStatsLayout.addWidget(self.tBatmanValueLow,3,1)
        batteryStatsLayout.addWidget(QLabel('V'),3,2)
        batteryStatsLayout.addWidget(self.tBatmanModuleLow,3,3)

        self.tRobinCurrent = QLabel('0.00')
        self.tRobinAverage = QLabel('0.00')
        self.tRobinValueHigh = QLabel('0.00')
        self.tRobinModuleHigh = QLabel('(#)')
        self.tRobinValueLow = QLabel('0.00')
        self.tRobinModuleLow = QLabel('(#)')
        self.tRobinCurrent.setText('%.2f' %self.RobinCurrent)
        batteryStatsLayout.addWidget(QLabel('Robin'),0,4)
        batteryStatsLayout.addWidget(QLabel('Average:'),1,4)
        batteryStatsLayout.addWidget(self.tRobinAverage,1,5)
        batteryStatsLayout.addWidget(QLabel('V'),1,6)
        batteryStatsLayout.addWidget(QLabel('High:'),2,4)
        batteryStatsLayout.addWidget(self.tRobinValueHigh,2,5)
        batteryStatsLayout.addWidget(QLabel('V'),2,6)
        batteryStatsLayout.addWidget(self.tRobinModuleHigh,2,7)
        batteryStatsLayout.addWidget(QLabel('Low:'),3,4)
        batteryStatsLayout.addWidget(self.tRobinValueLow,3,5)
        batteryStatsLayout.addWidget(QLabel('V'),3,6)
        batteryStatsLayout.addWidget(self.tRobinModuleLow,3,7)

        self.tBatteryCurrent = QLabel('0.00')
        self.tBatteryAverage = QLabel('0.00')
        self.tBatteryValueHigh = QLabel('0.00')
        self.tBatteryModuleHigh = QLabel('(#)')
        self.tBatteryValueLow = QLabel('0.00')
        self.tBatteryModuleLow = QLabel('(#)')
        self.tBatteryCurrent.setText('%.2f' %self.batteryCurrent)
        batteryStatsLayout.addWidget(QLabel('Total Current:'),0,8)
        batteryStatsLayout.addWidget(self.tBatteryCurrent,0,9)
        batteryStatsLayout.addWidget(QLabel('A'),0,10)
        batteryStatsLayout.addWidget(QLabel('Average:'),1,8)
        batteryStatsLayout.addWidget(self.tBatteryAverage,1,9)
        batteryStatsLayout.addWidget(QLabel('V'),1,10)
        batteryStatsLayout.addWidget(QLabel('High:'),2,8)
        batteryStatsLayout.addWidget(self.tBatteryValueHigh,2,9)
        batteryStatsLayout.addWidget(QLabel('V'),2,10)
        batteryStatsLayout.addWidget(self.tBatteryModuleHigh,2,11)
        batteryStatsLayout.addWidget(QLabel('Low:'),3,8)
        batteryStatsLayout.addWidget(self.tBatteryValueLow,3,9)
        batteryStatsLayout.addWidget(QLabel('V'),3,10)
        batteryStatsLayout.addWidget(self.tBatteryModuleLow,3,11)

        batteryStatsWidget.setLayout(batteryStatsLayout)

        #Motor Controller
        self.motorControllerWidget = MotorController('Motor Controller')

        #MPPTs
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

        #Main Layout
        self.main_frame1 = QWidget()
        main_layout1 = QVBoxLayout()
        main_layout1.addWidget(batteryWidget)
        main_layout1.addStretch(1)
        main_layout1.addWidget(batteryStatsWidget)
        main_layout1.addWidget(portname_groupbox)
        self.main_frame1.setLayout(main_layout1)

        self.main_frame2 = QWidget()
        main_layout2 = QVBoxLayout()
        main_layout2.addWidget(timeWidget)
        main_layout2.addWidget(self.motorControllerWidget)
        main_layout2.addWidget(mpptWidget)
        main_layout2.addStretch(1)
        self.main_frame2.setLayout(main_layout2)
        
        self.main_frame = QWidget()
        main_layout = QHBoxLayout()
        main_layout.addWidget(self.main_frame1)
        main_layout.addWidget(self.main_frame2)
        self.main_frame.setLayout(main_layout)

        self.setCentralWidget(self.main_frame)
        self.set_actions_enable_state()

        self.secTimer = QTimer()
        self.secTimer.timeout.connect(self.writeLog)
        self.secTimer.timeout.connect(self.updateTime)
        self.secTimer.start(1000)

    def updateTime(self):
        self.curTime = time.time()
        self.timeSinceStart = self.curTime - self.startTime
        self.currentTime.setText(time.strftime("%H:%M:%S", time.localtime(self.curTime)))
        self.timeSince.setText(time.strftime("%H:%M:%S", time.gmtime(self.timeSinceStart)))
        # print time.strftime("%d.%m.%Y.%H:%M:%S")

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
        self.logFile.write("timestamp, speed, motor current, array current, battery current, mppt voltages\n")

    def writeLog(self):
        if self.logging_active:
            self.logFile.write(time.strftime("%d-%m-%Y-%H:%M:%S,"))
            # self.logFile.write((self.motorControllerWidget.speed[-1] if self.speed.len() > 0 else "") + ",")
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
        """ Updates the state of the monitor window with new 
            data. The livefeed is used to find out whether new
            data was received since the last update. If not, 
            nothing is updated.
        """
        if self.livefeed.has_new_data:
            data = self.livefeed.read_data().strip("\r\n")

            # Regular Expressions
            batteryVoltageRX = re.compile("^\s*V\[([0-1])\]\[([0-1][0-9])\]\s\=\s(\d+)\s*$", re.MULTILINE)
            batteryTemperatureRX = re.compile("^\s*T\[([0-1])\]\[([0-1][0-9])\]\s\=\s(\d+)\s*$", re.MULTILINE)
            batteryCurrentRX = re.compile("^\s*C\s=\s(\d+)\s*$", re.MULTILINE)
            motorControllerVelocityRX = re.compile("^\s*S\s=\s(\d+)\s*$", re.MULTILINE)
            motorControllerEnergyRX = re.compile("^\s*E\s=\s(\d+)\s*$", re.MULTILINE)
            MPPTDataRX = re.compile("^\s*M\[([0-3])\]\s(\d+)\s(\d+)\s(\d+)\s*$", re.MULTILINE)

            if batteryVoltageRX.match(data):
                info = batteryVoltageRX.search(data).groups()
                self.batteries[int(info[0])][int(info[1])].setVoltage(int(info[2]))
                self.updateBatteries()

            elif batteryTemperatureRX.match(data):
                info = batteryTemperatureRX.search(data).groups()
                self.batteries[int(info[0])][int(info[1])].setTemperature(int(info[2]))

            elif batteryCurrentRX.match(data):
                info = batteryCurrentRX.search(data).groups()
                self.batteryCurrent = float(info[0])

            elif motorControllerVelocityRX.match(data):
                info = motorControllerVelocityRX.search(data).groups()
                self.motorControllerWidget.setSpeed(int(info[0]))

            elif motorControllerEnergyRX.match(data):
                info = motorControllerEnergyRX.search(data).groups()
                self.motorControllerWidget.setEnergy(int(info[0]))

            elif MPPTDataRX.match(data):
                info = MPPTDataRX.search(data).groups()
                self.mppts[int(info[0])].setInVoltage(int(info[1]))
                self.mppts[int(info[0])].setInCurrent(int(info[2]))
                self.mppts[int(info[0])].setOutVoltage(int(info[3]))
                self.updateMPPT()

            else:
                info =  "Could not match input '" + data + "'"
                print info

            # print info # debug

    def updateMPPT(self):
        total = 0.0
        for mppt in self.mppts:
            total += mppt.getOutCurrent()
        self.MPPTTotal.setText("%.3f" %total)

    def updateBatteries(self):
        #find highest, lowest, and average values
        highestBatteryValue = 0.0
        highestBatteryModule = 0
        lowestBatteryValue = 4.0
        lowestBatteryModule = 0
        averageBatteryValue = 0.0
        batterySum = 0.0
        for i in range(2):            
            for j in range(20):
                voltage = self.batteries[i][j].getVoltage()
                batterySum = batterySum + voltage
                if voltage > highestBatteryValue:
                    highestBatteryValue = voltage
                    highestBatteryModule = (20*i + j)
                if voltage < lowestBatteryValue:
                    lowestBatteryValue = voltage
                    lowestBatteryModule = (20*i + j)
        averageBatteryValue = batterySum / 40;

        highestBatmanModule, highestBatmanValue = max(enumerate(batman.getVoltage() for batman in self.batteries[0]), key=operator.itemgetter(1))
        lowestBatmanModule, lowestBatmanValue = min(enumerate(batman.getVoltage() for batman in self.batteries[0]), key=operator.itemgetter(1))
        averageBatmanValue = sum(batman.getVoltage() for batman in self.batteries[0]) / 20.0

        highestRobinModule, highestRobinValue = max(enumerate(robin.getVoltage() for robin in self.batteries[1]), key=operator.itemgetter(1))
        highestRobinModule += 20
        lowestRobinModule, lowestRobinValue = min(enumerate(robin.getVoltage() for robin in self.batteries[1]), key=operator.itemgetter(1))
        lowestRobinModule += 20
        averageRobinValue = sum(robin.getVoltage() for robin in self.batteries[1]) / 20.0

        self.tBatmanCurrent.setText('%.2f' %self.BatmanCurrent)
        self.tBatmanAverage.setText('%.2f' %averageBatmanValue)
        self.tBatmanValueHigh.setText('%.2f' %highestBatmanValue)
        self.tBatmanModuleHigh.setText('(#%d)' %highestBatmanModule)
        self.tBatmanValueLow.setText('%.2f' %lowestBatmanValue)
        self.tBatmanModuleLow.setText('(#%d)' %lowestBatmanModule)

        self.tRobinCurrent.setText('%.2f' %self.RobinCurrent)
        self.tRobinAverage.setText('%.2f' %averageRobinValue)
        self.tRobinValueHigh.setText('%.2f' %highestRobinValue)
        self.tRobinModuleHigh.setText('(#%d)' %highestRobinModule)
        self.tRobinValueLow.setText('%.2f' %lowestRobinValue)
        self.tRobinModuleLow.setText('(#%d)' %lowestRobinModule)

        self.tBatteryCurrent.setText('%.2f' %self.batteryCurrent)
        self.tBatteryAverage.setText('%.2f' %averageBatteryValue)
        self.tBatteryValueHigh.setText('%.2f' %highestBatteryValue)
        self.tBatteryModuleHigh.setText('(#%d)' %highestBatteryModule)
        self.tBatteryValueLow.setText('%.2f' %lowestBatteryValue)
        self.tBatteryModuleLow.setText('(#%d)' %lowestBatteryModule)

def main():
    app = QApplication(sys.argv)
    form = PlottingDataMonitor()
    form.show()
    app.exec_()

if __name__ == "__main__":
    main()
