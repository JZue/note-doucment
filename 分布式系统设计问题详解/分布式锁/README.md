分布式锁的目的：实现公共资源串行化使用

基于这个目的，我们的实现原则：

1. 强一致性
2. 服务高可用
3. 接入简单
4. 锁可自动续约及其释放
5. 最好还有可视化管理后台



分布式锁三种实现方式：

Redis,Zookeeper,Etcd,

优劣：