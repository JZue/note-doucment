[参考文章](https://blog.csdn.net/qq_35638156/article/details/80071951)

[POST乱码原因](https://www.jianshu.com/p/851522e4326c)

Tomcat 8.0 之前默认编码采用的是ISO8859-1

而在tomcat8之后，URL的编码改为了utf-8，其他的还是ISO8859-1

所以就出现了get请求没问题，然后post请求，由于参数在入参Object里，而不是在url上，故而就会出现乱码。





对于**URL的编码**，我们可以通过tomcat的server.xml的connector属性去配置

<Connector connectionTimeout="20000" port="8080" protocol="HTTP/1.1" redirectPort="8443" URIEncoding="utf-8"/>





那么Springboot又是怎么控制tomcat的编码的

`server.tomcat.uri-encoding=UTF-8`
`spring.http.encoding.charset=UTF-8`
`spring.http.encoding.enabled=true`
`spring.http.encoding.force=true`

`spring.messages.encoding=UTF-8`

