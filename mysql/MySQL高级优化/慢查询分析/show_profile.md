简介：分析MySQL语句查询性能的方法除了使用 EXPLAIN 输出执行计划，还可以让MySQL记录下查询超过指定时间的语句，我们将超过指定时间的SQL语句查询称为“慢查询”。捕获慢查询之后我们可以通过explain看执行计划之外，我们还可以通过show profile 来看sql的执行细节和生命周期情况。



1）慢查询日志的开启和捕获

2）explain+SQL分析

3）show profile查询SQL在MySQL服务器中的执行细节和生命周期情况

4）SQL数据库服务器的参数调优



查看是否开启profiling

```sql
show variables like 'profiling';
```

开启profiling

```
set profiling ='on';
```

执行sql，此处是一个全表扫描

```sql
select * from ms_drive_detail_log;
```

查询sql执行记录

```
show profiles;
```

根据记录的query_id查询对应的sql的执行情况

```
show profile cpu for query 13;
```



如果在如下执行情况都是有问题的

* Converting  Heap to MyISAM 查询结果集太大，内存不够用了
* Creating tmp table 创建零时表
  * 新建零时表
  * 使用
  * 再删除
* Copying  to tmp table on disk 把内存中临时表复制到磁盘，危险
* Locked
* ……....等等



上图中很清楚了描述了一个sql的执行过程，以及每一步的耗时，占用的cpu资源；

如果有很慢的

关闭profiling

```
set profiling ='off';
```



**实操实例**

![屏幕快照 2019-07-31 下午3.41.19](/Users/jzue/Desktop/blog_file/mysql_index_5_1.png)

上图很 详细的反应了上面的那个SQL 的执行状态：

每一步的执行的CPU 以及时间开销，全都详细的展示在上面了，如图，我们可以清晰的看到耗时最长的在sending data这一步。