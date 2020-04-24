#!/bin/bash

clear

export JAVA_HOME=/home/JDK8
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib
export PATH=$PATH:${JAVA_HOME}/bin:${JRE_HOME}/bin

start="nohup java -server -Djava.awt.headless=true -Dfile.encoding=utf8"
heap=" -Xmx256m -Xms256m"
gc=" -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps"
keyValue=" -Dlog.path=/home/springboot/log -Dlogging.config=classpath:logback-production.xml"

apply() {
	name=$1
	path=$2
	cmd="ps -ef | grep '${name}-1.0.0' | grep -v 'grep' | awk '{print \$2}'"
	pid=$("eval $cmd")
	kill -9 ${pid}
	echo "干掉进程${name}, 重启中..."
	cd /home/springboot/

	if [[ ! -d ${path} ]]; then
		mkdir ${path}
	fi

	cmd=${start}${heap}${gc}${keyValue}" -jar "${name}"-1.0.0.jar > "${path}"/api.log 2>&1 &"
	eval ${cmd}
	tail -f -n 200 ${path}/api.log
}

if [[ "$1" = "eureka" ]]; then
	apply "eureka" "log/eureka"
elif [[ "$1" = "config" ]]; then
	apply "config" "log/config"
elif [[ "$1" = "web" ]]; then
	apply "web" "log/api"
else
	echo "未能执行任务操作，请输入参数。"
fi