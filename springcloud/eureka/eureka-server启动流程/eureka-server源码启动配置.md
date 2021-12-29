### 环境

* JDK
* IntelliJ   IDEA

* Gradle
* Tomcat

大家本地环境自己各自配置

#### 下载源码

git clone https://github.com/Netflix/eureka.git
或者直接下载(https://github.com/Netflix/eureka)
版本自选

然后IDEA 选择下图的build.gradle，即可正常导入

![import-eurekamaster](/Users/jzue/Desktop/blog_file/文章图片/eureka/eureka-server源码启动配置/import-eurekamaster.png)



导入之后，下载依赖。

然后打包：点击右侧eureka->Tasks->build->war

开始打包。

然后IDEA配置Tomcat

<https://www.cnblogs.com/jtlgb/p/8465228.html>



环境准备成功。