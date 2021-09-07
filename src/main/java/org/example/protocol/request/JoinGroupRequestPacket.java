package org.example.protocol.request;

import lombok.Data;
import org.example.protocol.Packet;
import org.example.protocol.command.Command;

@Data
public class JoinGroupRequestPacket extends Packet {

    private String groupId;

    @Override
    public Byte getCommand() {
        return Command.JOIN_GROUP_REQUEST;
    }
}
