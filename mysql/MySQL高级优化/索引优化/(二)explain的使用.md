简介:explain 可以帮助我们分析 select 语句，让我们知道查询效率低下的原因，从而改进我们的查询,本文主要是explain 的一些字段项用处的解释，因为explain在工作用的很频繁，还是很有必要学会，应该说这个也算是索引基础知识



 **Id type key rows extra最重要** 

```sql
示例SQL:
explain 
select 
	a.dealer_id,
	count(1) 
from 
	ms_trial_run mt ,
	(select distinct
	 	dealer_id,
	 	vin 
	from
	 	ms_drive_detail_log 
	where 
		dealer_id in('HUB01','HUB02')) as a 
where  
	mt.vin=a.vin 
group by 
	a.dealer_id;
```



![mysql_index_2_1](/Users/jzue/Desktop/blog_file/mysql_index_2_1.png)

ID

```
1）id相同时，执行顺序是由table字段的由上至下

2）id不同时，id大的先执行

3）不同、相同都存在的时候先执行id大的，然后顺序由上至下执行

4）例如上图中的：顺序：第三条->第一条->第二条
```

Select_type 

```
1）SIMPLE 普通查询（不含Union 和任何子查询）

2）PRIMARY**

	 主查询  （查询中若包含任何复杂的子查询，则最外层被标记为paimary，俗称是鸡蛋壳）

3）SUBQUERY **

	子查询 

4）DERIVED**

	  在from列表中包含的子查询被标记为derived（衍生），mysql会递归执行这些子查询，把结果放在					临时表里（临时表会增加系统负担，但有时不得不用）（上面SQL 的a表）

5）UNION   

	有union操作
```



Table

```
对应操作的数据表
```



**UNION RESULT**

```
两种union结果的合并
```

type

```
system :表只要一行记录（等于系统表），这是const类型的特例，平时不会出现，这个也可以忽略不计
const:表示通过索引一次就找到了，const用于比较primary key 或者 unique索引，因为只匹配一行数据，所以很快，如将主键置于where列表中，mysql就能将该查询转换为一个常量。
eq_ref:唯一性索引扫描。对于每个索引建，表中只有一条记录与之匹配。常见于主键或唯一索引扫描
ref:非唯一性索引扫描，返回匹配某个单独值得所有行，本质上也是一种索引访问，它返回所有匹配某个单独值得行，然而它可能会找到多个符合条件的行，所以他应该属于查找和扫描的混合体
range:只检索给定范围的行，使用一个索引来选择行，key 列显示使用了哪个索引，一般就是你的where语句中出现了between、<、>、in等的查询(mysql5.7支持in走索引)，这种范围扫描索引扫描比全表扫描好，因为它至于要开始索引的某一点，而结束语另一点，不用扫描全部索引
index:full index scan(全索引扫描)，index与all区别为index类型只遍历索引树，这通常比all块，因为索引文件通常比数据文件小。（也就是说虽然all和index都是读全表，单index是从索引中读取的，而all是从硬盘中读的）
all:全表扫描

system>const>eq_ref>ref>range>index>ALL
```

possible_keys

```
可能用到的索引
```

key

```
实际用到的索引:
	如果为null，则没有使用索引
	查询中若使用了(覆盖索引)，则该索引仅出现在key列表中
```

key_len 

```
 使用索引的长度:在不损失精确性的情况下，长度越短越好。
 key_len显示的值为索引字段的最大可能长度，并非实际使用长度，即key_len是根据表定义计算而得，不是通过表内检索出的。
```

ref 

```
显示索引的哪些列被使用了
```

rows 

```
根据表统计信息及索引选用情况，大致估算出找到所需的记录所需要读取的行数(越少越好)，每张表有多少行被优化器查询
```

extra 

```
using filesort:一般出现在 使用了 order by 语句当中。说明mysql会对数据使用一个文件排序，而不是按照表内的索引顺序进行读取。

using temporary: 新建了临时表，常见于排序order by 和group by
  - 要么就不要建索引，建索引就要group by的优化
using index    用了覆盖索引，若同时出现using where ,表明查找使用了索引，但是需要的数据都在索引列中能找到，所以不需要回表查询数据
using where    SQL使用了where条件过滤数据。
using join buffer :使用了连接缓存
impossible where  表示 where子句的值总是false，比如where m.uid=1 and m.uid =2
select tables optimized away
distinct
........
情况挺多的，一般可以根据具体情况去Google相关文档，这也不可能都记得住，有点映像就行
```

总结：主要是explain 的一些字段项用处的解释，因为explain在工作用的很频繁，还是很有必要学会，应该说这个也算是索引基础知识