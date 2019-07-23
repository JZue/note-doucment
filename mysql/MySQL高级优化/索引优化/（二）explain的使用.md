 **Id type key rows extra最重要** 



* ID

  * id相同时，执行顺序是由table字段的由上至下
  * id不同时，id大的先执行
  * 不同、相同都存在的时候先执行id大的，然后顺序右上至下执行

* Select_type 

  * SIMPLE 普通查询（不含Union 和任何子查询）
  * PRIMARY 主查询
  * SUBQUERY 子查询
  * DERIVED
  * UNION
  * UNION RESULT

* type

     system>const>eq_ref>ref>range>index>ALL

* possible_keys

* key

* key_len  使用索引的长度

* ref 显示索引的哪些列被使用了

* rows  筛选出的数据行数

* extra 

  * using fieldsort:一般出现在 使用了 order by 语句当中。
  * using temporary: 新建了临时表，常见于排序order by 和group by
    * 要么就不要建索引，建索引就要group by的优化
  * using index  用了覆盖索引，若同时出现using where ,标明索引被用来执行对应的键值的查询
  * using where
  * using  join buffer  buffer可以调大一点
  * impossible where  表示 where子句的值总是false，比如where m.uid=1 and m.uid =2
  * select tables optimized away
  * distinct

* 覆盖索引
  * 查询列和索引的个数和顺序刚好吻合







p208节