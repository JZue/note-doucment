#!/bin/bash
cd /Users/jzue/Desktop/git-repo/note-doucment
unset msg

#read -p "请输入commit提交的描述: " msg
msg="系统定时任务自动提交"
git add -A
git commit -m $msg
git push
git status

