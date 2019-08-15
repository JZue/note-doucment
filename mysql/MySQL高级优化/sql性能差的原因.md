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



 a.当无order by条件时，根据实际情况，使用left/right/inner join即可，根据explain优化 ；

 b.当有order by条件时，如select * from a inner join b where 1=1 and other condition order by a.col；使用explain解释语句；

​     1）如果第一行的驱动表为a，则效率会非常高，无需优化；

​     2）否则，因为只能对驱动表字段直接排序的缘故，会出现using temporary，所以此时需要使用**STRAIGHT_JOIN**明确a为驱动表，来达到使用a.col上index的优化目的；**或者使用left join且Where条件中不含b的过滤条件**，此时的结果集为a的全集，**而STRAIGHT_JOIN为inner join且使用a作为驱动表**