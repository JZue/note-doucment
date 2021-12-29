* OptimisticLock、PessimisticLock相关的DEMO,首先得确实，自动生成的是Innodb存储引擎~关于Innodb我是已经配置了的，建议，可能有版本的bug，我还是在此提示一下。
* OptimisticLock（乐观锁）我默认直接使用的就是jpa的@Version注解作为示例.
    * OptimisticLock乐观锁的理解：主观认为一般情况下数据不会造成冲突，所以在数据进行提交更新时才会对数据的冲突与否进行检测。如果没有冲突那就OK；如果出现冲突了，则返回错误信息并让用户决定如何去做。
* OptimisticLock（乐观锁）--我此处示例使用的是for update 意向锁.
    * PessimisticLock（悲观锁）：主观认为一般情况下数据总会造成冲突，所以在数据进行提交更新时才会对数据加锁。然后事务串行执行。
* ShareLock s+x锁，实现了一个死锁的例子----》对应的LockService.shareLockExample()