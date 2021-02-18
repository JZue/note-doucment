https://blog.csdn.net/qq_38545713/article/details/105507366



Hash冲突的解决方案：





Rehash:

HashMap的rehash:

 当键值对的数量>=设定的阀值(capacity * load factor(0.75))时，为保证HashMap的性能，会进行重散列(rehash)。

HashMap中，重散列主要有两步：1、扩充table长度。2、转移table中的entry，从旧table转移到新的table。

table长度以2倍的方式扩充，一直到最大长度2^30。

entry转移的过程是真正意义上的重散列，在此过程中，对原来的每个entry的key重新计算新的散列地址，旧table中相同位置的entry极有可能会被散列到新table中不同的位置，这主要是因为table的length变化的原因。



redis的渐进式rehash