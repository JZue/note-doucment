yum install -y git





方式一、设置记住密码（默认15分钟）

git config --global credential.helper cache
方式二、自己设置时间（这里设置15分钟---60*15=900s）

git config credential.helper 'cache --timeout=900'
方式三、长期存储密码

git config --global credential.helper store