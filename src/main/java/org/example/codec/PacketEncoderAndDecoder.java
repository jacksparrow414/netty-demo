package org.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.example.protocol.Packet;
import org.example.protocol.PacketCodeC;

import java.util.List;

/**
 * 直接用Netty封装好的.可以实现自定义解码，而无需关心 ByteBuf 的强转和 解码结果的传递
 * 1. 可以直接获得ByteBuf，无需自己强转
 * 2. 可以自己释放内存
 */
public class PacketEncoderAndDecoder extends ByteToMessageCodec<Packet> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) {
        PacketCodeC.INSTANCE.encodeByteBuf(out, msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        out.add(PacketCodeC.INSTANCE.decode(in));
    }
}
