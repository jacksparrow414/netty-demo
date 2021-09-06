package org.example.util;

import io.netty.channel.Channel;
import org.example.attribute.Attributes;
import org.example.session.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtil {
    // userId -> Channel 对应关系
    private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap();

    public static void bindSession(Session session, Channel channel) {
        // 将userId和对应的连接关系放入map中保存
        userIdChannelMap.put(session.getUserId(), channel);
        // 给该条连接设置session
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unBindSession(Channel channel) {
        if (hasSession(channel)) {
            userIdChannelMap.remove(getSession(channel).getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    public static Channel getChannel(String userId) {
        return userIdChannelMap.get(userId);
    }

    public static boolean hasSession(Channel channel) {
        return channel.hasAttr(Attributes.SESSION);
    }
}
