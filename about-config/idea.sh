# VM options
java -Dlog.path=/output/springboot -Dlogging.config=classpath:logback-test.xml

# 查询所有的JAVA进程ID
pgrep -ef | grep java | awk 'NR{printf $2" ";next}'

# IDEA静态文件无法关联问题
echo '点击右上角 项目结构 图标 》Project Settings 》Modules 》选择一个Module 》点击右键 》Add 》Web 》Web Resource Directories 》添加路径即可'

#SSH无密码上传文件
ssh-keygen -t rsa -P ''
ssh-copy-id -i ~/.ssh/id_rsa.pub root@127.0.0.1

#SVN证书失效问题
svn ls https://192.168.0.122/svn/web

#Ubuntu修改时区
dpkg-reconfigure tzdata
