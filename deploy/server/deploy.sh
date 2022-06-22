#!/bin/bash

REPOSITORY=/home/ec2-user/SouP/dep/server
PROJECT_NAME=SouP


echo "> deploy server"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
chmod +x $JAR_NAME
mv -f $JAR_NAME $REPOSITORY/server.jar

pm2 restart soup-server