#### 用途

1. 用于在spring容器刷新之前初始化Spring ConfigurableApplicationContext的回调接口；

2. 通常用于需要对应用程序上下文进行编程初始化的web应用程序中
3. 可排序的（实现Ordered接口，或者添加@Order注解）

#### 用法

两种用法

1. 自定义```ApplicationContextInitializer ```接口的实现类，并实现逻辑。用```SpringApplication.addInitializers```,将自己新定义的Initializer加进去。
2. 自定义```ApplicationContextInitializer```接口的实现类，并实现逻辑。然后自定义```spring.factories```，新加一行自己的配置。

#### 用法demo 源码

 https://github.com/JZue/spring-study.git   

moudule:demo_applicationcontextinitializer

#### 源码分析

**什么时机获取相关的spring.factories的配置？**

1. SpringApplication.class的构造函数，

```setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));```的这一行。

**什么时机执行相关的代码逻辑？**

SpringApplication.run的prepareContext（刷新应用上下文前的准备阶段）往里看

```java
public ConfigurableApplicationContext run(String... args) {			
        
    ...

    context = createApplicationContext();
    //实例化SpringBootExceptionReporter.class，用来支持报告关于启动的错误
    exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
            new Class[] { ConfigurableApplicationContext.class }, context);
    // 刷新应用上下文前的准备阶段
    prepareContext(context, environment, listeners, applicationArguments, printedBanner);
    //5、刷新应用上下文
    refreshContext(context);
    //刷新应用上下文后的扩展接口
    afterRefresh(context, applicationArguments);
    //时间记录停止
    stopWatch.stop();
    if (this.logStartupInfo) {
        new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), stopWatch);
    }
    //发布容器启动完成事件
    listeners.started(context);
    callRunners(context, applicationArguments);
        
    ...

}
```

prepareContext方法

```java
private void prepareContext(ConfigurableApplicationContext context, ConfigurableEnvironment environment,
                            SpringApplicationRunListeners listeners, ApplicationArguments applicationArguments, Banner printedBanner) {
    //设置容器环境
    context.setEnvironment(environment);
    //执行容器后置处理
    postProcessApplicationContext(context);
    //执行容器中的 ApplicationContextInitializer 包括spring.factories和通过三种方式自定义的
    applyInitializers(context);
    
    ...

}		
```

执行方法：applyInitializers

```java
protected void applyInitializers(ConfigurableApplicationContext context) {
    // for循环执行
    for (ApplicationContextInitializer initializer : getInitializers()) {
        Class<?> requiredType = GenericTypeResolver.resolveTypeArgument(initializer.getClass(),
                ApplicationContextInitializer.class);
        Assert.isInstanceOf(requiredType, context, "Unable to call initializer.");
        initializer.initialize(context);
    }
}
```

