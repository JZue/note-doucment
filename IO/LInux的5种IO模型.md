```
阻塞IO模型
非阻塞IO模型 
IO复用模型
信号驱动IO
异步IO模型


（1）同步阻塞IO（Blocking IO）：即传统的IO模型。

（2）同步非阻塞IO（Non-blocking IO）：默认创建的socket都是阻塞的，非阻塞IO要求socket被设置为NONBLOCK。注意这里所说的NIO并非Java的NIO（New IO）库。

（3）IO多路复用（IO Multiplexing）：即经典的Reactor设计模式，有时也称为异步阻塞IO，Java中的Selector和Linux中的epoll都是这种模型。

（4）异步IO（Asynchronous IO）：即经典的Proactor设计模式，也称为异步非阻塞IO。
https://www.boost.org/doc/libs/1_61_0/doc/html/boost_asio/overview/core/async.html

```

```
select poll  epoll 区别

select 用数组存放的一个fd,所以存放的个数是有限的,32位机器限制为1024，64位机器限制为2048 (https://www.jianshu.com/p/397449cadc9a)

poll 用链表存储的fd所有存放的个数没有限制

poll和select 都是轮询机制

epoll是回调机制，处于某种状态之后，调用回调函数，epoll_await()可以收到回调

https://blog.csdn.net/jay900323/article/details/18141217#t5
```

