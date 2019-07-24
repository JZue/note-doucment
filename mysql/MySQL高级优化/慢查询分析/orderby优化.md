MySQL 支持filesort 和index两种排序

index 优于filesort 

order  by 

排序字段的顺序得按索引的顺序来，并且中间不能有间隔~

这里是与where后面的条件不同的，where 后面的条件，abc->a |ab|ba|abc|acb|bca|bac|cba|cab 都是可以的。

但是在order by 后面是abc->a|ab|abc   其他顺序都不可以



* group by 和order by 一致，但是有一点，能where 解决的就不要 having