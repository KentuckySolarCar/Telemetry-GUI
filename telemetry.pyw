"""
UKY Solar Car Telemetry Program
Stephen Parsons (stephen.parsons@uky.edu)
"""

import random, sys
from PyQt4.QtCore import *
from PyQt4.QtGui import *
import Queue
import re

from lib.com_monitor import ComMonitorThread
from lib.livedatafeed import LiveDataFeed

from lib.serialutils import full_port_name, enumerate_serial_ports
from lib.utils import get_all_from_queue, get_item_from_queue

class Battery(QGraphicsView):
    def __init__(self, parent=None):
        super(Battery, self).__init__(parent)
        self.voltage = None
        self.temperature = None

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
        self.voltage = inVoltage/1000.0
        self.drawBattery()

    def getVoltage(self):
        return self.voltage

    def setTemperature(self, inTemperature):
        self.temperature = inTemperature
        self.drawBattery()

    def getTemperature(self):
        return self.temperature

    def drawBattery(self):
        self.scene.clear()

        red = QColor(255,0,0)
        white = QColor(255,255,255)
        blue = QColor(0,0,255)

        pen = QPen()
        brush = QBrush()
        brush.setStyle(Qt.SolidPattern)

        if self.temperature > self.maxBatteryTemp:
            color = red
        else:
            color = blue

        pen.setColor(color)
        brush.setColor(color)

        if self.voltage > self.maxBatteryVoltage:
            self.scene.addRect(0,0,50,85, pen, brush)

        elif self.voltage > self.minBatteryVoltage:
            dif = self.voltage - self.minBatteryVoltage
            div = (self.maxBatteryVoltage - self.minBatteryVoltage) / 85.0
            dist = int(dif / div)
            self.scene.addRect(0,85-dist,50,dist,pen,brush)

        vText = QGraphicsTextItem('%0.2f V' %(self.voltage))
        vText.setPos(0,80)

        self.scene.addItem(vText)

        if self.temperature > self.maxBatteryTemp:
            color = white
            pen.setColor(red)
            brush.setColor(white)
            self.scene.addRect(5,65,40,15,pen,brush)
            tText = QGraphicsTextItem(u"%d \u2103" %(self.temperature))
            tText.setPos(3,60)
            tText.setDefaultTextColor(red)
            self.scene.addItem(tText)


class PlottingDataMonitor(QMainWindow):
    def __init__(self, parent=None):
        super(PlottingDataMonitor, self).__init__(parent)

        self.defaultSerialPort = "/dev/ttySy"

        self.batteries = [[],[]]
        for i in range(20):
            self.batteries[0].append(Battery())
            self.batteries[1].append(Battery())

        self.monitor_active = False
        self.com_monitor = None
        self.com_data_q = None
        self.com_error_q = None
        self.livefeed = LiveDataFeed()
        self.timer = QTimer()

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
        portname_layout.addStretch(1)

        portname_groupbox = QGroupBox('COM Port')
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
                tempLabel = QLabel(str(counter+1))
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
                tempLabel = QLabel(str(counter+1))
                tempLabel.setAlignment(Qt.AlignHCenter)
                batteryLayout2.addWidget(tempLabel,2*i+1,j)
                counter += 1

        batteryWidget1 = QGroupBox('Battery Box 1')
        batteryWidget1.setAlignment(Qt.AlignHCenter)
        batteryWidget1.setLayout(batteryLayout1)

        batteryWidget2 = QGroupBox('Battery Box 2')
        batteryWidget2.setAlignment(Qt.AlignHCenter)
        batteryWidget2.setLayout(batteryLayout2)

        batteryWidget = QWidget()
        batteryWidgetLayout = QHBoxLayout()
        batteryWidgetLayout.addWidget(batteryWidget1)
        batteryWidgetLayout.addWidget(batteryWidget2)
        batteryWidget.setLayout(batteryWidgetLayout)
        
        self.main_frame = QWidget()
        main_layout = QVBoxLayout()
        main_layout.addWidget(batteryWidget)
        main_layout.addStretch(1)
        main_layout.addWidget(portname_groupbox)
        self.main_frame.setLayout(main_layout)
        
        self.setCentralWidget(self.main_frame)
        self.set_actions_enable_state()

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
        
        self.timer.start(0.05)
        
        self.status_text.setText('Monitor running')

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
            data = self.livefeed.read_data()

            # batteryVoltageRX = re.compile("^V\[([0-1])\]\[([0-1][0-9]|20)\]\s\=\s(\d+)$")
            # batteryTemperatureRX = re.compile("^T\[([0-1])\]\[([0-1][0-9]|20)\]\s\=\s(\d+)$")
            # These next two lines do not accept 20 as a battery number, the above do
            batteryVoltageRX = re.compile("^V\[([0-1])\]\[([0-1][0-9])\]\s\=\s(\d+)$")
            batteryTemperatureRX = re.compile("^T\[([0-1])\]\[([0-1][0-9])\]\s\=\s(\d+)$")

            batteryCurrentRX = re.compile("^C\s=\s(\d+)$")
            motorControllerVelocityRX = re.compile("^S\s=\s(\d+)$")
            motorControllerEnergyRX = re.compile("^E\s=\s(\d+)$")
            MPPTDataRX = re.compile("^M\[([0-3])\]\s(\d+)\s(\d+)\s(\d+)$")

            if batteryVoltageRX.match(data):
                info = batteryVoltageRX.search(data).groups()
                self.batteries[int(info[0])][int(info[1])].setVoltage(int(info[2]))

            elif batteryTemperatureRX.match(data):
                info = batteryTemperatureRX.search(data).groups()
                self.batteries[int(info[0])][int(info[1])].setTemperature(int(info[2]))

            elif batteryCurrentRX.match(data):
                info = batteryCurrentRX.search(data).groups()

            elif motorControllerVelocityRX.match(data):
                info = motorControllerVelocityRX.search(data).groups()

            elif motorControllerEnergyRX.match(data):
                info = motorControllerEnergyRX.search(data).groups()

            elif MPPTDataRX.match(data):
                info = MPPTDataRX.search(data).groups()

            else:
                info =  "*** Could not match input: " + data + "***"

            print info

def main():
    app = QApplication(sys.argv)
    form = PlottingDataMonitor()
    form.show()
    app.exec_()

if __name__ == "__main__":
    main()
