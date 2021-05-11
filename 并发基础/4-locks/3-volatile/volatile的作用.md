



#### 可见性

```java
public class Demo {
    public  static  boolean flag=false;
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!flag){

                }
                System.out.println("single core is true:");

            }
        });
        thread.start();
        // Thread.sleep(1);
        flag = true;
        System.out.println("flag set end");
    }
}
```

#### 防止指令重排序





不保证原子性