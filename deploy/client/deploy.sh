#!/bin/bash

REPOSITORY=/home/ec2-user/SouP/dep
PROJECT_NAME=SouP

cd $REPOSITORY/zip

. ~/.nvm/nvm.sh
echo "> npx 버전"
npx -v

echo "> 3000번 포트 죽이기"
lsof -i:3000
kill -9 [PID]

echo "> react 실행"
npx next start