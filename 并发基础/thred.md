* java 是没有办法销毁一个线程的，销毁线程是由c++去做的，当线程执行完以后就会退出销毁

* 怎么控制线程的执行顺序呢？

  * join 去控制

  * ```java
       		Thread thread1 = new Thread();
            Thread thread2 = new Thread();
            Thread thread3 = new Thread();
    
            //启动线程
            thread1.start();
            //控制线程必须执行完成
            thread1.join();
            thread2.start();
            thread2.join();
            thread3.start();
            thread3.join();
    ```

  * 

  * 可以通过同步执行，isAlive去阻塞自旋

    ```
         thread1.start();
            while (thread1.isAlive()){
                
            }
            thread2.start();
            while (thread1.isAlive()){
    
            }
            thread3.start();
            while (thread1.isAlive()){
    
            }
    ```
