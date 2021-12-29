package com.jzue.concurrency.example.condition;

import java.util.concurrent.locks.Condition;

import static com.jzue.concurrency.example.condition.Bucket.lock;

/**
 * @Author: junzexue
 * @Date: 2019/4/12 下午3:48
 * @Description:
 **/
public class Producer implements Runnable {
    public static Condition producerCondition=lock.newCondition();
    private Bucket<Integer> bucket;

    public Producer(Bucket bucket) {
        this.bucket=bucket;
    }
    /**
     *  lock.lock();
     *  此处的lock的用处：如果不用回报java.lang.IllegalMonitorStateException.
     *  就是当前的线程不是此对象监视器的所有者。当前线程要锁定该对象之后，才能用锁定的对象执行这些方法，
     *  这里需要用到synchronized关键字/lock，
     *  锁定哪个对象就用哪个对象来执行notify(), notifyAll(),wait(), wait(long), wait(long, int)操作，
     *  否则就会报IllegalMonitorStateException异常。
     *  lock.unlock();
     **/
    @Override
    public void run() {
        int i=0;
        while (true){
            lock.lock();
            try {
                if (bucket.size()>0&&bucket.size()<100){
                    Consumer.consumerCondition.signal();
                }else if (bucket.size()>=100){
                    System.out.println("生产满了，请快点消费："+bucket.size());
                    Consumer.consumerCondition.signal();
                    producerCondition.await();
                }
                i++;
                bucket.add(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                lock.unlock();
            }

        }
    }
}
