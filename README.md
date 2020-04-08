# Spring Cloud 一点通
Spring Cloud 基础的脚手架，功能集全。

端口架构

```
390x    zookeeper 对外通信端口
391x    zookeeper 内部通信端口
392x    zookeeper 选举端口
394x    kafka 通信端口
3949    kafka manager(CMAK) 网页端口
395x    elasticsearch 通信端口
396x    elasticsearch HTTP端口
```

### ElasticSearch 集群安装方法
* 从ElasticSearch(以下简称ES)官网下载[该软件](https://www.elastic.co/cn/elasticsearch)，解压到本地目录。以我的电脑为例，解压到
```
cd /Users/murphy/Downloads/soft/elasticsearch/master/
```
* 我们先在master文件夹创建cert.yml文件，内容如下
```yaml
instances:
  - name: "elasticsearch"
    ip: 
      - "127.0.0.1" #本地IP
      - "172.20.10.8" #内网IP，你还可以使用外网IP
```
* 给ES增强安全，生成PEM格式证书
```
bin/elasticsearch-certutil cert --pem -in cert.yml -out es.zip
```
* 它会生成es.zip到你当前的目录，解压后有3个文件
* 把这3个文件单独拷贝到master文件夹所在的config文件夹里
* 修改配置文件config/elasticsearch.yml, 内容如下
```yaml
cluster.name: ElasticSearchCluster
node.name: master
node.master: true
path.data: data
path.logs: logs
network.host: 127.0.0.1
http.port: 3961
transport.tcp.port: 3951
discovery.seed_hosts: ["127.0.0.1:3951","127.0.0.1:3952","127.0.0.1:3953"]
cluster.initial_master_nodes: ["master"]
xpack.security.enabled: true
xpack.security.http.ssl.enabled: true
xpack.security.transport.ssl.enabled: true
xpack.security.http.ssl.key: elasticsearch.key
xpack.security.http.ssl.certificate: elasticsearch.crt
xpack.security.http.ssl.certificate_authorities: ca.crt
xpack.security.transport.ssl.key: elasticsearch.key
xpack.security.transport.ssl.certificate: elasticsearch.crt
xpack.security.transport.ssl.certificate_authorities: ca.crt
```
* 以上是第一个ES实例master，现在我们来启动该实例，日志是在logs/ElasticSearchCluster.log
```
bin/elasticsearch -d
```
* 安装中文分词插件 ik
```
bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v7.6.2/elasticsearch-analysis-ik-7.6.2.zip
```
* 统一修改 Elastic Stack 各组件的密码(elastic, apm, kibana等等组件)，我这里所有的密码全部填123123
```
bin/elasticsearch-setup-passwords interactive
```
* 复制文件夹master并改名为slave2,删掉里面的logs和data文件夹，修改部分配置，未提及的不需要改动
```yaml
node.name: slave2
node.master: false
http.port: 3962
transport.tcp.port: 3952
```
* 以上是第二个ES实例slave2，现在我们来启动该实例
```
cd /Users/murphy/Downloads/soft/elasticsearch/slave2
bin/elasticsearch -d
```
* 复制文件夹master并改名为slave3,删掉里面的logs和data文件夹，修改部分配置，未提及的不需要改动
```yaml
node.name: slave3
node.master: false
http.port: 3963
transport.tcp.port: 3953
```
* 以上是第二个ES实例slave3，现在我们来启动该实例
```
cd /Users/murphy/Downloads/soft/elasticsearch/slave3
bin/elasticsearch -d
```
* 三个实例已经全部安装完成，接口网址是
* https://127.0.0.1:3961 或者 https://127.0.0.1:3962 或者 https://127.0.0.1:3963 
* 用户名是elastic，密码是你修改的密码
* 注意：由于谷歌浏览器不支持[https://127.0.0.1](https://127.0.0.1),建议使用其它浏览器接口网址。