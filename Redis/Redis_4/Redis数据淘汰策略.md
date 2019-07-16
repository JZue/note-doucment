Redis 的数据淘汰策略（内存不足时的淘汰策略）：

volatile-lru【Least Recently Used】：从设置过期时间的数据，然后取得最少使用的数据

allkeys-lru：从所有的key中，移除最近最少使用的key（这个是最常用的）

volatile-ttl：在设置了过期时间的数据中，优先淘汰将要过期的数据，越早的越先淘汰

noeviction：内存不足时，新写入操作会报错

allkeys-random：内存不足时，在键空间中，随机移除某个key

volatile-random：内存不足时，在设置了过期时间的键空间中



Lru的算法实现思路

1. 新数据插入到链表头部；

2. 每当缓存命中（即缓存数据被访问），则将数据移到链表头部；

3. 当链表满的时候，将链表尾部的数据丢弃。

还有Lru-K 、2Q等，可以参考一下<https://www.cnblogs.com/Dhouse/p/8615481.html>