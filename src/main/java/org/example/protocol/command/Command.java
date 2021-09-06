package org.example.protocol.command;

/**
 * 指令.
 */
public interface Command {

    /**
     * 登录请求指令.
     */
    Byte LOGIN_REQUEST = 1;

    /**
     * 登录响应指令.
     */
    Byte LOGIN_RESPONSE = 2;

    /**
     * 发送消息请求指令.
     */
    Byte MESSAGE_REQUEST = 3;

    /**
     * 发送消息响应指令.
     */
    Byte MESSAGE_RESPONSE = 4;
}
