package org.example.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.protocol.request.LoginRequestPacket;
import org.example.protocol.response.LoginResponsePacket;
import org.example.util.LoginUtil;

public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) {
        System.out.println("LoginRequestPacket 解析成功......");
        // 回复给客户端
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setSuccess(true);
        loginResponsePacket.setReason("登录成功");
        // 将当前连接标记为已登录
        LoginUtil.markAsLogin(ctx.channel());
        // 将回复消息写入channel
        ctx.channel().writeAndFlush(loginResponsePacket);
    }
}
