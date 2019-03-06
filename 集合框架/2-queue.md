[参考文章](https://www.cnblogs.com/lemon-flm/p/7877898.html)

**Queue： 基本上，一个队列就是一个先入先出（FIFO）的数据结构**

**Queue接口与List、Set同一级别，都是继承了Collection接口。LinkedList实现了Deque接 口。**

* 队列是一种特殊的线性表，它只允许在表的前端（front）进行删除操作，而在表的后端（rear）进行插入操作。进行插入操作的端称为队尾，进行删除操作的端称为队头。队列中没有元素时，称为空队列。

## queue 基本操作

除了继承Collection的方法.Queue还添加了一些额外的

boolean add(E e);       增加一个元索                     如果队列已满，则抛出一个IIIegaISlabEepeplian异常
E remove();            移除并返回队列头部的元素    如果队列为空，则抛出一个NoSuchElementException异常
E element();             返回队列头部的元素             如果队列为空，则抛出一个NoSuchElementException异常
boolean offer(E e);       添加一个元素并返回true       如果队列已满，则返回false
E poll();                  移除并返问队列头部的元素    如果队列为空，则返回null
E peek();                  返回队列头部的元素             如果队列为空，则返回null

* Queue使用时要尽量避免Collection的add()和remove()方法，而是要使用offer()来加入元素，使用poll()来获取并移出元素。它们的优点是通过返回值可以判断成功与否，add()和remove()方法在失败的时候会抛出异常。 如果要使用前端而不移出该元素，使用element()或者peek()方法。



## queue实现类

**Deque, LinkedList, PriorityQueue, BlockingQueue 等类都实现了它。**

####  1-没有实现的阻塞接口的LinkedList



#### 2-实现阻塞接口的





