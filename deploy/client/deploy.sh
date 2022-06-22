#!/bin/bash

REPOSITORY=/home/ec2-user/SouP/dep/client
PROJECT_NAME=SouP

export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"

echo "> deploy client"

pm2 restart soup-client