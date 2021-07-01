虚拟机中容器间的通信--桥接网络

https://www.imooc.com/article/296815



https://www.cnblogs.com/xuezhigu/p/8257129.html

```
docker network create --driver bridge <network>
```

创建桥接网络后，默认会使用桥接网络，如果是先创建的其他服务，后创建的网络，重启docker后可生效

```
netstat -tunpl  查看网络端口监听状况

重启docker
systemctl restart docker
```

