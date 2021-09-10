package org.example.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.protocol.request.HeartBeatRequestPacket;
import org.example.protocol.response.HeartBeatResponsePacket;

import java.util.Date;

/**
 * 服务端回复客户端心跳请求.{@link IMIdleStateHandler}空闲检测并不能代表客户端假死.因为客户端很有可能确实是没有消息发送给服务端,但是连接正常
 */
@ChannelHandler.Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {

    public static final HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket msg) throws Exception {
        System.out.println(new Date() + ":收到心跳包......");
        ctx.writeAndFlush(new HeartBeatResponsePacket());
    }
}
