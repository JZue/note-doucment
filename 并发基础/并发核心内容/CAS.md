compareAndSwap()

比较-交换

```java

// AtomicInteger.java中cas 的调用
// 返回+1以后的值
public final int incrementAndGet( ) {
    return unsafe.getAndSetInt(this, valueOffset, 1)+1;
}
// 三个参数，
// 第一个，是当前对象，
// 第二个，是偏移量(这个是对象的地址偏移量，对一定的整型值，暂且理解为当前对象地址)
// 第三个，需要新增的新值
public final int getAndSetInt(Object var1, long var2, int var4) {
    int var5;
    do {
        // 获取内存中当前的值
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

    return var5;
}
/**
* 比较obj的offset处内存位置中的值和期望的值，如果相同则更新。此更新是不可中断的。
*
* @param obj 需要被更新的对象
* @param offset obj中整型field的偏移量
* @param expect 希望field中存在的值
* @param update 如果期望值expect与field内存中存储的当前值相同（就是说，此处，才是cas），设置filed的值为这个新值
* @return 如果field的值被更改返回true
*/
public native boolean compareAndSwapInt(Object obj, long offset, int expect, int update);
```



* 首先CAS 是原子操作
* 然后再修改之前，会比对当然的原对象的值跟内存中的值是否相等，不等，