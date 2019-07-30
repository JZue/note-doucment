<https://blog.csdn.net/u010983881/article/details/80257703>



<https://blog.csdn.net/qq_39949109/article/details/81777102>

```Eng
join() method suspends the execution of the calling thread until the object called finishes its execution.
```



Thread的join()的作用：阻塞调用此方法的线程（也就是最开始正在运行的线程），直到join()的线程完成，

也就是说，t.join()方法阻塞调用此方法的线程(calling thread)，直到线程t完成，此线程再继续；

```java
public static void main(String[] args) {
        Thread thread1=new Thread(()->{
            System.out.println("thread1");
        });
        thread1.start();
        System.out.println("main thread");
    }
```

执行结果：顺序不固定，这里不详细解释了，都已经在研究join原理了，应该对这个还是比较熟悉了

```
main thread
thread
或者
thread1
main thread
```

```java
   public static void main(String[] args) {
        Thread thread1=new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("thread1");
        });
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("main thread");
    }
```

执行结果：由于有一个join（）所以只有一个结果

```
thread1
main thread
```

再来看看jdk8中的join的实现

```java
    public final synchronized void join(long millis)
    throws InterruptedException {
        long base = System.currentTimeMillis();
        long now = 0;

        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }

        if (millis == 0) {
            while (isAlive()) {// 如果当前线程isAlive==true 那么就一直等到线程执行玩
                wait(0);
            }
        } else {
            while (isAlive()) {
                long delay = millis - now;
                if (delay <= 0) {
                    break;
                }
                wait(delay);
                now = System.currentTimeMillis() - base;
            }
        }
    }
```

