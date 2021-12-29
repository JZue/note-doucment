



## ThreadLocal定义

ThreadLocal 是一个本地线程副本变量工具类。在Thread类的定义中有 ThreadLocal.ThreadLocalMap的成员变量，主要的作用就是用于存储线程私有的变量，应用于一些线程隔离的场景。

```java
 // Thread类的成员变量
 ThreadLocal.ThreadLocalMap threadLocals = null;
 
```

#### 简单DEMO

```java
public class ThreadLocalDemo {
    static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    public static void main(String[] args) {
      Thread thread1 =new Thread(()->{
      threadLocal.set("thread1");
      System.out.println("thread1-set:"+threadLocal.get());
      threadLocal.remove();
      System.out.println("thread1-remove:"+threadLocal.get());
      });
      thread1.start();
    }  
  }
```

## ThreadLocal 与Thread逻辑架构

![ThreadLocal](http://file.xjzspace.com/20211014153233.png)

```
1. ThreadLocalMap是ThreadLocal的一个内部类
2. Thread和ThreadLocal是多对多的关系


那么这个多对多的关系怎么实现的呢？

Thread中有一个成员变量 ThreadLocal.ThreadLocalMap threadLocals
1）ThreadLocalMap的Key是ThreadLocal,value是需要存入线程的私有变量的值
2）threadLocals这个map可以存多个不同ThreadLocal为Key的数据,这样就实现了一个线程关联多个ThreadLocal
3）一个ThreadLocal,可以成为多个线程中的ThreadLocalMap的key，这样就实现了一个ThreadLocal关联多个线程
```



## 核心方法

从代码角度来看Thread和ThreadLocal的关系。

#### Thread下的成员变量threadLocals什么时候创建？

在创建线程时，threadLocals为null，类似于单例的懒汉模式，在用的时候才创建。

两个实例化的入口分别是ThreadLocal的set方法和get方法

**Get方法**

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

// setInitialValue
private T setInitialValue() {
    T value = initialValue();
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
    return value;
}
// createMap
void createMap(Thread t, T firstValue) {
    t.threadLocals = new ThreadLocalMap(this, firstValue);
}
```

从这个逻辑来看，在get的时候，会实例化threadLocals，并且将当前ThreadLocal的值设置为null.

**set**

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

set的时候，如果map为空，则createMap，然后将当前ThreadLocal的value进去



**remove**

```java
public void remove() {
    ThreadLocalMap m = getMap(Thread.currentThread());
    if (m != null)
        m.remove(this);
}

ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}
```

remove逻辑很直观，将threadLocals的值清空

#### ThreadLocal、ThreadLocalMap、Thread关系

* ThreadLocal类用于来操作Thread下的ThreadLocalMap。 
* ThreadLocalMap类是ThreadLocal的静态内部类，通过操作Entry来存储数据。 
* Thread类比较常用，线程类内部维持一个ThreadLocalMap类实例（jdk1.8.0_192 的182行的threadLocals：ThreadLocal.ThreadLocalMap threadLocals = null;）

## 内存泄漏问题

ThreadLocal 在 ThreadLocalMap 中是以一个弱引用身份被 Entry 中的 Key 引用的，因此如果 ThreadLocal 没有外部强引用来引用它，那么 ThreadLocal 会在下次 JVM 垃圾收集时被回收。这个时候 Entry 中的 key 已经被回收，但是 value 又是一强引用不会被垃圾收集器回收，这样 ThreadLocal 的线程如果一直持续运行，value 就一直得不到回收，这样就会发生内存泄露。

```
为什么 ThreadLocalMap 的 key 是弱引用？
我们知道 ThreadLocalMap 中的 key 是弱引用，而 value 是强引用才会导致内存泄露的问题，至于为什么要这样设计，这样分为两种情况来讨论：

key 使用强引用：这样会导致一个问题，引用的 ThreadLocal 的对象被回收了，但是 ThreadLocalMap 还持有 ThreadLocal 的强引用，如果没有手动删除，ThreadLocal 不会被回收，则会导致内存泄漏。

key 使用弱引用：这样的话，引用的 ThreadLocal 的对象被回收了，由于 ThreadLocalMap 持有 ThreadLocal 的弱引用，即使没有手动删除，ThreadLocal 也会被回收。value 在下一次 ThreadLocalMap 调用 set、get、remove 的时候会被清除。

比较以上两种情况，我们可以发现：由于 ThreadLocalMap 的生命周期跟 Thread 一样长，如果都没有手动删除对应 key，都会导致内存泄漏，但是使用弱引用可以多一层保障，弱引用 ThreadLocal 不会内存泄漏，对应的 value 在下一次 ThreadLocalMap 调用 set、get、remove 的时候被清除，算是最优的解决方案。
```