[参考文章](https://blog.csdn.net/jacabe/article/details/82391554)

 Connector的作用:底层使用Socket连接，接收请求并把请求封装成Request和Response。  
 (Connector实现了Http协议和TCP/IP协议，Request和Response内部是由Http协议封装)  
 (Connector在Catalina调用load方法创建，生命周期方法在Service中调用。)   
 封装完后------》交给Container处理------》然后container处理结果返回给Connector------》Connector使用Socket将结果返回给客户端。  
 
 #### ProtocolHandler  包含三个组件
 * 1-Endpoint(AbstractEndpoint)  处理底层Socket的网络连接 ----用来实现TCP/IP协议 （其也有三个主要的部分）
    * Acceptor-用于监听请求
    * Handler用于处理接收到的Socket.
    * AsyncTimeout-用于检查异步request的超时,
*  Processor 将Endpoint接收到的Endpoint封装成Request  ----实现HTTP协议
*  Adapter   将Request封装成交给Container处理。 ----请求转到Servlet容器处理
  