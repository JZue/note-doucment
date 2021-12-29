[参考文章](https://segmentfault.com/a/1190000012316621?utm_source=tag-newest)
#### 基于io与多线程的客户端和服务器端

    多个客户端同时向服务端发送请求
    服务端做出的措施是开启多个线程来匹配相对应的客户端
    并且每个线程去独自完成他们的客户端请求

## NIO

    三大核心：
        Selector: 选择器
        Buffer： 缓冲区
        Channel： 通道

#### Buffer

    Buffer 实际上是一个容器，一个连续的数组，它通过几个变量来保存这个数据的当前位置状态；
    1. Capacity: 容量，缓冲区能容纳的元素的数量
    2. position: 当前位置，是缓冲区中下一次发生读取和写入操作的索引，当前位置通过大多数读写操作向前推进

#### Selector 

    Selector 是NIO的核心，它是Channel的管理者，通过执行select()阻塞方法，监听是否有channel准备好，一旦有数据可读，此方法的返回值是SelectionKey的数量。

    所以服务端通常会死循环执行select()方法，直到有channl准备就绪，然后开始工作，每个channel都会和Selector绑定一个事件，然后生成一个SelectionKey的对象。

    需要注意的是：
    channel和Selector绑定时，channel必须是非阻塞模式，而FileChannel不能切换到非阻塞模式，因为它不是套接字通道，所以FileChannel不能和Selector绑定事件。

    在NIO中一共有四种事件：
        1.SelectionKey.OP_CONNECT：连@接事件
        2.SelectionKey.OP_ACCEPT：接收事件
        3.SelectionKey.OP_READ：读事件
        4.SelectionKey.OP_WRITE：写事件

#### Channel

    FileChannel：作用于IO文件流
    DatagramChannel：作用于UDP协议
    SocketChannel：作用于TCP协议
    ServerSocketChannel：作用于TCP协议


    clientChannel.configureBlocking(false);可以将此通道设置为非阻塞，也就是异步自由控制阻塞或非阻塞便是NIO的特性之一
                










1. 2-1线程阻塞==》  阻塞通信，socket

NioEventLoop  ==>类似于while（true）中的阻塞监听端口的 那个线程+客户端的处理读写的那个线程
Channel  数据传输管道  
Pipeline ==》logic  chain   （逻辑链）一系列的业务逻辑ChannelHandler
ByteBuf  
ChannelHandler  逻辑处理块

默认情况下， netty 服务端起多少线程 ，何时启动
netty是如何解决jdk空轮询的bug的
netty是保证异步串行无锁化
4-7
spring bootadmin