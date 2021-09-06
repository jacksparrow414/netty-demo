package org.example.protocol.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.protocol.Packet;
import org.example.protocol.command.Command;
@Data
@NoArgsConstructor
public class MessageRequestPacket extends Packet {

    private String message;

    public MessageRequestPacket(String message) {
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
