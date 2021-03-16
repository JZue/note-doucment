```
环境: jdk8
```

本节主要介绍aqs的基本原理：

分成两部分，设计思路（架构图），核心源码结构分析，最后给大家举了个大白话的例子，先宏观理解其实现的核心思想。



### 设计思路

生产者消费者模型

### 核心源码结构分析

首先我们得找到两个队列的源码实现在哪？

同步队列：AQS的成员变量head  和tail对应的队列

等待队列：内部类ConditionObject就是同步队列的实现

##### 同步队列操作

```
 private Node addWaiter(Node mode)
 private void setHead(Node node) 
 private void unparkSuccessor(Node node)
   
```

##### state操作方法

```
  protected final int getState();
  protected final void setState(int newState);
  protected final boolean compareAndSetState(int expect, int update)
```

#### 等待队列操作

```
ak:DgNC36j79ubynNdxaPyOcwMcVEUtr477N4h-MQnl
sk:3aLFvB70RDoKKuzJUGQKmB2Mnt4TCJPqYliD0kox
```







