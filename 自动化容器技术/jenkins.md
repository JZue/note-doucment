## linux  下安装 jenkins 

 1. 安装jdk

 2. 安装Jenkins

 3. 为jenkins 配置jdk环境

    ​	vi /etc/rc.d/init.d/jenkins

    ​	然后加上自己的jdk的路径


## 配置jenkins及遇到的坑

1. 首次登陆以后，安装插件，卡住
        网上试了诸多方法，最终解决问题是方式是重启下服务，然后再登陆resume，然后发现还是有没有一个插件没有下载好，再重启下服务，终于解决了。

