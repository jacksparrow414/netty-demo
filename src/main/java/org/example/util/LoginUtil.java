package org.example.util;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import org.example.attribute.Attributes;

/**
 * 客户端连接成功之后，标记该条channel连接为已登录状态.
 * 给 Channel 绑定一个登录成功的标志位，然后判断是否登录成功的时候取出这个标志位就可以了
 * 通过 channel.attr(xxx).set(xx) 的方式
 */
public class LoginUtil {

    /**
     * 调用channel.attr().set方法
     * @param channel
     */
    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(Boolean.TRUE);
    }

    public static boolean hasLogin(Channel channel) {
        Attribute<Boolean> attr = channel.attr(Attributes.LOGIN);
        return attr.get() != null;
    }
}
