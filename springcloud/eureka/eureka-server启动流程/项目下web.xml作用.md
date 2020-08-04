## tomcat和项目下的web.xml区别

Tomcal下有一个web.xml，在项目文件夹中同样存在一个web.xml文件，**那这两个文件有什么区别呢**？

tomcat中的web.xml是通用的，如果不设置，那么就会默认是同tomcat的web.xml，如果你设置了，那么当然是项目下的web.xml中的设置优先权更高一点

我们可以用编程中的全局变量和局部变量来进行理解这个

```
加载顺序是
	tomcat conf目录下；
    项目目录下的；　　
　　 tomcat config目录下的为服务器全局作用域，一般用来配置全局设置、数据源等，而项目目录下的为局部作用域。
```





## 完整格式的web.xml样例

**仅样例，并非源码中的web.xml，主要用于描述web.xml结构**

```xml
<!--首先是表明xml的使用版本start-->
<?xml version="1.0" encoding="UTF-8"?>
<!--首先是表明xml的使用版本end-->
<!--web-app是web.xml的根元素，包含着web.xml所有子元素。xmlns以及xmlns:xsi后面引进的连接是表明web.xml引进的模式文件，便能拥有该模式的相关功能 start--> 
<web-app version="2.5"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
	http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">
<!--end-->
    
    <!--指明项目名称start--> 
  	<display-name>web-SSMTest</display-name>
     <!--指明项目名称end--> 
    
    <!--
	 web项目加载web.xml首先读取这两个结点，加载spring容器及创建spring监听器；

	 ApplicationContext.xml 是spring 全局配置文件，用来控制spring 特性的

	 EurekaBootStrap启动容器时，装配ApplicationContext的配置信息。
	start
	-->
  	<context-param>
    	<param-name>contextConfigLocation</param-name>
    	<param-value>classpath:spring/applicationContext.xml</param-value>
  	</context-param>
  	<listener>
    	<listener-class>com.netflix.eureka.EurekaBootStrap</listener-class>
  	</listener>
  	<!-- end -->
    
    
    <!--过滤器 start -->
    
  	<filter>
    	<filter-name>jersey</filter-name>
    	<filter-class>com.sun.jersey.spi.container.servlet.ServletContainer</filter-class>
    	<init-param>
      		<param-name>com.sun.jersey.config.property.WebPageContentRegex</param-name>
      		<param-value>/(flex|images|js|css|jsp)/.*</param-value>
   	 	</init-param>
    	<init-param>
      		<param-name>com.sun.jersey.config.property.packages</param-name>
      		<param-value>com.sun.jersey;com.netflix</param-value>
    	</init-param>

    <!-- GZIP content encoding/decoding -->
        <init-param>
          	<param-name>com.sun.jersey.spi.container.ContainerRequestFilters</param-name>
          	<param-value>com.sun.jersey.api.container.filter.GZIPContentEncodingFilter</param-value>
        </init-param>
        <init-param>
          	<param-name>com.sun.jersey.spi.container.ContainerResponseFilters</param-name>
          	<param-value>com.sun.jersey.api.container.filter.GZIPContentEncodingFilter</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>jersey</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    <!--过滤器 end -->
    <!--配置DispatcherServlet 前端控制器，加载springMVC容器 start-->
    <servlet>
        <servlet-name>SpringMVC</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- 配置springMVC需要加载的配置文件-->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring/springMVC.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
        <async-supported>true</async-supported>
    </servlet>
   
    <servlet-mapping>
        <servlet-name>SpringMVC</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    <!--配置DispatcherServlet 前端控制器，加载springMVC容器 end-->
    
    <!--展示首页页面start-->
    <welcome-file-list>
        <welcome-file>jsp/status.jsp</welcome-file>
    </welcome-file-list>
    <!--展示首页页面end-->
    <!--session配置start-->
    <session-config>
    	<session-timeout>15</session-timeout>
  	</session-config>
     <!--session配置end-->
</web-app>

```





## web项目加载web.xml过程

Tomcat启动web项目时，web容器就会的加载首先加载web.xml文件，

加载过程如下：

* 启动WEB项目的时候，容器首先会去读取web.xml配置文件中的两个节点：<context-param> </context-param>和<listener> </listener>

* 紧接着，容器创建一个ServletContext(application),这个web项目的所有部分都将共享这个上下文。容器以<context-param></context-param>的name作为键，value作为值，将其转化为键值对，存入ServletContext。

* 容器创建<listener></listener>中的类实例，根据配置的class类路径<listener-class>来创建监听，在监听中会有初始化方法，启动Web应用时，系统调用Listener的该方法 contextInitialized(ServletContextEvent args),在这个方法中获得：

  ```
  application =ServletContextEvent.getServletContext();
  
  context-param的值= application.getInitParameter("context-param的键");
  　　
  得到这个context-param的值之后，你就可以做一些操作了。
  ```

  举例：你可能想在项目启动之前就连接数据库，那么这里就可以在<context-param>中设置数据库的连接方式（驱动、url、user、password），在监听类中初始化数据库的连接。这个监听是自己写的一个类，除了初始化方法，它还有销毁方法，用于关闭应用前释放资源。比如:说数据库连接的关闭，此时，调用contextDestroyed(ServletContextEvent args)，关闭Web应用时，系统调用Listener的该方法

* 接着，容器会读取<filter></filter>，根据指定的类路径来实例化过滤器

* 以上都是在WEB项目还没有完全启动起来的时候就已经完成了的工作。如果系统中有Servlet，则Servlet是在第一次发起请求的时候被实例化*，而且一般不会被容器销毁，它可以服务于多个用户的请求。所以，Servlet的初始化都要比上面提到的那几个要迟。

  所以，根据加载过程可以看出，web.xml的加载顺序是: <context-param>-> <listener> -> <filter> -> <servlet>。其中，如果web.xml中出现了相同的元素，则按照在配置文件中出现的先后顺序来加载

[加载过程参考文章](<https://www.cnblogs.com/shoshana-kong/p/10682662.html>)