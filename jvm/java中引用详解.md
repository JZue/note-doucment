从`JDK 1.2`版本开始，对象的引用被划分为`4`种级别，从而使程序能更加灵活地控制**对象的生命周期**。这`4`种级别**由高到低**依次为：**强引用**、**软引用**、**弱引用**和**虚引用**。

强引用：有强引用的对象垃圾收集器不会回收掉被引用的对象，即使是OOM

软引用：在系统即将发生OOM之前，会将这些对象列进回收范围之中，然后进行二次回收，如果回收之后依然没有足够的内存，才会OOM

弱引用：弱引用只能生存到下一次垃圾收集发生之前，当垃圾收集器工作时，无论内存是否足够，都会回收掉只被弱引用关联的对象

虚引用：一个对象有虚引用的存在，虚引用完全不会对对应的生存时间构成影响，也无法通过虚引用来取的对象实例。为一个对象设置虚引用关联的唯一目的是希望能在这个对象被垃圾回收的时候收到一个系统通知





