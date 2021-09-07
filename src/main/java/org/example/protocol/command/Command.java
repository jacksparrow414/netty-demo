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

    /**
     * 登录请求指令.
     */
    Byte LOGOUT_REQUEST = 5;

    /**
     * 登录响应指令.
     */
    Byte LOGOUT_RESPONSE= 6;

    /**
     * 创建群聊请求指令.
     */
    Byte CREATE_GROUP_REQUEST = 7;

    /**
     * 创建群聊响应指令.
     */
    Byte CREATE_GROUP_RESPONSE = 8;

    /**
     * 加入群聊请求指令.
     */
    Byte JOIN_GROUP_REQUEST = 9;

    /**
     * 加入群聊响应指令.
     */
    Byte JOIN_GROUP_RESPONSE = 10;

    /**
     * 退出群聊请求指令.
     */
    Byte QUIT_GROUP_REQUEST = 11;

    /**
     * 退出群聊响应指令.
     */
    Byte QUIT_GROUP_RESPONSE = 12;
}
