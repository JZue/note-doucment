参考文章：https://www.cnblogs.com/linjiqin/p/11254247.html

```
create table user (
    id int primary key,
    name varchar(20),
    sex varchar(5),
    index(name)
)engine=innodb;
用户表：id主键索引，name普通索引（非唯一），sex无索引；
```



#### using where

```
explain select * from user where sex='男';
Extra为Using where说明，SQL使用了where条件过滤数据。

需要注意的是：
（1）返回所有记录的SQL，不使用where条件过滤数据，大概率不符合预期，对于这类SQL往往需要进行优化；
（2）使用了where条件的SQL，并不代表不需要优化，往往需要配合explain结果中的type（连接类型）来综合判断；

本例虽然Extra字段说明使用了where条件过滤，但type属性是ALL，表示需要扫描全部数据，仍有优化空间。

常见的优化方法为，在where过滤属性上添加索引，但是性别就只有男女两个值，添加索引对性能提升有限。
```

