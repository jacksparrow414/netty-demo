# netty-demo

Netty入门demo
## 通信协议定义
### 魔数
用来判断是否为本协议
### 版本
用来后续升级
### 序列化算法
具体Java对象的序列化方式，JSON、ProtoBuf等
### 指令
此次数据包的指令
### 数据长度
代表真实数据长度
### 真正数据
真正的数据的字节,利用具体的序列化算法序列化之后的字节
## 拆包器的使用
再真正处理每一个包的时候,首先就要判断这个包是不是完整的包
### 基于长度域的拆包器
LengthFieldBasedFrameDecoder,两个变量,长度域偏移量和长度域的长度
## 编码和解码的封装
Netty内的基本单位为ByteBuf,所以编解码就是生成、解析ByteBuf的过程.在这个过程完成对通信协议的封装、解析.  
继承Netty内置的ByteToMessageCodec
## 内置Handler的使用
一般继承SimpleChannelInboundHandler,根据需求重写channelRead0等方法.SimpleChannelInboundHandler默认处理了handler的传播以及内存的释放.  
一般不继承ChannelInboundHandlerAdapter,因为这里需要我们自己手动处理handler的传播以及内存的释放,以及获取ByteBuf等
## Handler热插拔
TODO
## 压缩传播路径
TODO
## 空闲监测
对Channel进行空闲监测,如果Channel空闲,则关闭该条Channel,节省服务器资源.一般继承IdleStateHandler,子类构造方法中调用其构造方法,并重写channelIdle方法.  
但是空闲不意味着客户端的连接失效,有可能是客户端的确没有数据发送,但是确实这条连接是正常的.那么这时需要通过心跳机制来保持.通常空闲监测的时间要比心跳时间的2倍多一点
## 心跳
客户端定时发送心跳,服务器响应心跳.重写channelActive方法,在此方法中定时发送心跳.通过ctx.executor().schedule()方法实现.  
服务器端简单响应即可
## 一对一单聊实现与原理
- 所有消息都经过服务器,由服务器做具体的转发.
- 在客户端连接服务器成功之后,服务器保存当前客户端的连接Channel,并给予标识.以便能够根据标识找到对于的客户端连接.
- 客户端B向客户端A发送消息.消息先发送到服务器,服务器根据客户端B中携带的客户端A的标识,在服务器中找到客户端A的连接.
- 服务器使用客户端A的的连接Channel,向客户端A发送消息.
## 一对多群聊实现与原理
- 所有消息都经过服务器,由服务器做具体ChannelGroup的转发
- 在客户端创建群聊请求之后,服务器创建ChannelGroup,将当前连接Channel放入add进ChannelGroup.ChannelGroup类似于Map<GroupId, List<Channel>>结构
- 客户端A向群聊里发送消息,服务器根据携带的ChannelGroup的ID,获得对应的ChannelGroup,调用ChannelGroup.writeAndFlush()方法即可向群发送消息
- 此时所有客户端B、C,包括A都会收到消息.
- 退出群聊,则调用ChannelGroup.remove掉当前客户端的Channel即可
