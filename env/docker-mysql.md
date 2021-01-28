```
docker pull mysql:5.7


if [[ "$(docker images -q mysql:5.7 2> /dev/null)" == "" ]]; then
  echo "mysql:5.7 not exists";
	docker pull mysql:5.7;
fi
```

```
docker run --name mysql_5_7 -v /Data/volume/mysql/conf:/etc/mysql/conf.d -v /Data/volume/mysql/logs:/logs -v /Data/volume/mysql/data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=n&PgrPxEZ4>) -d -i -p 3306:3306 mysql:5.7
```

```
docker exec -it mysql_5_7 /bin/bash
mysql -u root -p
```

