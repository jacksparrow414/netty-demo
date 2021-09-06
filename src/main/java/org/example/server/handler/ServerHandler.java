package org.example.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.protocol.Packet;
import org.example.protocol.PacketCodeC;
import org.example.protocol.request.LoginRequestPacket;
import org.example.protocol.response.LoginResponsePacket;

/**
 * 服务器端Handler.
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        // 解码
        Packet packet = PacketCodeC.INSTANCE.decode(byteBuf);

        if (packet instanceof LoginRequestPacket) {
            System.out.println("LoginRequestPacket 解析成功......");
            // 回复给客户端
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setSuccess(true);
            loginResponsePacket.setReason("登录成功");
            // 获得当前连接的ByteBuf分配器
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), loginResponsePacket);
            // 将回复消息写入channel
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }
}
