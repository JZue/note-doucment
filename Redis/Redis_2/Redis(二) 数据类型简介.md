



简介：Redis 数据类型分为：字符串类型(string)、散列类型(hash)、列表类型(list)、集合类型(set)、有序集合类型(zset)。Redis的数据以K-V形式存储，K和V都是一个对象，每个对象都由结构体redisObject表示。掌握5种数据类型的特点，不论对于开发还是调优都有很大的必要学习，应该也算是Redis比较核心的知识点。



redis5.0 源码地址：https://github.com/antirez/redis/releases/tag/5.0.0  （这是redis5.0.0的源码，也是我服务器和本机所用的版本，如果文章有引用源码的也是基于此版本的，可自行下载）



```c
源码/src/server.h  

/* The actual Redis Object */
#define OBJ_STRING 0    /* String object. */
#define OBJ_LIST 1      /* List object. */
#define OBJ_SET 2       /* Set object. */
#define OBJ_ZSET 3      /* Sorted set object. */
#define OBJ_HASH 4      /* Hash object. */


typedef struct redisObject {
		// 类型 0-OBJ_STRING 1-OBJ_LIST 2-OBJ_SET 3-OBJ_ZSET 4-OBJ_HASH
        unsigned type:4;
		// 编码
        unsigned encoding:4;
		// 对象最后一次被访问的时间，可以用来计算多久没被访问，当服务器的内存超过了maxmemory设置的上限时，没有被访问的时间越长的键就越优先被服务器释放，从而回收内存
        unsigned lru:LRU_BITS; /* lru time (relative to server.lruclock) */
		// 对象的引用计数
        int refcount;
		//ptr指针指向真正的存储结构 
        void *ptr;

} robj;

```

![屏幕快照 2019-07-14 上午11.38.51](/Users/jzue/Desktop/blog_file/文章图片/屏幕快照 2019-07-14 上午11.38.51.png)

Redis的数据以K-V形式存储，K和V都是一个对象，每个对象都由结构体redisObject，那么我们每次创建一个Key—Value，一个Key一个redisObject，一个Value一个redisObject，redisObject结构如上。下面的描述大致就是介绍了属性的作用，我已经标注在结构体上了。



查看redis数据的数据类型的方法

``` type   [key] ```

![屏幕快照 2019-07-14 上午10.52.47](/Users/jzue/Desktop/blog_file/文章图片/屏幕快照 2019-07-14 上午10.52.47.png)



## 字符串

 字符串类型的源码在"源码/src/t_string.c "———主要是相关的命令，可以自己稍微浏览下，里面的方法，就可以看出来，基本就是各个命令的实现。当然要是有兴趣，并且比较熟悉c语言，完全可以看看逻辑。

字符串是redis中最基础的数据结构，所有的key的数据类型都是字符串类型的,其他的数据结构也是基于string的，string类型的不仅可以存简单的字符串，也可以任意格式的二进制数据；

**数据结构**

- int：8个字节的长整型。
- embstr：小于等于39个字节的字符串。
- raw：大于39个字节的字符串。

Redis的embstr编码方式和raw编码方式是以44字节为分界的，也就是说，如果一个字符串值的长度小于等于44字节，则按照embstr进行编码，否则按照raw进行编码。   关于这个分界值，3.2之前据说是39字节3.2 之后是44字节。

一般使用场景：

1）缓存

2）计数器

3）存用户token

4)  等等......还有很多其他用途，不依依列举...



## hash

hash的数据类型key依然是string类型，但是其value是类似于键值对的那种结构的

![屏幕快照 2019-07-14 下午12.41.25](/Users/jzue/Desktop/屏幕快照 2019-07-14 下午12.41.25.png)





画个图描述一下

![屏幕快照 2019-07-14 下午12.53.16](/Users/jzue/Desktop/屏幕快照 2019-07-14 下午12.53.16.png)



Key 就是example   value对应的是就是一个field-value的一个形式的一个映射表。所以说hash类型很适合用来存储对象；

- ziplist（压缩列表）：当哈希类型元素个数小于hash-max-ziplist-entries配置（默认512个），同时所有Value的长度都小于hash-max-ziplist-value配置（默认64个字节）时，Redis会使用ziplist作为哈希的内部实现ziplist使用更加紧凑的结构实现多个元素的连续存储，所以在节省内存方面比hashtable更加优秀。当然上面的默认值都是可以配置的。
  - hash-max-zipmap-entries 512 #配置字段最多512个
  - hash-max-zipmap-value 64 #配置value最大为64字节
- hashtable（哈希表）：当哈希类型无法满足ziplist的条件时，Redis会使用hashtable作为哈希的内部实现。因为此时ziplist的读写效率会下降，而hashtable的读写时间复杂度为O(1)。

## 列表（List）

```   c
源码/src/server.h 
typedef struct {
    listTypeIterator *li;
    quicklistEntry entry; /* Entry in quicklist */
} listTypeEntry;
```

列表类型存储是字符串的一个列表，可以从列表的两端push或者pop元素。

* ziplist： 当列表元素的个数小于512个（list-max-ziplist-entries 的默认配置), 同时列表中每个元素值大小小于64字节（list-max-ziplist-value），redis会选用ziplist来作为列表内部实现（这两个限制条件和哈希类型很类似）。使用ziplilst的好处是内存消耗少。

* linkedlist（其实是双链表），当列表类型无法满足ziplist条件时，redis会使用linkedlist作为列表类型的内部实现。redis5.0中，提供了quicklist的实现，它是以一个ziplist为节点的linkedlist，结合了ziplist和linkedlist的优势。

## 集合 Set

Redis的Set 感觉和java的hashset的特点很类似，无序，成员唯一，通过哈希表来实现的，而java的也是通过hashset内部也是通过hashmap来实现的。所以其增删改的复杂度都是o(1).

* intset:当对象保存的都是整数值，且对象的元素格式不超过512（默认）个时使用inset  （set-max-intset-entries可以设置默认值）
* Hashtable:当不满足上述条件时用hashtable

## 有序集合Zset

```c
源码/src/server.h 
typedef struct zset {
    dict *dict;
    zskiplist *zsl;
} zset;
```



Zset是有序集合，有序，唯一，string类型。

元素并不是有序的，但是其每个元素都会关联一个double类型的score（此处的double类型，只针对redis 5.0.0，因为我看到有文章说这里是float类型的额，应该是跟版本有关系，我也没有每个版本去找，具体自己理解吧。源码位于-"源码/src/t_zset.c"）

大致的结构：

借用网上的一张图：![2019021720360467](/Users/jzue/Desktop/2019021720360467.png)



底层数据结构：

* ziplist：有序集合保存的元素小于128个，且有序集合保存的长度小于64字节，满足俩条件的则用ziplist；其他情况会是跳表（skiplist）
* Skiplist





总结一下，实际上，我感觉就算你对redis的源码很恐惧，毕竟，也很多，特别是像我这样的不太熟悉c语言，完全停留在老师上课讲的那点东西，而且都还忘记的差不多了，但是依然可以找资料，看看少部分的核心代码，毕竟逻看看逻辑，稍微感受下是没问题的，至少你看看READ.ME肯定是啥问题都没问题的，里面有各个文件是干啥的介绍。然后看看server.h里面定义的结构体，也是没问题的，也不一定看多懂，假装看一下是应该的，毕竟开源的资源，而且你天天用，当然你如果说，你根本用不到它，那你就当我没说。

