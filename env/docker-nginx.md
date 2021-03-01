#### nginx 默认配置

```
user  nginx;
worker_processes  1;

error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';

    access_log  /var/log/nginx/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    keepalive_timeout  65;

    #gzip  on;

    server {
    	listen       80;
    	listen  [::]:80;
    	server_name  localhost;

    	#charset koi8-r;
    	#access_log  /var/log/nginx/host.access.log  main;

    	location / {
        	root   /usr/share/nginx/html;
        	index  index.html index.htm;
    	}

    	#error_page  404              /404.html;

    	# redirect server error pages to the static page /50x.html
    	#
    	error_page   500 502 503 504  /50x.html;
    	location = /50x.html {
        	root   /usr/share/nginx/html;
    	}

    	# proxy the PHP scripts to Apache listening on 127.0.0.1:80
   		#
    	#location ~ \.php$ {
    	#    proxy_pass   http://127.0.0.1;
    	#}

    	# pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
    	#
    	#location ~ \.php$ {
    	#    root           html;
    	#    fastcgi_pass   127.0.0.1:9000;
    	#    fastcgi_index  index.php;
    	#    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
    	#    include        fastcgi_params;
    	#}

    	# deny access to .htaccess files, if Apache's document root
    	# concurs with nginx's one
    	#
    	#location ~ /\.ht {
    	#    deny  all;
    	#}
    }
}
```



#### index.html

```
<h1>tencent-server-IP[49.232.219.109]</h1>
```

#### 环境搭建过程

```
# 机器创建目录和文件
mkdir /Data/conf/nginx/html /Data/logs/nginx
touch /Data/conf/nginx/nginx.conf
# 拉取镜像
docker pull nginx:1.19.4
# 默认配置启动镜像
docker run --name nginx-test -p 80:80 -d nginx:1.19.4
# cp默认配置
docker cp 358354f206fd:/etc/nginx/nginx.conf /Data/volume/nginx/nginx.conf
# 启动镜像
docker run -d --name nginx_1_19_4 -p 80:80 -p 443:443   \
-v /Data/volume/nginx/html:/usr/share/nginx/html \
-v /Data/volume/nginx/static:/usr/share/nginx/static \
-v /Data/volume/nginx/nginx.conf:/etc/nginx/nginx.conf \
-v /Data/volume/nginx/logs:/var/log/nginx \
-v /etc/ssl/:/etc/ssl \
nginx:1.19.4
```

#### nginx默认配置

```
server {
    listen       80;
    listen  [::]:80;
    server_name  localhost;

    #charset koi8-r;
    #access_log  /var/log/nginx/host.access.log  main;

    location / {
        root   /usr/share/nginx/html;
        index  index.html index.htm;
    }

    #error_page  404              /404.html;

    # redirect server error pages to the static page /50x.html
    #
    error_page   500 502 503 504  /50x.html;
    location = /50x.html {
        root   /usr/share/nginx/html;
    }

    # proxy the PHP scripts to Apache listening on 127.0.0.1:80
    #
    #location ~ \.php$ {
    #    proxy_pass   http://127.0.0.1;
    #}

    # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
    #
    #location ~ \.php$ {
    #    root           html;
    #    fastcgi_pass   127.0.0.1:9000;
    #    fastcgi_index  index.php;
    #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
    #    include        fastcgi_params;
    #}

    # deny access to .htaccess files, if Apache's document root
    # concurs with nginx's one
    #
    #location ~ /\.ht {
    #    deny  all;
    #}
}
```

```
docker exec -it nginx_1_19_4 /bin/bash



https://10.1.21.139/patientapi/patientmedicalrecord_getMedicalRecordList?ck=33AFA3F3-684F-4EAB-B890-597D22D675FC-i691&_hdfSignature=5ca87e283cea71739eb4ae25bd8bb3fa&app=haodf&b=620&certificateToken=ed9e219c65c5792b2804cfcd8dc52978&cid=30653027331&ck=33AFA3F3-684F-4EAB-B890-597D22D675FC-i691&ct=ed9e219c65c5792b2804cfcd8dc52978&deviceOpenUDID=B19770D3-89EC-4BCC-B47F-C867DD36D733&di=B19770D3-89EC-4BCC-B47F-C867DD36D733&dt=0d2663b2982056cfedb895b47459f44298002bb0bfbaccc8ff7aca81fbfbe6b7&hdfhs=ef941bdda9c2b4ed3f6b1538174840c3&hdfts=1605580278&m=iPhone%206%20Plus&n=2&os=ios&p=0&pageId=1&pageSize=10&patientId=312573980842&providerId=&s=APPL&sv=11.4.1&userId=30653027331&v=6.9.1
```

