#!/bin/bash

unset msg

read -p "������commit�ύ������: " msg

git add -A
git commit -m $msg
git push
git status

