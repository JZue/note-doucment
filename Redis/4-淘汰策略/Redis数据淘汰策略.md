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

volatile-lfu（Least Frequently Used）：对有过期时间的key采用`LFU`淘汰算法

allkeys-lfu：对全部key采用`LFU`淘汰算法



Redis 中的lru 算法的实现可以参考<https://segmentfault.com/a/1190000017555834>

Lru 算法的实现可以参考：<https://www.cnblogs.com/Dhouse/p/8615481.html>

lfu算法逻辑：<https://blog.csdn.net/michaelzhou224/article/details/78620990>

都可以了解一下