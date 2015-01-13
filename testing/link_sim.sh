
# For Windows, use com0com port emulator to link ports together

echo linking 2 serial ports in a bi directional way!
echo 

echo you can open these port by using the cu command 
echo "     example: cu /dev/pts/<your port>  -s <speed>"

if ["$1" != ""] && ["$2" != ""] ;then
	sudo socat -d -d PTY,link=/dev/ttySx,user=$USER PTY,link=/dev/ttySy,user=$USER
else
	sudo socat -d -d PTY,link=/dev/$1,user=$USER PTY,link=/dev/$2,user=$USER
fi
