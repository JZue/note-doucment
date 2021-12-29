## Jps (Java Virtual Machine Process Status Tool)

​	java  提供的java进程工具

**jps存放在JAVA_HOME/bin/jps，使用时为了方便需将JAVA_HOME/bin/加入到Path**



用法： 命令行直接jps -help  就可以看到具体的命令格式用法

常用命令：

​	**-q** 只显示pid，不显示class名称,jar文件名和传递给main方法的参数

​	**-m** 输出传递给main方法的参数，在嵌入式jvm上可能是null

​	**-l** 输出应用程序main class的完整package名或者应用程序的jar文件完整路径名

​	**-v** 输出传递给JVM的参数

​	**-V** 隐藏输出传递给JVM的参数

