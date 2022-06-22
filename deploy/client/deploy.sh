#!/bin/bash

REPOSITORY=/home/ec2-user/SouP/dep/client
PROJECT_NAME=SouP

exit 0

. ~/.nvm/nvm.sh
echo "> npx 버전"
npx -v


echo "> react 실행"
nohup npx next start > $REPOSITORY/nohup-client.out 2>&1 &