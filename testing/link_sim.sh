
# For Windows, use com0com port emulator to link ports together

echo linking 2 serial ports in a bi directional way!
sudo socat -d -d PTY,link=/dev/ttySx,user=$USER PTY,link=/dev/ttySy,user=$USER

