package org.example.protocol.response;

import lombok.Data;
import org.example.protocol.Packet;
import org.example.protocol.command.Command;
import org.example.session.Session;

@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {

        return Command.GROUP_MESSAGE_RESPONSE;
    }
}
