writeConcern 配置

```
w:

0：发起写操作不关心是否成功

1：（默认值）写到一个结点则算成功

​```值1是可能导致丢数据的，在写到rollback文件之前主节点crash掉```

majority:操作半数的节点成功，才算完成

all:全部结点确认模式




（journal）

j:true  写操作落到journal 文件才算成功
  false 写操作到大内存则算成功

```





