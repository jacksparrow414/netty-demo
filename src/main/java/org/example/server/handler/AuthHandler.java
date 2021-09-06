package org.example.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.util.LoginUtil;

/**
 * 负责判断用户是否登录成功.
 * 登录成功之后，利用【pipeline热插拔】将自身handler删除.避免以后每次都验证
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!LoginUtil.hasLogin(ctx.channel())) {
            // 直接关闭连接
            ctx.channel().close();
        }else {
            // 在经过第1次验证之后，移除。这样如果登录之后，有100请求，那么只需第一次请求是否登录即可，后续无需每次都验证。提高性能
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        if (LoginUtil.hasLogin(ctx.channel())) {
            System.out.println("当前连接登录验证完毕，无需再次验证, AuthHandler 被移除");
        } else {
            System.out.println("无登录验证，强制关闭连接!");
        }
    }
}
