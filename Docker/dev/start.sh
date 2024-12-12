#!/bin/bash

# 定义变量
JAR_NAME="/Users/susir/Documents/HdSource/HdbsThorStormbreaker/hdbsthor-012-airflow/target/hdbsthor-012-airflow.jar"
JAVA_OPTS="-Duser.timezone=GMT+08"
APP_ARGS="--server.port=60012 --spring.profiles.active=dev"

# 运行命令
java $JAVA_OPTS -jar $JAR_NAME $APP_ARGS