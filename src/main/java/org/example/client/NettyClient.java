package org.example.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 客户端的职责
 * 1、连接服务器
 * 2、连接服务器失败，能够自动重连，达到最大次数后，停止连接
 */
public class NettyClient {
    private static final int MAX_RETRY = 5;
    private static final String HOST = "127.0.0.1";
    public static void main(String[] args) {
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        // 引导类，客户端启动
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(workGroup)
                // 指定服务端的 IO 模型为NIO
                .channel(NioSocketChannel.class)
                // 这里主要就是定义连接的业务处理逻辑
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        System.out.println("客户端启动成功......");
                    }
                })
                .option(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                // 这里是TCP的连接超时时间,而不是客户端真正连接上服务器的时间，与下面的重连不冲突
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
        // 客户端失败重连
        connect(bootstrap, HOST, 18076, MAX_RETRY);
    }

    /**
     * 连接，包含自动重连.重连就是递归调用自己
     * @param bootstrap bootstrap
     * @param host IP
     * @param port 端口
     * @param retry 重试次数
     */
    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap
                // 客户端连接使用connect
                .connect(host, port)
                .addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println((new Date() + ":客户端连接服务器端成功"));
                    }else if(retry == 0) {
                        System.err.println("重连次数已用完......");
                    } else {
                        int order = (MAX_RETRY - retry) + 1;
                        // 重连间隔为： 重试第N次乘2，2秒，4秒，6秒，左移几位，就是 【右边 * 2 * 左边】
                        // 1 << order 和下面的写法相等
                        int delay = order << 1;
                        System.err.println(new Date() + ": 连接失败，第" + order + "次重连......");
                        bootstrap
                                .config()
                                .group()
                                // 设置线程工作组的定时任务
                                .schedule(() -> connect(bootstrap, host, port+1, retry-1), delay, TimeUnit.SECONDS);
                    }
                });
    }
}
