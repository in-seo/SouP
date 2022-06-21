#!/bin/bash

REPOSITORY=/home/ec2-user/SouP/dep
PROJECT_NAME=SouP

cd $REPOSITORY/zip

. ~/.nvm/nvm.sh

echo "> react 실행"
npm start