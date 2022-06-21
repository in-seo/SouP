#!/bin/bash

REPOSITORY=/home/ec2-user/SouP/dep
PROJECT_NAME=SouP

cd $REPOSITORY/zip

. ~/.nvm/nvm.sh
echo "> npm 버전"
npm -v


echo "> react 실행"
cd .next
npm start