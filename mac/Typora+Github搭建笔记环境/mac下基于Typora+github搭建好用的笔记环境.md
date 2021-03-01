环境分为四部分

* github仓库
* 本地Typora环境
* git自动提交脚本
* 本地快捷命令

以我本地环境为例

1. 在github上新建仓库

```
https://github.com/JZue/note-doucment.git
```

2. 配置Typora环境

```java
vim ~/.bash_profile
指定笔记目录(前提是你先创建一个本地的笔记仓库目录，对应上的是github的本地仓库)
export NOTEBOOK=/Users/jzue/Desktop/git-repo/note-doucment
alias opennote='open -a typora $NOTEBOOK'

```

3. git 自动提交脚本

```shell
#!/bin/bash
cd /Users/jzue/Desktop/git-repo/note-doucment
git add .
git commit -m 'crontab task auto commit...'
git push origin master
echo "---------------------------------------------------------------------"
```

上面的脚本可以自定义一些校验之类的，比如没有新增or修改则直接不执行提交

配置脚本快捷执行命令

```
alias commitnote='sh $NOTEBOOK/note-commit.sh'  用于手动的主动触发
```

4. 定时任务

```
编辑定时任务
crontab -e

定时任务，半个小时自动执行提交命令：
*/30 * * * * sh /Users/jzue/Desktop/git-repo/note-doucment/note-commit.sh  >> /tmp/logs/notebook/commit.log


```



