#### SortedMap


#### HashMap
* HashMap 是一个最常用的Map，它根据键的HashCode 值存储数据，根据键可以直接获取它的值，具有很快的访问速度。遍历时，取得数据的顺序是完全随机的。
* HashMap最多只允许一条记录的键为Null；允许多条记录的值为 Null。
* HashMap不支持线程的同步（即任一时刻可以有多个线程同时写HashMap），可能会导致数据的不一致。如果需要同步，可以用 Collections的synchronizedMap方法使HashMap具有同步的能力，或者使用ConcurrentHashMap。
* Hashtable与 HashMap类似，它继承自Dictionary类。不同的是：它不允许记录的键或者值为空；它支持线程的同步（即任一时刻只有一个线程能写Hashtable），因此也导致了 Hashtable在写入时会比较慢。

**HashMap遍历的性能的问题**
* 通过map.keySet()来遍历：源码先获取了key 对应的hash值 ，然后根据hash值在HashMap 的
        Entry 数组内遍历出hash值与key的hash值相等的Entry，并返回对应的Value。
        所以get() 方法在获取Value 的时候又进行了一次循环，这导致了性能的下降。
* 通过map.entrySet()来遍历(推荐，性能好)
* 通过map.entrySet().iterator()来遍历（基于entrySet,性能好，代码没有上一种简洁）
* Stream表达式不推荐，据说性能巨差


#### LinkedHashMap
* 数据结构是hashMap+双向链表  
* 有两种顺序-----插入顺序和读取顺序--------由accessOrder可以控制，  
    * 默认false 按照插入顺序输出
    * 为true 按照读取顺序，大致逻辑，每次把取到的对象放到顺序控制链表的末尾，
        然后输出的时候就从尾开始输出最近的读取顺序，这样就可以维护一个recentLy used
**特点**
* LinkedHashMap保存了记录的插入顺序，在用Iterator遍历LinkedHashMap时，先得到的记录肯定是先插入的。也可以在构造时带参数，按照应用次数排序。
*在遍历的时候会比HashMap慢，不过有种情况例外：当HashMap容量很大，实际数据较少时，遍历起来可能会比LinkedHashMap慢。因为LinkedHashMap的遍历速度只和实际数据有关，和容量无关，而HashMap的遍历速度和他的容量有关。

#### TreeMap
    基于红黑树实现的。
　　TreeMap实现SortMap接口，能够把它保存的记录根据键排序。
　　默认是按键值的升序排序，也可以指定排序的比较器，当用Iterator 遍历TreeMap时，得到的记录是排过序的。

#### TreeMap/LinkedHashMap/HashMap对比
https://www.cnblogs.com/acm-bingzi/p/javaMap.html