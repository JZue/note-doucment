[镜像加速配置](https://www.cnblogs.com/liyuanhong/p/13683783.html)





# docker启动的时候提示WARNING: IPv4 forwarding is disabled. Networking will not work.

修改配置文件：

vim /usr/lib/sysctl.d/00-system.conf

追加

 net.ipv4.ip_forward=1

接着重启网络

 systemctl restart network



然后将之前错误的docker删掉，重新创建启动即可