#!/bin/bash
clear

export JAVA_HOME=/home/JDK8
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=$PATH:${JAVA_HOME}/bin:${JRE_HOME}/bin

start="nohup java -server -Djava.awt.headless=true -Dfile.encoding=utf8 "
heap=" -Xmx4g -Xms4g"
gc=" -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps "
keyValue=" -Dlog.path=/home/springboot/log -Dlogging.config=classpath:logback-production.xml"

apply() {
    name=$1
    path=$2
    pid=$(ps -ef | grep ${name}.jar | grep -v grep | awk '{print \$2}')
    kill -9 ${pid}
    echo "干掉进程${name}, 重启中..."
    cd /home/springboot/

    if [[ ! -d ${path} ]]; then
        mkdir ${path}
    fi

    cmd=${start}${heap}${gc}${keyValue}" -jar "${name}".jar > "${path}"/api.log &"
    echo ${cmd}
    eval ${cmd}
    tail -f -n 200 ${path}/api.log
}

if [[ "$1" == "admin" ]]; then
    apply "platform-admin" "log/api"
elif [[ "$1" == "api" ]]; then
    apply "platform-api" "log/api"
else
    echo "未能执行任务操作，请输入参数。"
fi
