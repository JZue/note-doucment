## Spring事件机制的结构分析

Spring的事件机制是基于观察者模式设计的。下图是一个观察者模式的结构图。

不太熟悉观察者模式的小伙伴可以[参考](https://www.runoob.com/design-pattern/observer-pattern.html)

![观察者模式、spring事件机制](http://file.xjzspace.com/20210817104127.png)

根据观察者模式的结构图，先提出两个问题。

1）现在的subject 是有存对应的observer的，这两者的对应关系是否可以完全抽离一个公共的管理者实现解耦？

2）每个subject 都需要实现notify 对应observer的逻辑，并且这个逻辑对于所有的subject也是类似的，是否可以抽离？

基于上面的两个问题，Spring事件机制的实现，如下图

![spring事件机制 (1)](http://file.xjzspace.com/20210817215132.png)

ApplicationEvent  对应的是Subject

ApplicationListener对应的是Observer

ApplicationMultiCaster则是为了解决上面的两个问题。

ApplicationEventPublisher则是消息发布者，消息发布者将自己的需求给ApplicationMultiCaster，ApplicationMultiCaster替他发布给各个Observer

**注意点：图中ApplicationEvent 对应了ApplicationEventPublisher，但是他们之间没有对应关系，ApplicationEventPublisher，可以发布各种各样的消息，所以用的是粗箭头。**

这么实现的好处是什么呢？

对于使用者来说，更简单了。使用者只用实现对应的event,实现对应的applicationListener，那么就可以完成对应的事件的发布和监听

## 使用案例

```
用户发布的事件类型可以是：
1. 用户可以继承ApplicationEvent从而自定义Event类型
2. 也可以使用任意Object类型，但是如果event真实类型不是ApplicationEvent的话，那么event会被封装成PayloadApplicationEvent()

监听者的实现可以是：
1. 实现ApplicationListener接口
2. 使用默认的PayloadApplicationListener
3. @Listener注解
```

关于发布出去的事件，那些监听者会监听到？

1. 发布的事件类型是ApplicationEvent的实现类A
    那么所有监听者的`onApplicationEvent`的参数类型是A或者A的子类都会收到事件。
2. 发布的事件类型是不是ApplicationEvent类型，类型是B
    这种情况下，最终事件会被包装成`PayloadApplicationEvent<B>`, 那么所有监听者方法`onApplicationEvent`的参数是`PayloadApplicationEvent<B>`的监听者会收到， 假设有C是B的父类，且有一个监听者X监听`PayloadApplicationEvent<C>`,那X是收不到`PayloadApplicationEvent<B>`类型的事件的



**如下使用案例源码：https://github.com/JZue/spring-study.git    moudule:demo_applicationlistener**

#### 方式一:继承ApplicationEvent自定义Event类型

Event:

```java
import org.springframework.context.ApplicationEvent;
public class MyApplicationEvent extends ApplicationEvent {
    public MyApplicationEvent(Object source) {
        super(source);
    }
}
```

ApplicationListener

```java
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
@Component
public class MyApplicationListener implements ApplicationListener<MyApplicationEvent> {
    @Override
    public void onApplicationEvent(MyApplicationEvent myApplicationEvent) {
        System.out.println("MyApplicationListener==============>"+myApplicationEvent.getSource());
    }
}
```

usage

```java
@SpringBootApplication
public class DemoApplicationlistnerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DemoApplicationlistnerApplication.class, args);
        run.publishEvent(new MyApplicationEvent("MyApplicationEvent"));
    }

}
```

#### 方式二:使用任意Object类型

ApplicationListener

```java
import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.stereotype.Component;
@Component
public class PayloadApplicationListener implements ApplicationListener<PayloadApplicationEvent<String>> {
    @Override
    public void onApplicationEvent(PayloadApplicationEvent<String> stringPayloadApplicationEvent) {
        System.out.println("PayloadApplicationListener："+stringPayloadApplicationEvent.getPayload());
    }
}
```

event

```java
public class MyApplicationEvent extends ApplicationEvent {
    public MyApplicationEvent(Object source) {
        super(source);
    }
}
```



usage

```java
@SpringBootApplication
public class DemoApplicationlistnerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DemoApplicationlistnerApplication.class, args);
        run.publishEvent("payload event");
    }

}
```

#### 方式三：基于@EventListener注解实现Listener

Listener

```java
@Component
public class MyEventListener {

    @EventListener
    public void eventApplicationEvent(MyApplicationEvent myApplicationEvent){
        System.out.println("EventListener:ApplicationEvent"+myApplicationEvent.getSource());
    }
    @EventListener
    public void eventPayload(PayloadApplicationEvent<String> myApplicationEvent){
        System.out.println("EventListener:eventPayload"+myApplicationEvent.getPayload());
    }
}
```



Pubsher:

```
@SpringBootApplication
public class DemoApplicationlistnerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DemoApplicationlistnerApplication.class, args);
        run.publishEvent(new MyApplicationEvent("MyApplicationEvent"));
        run.publishEvent("payload event");
    }

}

```



## 关键源码分析

**Spring在ApplicationContext接口的抽象实现类AbstractApplicationContext中完成了事件体系的搭建。**

Spring事件机制核心流程

1. 初始化ApplicationEventMultiCaster       (AbstractApplicationContext#refresh)
2. 注册ApplicationListener到ApplicationEventMultiCaster
   1. refresh->registerListeners   将Listener注册到ApplicationEventMultiCaster（?为什么有这一步，还有下一步呢，为了懒加载的漏网之鱼）  (AbstractApplicationContext#refresh)
   2. 在初始化后BeanPostProcerssor中调用ApplicationListenerDetector将ApplicationListener添加至ApplicationEventMultiCaster    (BeanPostProcessor源码分析)

3. EventListener 注解解析过程
4. publishEvent 发布事件流程



####1.初始化ApplicationEventMultiCaster 

在容器启动方法refresh中，就会初始化对应的ApplicationEventMultiCaster 

代码位置：AbstractApplicationContext#refresh--》this.initApplicationEventMulticaster();

```java
// 如果容器中存在applicationEventMulticaster，则取容器中的实例，否则new SimpleApplicationEventMulticaster(beanFactory);并置于容器中
protected void initApplicationEventMulticaster() {
	ConfigurableListableBeanFactory beanFactory = this.getBeanFactory();

	if (beanFactory.containsLocalBean("applicationEventMulticaster")) {
		this.applicationEventMulticaster = (ApplicationEventMulticaster)beanFactory.getBean("applicationEventMulticaster", ApplicationEventMulticaster.class);
		if (this.logger.isTraceEnabled()) {
			this.logger.trace("Using ApplicationEventMulticaster [" + this.applicationEventMulticaster + "]");
		}
	} else {
		this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
		beanFactory.registerSingleton("applicationEventMulticaster", this.applicationEventMulticaster);
		if (this.logger.isTraceEnabled()) {
			this.logger.trace("No 'applicationEventMulticaster' bean, using [" + this.applicationEventMulticaster.getClass().getSimpleName() + "]");
		}
	}
}
```

#### 2.注册ApplicationListener到ApplicationEventMultiCaster

**refresh->registerListeners**

```java
protected void registerListeners() {
  //获取容器中所有的监听器对象
  // 这个时候正常流程是不会有监听器的
  // （因为监听器不会在这之前注册，在initApplicationEventMulticaster后在registerListeners之前，只有一个可能在：在onRefresh里面注册了监听器）
  for (ApplicationListener<?> listener : getApplicationListeners()) {
  //把监听器挨个的注册到我们的多播器上去
  	getApplicationEventMulticaster().addApplicationListener(listener);
  }


  //获取bean定义中的监听器对象
  String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
  //把监听器的名称注册到我们的多播器上
  for (String listenerBeanName : listenerBeanNames) {
  	getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
  }


  //在这里获取我们的早期事件
  Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
  // 在这里赋null。  也就是值此之后都将没有早期事件了
  this.earlyApplicationEvents = null;
  if (earlyEventsToProcess != null) {
  //通过多播器进行播发早期事件
  	for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
  		getApplicationEventMulticaster().multicastEvent(earlyEvent);
  	}
	}
}
```

**ApplicationListenerDetector注册对应的ApplicationListener**

```
为什么前面registerListeners的逻辑不讲所有的ApplicationListener注册到ApplicationEventMultiCaster？

主要针对Bean 懒加载（@Lazy注解）的情况，在registerListeners方法 是解析不到的。
```

 ApplicationListenerDetector：主要作用：

    1、在Bean初始化完成之后：如果Bean是单例的则并且bean instanceof ApplicationListener。加入到this.applicationListeners中。
    对应的是ApplicationListenerDetector#postProcessAfterInitialization方法
    
    2、在Bean销毁之前搞事情： 如果Bean是一个ApplicationListener，则会从ApplicationEventMulticaster（事件广播器）中提前删除了。
    对应的是ApplicationListenerDetector#postProcessBeforeDestruction方法
这块跟BeanPostProcessor相关的逻辑请参考[BeanPostProcessor待更新]()

#### 3.EventListener 注解解析过程

```java
EventListener 注解解析过程在EventListenerMethodProcessor实现；
EventListenerMethodProcessor实现了SmartInitializingSingleton接口;


SmartInitializingSingleton接口的作用：当所有单例 bean 都初始化完成以后， Spring的IOC容器会回调该接口的 afterSingletonsInstantiated()方法
```

SmartInitializingSingleton接口中afterSingletonsInstantiated在哪被调用的呢？

```java
AbstractApplicationContext#refresh->
finishBeanFactoryInitialization->
beanFactory.preInstantiateSingletons()-->
DefaultListableBeanFactory.preInstantiateSingletons-->
smartSingleton.afterSingletonsInstantiated();
```



核心源码：

```java
// 这个方法的主要功能就是，通过beanfactory获取所有的beannames，遍历每一beannames,然后取出对应的bean的classType,然后核心的逻辑在processBean(beanName, type);方法
public void afterSingletonsInstantiated() {
	ConfigurableListableBeanFactory beanFactory = this.beanFactory;
	Assert.state(this.beanFactory != null, "No ConfigurableListableBeanFactory set");
	String[] beanNames = beanFactory.getBeanNamesForType(Object.class);
	for (String beanName : beanNames) {
		if (!ScopedProxyUtils.isScopedTarget(beanName)) {
			Class<?> type = null;
			try {
				type = AutoProxyUtils.determineTargetClass(beanFactory, beanName);
			}
			catch (Throwable ex) {
				// An unresolvable bean type, probably from a lazy bean - let's ignore it.
				if (logger.isDebugEnabled()) {
					logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
				}
			}
			if (type != null) {
				if (ScopedObject.class.isAssignableFrom(type)) {
					try {
						Class<?> targetClass = AutoProxyUtils.determineTargetClass(
								beanFactory, ScopedProxyUtils.getTargetBeanName(beanName));
						if (targetClass != null) {
							type = targetClass;
						}
					}
					catch (Throwable ex) {
						// An invalid scoped proxy arrangement - let's ignore it.
						if (logger.isDebugEnabled()) {
							logger.debug("Could not resolve target bean for scoped proxy '" + beanName + "'", ex);
						}
					}
				}
				try {
					processBean(beanName, type);
				}
				catch (Throwable ex) {
					throw new BeanInitializationException("Failed to process @EventListener " +
							"annotation on bean with name '" + beanName + "'", ex);
				}
			}
		}
	}
}
```

```java
// nonAnnotatedClasses存储的是没有@EventListener注解标注方法的class
private void processBean(final String beanName, final Class<?> targetType) {
  //nonAnnotatedClasses里没有targetType才会走下面逻辑
	if (!this.nonAnnotatedClasses.contains(targetType) &&
			!targetType.getName().startsWith("java") &&
			!isSpringContainerClass(targetType)) {

		Map<Method, EventListener> annotatedMethods = null;
		try {
			// 拿到@EventListener注解标注的方法
			annotatedMethods = MethodIntrospector.selectMethods(targetType,
					(MethodIntrospector.MetadataLookup<EventListener>) method ->
							AnnotatedElementUtils.findMergedAnnotation(method, EventListener.class));
		}
		catch (Throwable ex) {
			// An unresolvable type in a method signature, probably from a lazy bean - let's ignore it.
			if (logger.isDebugEnabled()) {
				logger.debug("Could not resolve methods for bean with name '" + beanName + "'", ex);
			}
		}
    //  如果没有@EventListener注解标注的方法，则将对应的目标bean类型放入nonAnnotatedClasses
		if (CollectionUtils.isEmpty(annotatedMethods)) {
			this.nonAnnotatedClasses.add(targetType);
			if (logger.isTraceEnabled()) {
				logger.trace("No @EventListener annotations found on bean class: " + targetType.getName());
			}
		}
		else {
			// Non-empty set of methods
			ConfigurableApplicationContext context = this.applicationContext;
			Assert.state(context != null, "No ApplicationContext set");
      // eventListenerFactories是容器中所有的EventBeanFactory或子类的bean
			List<EventListenerFactory> factories = this.eventListenerFactories;
			Assert.state(factories != null, "EventListenerFactory List not initialized");
			for (Method method : annotatedMethods.keySet()) {
				for (EventListenerFactory factory : factories) {
					// 判断是否支持该方法  这里用的DefaultEventListenerFactory 5.1.8.RELEASE 写死的返回true
					if (factory.supportsMethod(method)) {
            // ?这一行的意义我也还没搞明白
						Method methodToUse = AopUtils.selectInvocableMethod(method, context.getType(beanName));
            // 替对应的方法，创建一个ApplicationListenerMethodAdapter对象
						ApplicationListener<?> applicationListener =
								factory.createApplicationListener(beanName, targetType, methodToUse);
            // 如果是ApplicationListenerMethodAdapter对象 就把context和evaluator传进去
						if (applicationListener instanceof ApplicationListenerMethodAdapter) {
							((ApplicationListenerMethodAdapter) applicationListener).init(context, this.evaluator);
						}
						context.addApplicationListener(applicationListener);
						break;
					}
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug(annotatedMethods.size() + " @EventListener methods processed on bean '" +
						beanName + "': " + annotatedMethods);
			}
		}
	}
}
```

####4.发布事件流程publishEvent 

AbstractApplicationContext#publishEvent 

```java
protected void publishEvent(Object event, @Nullable ResolvableType eventType) {
	Assert.notNull(event, "Event must not be null");
	Object applicationEvent;
	// 如果事件是ApplicationEvent实现类，那么applicationEvent 就是时间本身
	// 如果时间不是ApplicationEvent实现类，那么则会包装成PayloadApplicationEvent
	if (event instanceof ApplicationEvent) {
		applicationEvent = (ApplicationEvent)event;
	} else {
		applicationEvent = new PayloadApplicationEvent(this, event);
		if (eventType == null) {
			eventType = ((PayloadApplicationEvent)applicationEvent).getResolvableType();
		}
	}

	
  // 这里是唯一添加早期事件的地方。所以一定要发布事件才能添加早期事件
  // 只有当执行力refresh-->registerListeners 才会将earlyApplicationEvents赋为null,
  // 结合refresh->registerListeners
	if (this.earlyApplicationEvents != null) {
		this.earlyApplicationEvents.add(applicationEvent);
	} else {
		// 交给getApplicationEventMulticaster去发布
		this.getApplicationEventMulticaster().multicastEvent((ApplicationEvent)applicationEvent, eventType);
	}
	if (this.parent != null) {
		if (this.parent instanceof AbstractApplicationContext) {
			((AbstractApplicationContext)this.parent).publishEvent(event, eventType);
		} else {
			this.parent.publishEvent(event);
		}
	}

}
```

后面的这几段代码就很好理解了...

SimpleApplicationEventMulticaster#multicastEvent

```java
@Override
public void multicastEvent(final ApplicationEvent event, @Nullable ResolvableType eventType) {
	ResolvableType type = (eventType != null ? eventType : resolveDefaultEventType(event));
	Executor executor = getTaskExecutor();
	for (ApplicationListener<?> listener : getApplicationListeners(event, type)) {
    // 异步
		if (executor != null) {
			executor.execute(() -> invokeListener(listener, event));
		}
    // 同步
		else {
			invokeListener(listener, event);
		}
	}
}
```

```java
protected void invokeListener(ApplicationListener<?> listener, ApplicationEvent event) {
	ErrorHandler errorHandler = getErrorHandler();
	if (errorHandler != null) {
		try {
			doInvokeListener(listener, event);
		}
		catch (Throwable err) {
			errorHandler.handleError(err);
		}
	}
	else {
		doInvokeListener(listener, event);
	}
}
```

```java
private void doInvokeListener(ApplicationListener listener, ApplicationEvent event) {
	try {
		// 真正的调用就在此处！！！！！
		listener.onApplicationEvent(event);
	}
	catch (ClassCastException ex) {
		String msg = ex.getMessage();
		if (msg == null || matchesClassCastMessage(msg, event.getClass())) {
			// Possibly a lambda-defined listener which we could not resolve the generic event type for
			// -> let's suppress the exception and just log a debug message.
			Log logger = LogFactory.getLog(getClass());
			if (logger.isTraceEnabled()) {
				logger.trace("Non-matching event type for listener: " + listener, ex);
			}
		}
		else {
			throw ex;
		}
	}
}
```

至此，结束！！
