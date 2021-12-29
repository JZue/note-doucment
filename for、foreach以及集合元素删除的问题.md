* for循环是基于下标（偏移量）来定位的循环。
* foreach是基于指针直接移动（地址运算）的循环。
* 对于通过下标访问的ArrayList。使用下标访问的for循环效率本身就很高。所以foreach这种指针直接移动的效率可能甚至不如通过下标访问。但差别不会太大
* 但对于链式结构LinkedList，for基于下标访问会每次从头查询，最好不要使用for。foreach循环使用指针直接偏移的高效的地址运算，效率会高非常多，差距也很大。链表循环超过10万次for循环可能会直接卡死，而foreach仍然只需要几毫秒。

## 删除操作的问题



* foreach 循环会锁定集合中的对象，期间不能修改，必须遍历出结果以后再去另外的做修改操作



#### for循环遍历list

```java
for(int i=0;i<list.size();i++){
    if(list.get(i).equals("del"))
        list.remove(i);
}
```

​	这种方式的问题在于，删除某个元素后，list的大小发生了变化，而你的索引也在变化，所以会导致你在遍历的时候漏掉某些元素。比如当你删除第1个元素后，继续根据索引访问第2个元素时，因为删除的关系后面的元素都往前移动了一位，所以实际访问的是第3个元素。因此，这种方式可以用在删除特定的一个元素时使用，但不适合循环删除多个元素时使用。或者从后往前删除，就不会有问题了。

#### foreach 

```java
for(String x:list){
    if(x.equals("del"))
        list.remove(x);
}
```

这种方式的问题在于，删除元素后继续循环会报错误信息ConcurrentModificationException，因为元素在使用的时候发生了并发的修改，导致异常抛出。但是删除完毕马上使用break跳出，则不会触发报错。但是，这样的结果就是只能删除一个元素

#### iterator遍历

```java
Iterator<String> it = list.iterator();
while(it.hasNext()){
    String x = it.next();
    if(x.equals("del")){
        it.remove();
    }
}
```

这种方式可以正常的循环及删除。但要注意的是，使用iterator的remove方法，如果用list的remove方法同样会报上面提到的ConcurrentModificationException错误。