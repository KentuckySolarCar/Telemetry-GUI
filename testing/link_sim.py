# Only use on Linux
# For Windows, use com0com port emulator to link ports together
from subprocess import call
import getpass

user = getpass.getuser()

call_string = "sudo socat -d -d PTY,link=/dev/ttySx,user=%s PTY,link=/dev/ttySy,user=%s" %(user, user)
call_string = call_string.split(' ')

call(call_string)