```
docker pull redis:5.0.0-alpine
```

```
docker run -p 6379:6379 -v /Data/volume/redis/conf:/etc/redis/redis.conf -v /Data/volume/redis/data:/data --name redis -d redis:5.0.0-alpine  redis-server --appendonly yes


命令说明：
redis-server --appendonly yes : 在容器执行redis-server启动命令，并打开redis持久化配置

xh*0jFBNLb

redis-server --requirepass xh*0jFBNLb
```

```
 docker exec -it 5ef redis-cli

127.0.0.1:6379> keys *
1) "backup3"
2) "series_code"
3) "47222.131.155.180"
4) "TshsvbJJNwGpgeTHJzonAlJPVWCXLaji"
5) "backup1"
6) "ArticleCount"
7) "backup2"
8) "backup4"
9) "article_views
```

