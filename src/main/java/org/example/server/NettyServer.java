package org.example.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.codec.PacketEncoderAndDecoder;
import org.example.codec.Spliter;
import org.example.server.handler.AuthHandler;
import org.example.server.handler.CreateGroupRequestHandler;
import org.example.server.handler.GroupMessageRequestHandler;
import org.example.server.handler.HeartBeatRequestHandler;
import org.example.server.handler.IMIdleStateHandler;
import org.example.server.handler.JoinGroupRequestHandler;
import org.example.server.handler.ListGroupMembersRequestHandler;
import org.example.server.handler.LoginRequestHandler;
import org.example.server.handler.MessageRequestHandler;
import org.example.server.handler.QuitGroupRequestHandler;

/**
 * 服务器职责
 * 1、启动服务，使服务可用
 * 2、能够接受来自客户端的连接
 */
public class NettyServer {
    private static final int PORT = 18080;
    public static void main(String[] args) {
        // 定义线程工作组
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        // 引导类，代表服务端启动
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 配置线程组
        serverBootstrap.group(bossGroup, workerGroup)
                // 指定服务端的 IO 模型为NIO
                .channel(NioServerSocketChannel.class)
                //这里主要就是定义后续每条连接的数据读写，业务处理逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        System.out.println("服务器连接初始化......");
//                        nioSocketChannel.pipeline().addLast(new FirstServerHandler());
//                        nioSocketChannel.pipeline().addLast(new ServerHandler());
                        // 空闲监测放在最前面
                        nioSocketChannel.pipeline().addLast(new IMIdleStateHandler());
                        nioSocketChannel.pipeline().addLast(new Spliter());
                        nioSocketChannel.pipeline().addLast(new PacketEncoderAndDecoder());
                        nioSocketChannel.pipeline().addLast(LoginRequestHandler.INSTANCE);
                        // 心跳响应不需要登录,所以放在认证Handler的前面
                        nioSocketChannel.pipeline().addLast(HeartBeatRequestHandler.INSTANCE);
                        nioSocketChannel.pipeline().addLast(new AuthHandler());
                        nioSocketChannel.pipeline().addLast(MessageRequestHandler.INSTANCE);
                        nioSocketChannel.pipeline().addLast(CreateGroupRequestHandler.INSTANCE);
                        nioSocketChannel.pipeline().addLast(JoinGroupRequestHandler.INSTANCE);
                        nioSocketChannel.pipeline().addLast(QuitGroupRequestHandler.INSTANCE);
                        nioSocketChannel.pipeline().addLast(ListGroupMembersRequestHandler.INSTANCE);
                        nioSocketChannel.pipeline().addLast(GroupMessageRequestHandler.INSTANCE);
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE);
        serverBootstrap
                // 绑定端口
                .bind(PORT)
                // 添加监听器,监听端口是否绑定成功
                .addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println("端口绑定成功，服务器启动成功");
                    }
                });
    }
}
