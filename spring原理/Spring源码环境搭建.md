##（一）获取源码

方式1

```
git clone https://github.com/spring-projects/spring-framework.git
```

方式2（推荐，快很多）

```
git clone https://gitee.com/mirrors/Spring-Framework.git
```

## （二）修改配置



#### 修改build.gradle

如图所示文件

#### <img src="http://file.xjzspace.com/20210729161952.png" alt="企业微信截图_e15e2796-594a-4a3d-904d-91dfb918281b" style="zoom: 50%;" />

第一步：打开文件，搜索repositories，添加阿里云maven镜像仓库（有的版本可能会搜到多个，每一个都替换成这个）

```groovy
repositories {
	maven{ url 'https://maven.aliyun.com/nexus/content/groups/public/'}
  maven{ url 'https://maven.aliyun.com/nexus/content/repositories/jcenter'}
	mavenCentral()
	maven { url "https://repo.spring.io/libs-spring-framework-build" }
}
```

前面俩是新加的阿里云配置，后面俩是之前默认的，后面俩最好不要删除，避免在阿里云找不到对应的jar,会去后面这俩取

第二步：搜索`configurations`，将其注释掉（有的版本的configuration.all配置不完全一样，也可同样操作）

```
	// configurations.all {
	// 	resolutionStrategy {
	// 		cacheChangingModulesFor 0, "seconds"
	// 		cacheDynamicVersionsFor 0, "seconds"
	// 	}
	// }
```

#### 修改settings.gradle

`repositories`加上阿里云的仓库地址，其余两个不要动。如下所示：

```
pluginManagement {
	repositories {
		maven { url "https://maven.aliyun.com/repository/public" }
		gradlePluginPortal()
		maven { url 'https://repo.spring.io/plugins-release' }
	}
}
```



#### 修改gradle-wrapper.properties

文件位置：Spring-Framework/gradle/wrapper/gradle-wrapper.properties

```
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-6.8.3-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

拿着distributionUrl  (https://services.gradle.org/distributions/gradle-4.10.3-bin.zip)这个url,下载对应的gradle.zip，下载完成之后，建议直接放在和这个配置文件同目录，

<img src="http://file.xjzspace.com/20210729194049.png" alt="企业微信截图_4583fe87-52a4-4975-bdb7-cbef20cbddc8" style="zoom:50%;" />

然后修改对应的配置如下，（你下载的什么版本就是什么版本的gradle，但是建议用默认的spring框架对应推荐的版本，避免踩坑）

```
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=gradle-6.8.3-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```


#### 执行编译命令

```
./gradlew :spring-oxm:compileTestJava
```

<img src="http://file.xjzspace.com/20210729193926.png" alt="企业微信截图_4cddc44e-8108-4b27-9f26-d68f5eb532a8" style="zoom:50%;" />

编译完成之后，

## 导入IDEA

#### 导入

Open or Import   ==>  选中刚才修改的build.gradle 导入即可

#### 配置环境

导入以后，配置环境（这个是我本地自己装的gradle对应的目录，可以不装，如果要装，自己搜教程吧）

<img src="http://file.xjzspace.com/20210729194408.png" alt="企业微信截图_b2537dc5-e550-4f88-a2ee-7b0de43a15a7" style="zoom:25%;" />

配置好环境以后，然后点击IDEA右侧的gradle=>refresh

<img src="http://file.xjzspace.com/20210729194518.png" alt="企业微信截图_0f9857f6-aadd-495f-8aed-ebfaeb004756" style="zoom:25%;" />

在等待编译完成的过程中，可以配置下jdk

Project Structure==>ProjectSettings  == > Project 配置对应的jdk

<img src="http://file.xjzspace.com/20210729195009.png" alt="企业微信截图_d26f1ec8-9293-418d-893e-6914c6bc54b6" style="zoom:33%;" />

Refresh 之后会编译，我们就可以正常的来搭建自己的demo了

## DEMO

新建demo的module

<img src="http://file.xjzspace.com/20210729211707.png" style="zoom: 50%;" />



<img src="http://file.xjzspace.com/20210729211856.png" alt="企业微信截图_32740d7a-a464-4ca6-9253-b269d4a1a7ca" style="zoom:50%;" />



build.gradle 需要改成你新加的moudule名对应的名字，否则就会出现下面的找不到包的报错;

我们用到的spring-context ，索引spring-demo.gradle 中需要引入对应的包,配置如下

```groovy
dependencies {
    testCompile group: 'junit', name: 'junit', version: '4.12'

    compile(project(":spring-context"))
}
```



<img src="http://file.xjzspace.com/20210729221323.png" alt="demo" style="zoom:50%;" />



#### demo的代码

```java

@ComponentScan("com.spring.demo")
public class DemoMain {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(MyDemoBean.class);
	}
}


@Component
public class MyDemoBean {
	public MyDemoBean(){
		System.out.println("=====spring demo ==============");
	}
}

```

正确打印=====spring demo ============== 则说明已经完成

教程结束