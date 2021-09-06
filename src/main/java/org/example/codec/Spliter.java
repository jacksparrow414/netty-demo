package org.example.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.example.protocol.PacketCodeC;

/**
 * 基于长度域的拆包器.
 */
public class Spliter extends LengthFieldBasedFrameDecoder {
    // 在自定义协议中数据长度的偏移量，因为前面有魔数(4)+版本(1)+序列化算法(1)+指令(1)，所以是7
    private static final int LENGTH_FIELD_OFFSET = 7;
    // 在自定义协议中数据长度为4个字节
    private static final int LENGTH_FIELD_LENGTH = 4;

    public Spliter() {
        // 长度域偏移量、长度域长度
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        // 屏蔽非本协议的连接,利用协议前4个字节，魔数来判断
        if (in.getInt(in.readerIndex()) != PacketCodeC.MAGIC_NUMBER) {
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
