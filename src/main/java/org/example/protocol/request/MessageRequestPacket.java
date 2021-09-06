package org.example.protocol.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.protocol.Packet;
import org.example.protocol.command.Command;
@Data
@NoArgsConstructor
public class MessageRequestPacket extends Packet {

    private String toUserId;
    private String message;

    public MessageRequestPacket(String toUserId, String message) {
        this.message = message;
        this.toUserId = toUserId;
    }

    public MessageRequestPacket(String message) {
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return Command.MESSAGE_REQUEST;
    }
}
