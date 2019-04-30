Hashtable:
 1. key和value都不许有null值
 2. 使用enumeration遍历
 3. 同步的，每次只有一个线程能够访问
 4. 在java中Hashtable是H大写，t小写，而HashMap是H大写，M大写
 
HashMap:
 1. key和value可以有null值,但是key只允许一个null，因为key是不允许重复的
 2. 使用iterator遍历 
 3. 未同步的，多线程场合要手动同步HashMap
 
 HashSet
 1. 底层调用HashMap
 2. 不允许有重复值 
List 集合可以存储null，添加几个，存储几个；
Set集合也可以存储null，但只能存储一个，即使添加多个也只能存储一个；
HashMap可以存储null键值对，键和值都可以是null，但如果添加的键值对的键相同，则后面添加的键值对会覆盖前面的键值对，即之后存储后添加的键值对；
Hashtable不能碰null，不管是值还是键，一见null就报空指针

List特点：元素有放入顺序，元素可重复
Map特点：元素按键值对存储，无放入顺序 (LinkeHashMap有序)
Set特点：元素无放入顺序，元素不可重复（注意：元素虽然无放入顺序，但是元素在set中的位置是有该元素的HashCode决定的，其位置其实是固定的）
List接口有三个实现类：LinkedList，ArrayList，Vector
LinkedList：底层基于链表实现，链表内存是散乱的，每一个元素存储本身内存地址的同时还存储下一个元素的地址。链表增删快，查找慢
ArrayList和Vector的区别：ArrayList是非线程安全的，效率高；Vector是基于线程安全的，效率低
Set接口有两个实现类：HashSet(底层由HashMap实现)，LinkedHashSet
SortedSet接口有一个实现类：TreeSet（底层由平衡二叉树实现）
Query接口有一个实现类：LinkList
Map接口有三个实现类：HashMap，HashTable，LinkeHashMap
  HashMap非线程安全，高效，支持null；HashTable线程安全，低效，不支持null
SortedMap有一个实现类：TreeMap
其实最主要的是，list是用来处理序列的，而set是用来处理集的。Map是知道的，存储的是键值对
