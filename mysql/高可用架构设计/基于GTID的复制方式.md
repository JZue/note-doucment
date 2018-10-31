# 基于GTID的复制（mysql5.6及以后）

(imooc Mysql性能管理及架构设计-5.06基于日志点的复制)
[参考文章](https://www.linuxidc.com/Linux/2016-09/135577.htm)

###### 注意：使用GTID可以保证同一事务只在指定的从库执行一次
	
- 从库先告诉主库已经执行了哪些事务的GTID值
- 主库返回未执行的事务的 `GTID` 值，然后让从库执行

### 1.什么是GTID

`GTID` 即全局事务 `ID` ,器保证为每一个在主上提交的事务在复制集群中可以生成一个唯一的 `ID`.

	GTID=source_id:transaction_id
	
`source_id` :是主库的 `server UUID` ，在数据目录的 `auto.cnf` 文件中。
`transaction_id` : 从1开始的一个序列。

### 2.基于GTID复制的步骤

1. 在主DB服务器上建立复制帐号。和基于日志点是一样的。

	create user ‘repl’@ip 段 identified by ‘pwd’；

	grant replication slave on *.* to ‘repl’@ip 段；

2. 配置主数据库服务器

	bin_log =/usr/local/mysql/log/mysql-bin  //最好区分数据和日志的存储

	server_id=1001

	gtid_mode=on //是否启用gtid

	enforce-gtid-consiste: //强制事务一致性，保证事务的安全
		enforce-gtid-consiste注意点：不能使用：
			1.create table 。。select
			2.在事务中使用create temporary table 建立临时表，使用关联更新事务表和非事务表。

	log-slave-updates=on //从服务器中记录主服务器发过来的日志修改 （5.7之后不一定要启动这个，但是再5.7之前要使用gtid 就必须开启）

3. 配置从服务器。

	server_id=1002

	relay_log=relay_log

	gtid_mode=on

	enforce-gtid-consistency

建议配置

	read_only=on

保证从服务器数据安全性

	master_info_reposistory=TABLE

	relay_log_info_reposistory=TABLE

从服务器连接主服务器的信息和中继日志存放在 `master_info` ，和 `relay_log` 中。

4. 初始化从服务器数据

	mysqldump --master-data=2 -single-transaction
	xtarbackup –slave-info

记录备份时最后的事务 `GTID` 值

导出数据

	mysqldump --single-transaction --master-data=2 --triggers -routines --all-databases -uroot -p -P3308 >all2.sql

导入数据

	mysql -uroot -p -P3309 < all2.sql

5. 启动基于GTID的复制

	change master to master-host=’主服务IP’,

	master_user=’repl’,

	master_password=’password’,

	master_auto_position=1

	change master to MASTER_HOST='192.168.1.106', 
	MASTER_PORT=3308, 
	MASTER_USER='repl', 
	MASTER_PASSWORD='repl', 
	master_auto_position=1;//表明这是基于gtid的复制方式

	start slave;

	show slave status \G;
	
### 优点：
	
1. 可以方便的进行故障转移
2. 从库不会丢失主库上的任何修改

### 缺点：

1. 故障出来比较复杂
2. 对执行的SQL # to be continue
