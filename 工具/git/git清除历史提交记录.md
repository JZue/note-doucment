```
git clone [URL]  克隆git仓库代码
cd [仓库名] 进入git仓库目录
git checkout --orphan new_brach 创建一个名为new_branch新的空的分支
git add -A 添加所有文件到new_branch分支
git commit -am "提交信息" 对new_branch 分支做一次提交
git branch -D master 删除master分支
git branch -m master 将当前所在的new_branch分支重命名为master
git push origin master --force 
```

