| 软件                    | 作用                                           |
| ----------------------- | ---------------------------------------------- |
| mongod                  | 数据库软件                                     |
| mongo                   | mongodb 命令行工具                             |
| Mongodump/mongorestore  | 命令行数据库备份和恢复工具                     |
| mongos                  | momgodb路由进程，分片环境下使用                |
| Mongoexport/mongoimport | Csv/json导入与导出，主要用于不同系统间数据迁移 |
| compass                 | mongodb官方提供的gui工具                       |
| Ops Manager (企业版)    | mongodb集群管理工具                            |
| BI Connector（企业版）  | SQL解释器、BI套接件（兼容MySQL的语法）         |
| Atlas(付费及免费版)     | mongo云托管服务，包括永久免费云数据库          |
| mongo charts            | mongo可视化软件                                |



### mongodump/mongorestore与mongoexport/mongoimport的区别



mongoexport/mongoimport导入/导出的是JSON格式，而mongodump/mongorestore导入/导出的是BSON格式。JSON可读性强但体积较大，BSON则是二进制文件，体积小但对人类几乎没有可读性。
在一些mongodb版本之间，BSON格式可能会随版本不同而有所不同，所以不同版本之间用mongodump/mongorestore可能不会成功，具体要看版本之间的兼容性。当无法使用BSON进行跨版本的数据迁移的时候，使用JSON格式即mongoexport/mongoimport是一个可选项。跨版本的mongodump/mongorestore个人并不推荐，实在要做请先检查文档看两个版本是否兼容（大部分时候是的）。JSON虽然具有较好的跨版本通用性，但其只保留了数据部分，不保留索引，账户等其他基础信息。使用时应该注意。
总之，这两套工具在实际使用中各有优势，应该根据应用场景选择使用（好像跟没说一样）。但严格地说，mongoexport/mongoimport的主要作用还是导入/导出数据时使用，并不是一个真正意义上的备份工具。所以这里也不展开介绍了   

