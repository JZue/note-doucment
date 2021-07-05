如果是通过包管理工具安装的话，那就和包管理工具卸载

yum remove nodejs npm -y



\## 配置需要的大版本 setup_12.x 

```
 curl -sL https://rpm.nodesource.com/setup_12.x | sudo bash -
 
 
 sudo yum install nodejs
## 查看版本
node --version
```

