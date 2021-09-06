package org.example.protocol.command;

import lombok.Data;

/**
 * 具体的请求包
 */
@Data
public class LoginRequestPacket extends Packet{

    private Integer userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
