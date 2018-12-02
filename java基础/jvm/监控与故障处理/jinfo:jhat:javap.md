# jinfo

jinfo 是jdk自带的一个工具，它可以用来**查看**正在运行的java应用程序的**扩展参数**（JVM中-X标示的参数）；**甚至支持在运行时修改部分参数**。

​	   jinfo [ option ] pid

​    	  jinfo [ option ] executable core

​    	  jinfo [ option ][server-id@]remote-hostname-or-IP



# jhat

jhat也是jdk内置的工具之一。主要是用来分析java堆的命令，可以将堆中的对象以html的形式显示出来，包括对象的数量，大小等等，并支持**对象查询语言**。



# javap

javap是JDK自带的反汇编器，可以查看java编译器为我们生成的字节码。通过它，我们可以对照源代码和字节码，从而了解很多编译器内部的工作