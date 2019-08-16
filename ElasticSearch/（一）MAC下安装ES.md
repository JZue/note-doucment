安装步骤:

首先得必须先安装jdk，此处不介绍了

**更新brew**

```
brew update
```

**安装es**

```
brew install elasticsearch
```

**运行es**

```
brew services start elasticsearch
```

```
访问http://localhost:9200
```

**安装Kibana**

**Kibana**是**ES**的一个配套工具，可以让用户在网页中与**ES**进行交互

```
brew install kibana
```

**启动Kibana**

```
brew services start kibana
```

**本地浏览器访问**

```
http://localhost:5601
```

```
localhost:9200/_cat/health?v
```



> 注意看 `status`字段,他有三个值含义分别如下

- green:一切都很好（集群功能齐全）
- yellow:所有数据都可用，但尚未分配一些副本（群集功能齐全）
- Red:某些数据由于某种原因不可用（群集部分功能）

