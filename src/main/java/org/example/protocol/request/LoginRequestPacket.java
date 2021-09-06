package org.example.protocol.request;

import lombok.Data;
import org.example.protocol.command.Command;
import org.example.protocol.Packet;

/**
 * 具体的请求包
 */
@Data
public class LoginRequestPacket extends Packet {

    private String userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_REQUEST;
    }
}
