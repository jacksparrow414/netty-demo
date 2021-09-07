package org.example.protocol.response;

import lombok.Data;
import org.example.protocol.Packet;
import org.example.protocol.command.Command;
import org.example.session.Session;

import java.util.List;

@Data
public class ListGroupMembersResponsePacket extends Packet {

    private String groupId;

    private List<Session> sessionList;

    @Override
    public Byte getCommand() {
        return Command.LIST_GROUP_RESPONSE;
    }
}
