#!/bin/bash

ROOT_PATH="/home/ubuntu/spring-github-action"
JAR="$ROOT_PATH/HanghaeTinder_BE-main-0.0.1-SNAPSHOT.jar"

APP_LOG="$ROOT_PATH/application.log"
ERROR_LOG="$ROOT_PATH/error.log"
START_LOG="$ROOT_PATH/start.log"

NOW=$(date +%c)

echo "[$NOW] 이전에 실행 중인 서비스를 종료합니다." >> $START_LOG
SERVICE_PIDS=$(pgrep -f "java -jar")
for SERVICE_PID in $SERVICE_PIDS; do
  SERVICE_JAR=$(readlink -f /proc/$SERVICE_PID/exe | cut -d' ' -f1-)
  if [[ "$SERVICE_JAR" =~ .*\.jar$ ]]; then
    kill $SERVICE_PID
    echo "[$NOW] > 종료한 서비스: $SERVICE_JAR, PID: $SERVICE_PID" >> $START_LOG
  fi
done

echo "[$NOW] $JAR 파일을 복사합니다." >> $START_LOG
cp $ROOT_PATH/build/libs/*.jar $JAR

echo "[$NOW] $JAR 파일을 실행합니다." >> $START_LOG
nohup java -jar $JAR > $APP_LOG 2> $ERROR_LOG &

SERVICE_PID=$(pgrep -f $JAR)
echo "[$NOW] > 새로운 서비스 PID: $SERVICE_PID" >> $START_LOG
