https://blog.csdn.net/zwy8831/article/details/104587160

```
docker pull elasticsearch:6.8.3


docker run -d --name es6.8.3 -p 9200:9200 -p 9300:9300 elasticsearch:6.8.3

第一次运行有可能会报错，虚拟内存太小了
vim /etc/sysctl.conf
新增
vm.max_map_count=262144 
docker restart [containerId]

docker cp [containerId]:/usr/share/elasticsearch/data /Data/volume/es/

docker cp [containerId]:/usr/share/elasticsearch/config /Data/volume/es/

docker cp [containerId]:/usr/share/elasticsearch/plugins /Data/volume/es/

docker stop [containerId]

docker rm [containerId]

docker run -d --name es6.8.3 -p 9200:9200 -p 9300:9300 -v /Data/volume/es/data:/usr/share/elasticsearch/data -v /Data/volume/es/config:/usr/share/elasticsearch/config elasticsearch:6.8.3
/Data/volume/es/plugins:/usr/share/elasticsearch/plugins 



修改配置文件/Data/volume/es/config/elasticsearch.yml新增跨域配置
http.cors.enabled: true
http.cors.allow-origin: "*"

访问 https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.8.3/elasticsearch-analysis-ik-6.8.3.zip   下载，上传至服务器

mv elasticsearch-analysis-ik-6.8.3.zip  /Data/volume/es/plugins


进容器，新增分词器
docker exec -it es6.8.3 /bin/bash

./bin/elasticsearch-plugin install https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v6.8.3/elasticsearch-analysis-ik-6.8.3.zip

./bin/elasticsearch-plugin install ingest-attachment


```





```

```

```

```



安装分词插件

```

```

```

```

