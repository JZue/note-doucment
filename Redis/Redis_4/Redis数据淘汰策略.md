 设置内存的限制-配置redis.conf：

`maxmemory 300mb`

如果maxmemory 设置为0，则表示不进行内存限制；如果超过限制，就会根据对应的淘汰策略，淘汰数据

Redis 的数据淘汰策略（内存不足时的淘汰策略）：(maxmemory-policy)

volatile-lru【Least Recently Used】：从设置过期时间key的哈希表(server.db[i].expires)，随机挑选一部分key，然后取得最近最少使用的数据

allkeys-lru：从所有key的哈希表（server.db[i].dict中，随机挑选一部分key，移除最近最少使用的key（这个是最常用的）

volatile-ttl：在设置了过期时间key的哈希表（server.db[i].expires)中，随机挑选部分key，优先淘汰将要过期的数据，越早的越先淘汰

no-eviction：内存不足时，新写入操作会报错

allkeys-random：：从所有的key的哈希表（server.db[i].dict），在键空间中，随机移除

volatile-random：从已设置过期时间的哈希表（server.db[i].expires）中随机挑选key淘汰掉。





Redis 中的lru 算法的实现可以参考<https://segmentfault.com/a/1190000017555834>

Lru 算法的实现可以参考：<https://www.cnblogs.com/Dhouse/p/8615481.html>

都可以了解一下

Lru的算法实现思路（我觉得这是最好理解，b）

1. 新数据插入到链表头部；

2. 每当缓存命中（即缓存数据被访问），则将数据移到链表头部；

3. 当链表满的时候，将链表尾部的数据丢弃。
