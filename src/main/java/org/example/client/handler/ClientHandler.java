package org.example.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.protocol.Packet;
import org.example.protocol.PacketCodeC;
import org.example.protocol.request.LoginRequestPacket;
import org.example.protocol.response.LoginResponsePacket;
import org.example.protocol.response.MessageResponsePacket;
import org.example.util.LoginUtil;

import java.util.Date;
import java.util.UUID;

/**
 * 客户端登录Handler.
 * 继承ChannelInboundHandlerAdapter这种方式需要自己手动释放内存.
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 代表TCP连接的建立成功，
     * 通常我们在这个回调里面统计单机的连接数，
     * channelActive() 被调用，连接数加一，
     * channelInActive 代表TCP连接释放
     * channelInActive() 被调用，连接数减一
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(new Date() + ":客户端开始登录......");
        //构造Packet
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("jack");
        loginRequestPacket.setPassword("123456");
        // 获取当前连接的ByteBuf分配器，建议实际使用中这样使用
        ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginRequestPacket);
        // 向当前channel里开始写
        ctx.channel().writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println(new Date() + ":收到服务器端消息......");
        ByteBuf byteBuf = (ByteBuf) msg;
        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);
        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;
            if (loginResponsePacket.getSuccess()) {
                System.out.println(loginResponsePacket.getReason());
                LoginUtil.markAsLogin(ctx.channel());
            }else {
                System.out.println("登录失败,原因为:" + loginResponsePacket.getReason());
            }
        }else if (packet instanceof MessageResponsePacket) {
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());
        }
        // 事件传播，如果不是当前处理的packet，直接向下一个handler进行传递
//        ctx.fireChannelRead(packet)
    }
}
