####要素

    1. 稳定性，高可用
    2. 安全性
    3. 性能，并发性 
    4. 扩展性

####网关方案

    1. Nginx+Lua
    2. Tyk(go语言开发的)
    3. Spring Cloud Zuul
        路由+过滤器=Zuul
        核心是一系列的过滤器
        4种核心过滤器：
            前置（pre filter）：
                限流（早于鉴权，防止请求轰炸之类的）
                    eg: 令牌桶限流  (guava组件中已经有实现了)
                鉴权 后端在session中放入自己的token，然后网关访问session中的token校验；
                    注意，写入到redis要写在前面，免得出现，cookie写入成功，redis写入失败，然后遇到bug 
                参数校验 等等
            路由（routing filter）
            后置 (post filter)
            错误 (error filter)
九大内置对象

####Hystrix
    
    服务降级
        基本思想：优先核心任务，非核心服务不可以或者弱可用
    超时隔离
    依赖隔离：
        线程池隔离
        Hystrix 自动实现了依赖隔离
    熔断：
        Circuit Breaker 断路器：类似于跳闸

####Sleuth  链路监控
    
    引入依赖
    ZipKin  基于Docker来可视化链路监控
    观察Zipkin

####网易163yun Docker镜像：c.163yun.com
    Rancher： 更好的管理Docker
