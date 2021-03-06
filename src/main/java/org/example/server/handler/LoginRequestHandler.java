package org.example.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.protocol.request.LoginRequestPacket;
import org.example.protocol.response.LoginResponsePacket;
import org.example.session.Session;
import org.example.util.LoginUtil;
import org.example.util.SessionUtil;

import java.util.UUID;

@ChannelHandler.Sharable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    public static final LoginRequestHandler INSTANCE = new LoginRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) {
        // 回复给客户端
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setSuccess(true);
        loginResponsePacket.setReason("登录成功");
        String userId = randomUserId();
        loginResponsePacket.setUserId(userId);
        loginResponsePacket.setUserName(msg.getUsername());
        // 将当前连接标记为已登录
        LoginUtil.markAsLogin(ctx.channel());
        // 为当前连接设置session
        SessionUtil.bindSession(new Session(userId, msg.getUsername()), ctx.channel());
        // 将回复消息写入channel
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        SessionUtil.unBindSession(ctx.channel());
    }

    private static String randomUserId() {
        return UUID.randomUUID().toString().split("-")[0];
    }
}
