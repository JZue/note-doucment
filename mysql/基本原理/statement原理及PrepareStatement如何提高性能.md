#### PrepareStatement  Statement 关联

* public class PreparedStatement extends StatementImpl implements java.sql.PreparedStatement{...}
* public class StatementImpl implements Statement {....}

* StatementImpl实现了Statement接口;
* PreparedStatement继承了StatementImpl并且实现了PreparedStatement接口

#### 技术原理

```
Statement.executeUpdate("INSERT INTO tb1_students (name,age,sex,address) VALUES('"+var1+"','"+var2+"',"+var3+",'"+var4+"')");


prepareStatement= connection.prepareStatement("INSERT INTO tb1_students (name,age,sex,address) VALUES (?,?,?,?)"); 
prepareStatement.setString(1,var1); 
prepareStatement.setString(2,var2); 
prepareStatement.setString(3,var3); 
prepareStatement.setString(4,var4); 
prepareStatement.executeUpdate(); 
```

* 每一种数据库都会尽最大努力对预编译语句提供最大的性能优化。

* 因为预编译语句有可能被重复调用  ，所以语句在被数据库(DB)的编译器编译后的执行代码被缓存下来,那么下次调用时只要是相同的预编译语句就不需要编译,只要将参数直接传入编译过的语句执行代码中(相当于一个函数)就会得到执行。这并不是说只有一个Connection中多次执行的预编译语句被缓存,而是对于整个DB中,只要预编译的语句语法和缓存中匹配。那么在任何时候就可以不需要再次编译而可以直接执行，
* 而在statement的语句中,即使是相同一操作,而由于每次操作的数据不同所以使整个语句相匹配的机会极小,几乎不太可能匹配

#### statement   SQL注入

![](/Users/jzue/Downloads/20171127152605476.png)





