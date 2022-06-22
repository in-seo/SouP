#!/bin/bash

REPOSITORY=/home/ec2-user/SouP/dep/server
PROJECT_NAME=SouP

# cp $REPOSITORY/zip/*.jar $REPOSITORY/

exit 0

echo "> 서버 pid 확인"

CURRENT_PID=$(pgrep -fl SouP | grep jar | awk '{print $1}')

echo "> 서버 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 구동 중인 서버가 없음"
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 서버 배포"

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar $JAR_NAME --spring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ec2-user/SouP/application-oauth.properties,/home/ec2-user/SouP/application-real-db.properties > $REPOSITORY/nohup-server.out 2>&1 &