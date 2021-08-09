本文前篇幅**前篇部分**我写了下我今天配置的时候遇到的一个小问题，然后**原理部分**，参考<https://www.cnblogs.com/frankb/p/7427944.html>，而且这篇文章的命令详解也很详细，但是我觉得我看起来，不那么直观的感觉，有兴趣的可以点进去看一下。而我**最后命令详解的部分**，参考其他文章

## 今天配置遇到的小坑

具体的配置文件就是上面的那个配置文件。情况大概就是，我博客小站最开始的时候，并没有启动防火墙，只配置了安全组。这几天有时间，所以准备稍微整一下自己的小站。

**防火墙与安全组的区别**

安全组很像防火墙参考实现,它们都是使用IPTables规则来做包过滤。他们之间的区别在于:

```
1. 安全组由L2 Agent来实现,也就是说L2 Agent,比如neutron-openvswitch-agent和neutron-linuxbridge-agent,会将安全组规则转换成IPTables规则,而且一般发生在所有计算节点上。防火墙由L3 Agent来实现,它的规则会在租户的Router所在的L3 Agent节点上转化成IPTables规则。

2. 防火墙保护只能作用于跨网段的网络流量,而安全组则可以作用于任何进出虚拟机的流量。

3. 防火墙作为高级网络服务,将被用于服务链中,而安全组则不能。
```

在Neutron中同时部署防火墙和安全组可以达到双重防护。外部恶意访问可以被防火墙过滤掉,避免了计算节点的安全组去处理恶意访问所造成的资源损失。即使防火墙被突破,安全组作为下一到防线还可以保护虚拟机。最重要的是,安全组可以过滤掉来自内部的恶意访问。



然后一顿查资料，配出了上面的那个配置文件。但是现在的情况是，请求小站的时候，一直请求超时。并且把防火墙一关闭，又没问题了。

**这时候就到了排错环节**

- 第一反应，防火墙配置有问题。

  - ping ip 正常响应
  - Telnet  ip  port  发现已经是可以访问的了。

  这个时候真的很疑惑，查了很多资料，感觉可能是自己的iptables的配置和Nginx的配置有啥不契合的地方

- 查看nginx 的log

  ```java
  [error] 32192#0: *9450 upstream timed out (110: Connection timed out) while reading response header from upstream, client: 113.57.169.98, server: jzuekk.com, request: "GET / HTTP/1.1", upstream: "http://127.0.0.1:8080/", host: "jzuekk.com"
  ```

  解释一下，大致意思就是超时了，并且是在读取这个响应的header的时候，然后这个响应是来自于http://127.0.0.1:8080/。

  消化一下，响应的时候超时，并且有了Nginx日志，那么说明请求已经到了Nginx，并且已经到了对应的服务，只是响应消息到不了Nginx。

  响应消息为啥到不了Nginx呢？而且是跟防火墙有关。难道是本机调用的响应也是需要配置防火墙的？

  但是，怎么可能，这明显不是答案。那么难道是我代码本身里面的调用又问题了？

  想想代码里调用了哪些？mysql/redis/es...

  就这些啊，而且服务本身和这些应用，我都是部署到一个服务器上的，怎么可能会有问题呢？

  但是，我还是去看了下日志，

  ```java
  org.springframework.dao.QueryTimeoutException: Redis command timed out; nested exception is io.lettuce.core.RedisCommandTimeoutException: io.lettuce.core.RedisCommandTimeoutException: Command timed out after 1 minute(s)
  ```

  Redis连接超时，我擦？赶紧去看了下线上环境的配置文件，原来我redis的配置并非是直接用的localhost，而是用的ip+port+password，这样，访问的时候，就会经过防火墙了，那么，解决方案就只有俩：

  要么改application.properties配置（把redis的host 改为127.0.0.1），要么改防火墙配置(开启redis端口的访问权限)；

  至此，自己把自己坑了一把~~~~~



#### linux 的iptables 和firewall到底啥关系

```
firewall是centos7里面的新的防火墙命令，它底层还是使用 iptables 对内核命令动态通信包过滤的，实际上就是说，firewall是调用的iptables 的 command，去执行内核的 netfilter。
```

#### iptables是什么

```
iptables是隔离主机以及网络的工具，通过自己设定的规则以及处理动作对数据报文进行检测以及处理。
防火墙的发展史就是从墙到链再到表的过程，也即是从简单到复杂的过程。为什么规则越来越多，因为互联网越来越不安全了，所有防火墙的的规则也越来越复杂。防火的工具变化如下：ipfirewall(墙)-->ipchains（链条）--iptables（表）
2.0版内核中，包过滤机制是ipfw，管理工具是ipfwadm；
2.2 版内核中，包过滤机制ipchain，管理工具是ipchains；
2.4版及以后的内核中，包过滤机制是netfilter，管理工具iptables。
```

## 原理

#### 组成

```java
	linux的防火墙由netfilter和iptables组成。用户空间的iptables制定防火墙规则，内核空间的netfilter实现防火墙功能。
    netfilter（内核空间）位于Linux内核中的包过滤防火墙功能体系，称为Linux防火墙的“内核态”。
    iptables(用户空间)位于/sbin/iptables，是用来管理防火墙的命令的工具，为防火墙体系提供过滤规则/策略，决定如何过滤或处理到达防火墙主机的数据包，称为Linux防火墙的“用户态”。
```

#### 实现方式

Linux系统的防火墙功能是由内核实现的，包过滤防火墙工作在TCP/IP的网络层。

![1](http://file.xjzspace.com/20210722111158.png)

```java
用户空间的iptables制定相应的规则策略控制内核空间的netfilter处理相应的数据访问控制。
iptables有四表五链(其实有五表，一个是后来加进来的)，四表分别是下图的的raw，mangle,nat，filter表。五链分别是PREROUTING,INPUT,OUTPUT,FORWARD,POSTROUTING链。表有什么用？链又有什么用呢？其实表决定了数据报文处理的方式，而链则决定了数据报文的流经哪些位置。
你可以从下图中看出规则表的优先级：raw-->mangle-->nat-->filter。
```

![2](http://file.xjzspace.com/20210722111202.png)



**四表（ps：现在有五表了，所以把另一个表也加了进来）**

```
filter表:确定是否方形该数据包(过滤)
nat表:修改数据包的源、目标IP或者端口
mangle表:为数据包设置标记
raw表:确定是否对该数据包进行状态跟踪
security表:是否丁艺强制访问控制规则(MAC)
```

**内置五链**

```
INPUT:处理入站数据包
OUTPUT:处理出站数据包
FORWARD:处理转发数据包(主要讲数据转发到本机的其他网卡设备)
PREROUTING链:在进行路由选择前的数据包处理(判断目标主机)
POSTROUTING链:在进行路由选择后的处理数据包(判断经由哪一接口送往下一跳)
```

​	制作防火墙规则通常有两种基本策略。**一是黑名单策略；二是白名单策略。**
​	黑名单策略指没有被拒绝的流量都可以通过，这种策略下管理员必须针对每一种新出现的攻击，制定新的规则，因此不推荐。
​	白名单策略指没有被允许的流量都要拒绝，这种策略比较保守，根据需要，逐渐开放，目前一般都采用白名单策略，推荐。

#### 数据包过滤匹配流程

![3](http://file.xjzspace.com/20210722111211.png)

如图，我们可以分析数据报文进入本机后应用了哪些表规则以及链规则，根据表规则和链规则我们分析数据包的过滤匹配流程。
比如我们制定一个filter表的规则，filter表决定是否放行数据包通过，那如果通过，则必须经由INPUT链流入数据包，INPUT链是处理入站数据的，如果没问题，继续放行到用户空间，再经由OUTPUT链将数据包流出。
那如果是nat表的规则，nat表主要实现转发功能，数据包先经由PREROUTING链进行路由选择，选择好路线后再经由FORWARD链转发数据，然后再进行一个路由选择，最后由POSTROUTING链流出数据。
其他表规则的数据包流程不做介绍，图中介绍的十分详尽。





## /etc/sysconfig/iptables配置文件详解

**这是一份配置文件的例子**

```shell
# sample configuration for iptables service
# you can edit this manually or use system-config-firewall
# please do not ask us to add additional ports/services to this default configuration
*filter
:INPUT ACCEPT [0:0]
:FORWARD ACCEPT [0:0]
:OUTPUT ACCEPT [0:0]
-A INPUT -m state --state RELATED,ESTABLISHED -j ACCEPT
-A INPUT -p icmp -j ACCEPT
-A INPUT -i lo -j ACCEPT
-A INPUT -p tcp  --dport 22 -j ACCEPT
-A INPUT -s localhost -d localhost -j ACCEPT
-A INPUT -p tcp  --dport 443 -j ACCEPT
# 具体配置请置于上面，或者使用-I，相当于在链头插入一条配置记录,具体规则见下表
-A INPUT -j REJECT --reject-with icmp-host-prohibited
-A FORWARD -j REJECT --reject-with icmp-host-prohibited
COMMIT
```

**管理iptables规则**

```
                iptables 命令的管理控制选项
选项名     功能及特点
-A      在指定链的末尾添加（--append）一条新的规则
-D      删除（--delete）指定链中的某一条规则，按规则序号或内容确定要删除的规则
-I      在指定链中插入（--insert）一条新的规则，若未指定插入位置，则默认在链的开头插入
-R      修改、替换（--replace）指定链中的某一条规则，按规则序号或内容确定要替换的规则
-L      列出（--list）指定链中的所有的规则进行查看，若未指定链名，则列出表中所有链的内容
-F      清空（--flush）指定链中的所有规则，若未指定链名，则清空表中所有链的内容
-N      新建（--new-chain）一条用户自己定义的规则链
-X      删除指定表中用户自定义的规则链（--delete-chain）
-P      设置指定链的默认策略（--policy）
-n      使用数字形式（--numeric）显示输出结果，若显示主机的IP地址而不是主机名
-v      查看规则列表时显示详细（--verbose）的信息
-V      查看iptables命令工具的版本（--Version）信息
-h      查看命令帮助信息（--help）
--line-number   查看规则列表时，同时显示规则在链中的顺序号
```





**下面是常见命令的解释(注意如果用命令行，只需要在下面的这些命令前加上“iptables”，然后如果直接修改配置文件，-A的得注意要放在最后的host prohibited的上面，或者直接用-I)**

```powershell
:INPUT ACCEPT [0:0]
# 该规则表示INPUT表默认策略是ACCEPT
#ACCEPT（允许流量通过）、LOG（记录日志信息）、REJECT（拒绝流量通过）、DROP（拒绝流量通过）；其中REJECT和DROP的动作操作都是把数据包拒绝，DROP是直接把数据包抛弃不响应，而REJECT会拒绝后再回复一条“您的信息我已收到，但被扔掉了”，让对方清晰的看到数据被拒绝的响应(下面的几个命令例子都是基于ACCEPT)
```

```shell
:FORWARD ACCEPT [0:0]
　　# 该规则表示FORWARD表默认策略是ACCEPT
　　#ACCEPT（允许流量通过）、LOG（记录日志信息）、REJECT（拒绝流量通过）、DROP（拒绝流量通过）；其中REJECT和DROP的动作操作都是把数据包拒绝，DROP是直接把数据包抛弃不响应，而REJECT会拒绝后再回复一条“您的信息我已收到，但被扔掉了”，让对方清晰的看到数据被拒绝的响应(下面的几个命令例子都是基于ACCEPT)
```

```shell
:OUTPUT ACCEPT [0:0]
　　# 该规则表示OUTPUT表默认策略是ACCEPT
　　#ACCEPT（允许流量通过）、LOG（记录日志信息）、REJECT（拒绝流量通过）、DROP（拒绝流量通过）；其中REJECT和DROP的动作操作都是把数据包拒绝，DROP是直接把数据包抛弃不响应，而REJECT会拒绝后再回复一条“您的信息我已收到，但被扔掉了”，让对方清晰的看到数据被拒绝的响应(下面的几个命令例子都是基于ACCEPT)
```

```shell
-A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT
　　# 意思是允许进入的数据包只能是刚刚我发出去的数据包的回应，ESTABLISHED：已建立的链接状态。RELATED：该数据包与本机发出的数据包有关。
```

```shell
-A INPUT -j REJECT --reject-with icmp-host-prohibited
-A FORWARD -j REJECT --reject-with icmp-host-prohibited
　　# 这两条的意思是在INPUT表和FORWARD表中拒绝所有其他不符合上述任何一条规则的数据包。并且发送一条host prohibited的消息给被拒绝的主机。
```

```
-A INPUT -p icmp -j ACCEPT
#允许别人通过ping命令来查看我们的主机是否在线
```

```
-A INPUT -s 192.168.1.0/24 -p tcp --dport 22 -j ACCEPT
#只允许本机22端口被192.168.1.0/24网段访问，其他流量均被拒绝，tcp协议
```

```
-A INPUT -p tcp --dport 8888 -j REJECT
#拒绝所有人访问本机8888端口，tcp协议
```

```
-A INPUT -s 192.168.1.200 -p tcp --dport 80 -j REJECT
#拒绝指定主机192.168.1.200访问本机80端口，tcp协议
```

```
-A INPUT -p tcp --dport 4444:5555 -j REJECT
#拒绝所有人访问本机4444到5555端口,tcp协议
```

还有挺多的，具体的命令的话，要用的时候翻翻文档，主要在于理解这个东西。



