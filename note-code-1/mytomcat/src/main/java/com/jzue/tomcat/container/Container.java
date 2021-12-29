package com.jzue.tomcat.container;

/**
 * @Author: junzexue
 * @Date: 2019/3/22 下午5:49
 * @Description:Container是容器的父接口，
 * 所有子容器都必须实现这个接口，Container容器的设计用的是典型的责任链的设计模式，
 * 他由4个子容器组件构成，分别实Engine、Host、Context和Wrapper，这4个组件不是平行的，而是父子关系，
 * Engine包含Host，Host包含Context，Context包含Wrapper。
 * 通常一个Servlet class对应一个Wrapper，如果有多个Servlet，则可以定义多个Wrapper；
 * 如果有多个Wrapper，则要定义一个更高的Container
 **/
/**
 * @Description:
 * org.apache.catalina.Container这个接口有着很详细的类注释, 基本上都把它的作用给说的很清楚.大意是容器是可以执行从客户端收到的请求.
 * 并根据这些请求返回响应对象, 它支持管道阀门.这里说的管道是org.apache.catalina包下的Pipeline而阀门指的是Value.
 * 对于容器接口它的4个概念层次分别是:
     * Engine:表示Tomcat的整个Servlet引擎.
     * Host:表示包含一个或者多个Context容器的虚拟主机
     * Context:表示一个Web应用程序.
     * Wrapper:表示一个独立的Servlet.
 * 这4个接口的都有一个标准的实现,对应于org.apache.catalina.core下的StandardEngine,StandardHost,StandardContext和StandardWrapper.
 * 有一点需要注意的是Container的实现类都继承自ContainerBase抽象类.
 * 此外ContainerBase也是继承自LifeCycle的类层次.因此它也必须实现一些生命周期方法.
 * 这些方法可以让StandardService等对其进行生命周期的管理.
 **/
public interface Container {
}
