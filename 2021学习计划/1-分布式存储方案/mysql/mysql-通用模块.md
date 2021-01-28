#### sql执行的过程

[一条SQL查询语句是如何执行的](file:///Users/jzue/Desktop/44-MySQL%E5%AE%9E%E6%88%9845%E8%AE%B2/02-%E5%9F%BA%E7%A1%80%E7%AF%87%20(8%E8%AE%B2)/01%E4%B8%A8%E5%9F%BA%E7%A1%80%E6%9E%B6%E6%9E%84%EF%BC%9A%E4%B8%80%E6%9D%A1SQL%E6%9F%A5%E8%AF%A2%E8%AF%AD%E5%8F%A5%E6%98%AF%E5%A6%82%E4%BD%95%E6%89%A7%E8%A1%8C%E7%9A%84%EF%BC%9F.html)

```
客户端->连接器(认证通过)->判断缓存->分析器->优化器->执行器—>[存储引擎]

```

#### MYISAM、INNODB 区别

事务、行级锁、全文索引、各自的性能优势、以及各自的特性，比如innodb的4大特性

#### Innodb的4大特性

https://www.cnblogs.com/zhs0/p/10528520.html

```
#### insert buffer/change buffer  插入缓冲
https://www.cnblogs.com/zuoxingyu/p/3761461.html
基于传统硬盘顺序写比随机写性能高很多的特点，合并多次插入或者更新，一次写入

#### double write
https://blog.csdn.net/shenchaohao12321/article/details/82970853
https://blog.csdn.net/qq_21989927/article/details/108881401
https://blog.csdn.net/qq_21989927/article/details/108881401
关于IO的最小单位：
　　1、数据库IO的最小单位（Page）是16K（MySQL默认，oracle是8K）
　　2、文件系统IO的最小单位（Page）是4K（也有1K的）
　　3、磁盘IO的最小单位是512字节
因此，如果IO写入中途，突然掉电等突发状况出现时，可以出现页

#### 自适应哈希索引(adaptive hash index)
Innodb存储引擎会监控对表上二级索引的查找，如果发现某二级索引被频繁访问，二级索引成为热数据，建立哈希索引可以带来速度的提升。
经常访问的二级索引数据会自动被生成到hash索引里面去(最近连续被访问三次的数据)，自适应哈希索引通过缓冲池的B+树构造而来，因此建立的速度很快。

####  预读
64个page为一个extent
线性预读 预读下一个extent、随机预读 ：预读extent剩下的page
```

#### innodb pool buffer

#### MYSQL自增主键持久化BUG

https://www.cnblogs.com/ivictor/p/9110994.html

ID生成算法优劣

#### 共享表空间、独立表空间

https://www.cnblogs.com/rxbook/p/10786751.html