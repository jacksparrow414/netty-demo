package org.example.protocol.request;

import lombok.Data;
import org.example.protocol.Packet;
import org.example.protocol.command.Command;

import java.util.List;

@Data
public class CreateGroupRequestPacket extends Packet {

    private List<String> userIdList;

    @Override
    public Byte getCommand() {
        return Command.CREATE_GROUP_REQUEST;
    }
}
