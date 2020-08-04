```
# 安装ssh，一般系统默认会有的，如果没有的话需要自己安装。
apt-get install openssh-server openssh-client
apt-get install ssh

# 输入sudo ps -ef |grep ssh 查看是否启动服务，没有的话输入下面命令启动
/etc/init.d/ssh start


#将PermitRootLogin prohibit-password 这一行注释掉，并在下边添加一行PermitRootLogin yes
vim /etc/ssh/sshd_config
PermitRootLogin yes
#重启
/etc/init.d/ssh restart


ufw disable
ufw allow 22
```

