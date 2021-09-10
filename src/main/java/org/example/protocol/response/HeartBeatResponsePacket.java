package org.example.protocol.response;

import org.example.protocol.Packet;
import org.example.protocol.command.Command;

public class HeartBeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return Command.HEARTBEAT_RESPONSE;
    }
}
