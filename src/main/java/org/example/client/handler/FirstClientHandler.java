package org.example.client.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 继承ChannelInboundHandlerAdapter这种方式需要自己手动释放内存.
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当连接可用时.
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端开始写数据......");
        // 拿到该条连接的channel
        Channel channel = ctx.channel();
        ByteBuf byteBuf = getByteBuf(ctx);
        // netty中数据是以ByteBuf为单位
        // ByteBuf 中有readerIndex读指针、writerIndex写指针、capacity容量、maxCapacity最大容量
        // 每读一个字节,readerIndex + 1;
        // 每写一个字节,writerIndex + 1;
        // 可读字节 = writerIndex - readerIndex
        channel.writeAndFlush(byteBuf);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // ctx.alloc() 获取到一个 ByteBuf 的内存管理器，这个 内存管理器的作用就是分配一个 ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();
        byte[] bytes = "你好, jack".getBytes(StandardCharsets.UTF_8);
        // 写数据到ByteBuf中
        buffer.writeBytes(bytes);
        return buffer;
    }

    /**
     * 在连接中读取数据.
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("收到服务器回复消息:" + byteBuf.toString(Charset.defaultCharset()));
    }
}
