* 单表数据过多
* 关联表太多，太多join
* 没有利用索引或者索引失效或者低性能索引
* 服务器调优及各个参数设置（缓冲、线程数等）
* SQL写的烂
* 死锁，锁等待。游标，临时表等待
* Exist 代替in
* Between  代替in 
* left outer join 代替Not IN
* union all 代替union
* 常用SQL尽量用绑定变量方法