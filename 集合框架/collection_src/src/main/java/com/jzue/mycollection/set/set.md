* HashSet基于HashMap 实现
    主要是利用HashMap的Key的不可重复性和散列表的o(1)的高效性
    看到 private static final Object PRESENT = new Object();不知道你有没有一点疑问呢。
    这里使用一个静态的常量Object类来充当HashMap的value，既然这里map的value是没有意义的，
    为什么不直接使用null值来充当value呢？比如写成这样子 private final Object PRESENT = null ;
    我们都知道的是，Java首先将变量PRESENT分配在栈空间，而将new出来的Object分配到堆空间，
    这里的new Object()是占用堆内存的（一个空的Object对象占用8byte），而null值我们知道，
    是不会在堆空间分配内存的。那么想一想这里为什么不使用null值。想到什么吗，
    看一个异常类 java.lang.NullPointerException ， 噢买尬，这绝对是Java程序员的一个噩梦，
    这是所有Java程序猿都会遇到的一个异常，你看到这个异常你以为很好解决，但是有些时候也不是那么容易解决
    ，Java号称没有指针，但是处处碰到NullPointerException。所以啊，
    为了从根源上避免NullPointerException的出现，浪费8个byte又怎么样，
    在下面的代码中我再也不会写这样的代码啦if (xxx == null) { … } else {….}，好爽。  
* LinkedHashSet基于LinkedHashMap实现
* TreeSet基于NavigableMap(接口)实现
