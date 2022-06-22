#!/bin/bash

REPOSITORY=/home/ec2-user/SouP/dep
PROJECT_NAME=SouP

cd $REPOSITORY/zip

. ~/.nvm/nvm.sh
echo "> npx 버전"
npx -v


echo "> react 실행"
nohup npx next start > $REPOSITORY/nohup-client.out 2>&1 &