```
SerializationException, Cannot deserialize; nested exception is org.springframework.core.serializer.support.SerializationFailedException: Failed to deserialize payload. Is the byte array a result of corresponding serialization for DefaultDeserializer?; nested exception is java.io.InvalidClassException: org.springframework.security.core.authority.SimpleGrantedAuthority; local class incompatible: stream classdesc serialVersionUID = 510, local class serialVersionUID = 500
```

SpringBoot +Spring Security 基于OAuth2.0协议实现token鉴权

我先基于一个SpringBoot 2.0.1 RELEASE 的版本做demo实现了上面的功能，后面把我demo完的功能准备集成到自己的小站（SpringBoot 2.1.6 RELEASE）上，就出现了序列化的问题，看到上面的错误。

原因是在springboot 2.0 上的

```
org.springframework.security.core.authority.SimpleGrantedAuthority的serialVersionUID=500L
```

而在SpringBoot2.1

```
org.springframework.security.core.authority.SimpleGrantedAuthority的serialVersionUID=510L
```