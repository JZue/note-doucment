查看是否开启profiling

show variables like 'profiling';

开启profiling

set profiling ='on';

执行sql，此处是一个全表扫描

select * from ms_drive_detail_log;

查询sql执行记录

show profiles;

根据记录的query_id查询对应的sql的执行情况

show profile cpu for query 13;

如果在如下执行情况都是有问题的

* Converting  Heap to MyISAM 查询结果集太大，内存不够用了
* Creating tmp table 创建零时表
  * 新建零时表
  * 使用
  * 再删除
* Copying  to tmp table on disk 把内存中临时表复制到磁盘，危险
* Locked



关闭profiling

set profiling ='off';