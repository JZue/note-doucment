```
cd /root/admin-blog
docker build -t admin-blog -f ./Dockerfile .
```



```
docker run --name admin-blog -d  -p 8000:80 admin-blog:latest
```

```
FROM circleci/node:latest-browsers as builder

WORKDIR /usr/src/app/
USER root
COPY package.json ./
RUN yarn

COPY ./ ./


RUN npm run build


FROM nginx

WORKDIR /usr/share/nginx/html/

COPY ./docker/nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=builder /usr/src/app/dist  /usr/share/nginx/html/

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

