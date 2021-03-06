package org.example.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.example.protocol.response.LoginResponsePacket;
import org.example.session.Session;
import org.example.util.LoginUtil;
import org.example.util.SessionUtil;

/**
 * 直接获得读取数据的对象，不再需要像{@link ClientHandler#channelRead(io.netty.channel.ChannelHandlerContext, java.lang.Object)}那样强转
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//        System.out.println(new Date() + ":客户端开始登录......");
//        // //构造Packet
//        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
//        loginRequestPacket.setUserId(UUID.randomUUID().toString());
//        loginRequestPacket.setUsername("jack");
//        loginRequestPacket.setPassword("123456");
//        // 向当前channel里开始写
//        ctx.channel().writeAndFlush(loginRequestPacket);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {
        if (loginResponsePacket.getSuccess()) {
            System.out.println(loginResponsePacket.getReason());
            LoginUtil.markAsLogin(ctx.channel());
            System.out.println("[" + loginResponsePacket.getUserName() + "]登录成功，userId 为: " + loginResponsePacket.getUserId());
            SessionUtil.bindSession(new Session(loginResponsePacket.getUserId(), loginResponsePacket.getUserName()), ctx.channel());
        }else {
            System.out.println("登录失败,原因为:" + loginResponsePacket.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("客户端连接被关闭!");
    }
}
