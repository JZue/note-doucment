## 本地环境

```
maven:   apache-maven-3.6.1
jdk:     java version "1.8.0_192"
源码版本： 2.1.6.RELEASE
IDE:     社区版本
```

## 本地搭建源码环境

```
1)  git clone https://github.com/spring-projects/spring-boot   
    或者直接下载源码解压

2)  idea->import from existing sources ->选中pom.xml

3)  执行编译命令  mvn clean install -DskipTests -Pfast（有小伙伴问，为什么我import的源码文件夹没有正常的被标记source/Resource/Test... 先编译，就不会有这个问题了）

done
```



