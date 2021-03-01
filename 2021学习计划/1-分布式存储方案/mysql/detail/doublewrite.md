

https://blog.csdn.net/shenchaohao12321/article/details/82970853
https://blog.csdn.net/qq_21989927/article/details/108881401
https://blog.csdn.net/qq_21989927/article/details/108881401



```
关于IO的最小单位：
　　1、数据库IO的最小单位（Page）是16K（MySQL默认，oracle是8K）
　　2、文件系统IO的最小单位（Page）是4K（也有1K的）
　　3、磁盘IO的最小单位是512字节
因此，如果IO写入中途，突然掉电等突发状况出现时，可以出现页


两次写给 InnoDB 带来的是可靠性，主要用来解决部分写失败(partial page write)。doublewrite 有两部分组成，一部分是内存中的 doublewrite buffer ，大小为 2M，另外一部分就是物理磁盘上的共享表空间中连续的 128 个页，即两个区，大小同样为 2M。当缓冲池的作业刷新时，并不直接写硬盘，而是通过 memcpy 函数将脏页先拷贝到内存中的 doublewrite buffer，之后通过 doublewrite buffer 再分两次写，每次写入 1M 到共享表空间的物理磁盘上，然后马上调用 fsync 函数，同步磁盘。如下图所示

```

![img](https://images.gitbook.cn/Fh1qmU3TsCwz4YDcT9C92j0GWfDo)



