[atomic包原理浅谈参考](https://www.jb51.net/article/129690.htm)

[atomic包介绍](https://chenzehe.iteye.com/blog/1759884)

[atomic包类摘要](https://blog.csdn.net/qq_31615049/article/details/81062082)

[CAS原理](https://www.cnblogs.com/javalyy/p/8882172.html)

[unsafe类浅析](https://www.cnblogs.com/pkufork/p/java_unsafe.html)



## 特性

* atomic是借助  **硬件**  的相关指令来实现的
* 不会阻塞线程(或者说，只会在硬件级别上阻塞)
* Atomic包中的类基本的特性就是在多线程环境下，当有多个线程同时对单个（包括基本类型及引用类型）变量进行操作时，具有排他性，即当多个线程同时对该变量的值进行更新时，仅有一个线程能成功，而未成功的线程可以向自旋锁一样，继续尝试，一直等到执行成功。

## 简介

 	Atomic的核心操作就是CAS（compareandset,利用CMPXCHG指令实现，它是一个原子指令）,该指令有三个操作数，变量的内存值V（value的缩写），变量的当前预期值E（exception的缩写），变量想要更新的值U（update的缩写），当内存值和当前预期值相同时，将变量的更新值覆盖内存值。伪代码：

```c
if (V == E){ 
   V = U 
   return true
} else { 
  return  false
}
```



​	Atomic系列的类中的核心方法都会调用unsafe类中的几个本地方法。我们需要先知道一个东西就是Unsafe类，全名为：sun.misc.Unsafe，这个类包含了大量的对C代码的操作，包括很多直接内存分配以及原子操作的调用，而它之所以标记为非安全的，是告诉你这个里面大量的方法调用都会存在安全隐患，需要小心使用，否则会导致严重的后果，例如在通过unsafe分配内存的时候，如果自己指定某些区域可能会导致一些类似C++一样的指针越界到其他进程的问题



Atomic包中的类按照操作的数据类型可以分成4组

```java
AtomicBoolean，AtomicInteger，AtomicLong
```

线程安全的基本类型的原子性操作

```java
AtomicIntegerArray，AtomicLongArray，AtomicReferenceArray
```

线程安全的数组类型的原子性操作，它操作的不是整个数组，而是数组中的单个元素

```java
AtomicLongFieldUpdater，AtomicIntegerFieldUpdater，AtomicReferenceFieldUpdater
```

基于反射原理对象中的基本类型（长整型、整型和引用类型）进行线程安全的操作

```java
AtomicReference，AtomicMarkableReference，AtomicStampedReference
```





## CAS（CompareAndSwap）无锁自旋与同步锁线程切换比较

* 对于资源竞争较少的情况，使用同步锁进行线程阻塞和唤醒切换以及用户态内核态间的切换操作额外浪费消耗CPU资源；而CAS基于硬件实现，不需要进入内核，不需要切换线程，操作自旋几率较少，因此可以获得更高的性能。

* 对于资源竞争严重的情况，CAS自旋的概率会比较大，从而浪费更多的CPU资源，效率低于锁。CAS在判断两次读取的值不一样的时候会放弃操作，但为了保证结果正确，通常都会继续尝试循环再次发起CAS操作，如Jdk1.6版本的AtomicInteger类的getAndIncrement()方法，就是利用了自旋实现多次尝试：


```java
public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

    return var5;
}
```

入参：

* var1：就是调用的对象（例如：count.incrementAndGet()）  var1就是count这个AtomicInteger类型的对象
* var2: 代表的进入这个方法之前当前的值(工作内存中的值)（**描述不太准确，也没想到很好的描述**）
* Var3: 代表的要改变的偏移量

其他变量：

* var5: 代表的从内存中当前取得的值(主内存中的值)

compareAndSwapInt(.....)方法的逻辑就是，当var2和var5相同时，就对这个值执行对应偏移量的操作，如果成功则返回，否则重新循环。**这个时候就会出现问题，如果线程竞争激烈，一直返回失败，则会一直循环等待，直到耗尽cpu分配给该线程的时间片，从而大幅降低效率。**

----------------以上是对int的例子分析，其他的类似----------------

总结：
1、使用CAS在线程冲突严重时，因为自旋会大幅降低程序性能；CAS只适合于线程冲突较少的情况使用。
2、线程冲突严重的情况下，同步锁能实现线程堵塞和唤醒切换，不会出现自旋，避免了上述的情况，从而让性能远高于CAS

## 问题

ABA等问题及源码分析见上参考文章链接















