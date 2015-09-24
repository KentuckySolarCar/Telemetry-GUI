"""
Some serial port utilities for Windows and PySerial

Eli Bendersky (eliben@gmail.com)
License: this code is in the public domain
"""
import re, itertools
from sys import platform as _platform
if _platform == "linux" or _platform == "linux2":
    # linux
    import subprocess
elif _platform == "darwin":
    # OS X
    import subprocess
elif _platform == "win32":
    # Windows...
    import _winreg as winreg

   
def full_port_name(portname):
    if _platform == "linux" or _platform == "linux2":
        # linux
        return portname
    elif _platform == "darwin":
        # OS X
        return portname
    elif _platform == "win32":
        # Windows...
        """ Given a port-name (of the form COM7, 
            COM12, CNCA0, etc.) returns a full 
            name suitable for opening with the 
            Serial class.
        """
        m = re.match('^COM(\d+)$', portname)
        if m and int(m.group(1)) < 10:
            return portname    
        return '\\\\.\\' + portname    
    

def enumerate_serial_ports():
    if _platform == "linux" or _platform == "linux2":
        # linux
        p = subprocess.Popen('ls /dev/ttyS*', shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        l=[]
        for line in p.stdout.readlines():
            l.append(line.rstrip('\n'))
        retval = p.wait()
        return l
    elif _platform == "darwin":
        # OS X
        p = subprocess.Popen('ls /dev/ttyS*', shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        l=[]
        for line in p.stdout.readlines():
            l.append(line.rstrip('\n'))
        retval = p.wait()
        return l
    elif _platform == "win32":
        # Windows...
        """ Uses the Win32 registry to return an 
            iterator of serial (COM) ports 
            existing on this computer.
        """
        path = 'HARDWARE\\DEVICEMAP\\SERIALCOMM'
        try:
            key = winreg.OpenKey(winreg.HKEY_LOCAL_MACHINE, path)
        except WindowsError:
            raise StopIteration

        l = []
        for i in itertools.count():
            try:
                val = winreg.EnumValue(key, i)
                l.append(str(val[1]))
            except EnvironmentError:
                break
        return l


if __name__ == "__main__":
    import serial
    for p in enumerate_serial_ports():
        print p, full_port_name(p)
