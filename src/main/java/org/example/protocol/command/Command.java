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
     * 登录相应指令.
     */
    Byte LOGIN_RESPONSE = 2;
}
