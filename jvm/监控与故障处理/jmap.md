**jmap**

JVM Memory Map命令用于生成heap dump文件，如果不使用这个命令，还可以使用-XX:+HeapDumpOnOutOfMemoryError参数来让虚拟机出现OOM的时候自动生成dump文件。 jmap不仅能生成dump文件，还可以查询finalize执行队列、Java堆和永久代的详细信息，如当前使用率、当前使用的是哪种收集器等。



命令格式

```
`jmap [ option ] pid``jmap [ option ] executable core``jmap [ option ] [server-``id``@]remote-``hostname``-or-IP`
```





Mac  环境下    jmap   和jstack    连接不上

linux  环境下正常