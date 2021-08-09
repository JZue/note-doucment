



```java
public ConfigurableApplicationContext run(String... args) {
		//  StopWatch 是org.springframework.util包下的一个小工具，
		//  用于对程序部分代码进行计时(ms级别)，适用于同步单线程代码块，里面就是简单的几个方法，没有复杂逻辑
		//  https://blog.csdn.net/gxs1688/article/details/87185030
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();

		ConfigurableApplicationContext context = null;
		Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
		// java.awt.headless是J2SE的一种模式用于在缺少显示屏、键盘或者鼠标时的系统配置，
		// 很多监控工具如jconsole 需要将该值设置为true，系统变量默认为true
		configureHeadlessProperty();
		// 1）获取并启动监听器。详解请看getRunListeners注释
		// SpringApplicationRunListeners 是一个SpringApplicationRunListener的批量管理类
		SpringApplicationRunListeners listeners = getRunListeners(args);
		listeners.starting();
		try {
			// ApplicationArguments:Provides access to the arguments that were used to run a {@link SpringApplication}.
			// ApplicationArguments:提供解析好的SpringApplication#run的参数
			ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
			// 2）创建运行环境environment，创建当前SpringBoot应用要使用的environment
			ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
			// 处理需要忽略的Bean
			configureIgnoreBeanInfo(environment);
			// 打印banner
			Banner printedBanner = printBanner(environment);
			// 3）初始化应用上下文
			context = createApplicationContext();
			// 实例化SpringBootExceptionReporter.class，用来支持报告关于启动的错误
			exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
					new Class[] { ConfigurableApplicationContext.class }, context);
			// 4）刷新应用上下文前的准备阶段
			prepareContext(context, environment, listeners, applicationArguments, printedBanner);
			// 5）刷新应用上下文
			refreshContext(context);
			// 6）刷新应用上下文后的扩展接口
			afterRefresh(context, applicationArguments);
			//时间记录停止
			stopWatch.stop();
			if (this.logStartupInfo) {
				new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), stopWatch);
			}
			//发布容器启动完成事件
			listeners.started(context);
			callRunners(context, applicationArguments);
		}
		catch (Throwable ex) {
			handleRunFailure(context, ex, exceptionReporters, listeners);
			throw new IllegalStateException(ex);
		}

		try {
			listeners.running(context);
		}
		catch (Throwable ex) {
			handleRunFailure(context, ex, exceptionReporters, null);
			throw new IllegalStateException(ex);
		}
		return context;
	}
```



#### 核心步骤

1）获取并启动监听器。详解请看getRunListeners注释;【本文介绍】
2）创建运行环境environment，创建当前SpringBoot应用要使用的environment 【本文介绍】
3）初始化应用上下文 <本文介绍>
4）刷新应用上下文前的准备阶段
5）刷新应用上下文
6）刷新应用上下文后的扩展接口



#### 获取并启动监听器(SpringApplication#getRunListeners)

```java
	/**
	 * @param args 参数就是启动参数
	 * @return 返回值SpringApplicationRunListeners  本质上是SpringApplicationRunListener的一个集合，加上Log日志对象
	 *
	 * 逻辑上，getSpringFactoriesInstances ： 获取META-INF/spring.factories中的类型SpringApplicationRunListener的所有className
	 *  然后实例化
	 */	
 private SpringApplicationRunListeners getRunListeners(String[] args) {
		Class<?>[] types = new Class<?>[] { SpringApplication.class, String[].class };
		return new SpringApplicationRunListeners(logger,
				getSpringFactoriesInstances(SpringApplicationRunListener.class, types, this, args));
	}
	
/**
 * @param type 从 spring.factories 中获取此类型的class
 * @param parameterTypes 实例化时，获取构造方法用的参数类型集合
 * @param args 构造方法实例化对象时的参数
 * @param <T> 具体的类型
 * @return 返回的是spring.factories 中获取此类型的class的实例化对象的集合
 */
private <T> Collection<T> getSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes, Object... args) {
	ClassLoader classLoader = getClassLoader();
	// 从 spring.factories 中获取对应类型的不重复的names
	Set<String> names = new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(type, classLoader));
	// 实例化
	List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);
	AnnotationAwareOrderComparator.sort(instances);
	return instances;
}


// 根据classname 获取构造Class信息
// 然后根据参数类型获取构造方法
// 最后根据参数和构造方法实例化
private <T> List<T> createSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes,
		ClassLoader classLoader, Object[] args, Set<String> names) {
	List<T> instances = new ArrayList<>(names.size());
	for (String name : names) {
		try {
			Class<?> instanceClass = ClassUtils.forName(name, classLoader);
			Assert.isAssignable(type, instanceClass);
			Constructor<?> constructor = instanceClass.getDeclaredConstructor(parameterTypes);
			// 里面判断是否是Kotlin，是则走Kotlin的构造方法，否则通过正常构造方法，和构造方法参数生成实例。没有其他逻辑了
			T instance = (T) BeanUtils.instantiateClass(constructor, args);
			instances.add(instance);
		}
		catch (Throwable ex) {
			throw new IllegalArgumentException("Cannot instantiate " + type + " : " + name, ex);
		}
	}
	return instances;
}
```

