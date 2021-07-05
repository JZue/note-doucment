```
docker build -t kk-blog-docker-image .
```





```
docker run --name kk-blog-docker-image -d 
-v /Data/logs/kk-blog:/log
-v /Data/volume/nginx/html/sitemap/:/https:/xjzspace.com/
-p 8080:8080 kk-blog-docker-image:latest
```



```
docker exec -it kk-blog-docker-image /bin/bash
```



```
docker run -ti -d --name my-nginx -v /etc/localtime:/etc/localtime:ro  docker.io/nginx  /bin/bash

```

