Jstat是JDK自带的一个轻量级小工具；

全称“Java Virtual Machine statistics monitoring tool”，它位于java的bin目录下；

主要利用JVM内建的指令对Java应用程序的资源和性能进行实时的命令行的监控；

包括了对Heap size和垃圾回收状况的监控。

**主要作用就是查询垃圾回收的信息**

jstat  -option pid  





jstat -gccause pid 2000 每格2秒输出结果

Aacc123456.

生产环境性能影响

```
如果我们在生产环境中继续运行 jstat 命令（比如每5分钟左右）来监控JVM（6.x）内存，是否会对性能产生影响？目标系统是一个实时应用程序，甚至一秒钟的停顿也很重要。

答案 0 :(得分：5)

不，jstat不会对性能造成任何明显的影响，即使每秒都有效。

此工具依赖于HotSpot Performance Counters（又名PerfData）。 jstat基本上从mmap'ed /tmp/hsperfdata_username/vmid读取数据。无论你是否阅读，HotSpot都会导出这些计数器。
```



