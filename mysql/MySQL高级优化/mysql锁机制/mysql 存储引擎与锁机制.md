简介：锁是计算机协调多个进程或线程并发访问某一资源的机制。在数据库中，除传统的 计算资源（如CPU、RAM、I/O等）的争用以外，数据也是一种供许多用户共享的资源。如何保证数据并发访问的一致性、有效性是所有数据库必须解决的一 个问题，锁冲突也是影响数据库并发访问性能的一个重要因素。从这个角度来说，锁对数据库而言显得尤其重要，也更加复杂。

MySQL 的存储引擎:

```
# 查看mysql支持的存储引擎，以及特点
show engines
```

![mysql_engines_1](/Users/jzue/Desktop/blog_file/mysql_engines_1.png)

就可以看到他们的特点了。



MySQL锁机制

不同的存储引擎支持不同的锁机制。

MyISAM和MEMORY存储引擎采用的是表级锁（table-level locking）；

InnoDB存储引擎既支持行级锁（row-level locking），也支持表级锁，但默认情况下是采用行级锁。



**表级锁：**开销小，加锁快；不会出现死锁；锁定粒度大，发生锁冲突的概率最高，并发度最低。

**行级锁：**开销大，加锁慢；会出现死锁；锁定粒度最小，发生锁冲突的概率最低，并发度也最高。 

**页面锁：**开销和加锁时间界于表锁和行锁之间；会出现死锁；锁定粒度界于表锁和行锁之间，并发度一般 

show open tables   0-无锁 其他值为有锁



本文只讲一下InnoDB，MyISAM经常是mysql曾经（5.5.5之前）的默认的数据库引擎，而innodb作为之后的默认的存储引擎，而且也是使用最广泛的存储引擎，所以主要讲一下它的锁机制。

**其实，说实在的，我真的只用过innodb**



然后对于数据库锁而已，处理x锁/s锁、自增锁、自增锁，其他的平时你可能经常接触，但是概念还真不一定记得住

<https://www.cnblogs.com/wanbin/p/9599553.html>

在MySQL InnoDB中共有七种类型的锁：

### 共享/排他锁（shared and exclusion locks）

​	InnoDB实现标准的**行级锁**，其中有两种类型的锁，共享（S）锁和独占（X）锁。

**共享（S）锁允许持有锁的事务读取行。独占（X）锁允许持有锁的事务更新或删除行。**

* 如果事务T1在行r上获得共享（S）锁，则来自某个不同事务T2的对行r的锁的请求按如下方式处理：
  * 可以立即授予T2对S锁的请求。 结果，事务T1和T2都在r上都获得S锁。
  * 如果事务T2请求的是X锁，则需等待t1事务释放行r上的s锁

* 如果事务T1在行r上保持独占（X）锁定，则不能立即授予来自某个不同事务T2的对r上任一类型的锁定的请求。 相反，事务T2必须等待事务T1释放其对行r的锁定。

### 意向锁（intention locks）

​	InnoDB支持多个粒度锁定，允许行锁和表锁共存。例如，`LOCK TABLES ... WRITE`等语句在指定的表上采用独占锁（X锁）。为了实现多个粒度级别的锁定，InnoDB使用意向锁。意向锁是表级锁，指示事务**稍后**对表中的行所需的锁定类型（共享或独占）。意向锁有两种类型：

- **意向共享锁（IS）**表示事务打算在表中的某几行上设置共享锁。
- **意向排他锁（IX）**表示事务打算在表中的某几行上设置独占锁。

例如，`SELECT ... LOCK IN SHARE MODE`设置IS锁定，`SELECT ... FOR UPDATE`设置IX锁定。

意向锁协议如下：

- 在事务可以获取表中某一行的共享锁之前，它必须首先在表上获取IS锁或更高级别的锁。
- 在事务可以获取表中某一行的独占锁之前，它必须首先获取表上的IX锁。

就网上的大部分文章而已，或许是我智商不太够，我搞不太懂他们在说啥~<https://www.zhihu.com/question/51513268>，然后我在知乎中找到了意向锁的一个比较容易理解的解释。



注意：**下面表中的X，IX，S，IS都是指表级锁类型，不是想当然的X和S就是行级锁定。**（这个很多文章没标明，看的我云里雾里）

![mysql_lock_1](/Users/jzue/Desktop/blog_file/mysql_lock_1.png)

用X锁锁表，是不需要申请IX的，用X锁锁行，才需要申请IX（IX锁的定义）。表格中的X、S都是指表级别锁， 比如alter table改变表结构的语句，可能需要把整个表锁起来，这个就是表级别锁，加这个锁，不需要提前持有IX锁，如果其他事务当前持有这个表的IX锁，那么alter table语句就会被阻塞，正如兼容表格中看到的，X和IX是不兼容的。

意向锁的意义：如果没有意向锁的话，则需要遍历所有整个表判断是否有行锁的存在，以免发生冲突。

### 记录锁（record locks）

记录锁是对索引记录的锁定。

 例如，`SELECT c1 FROM t WHERE c1 = 10 FOR UPDATE;` 

防止任何其他事务`插入，更新或删除`t.c1的值为10的行。

### 间隙锁（Gap locks）

​	当我们用范围条件而不是相等条件检索数据，并请求共享或排他锁时，InnoDB会给符合条件的已有数据记录的索引项加锁；对于键值在条件范围内但不存在的记录，叫做“间隙(GAP)”，InnoDB也会对这个“间隙”加锁，这种锁机制就是所谓的间隙锁(Gap Locks)锁。

间隙锁，锁定一个范围，但不包括记录本身。GAP锁的目的，是为了防止同一事务的两次当前读，出现幻读的情况。

### 临键锁（Next-key locks）

<https://www.cnblogs.com/zhoujinyi/p/3435982.html>

锁定一个范围，并且锁定记录本身。对于行的查询，都是采用该方法，主要目的是解决幻读的问题。

区别于间隙锁的不包括记录本身

```
select * from user where userId between 5 and 7;  MySQL中的between是包含边界的，反之not  between的范围是不包含边界值。(此处复习一下)
```

### 插入意向锁（insert intention locks）

插入意向锁是在行插入之前由INSERT操作设置的一种间隙锁定。

该锁表示以这样的方式插入的意向：如果有多个session插入同一个GAP时，他们无需互相等待，例如当前索引上有记录4和8，两个并发session同时插入记录6，7。他们会分别为(4,8)加上GAP锁，但相互之间并不冲突（因为插入的记录不冲突）。

### 自增锁（auto-inc locks）

AUTO-INC锁是由插入到具有AUTO_INCREMENT列的表中的事务所采用的特殊表级锁。

在最简单的情况下，如果一个事务正在向表中插入值，则任何其他事务必须等待对该表执行自己的插入，以便第一个事务插入的行接收连续的主键值。

InnoDB 在RR（read repeatable）隔离级别下，**能解决幻读问题**。

​	

#### 间隙锁危害

​	因为Query执行过程中通过范围查找的话，他会锁定整个范围内所有的索引键值，即使这个键值并不存在。
​	间隙锁有一个比较致命的弱点，就是当锁定一个范围键值之后，即使某些不存在的键值也会被无辜的锁定，而造成在锁定的时候无法插入锁定值范围内的任何数据，在某些场景下这可能会针对性造成很大的危害。



### MySQL 中的MVCC机制

英文全称为Multi-Version Concurrency Control,翻译为中文即 多版本并发控制。

这个得了解一下

MVCC并没有简单的使用数据库的行锁，而是使用了行级锁，row_level_lock,而非InnoDB中的innodb_row_lock.

<https://blog.csdn.net/w2064004678/article/details/83012387>

<https://www.cnblogs.com/chenpingzhao/p/5065316.html>



