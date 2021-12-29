### centos 7 安装Docker

```
yum list installed | grep docker

yum -y remove xxxxxx


yum install -y yum-utils

yum-config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

yum list docker-ce --showduplicates|sort -r

yum install docker-ce-18.06.1.ce-3.el7 docker-ce-selinux-18.06.1.ce-3.el7


systemctl enable docker && sudo systemctl start docker

docker --version
```

