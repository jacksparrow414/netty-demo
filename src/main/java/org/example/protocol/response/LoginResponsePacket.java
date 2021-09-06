package org.example.protocol.response;

import lombok.Data;
import org.example.protocol.Packet;
import org.example.protocol.command.Command;

@Data
public class LoginResponsePacket extends Packet {

    private String userId;

    private String userName;

    private Boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return Command.LOGIN_RESPONSE;
    }
}
