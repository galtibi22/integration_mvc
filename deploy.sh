#!/bin/bash
echo "pull last updates from bitbucket"
git pull
echo "kill the current version of the server"
pkill -9 java
echo "Compile and start new version of the server in the background"
nohup mvn spring-boot:run -Dspring.config.location="file:///home/ec2-user/app/master/src/main/resources/aws.properties" &
sleep 15
ps aux |grep java

