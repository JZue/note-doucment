问题表现：接口首次调用慢，并且慢的原因是因为其中的一个rpc,耗时占比70%

问题北京：一个功能，时间上有要求，并且表数据量大，单表8000万，另一张表4000万数据，表结构通过sql 完成不了需求。方案上，

 	1. 要么改表结构，导数据
 	2. 一次性拉出所有数据，内存中操作



中间，调优时发现，系统启动，首次调用接口时，其中的第一个rpc，rpc耗时占用70%



```
Ribbon的默认加载策略是懒加载。当第一次访问的时候，不仅会发送访问请求，还有初始化相关的服务。
```

```
: DynamicServerListLoadBalancer for client serverxjz initialized: DynamicServerListLoadBalancer:{NFLoadBalancer:name=webfrontxjz,current list of Servers=[10.1.22.192:8000],Load balancer stats=Zone stats: {defaultzone=[Zone:defaultzone;	Instance count:1;	Active connections count: 0;	Circuit breaker tripped count: 0;	Active connections per server: 0.0;]
},Server stats: [[Server:10.1.22.192:8000;	Zone:defaultZone;	Total Requests:0;	Successive connection failure:0;	Total blackout seconds:0;	Last connection made:Thu Jan 01 08:00:00 CST 1970;	First connection made: Thu Jan 01 08:00:00 CST 1970;	Active Connections:0;	total failure count in last (1000) msecs:0;	average resp time:0.0;	90 percentile resp time:0.0;	95 percentile resp time:0.0;	min resp time:0.0;	max resp time:0.0;	stddev resp time:0.0]
]}ServerList:org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServerList@16446066
```

