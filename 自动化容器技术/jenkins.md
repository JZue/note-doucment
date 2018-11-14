## linux  下安装 jenkins 
jdk:JAVA_HOME=/usr/java/jdk1.8.0_141
git: /usr/local/git/bin
 1. 安装jdk  JAVA_HOME=/usr/java/jdk1.8.0_141

 2. 安装Jenkins

 3. 为jenkins 配置jdk环境

    ​	vi /etc/rc.d/init.d/jenkins

    ​	然后加上自己的jdk的路径


## 配置jenkins及遇到的坑

1. 首次登陆以后，安装插件，卡住
        网上试了诸多方法，最终解决问题是方式是重启下服务，然后再登陆resume，然后发现还是有一个插件没有下载好，再重启下服务，再resume，终于解决了。
2. 系统管理=》可选插件    然后选择自己 想要安装的插件

3. jenkins 插件只是支持功能，实现功能还需要在本地安装对应的环境，例如maven 则需要你自己jenkins
服务器上也配置环境，或者你在jenkins上  jenkins=》全局工具配置中自己配置自动下载

4. jenkins 系统配置中的github服务器的用法：
      ssh的秘钥：公钥配在github，私钥在系统配置的github服务器的凭据点击Add选择SSH Username with private key类型的凭据，   参数：
                    username随意，
                    private Key 为你的私钥内容或者配在你的jenkins服务器中就不用配置凭据了，
                    Passphrase 在你创建ssh key的时候如果填了，就填你填的内容，如果没填，空着，
                    id空着，
                    描述空着
    usernameAndPassword：在你项目配置的时候会有一个git地址填写栏，然后下面会有一个凭据，添加或者选择一个你创建的此类型的凭据即可

5. 在服务器上可以git clone 下github的代码，jenkins上一直都clone不下来吗，没找到到原因，但换成gitee（码云）就没问题； 用jenkins 连github一直都超时，应该是网络原因·  
