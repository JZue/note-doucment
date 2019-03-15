[参考文章](https://www.cnblogs.com/welen/articles/5385837.html)

[关键图片](https://images0.cnblogs.com/blog/405877/201411/142333254136604.png)

#### Reactor 模式中的Reactor 是一个单独的线程嘛？还是说是和用户线程是同一个线程

用户线程专注于IO操作，而Reactor是一个单独的线程，其专注于对Socket的监听事件。

这时候你可能会想到Redis的epoll IO 模型，其实也是这样的，Redis所谓的单线程并非真正的单线程，只是IO是单线程，其实还会有很多例如Reactor类似的后台线程。普通的一个线程处理一个Socket的IO模型与epoll的线程IO模型，与集群和分布式的区别很类似，就是说，可以理解为，它实际上是把其他工作划分出去了，IO线程就专注于处理IO事件，并不是说是所有的事件都是一个线程完成的，是这个道理吧





