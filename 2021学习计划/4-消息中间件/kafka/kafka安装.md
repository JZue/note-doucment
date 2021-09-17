处于简便 docker搭建demo环境



https://www.cnblogs.com/yaohuiqin/p/12530245.html

拉取kafka镜像

```
docker pull wurstmeister/kafka:2.7.1
```

拉取zk镜像

```
docker pull zookeeper:3.5.7
```

启动zk,其中的bridge网桥，参考docker-network

```
docker run --net=bridge--name yhq_zookeeper1 -p 21810:2181 -d zookeeper:3.5.7
```

查看zk连接到bridge 网络的ip

```
方式1：我用的桥接网络可以参考【docker-network】一文，所以可以直接查看容器的docker network inspect bridge

方式2：也可以直接查看zk的网络：docker inspect yhq_zookeeper1
```

创建kafka容器

```
docker run --net=bridge --name yhq_kafka1 -p 9093:9092 \
-e KAFKA_ZOOKEEPER_CONNECT=172.17.0.5:2181 \
-e KAFKA_ADVERTISED_HOST_NAME=x.x.x.x \
-e KAFKA_ADVERTISED_PORT=9092 \
-d wurstmeister/kafka

KAFKA_ADVERTISED_HOST_NAME  外网访问ip，填自己的，本机访问可以填127.0.0.1
KAFKA_ADVERTISED_PORT  外网访问端口
KAFKA_ZOOKEEPER_CONNECT  连接zk配置
```



```
	
docker exec -it yhq_kafka1 bash


cd /opt/kafka_2.13-2.7.1/config


server.properties和zookeeper.properties  可以看到已经配置好zk等相关配置
```

Kafka 查看当前的topic列表

```
cd /opt/kafka_2.13-2.7.1/bin

kafka-topics.sh --list  --zookeeper 172.17.0.5:2181
```

创建一个topic

```
cd /opt/kafka_2.13-2.7.1/bin

kafka-topics.sh --create --zookeeper 172.17.0.5:2181 --replication-factor 1 --partitions 1 --topic createtopic1
```

