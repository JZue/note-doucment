#!/bin/bash
cd /Users/jzue/Desktop/git-repo/note-doucment
unset msg

#read -p "������commit�ύ������: " msg
msg="ϵͳ��ʱ�����Զ��ύ"
git add -A
git commit -m $msg
git push
git status

