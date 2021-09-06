package org.example.protocol.request;

import lombok.Data;
import org.example.protocol.Packet;
import org.example.protocol.command.Command;
@Data
public class MessageRequestPacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
