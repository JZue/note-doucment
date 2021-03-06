> 现代CPU为了提升执行效率，减少CPU与内存的交互(交互影响CPU效率)，一般在CPU上集成了多级缓存架构，常见的为三级缓存结构

- `L1 Cache`，分为数据缓存和指令缓存，逻辑核独占
- `L2 Cache`，物理核独占，逻辑核共享
- `L3 Cache`，所有物理核共享

#### cpu三级缓存架构

[参考文章](https://blog.csdn.net/qq_38877888/article/details/103118068)

![cpu概貌](https://img-blog.csdnimg.cn/20191118103700419.jpg)

![cpu三级缓存架构](./cpu_cache_level.png)

#### 各级缓存的定义

L1是最接近CPU的，它容量最小，速度最快，每个核上都有L1 Cache(准确地说每个核上有两个L1 Cache， 一个存数据 L1d Cache， 一个存指令 L1i Cache)；

L2 Cache 更大一些，例如256K，速度要慢一些，一般情况下每个核上都有一个独立的L2 Cache；二级缓存就是一级缓存的缓冲器：一级缓存制造成本很高因此它的容量有限，二级缓存的作用就是存储那些CPU处理时需要用到、一级缓存又无法存储的数据。

L3 Cache是三级缓存中最大的一级，例如12MB，同时也是最慢的一级，在同一个CPU插槽之间的核共享一个L3 Cache。三级缓存和内存可以看作是二级缓存的缓冲器，它们的容量递增，但单位制造成本却递减。

#### 各级缓存的关系

一级缓存中还分数据缓存（data cache，d-cache）和指令缓存（instruction cache，i-cache）。二者分别用来存放数据和执行这些数据的指令，而且两者可以同时被cpu访问，所以一级cache间数据时独立的。

一级没有的数据二级可能有也可能没有。因为一级缓存miss会接着访问二级缓存。

一级有二级一定有，三级也一定有。因为一级的数据从二级中读上来的。在一级缺失二级命中时发生。

二级没有的数据三级可能有也可能没有。因为二级确实会接着访问三级缓存。找不到会继续访问主存。

二级有的数据三级一定有。在二级缺失三级命中时，数据会从三级缓存提到二级缓存。

三级没有的数据，主存可能有也可能没有。三级缓存缺失，会访问主存，主存也缺失就要从外存访问数据了。

三级缓存有的数据主存一定有。因为在三级缺失主存命中时，数据会从主存提到三级缓存中来。




存储器存储空间大小：内存>L3>L2>L1>寄存器；
存储器速度快慢排序：寄存器>L1>L2>L3>内存；
什么是缓存行？
缓存是由最小的存储区块-缓存行(cacheline)组成，缓存行大小通常为64byte。

缓存行是什么意思呢？

比如你的L1缓存大小是512kb,而cacheline = 64byte,那么就是L1里有512 * 1024/64个cacheline



寄存器：

https://blog.csdn.net/Iamthedoctor123/article/details/84451724



基于栈的虚拟机和基于寄存器的虚拟机

https://blog.csdn.net/dashuniuniu/article/details/50347149