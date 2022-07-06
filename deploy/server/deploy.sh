#!/bin/bash

REPOSITORY=/home/ec2-user/SouP/dep/server
PROJECT_NAME=SouP

export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"

echo "> deploy server"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
chmod +x $JAR_NAME
mv -f $JAR_NAME $REPOSITORY/server.jar

cd /home/ec2-user/SouP
pm2 restart soup-server