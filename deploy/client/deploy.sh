#!/bin/bash

REPOSITORY=/home/ec2-user/SouP/dep/client
PROJECT_NAME=SouP


echo "> deploy client"

pm2 restart soup-client