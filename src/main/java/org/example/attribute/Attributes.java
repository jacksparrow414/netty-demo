package org.example.attribute;

import io.netty.util.AttributeKey;
import org.example.session.Session;

public interface Attributes {

    /**
     * channel 是否登录连接标识
     */
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");

    /**
     * channel 上的session标识
     */
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
