mac下启动federated引擎的方法：

方法一：执行命令sudo /usr/local/mysql-5.7.24-macos10.14-x86_64/support-files/mysql.server start --federated

方法二：直接在配置文件加：

但是要注意mac下的配置文件是要自己新建对的，新建的配置文件权限不要配赋为全局可写，不然会被忽略：

报出警告如下！！！！！

my_print_defaults: [Warning] World-writable config file '/etc/my.cnf' is ignored.
Starting MySQL
.my_print_defaults: [Warning] World-writable config file '/etc/my.cnf' is ignored.
my_print_defaults: [Warning] World-writable config file '/etc/my.cnf' is ignored.