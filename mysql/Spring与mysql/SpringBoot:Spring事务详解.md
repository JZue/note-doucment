[参考文章](https://blog.csdn.net/hxpjava1/article/details/80976777)

## 声明式事务

### 概念

​	声明式事务基于AOP，其本质是对方法前后进行拦截，在方法开始前创建或加入一个事务，再根据目标方法执行的结果决定提交还是回滚事务。只需要在类、方法加上@Transactional注解就可以使用事务，没有入侵性，简单粗暴。

### 使用技巧

* @Transactional可以作用于接口、接口方法、类、类方法上，当作用到类时，该类下所有public方法都将具有该类型的事务属性，同时，也可以在方法级别使用该注解来覆盖类级别的定义。Spring的建议是在具体的实现类和类方法使用@Transactional注解，而不是使用在接口上。因为注解不能继承，不能被基于接口的代理类所识别，注解失效。
* 声明式事务管理默认只对非检查型异常unchecked Exception进行回滚，也就是对RuntimeException异常以及它的子类进行回滚操作。

![20180525160943689](/Users/jzue/Documents/picture/20180525160943689.png)

* 如果需要让checked Exception也进行回滚，需加上@Transactional(rollbackFor=Exception.class)
* 如果需要让unchecked Exception不进行回滚，需加上@Transactional(notRollbackFor=Exception.class)
* 在Springboot使用声明式事务需要在Application启动类加入@EnableTransactionManagement注解，相当于Spring的自动扫描
* 当有try catch后捕获了异常，事务不会回滚，如果不得不在service层写try catch 需要catch后 throw new RuntimeException 让事务回滚；Spring的AOP即声明式事务管理默认是针对unchecked exception回滚。也就是默认对R untimeException()异常或是其子类进行事务回滚；checked异常,即Exception可try{}捕获的不会回滚，如果使用try-catch捕获抛出的unchecked异常后没有在catch块中采用页面硬编码的方式使用spring api对事务做显式的回滚，则事务不会回滚， “将异常捕获,并且在catch块中不对事务做显式提交=生吞掉异常”

## 编程式事务

[参考](https://blog.csdn.net/tianyaleixiaowu/article/details/73123242)

**说实话，现阶段项目声明式事务已经可以满足基本需求了**