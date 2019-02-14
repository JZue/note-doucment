* Ad-hoc 线程封闭：程序控制实现，最糟糕，忽略
* 堆栈封闭：局部变量，无并发问题
* ThreadLocal线程封闭

# ThreadLocal

## ThreadLocal与synchronized

* ThreadLocal是为每个线程中的并发数据提供一个副本，可以把它理解为一个线程独有的局部变量(其实就是一个map)，每个线程都可以有自己独立的副本，并对其进行操作，这样就避免的进程之间的影响。**空间换取多线程安全** 。以消耗内存为代价，大大减少了线程同步（synchronized）所带来的性能问题。

* 而synchronized关键字是Java利用锁的机制自动实现的，一般有同步方法和同步代码块两种使用方式。在同步机制中，通过对象的锁机制保障统一时间只有一个线程访问变量，相当于大家想访问变量就得在后面排队等待前面的操作完，然后才可以操作。**时间换取线程安全**

## 核心方法

```java
 protected T initialValue() {
        return null;
    }
```

```
/**
 * Returns the current thread's "initial value" for this
 * thread-local variable.  This method will be invoked the first
 * time a thread accesses the variable with the {@link #get}
 * method, unless the thread previously invoked the {@link #set}
 * method, in which case the {@code initialValue} method will not
 * be invoked for the thread.  Normally, this method is invoked at
 * most once per thread, but it may be invoked again in case of
 * subsequent invocations of {@link #remove} followed by {@link #get}.
 *
 * <p>This implementation simply returns {@code null}; if the
 * programmer desires thread-local variables to have an initial
 * value other than {@code null}, {@code ThreadLocal} must be
 * subclassed, and this method overridden.  Typically, an
 * anonymous inner class will be used.
 *
 * @return the initial value for this thread-local
 */
```

 返回线程局部变量初始值，该方法是一个protected的方法，显然是为了让子类覆盖而设计的。这个方法是一个延迟调用方法，在线程第1次调用get()或set(Object)时才执行，并且仅执行1次。ThreadLocal中的缺省实现直接返回一个null。

```java
  public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }
```

该方法返回当前线程所对应的线程局部变量。

```java
 public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }
```

设置当前线程的线程局部变量的值。

```java
public void remove() {
    ThreadLocalMap m = getMap(Thread.currentThread());
    if (m != null)
        m.remove(this);
}
```

## 源码浅析

从上面的几个方法大致也可以看出，ThreadLocal的数据操作实际上都是ThreadLocalMap来完成的。

#### ThreadLocal、ThreadLocalMap、Thread关系

* ThreadLocal类用于存储以线程为作用域的数据，线程之间数据隔离。 
* ThreadLocalMap类是ThreadLocal的静态内部类，通过操作Entry来存储数据。 
* Thread类比较常用，线程类内部维持一个ThreadLocalMap类实例（jdk1.8.0_192 的182行的threadLocals：ThreadLocal.ThreadLocalMap threadLocals = null;）

#### ThreadLocal的大致设计思想

* ThreadLocal仅仅是个变量访问的入口（点开源码会发现ThreadLocalMap所有操作方法全为private）；

*  每一个Thread对象都有一个ThreadLocalMap对象，这个ThreadLocalMap持有对象的引用；

* ThreadLocalMap以当前的threadLocal对象为key，以真正的存储对象为value。get()方法时通过threadLocal实例就可以找到绑定在当前线程上的副本对象。

说的简单的，我觉得可以理解为Map<Thread,Value>,即key对应的是当前对象，value是线程独有的需要存储的值



## ThreadLocal结构图

![avatar](https://upload-images.jianshu.io/upload_images/7432604-ad2ff581127ba8cc.jpg)

## 内存泄漏问题

[内存泄漏浅析参考](https://www.jianshu.com/p/a1cd61fa22da)

文章中：（key 使用强引用：**引用的ThreadLocal的对象**被回收了，但是ThreadLocalMap还持有ThreadLocal的强引用，如果没有手动删除，ThreadLocal不会被回收，导致Entry内存泄漏）这段的黑体加关键字的“引用的ThreadLocal的对象”改为“持有ThreadLocal的引用的对象”(第一次看了半天没搞懂他在说啥子)

这段举个例子，大致理解为：比如你有一个对象obj，obj的成员变量是threadlocal，obj被回收了但是threadlocal不会被回收，因为threadlocalmap还有threadlocal的强引用。如果是弱引用就能解决这个问题。

## 使用方法实例

实际上，废话这么多，还有个最重要的，到底怎么用，什么情况下用，并没有阐述清楚！

这类文章网上挺多的，我确实没找到比较好的，或者说，我自己也确实不确定到底什么时候要用这个!

大致可以总结为，hreadLocal的主要用途是为了保持线程自身对象和避免参数传递，主要适用场景是按线程多实例（每个线程对应一个实例）的对象的访问，并且这个对象很多地方都要用到

[使用场景参考](https://blog.csdn.net/qq_36632687/article/details/79551828)

