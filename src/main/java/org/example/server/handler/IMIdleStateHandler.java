package org.example.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 服务器空闲监测.
 * 通常空闲检测时间要比发送心跳{@link org.example.client.handler.HeartBeatTimerHandler}的时间的两倍要长一些，这也是为了排除偶发的公网抖动，防止误判
 * 继承Netty内置的IdleStateHandler，重写channelIdle方法.
 * 15秒检测不到就关闭这条连接
 * 这里不用单例，是因为每个客户端的连接都应该是单独的对象，也就是【有状态的】
 * 如果用单例,则所有客户端的检测handler都不能用
 */
public class IMIdleStateHandler extends IdleStateHandler {

    private static final int READER_IDLE_TIME = 15;

    public IMIdleStateHandler() {
        super(READER_IDLE_TIME, 0,0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println(READER_IDLE_TIME + "秒内未读到数据，关闭连接");
        ctx.channel().close();
    }
}
