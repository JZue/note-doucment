![img](./bio.jpeg)

BIO 是一个一请求一应答的通讯模型，怎么理解一请求一应答呢？

服务端由一个独立的 Acceptor 线程负责监听所有客户端的连接，接收到客户端连接请求之后为每个客户端创建一个新的线程进行处理，处理完成之后，通过输出流返回应答给客户端，然后线程销毁。也即是一个客户端请求会对应一个服务端线程来处理。