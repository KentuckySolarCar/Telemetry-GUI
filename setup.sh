#!/bin/bash

RED='\033[0;31m'
BLUE='\033[0;34m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

READ_RED='\e[0;31m'
READ_BLUE='\e[0;34m'
READ_GREEN='\e[0;32m'
READ_NC='\e[0m' # No Color

# Check to make sure that it has been given sudo privlages
if [ "$(whoami)" = "root" ] ; then

	# Update machine
	read -p $'\e[0;32m[setup.sh]>\e[0;34m Would you like to update your machine? (yes/no): ' input
	if [[ $input =~ y|Y|yes|Yes ]] ; then
		printf "${BLUE}Updating this machine${NC}\n"
		apt-get update
	else
		printf "${BLUE}Skipping machine updates${NC}\n"
	fi

	# Installing stuff
	read -p $'\e[0;32m[setup.sh]>\e[0;34m Would you like to install all requied software? (yes/no): ' input
	if [[ $input =~ y|Y|yes|Yes ]] ; then
		printf "${BLUE}Downloading/Installing required packages${NC}\n"
		apt-get install mysql-server python3-pip python3-dev libmysqlclient-dev
		printf "${BLUE}Downloading/Installing required python3 libraries${NC}\n"
		apt-get install python-matplotlib
		pip3 install pyserial setproctitle python-qt4
	else
		printf "${BLUE}Skipping software installation${NC}\n"
	fi


else
	printf "${BLUE}Please run this program with sudo privalges!${NC}\n"
fi