#### 接受所有ip的数据包转发

```

vim /lib/systemd/system/docker.service
#找到ExecStart=xxx，在这行上面加入一行，内容如下：(k8s的网络需要)
ExecStartPost=/sbin/iptables -I FORWARD -s 0.0.0.0/0 -j ACCEPT

```

#### 重启服务

```
 systemctl daemon-reload
 service docker restart
```

#### 禁用防火墙

```
# 禁用
systemctl stop firewalld.service
# 查看状态
systemctl status firewalld.service
```

