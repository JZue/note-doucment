* 请求先检查本地hosts文件是否有域名映射
* 然后查看本地的dns缓存
* 然后到本地的最近的本地dns服务器，然后如果不存在
* 然后到顶层的dns服务器，顶层的根dns服务器返回一个顶级域名服务器（在根域名服务器中虽然没有每个域名的具体信息，但储存了负责每个域（如.com,.xyz,.cn,.ren,.top等）的解析的域名服务器的地址信息）
* 然后顶级域名服务器为将对应的这个请求的再一层一层分发下去，到对应的域名服务器做dns解析，解析成ip，说白了就是找到对应的ip
* cdn预热的概念就在客户端根据域名去访问对应主机的过程）
* 然后到指定ip的指定port，
* Tomcat  开始起作用了，然后service(server下是可以配置多个sevice),
* Connector中专门处理协议的serverSockect ，监听配置文件配置的那个端口，取到socket对象（这个过程设涉及到几种方式bio,apr,nio,nio2）这个然后将socket 封装成request 对象
* 然后根据Request 对象在服务器上找出这个请求对应的engine
* 然后找到对应的虚拟机host
* 然后就是对应的应用
* StandardWrapperValve 先执行filterchain  然后单例创建servlet(这个servlet对于spring mvc来说就是DispatcherServlet)



springMVC只需要看一个方法~就是doService->doDispatch(HttpServletRequest request, HttpServletResponse response)

* 然后就就到了dispatcherservlet，其根据request找到对应的HandlerExecutionChain（除了handler 还有要执行的拦截器等等）,从中找到对应的handler,然后让HandlerAdapter的handle方法执行handler，然后就到controller中来了





context-param ->listener -> filter -> servlet>interceptor





为什么@Transaction，@Async在同一个类中注解失效的原因

<https://blog.csdn.net/qq602757739/article/details/81327990

同一个类之内执行不会走代理

