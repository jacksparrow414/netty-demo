package org.example.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.example.protocol.Packet;
import org.example.protocol.request.CreateGroupRequestPacket;
import org.example.protocol.request.GroupMessageRequestPacket;
import org.example.protocol.request.HeartBeatRequestPacket;
import org.example.protocol.request.JoinGroupRequestPacket;
import org.example.protocol.request.ListGroupMembersRequestPacket;
import org.example.protocol.request.LoginRequestPacket;
import org.example.protocol.request.MessageRequestPacket;
import org.example.protocol.request.QuitGroupRequestPacket;
import org.example.protocol.response.CreateGroupResponsePacket;
import org.example.protocol.response.GroupMessageResponsePacket;
import org.example.protocol.response.HeartBeatResponsePacket;
import org.example.protocol.response.JoinGroupResponsePacket;
import org.example.protocol.response.ListGroupMembersResponsePacket;
import org.example.protocol.response.LoginResponsePacket;
import org.example.protocol.response.MessageResponsePacket;
import org.example.protocol.response.QuitGroupResponsePacket;
import org.example.serialize.Serializer;
import org.example.serialize.impl.JSONSerializer;

import java.util.HashMap;
import java.util.Map;

import static org.example.protocol.command.Command.CREATE_GROUP_REQUEST;
import static org.example.protocol.command.Command.CREATE_GROUP_RESPONSE;
import static org.example.protocol.command.Command.GROUP_MESSAGE_REQUEST;
import static org.example.protocol.command.Command.GROUP_MESSAGE_RESPONSE;
import static org.example.protocol.command.Command.HEARTBEAT_REQUEST;
import static org.example.protocol.command.Command.HEARTBEAT_RESPONSE;
import static org.example.protocol.command.Command.JOIN_GROUP_REQUEST;
import static org.example.protocol.command.Command.JOIN_GROUP_RESPONSE;
import static org.example.protocol.command.Command.LIST_GROUP_REQUEST;
import static org.example.protocol.command.Command.LIST_GROUP_RESPONSE;
import static org.example.protocol.command.Command.LOGIN_REQUEST;
import static org.example.protocol.command.Command.LOGIN_RESPONSE;
import static org.example.protocol.command.Command.MESSAGE_REQUEST;
import static org.example.protocol.command.Command.MESSAGE_RESPONSE;
import static org.example.protocol.command.Command.QUIT_GROUP_REQUEST;
import static org.example.protocol.command.Command.QUIT_GROUP_RESPONSE;

/**
 * ?????????????????????????????????.
 */
public class PacketCodeC {

    public static final int MAGIC_NUMBER = 0x12345678;
    public static final PacketCodeC INSTANCE = new PacketCodeC();
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    private static final Map<Byte, Serializer> serializerMap;

    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        packetTypeMap.put(JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        packetTypeMap.put(JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
        packetTypeMap.put(QUIT_GROUP_REQUEST, QuitGroupRequestPacket.class);
        packetTypeMap.put(QUIT_GROUP_RESPONSE, QuitGroupResponsePacket.class);
        packetTypeMap.put(LIST_GROUP_REQUEST, ListGroupMembersRequestPacket.class);
        packetTypeMap.put(LIST_GROUP_RESPONSE, ListGroupMembersResponsePacket.class);
        packetTypeMap.put(GROUP_MESSAGE_REQUEST, GroupMessageRequestPacket.class);
        packetTypeMap.put(GROUP_MESSAGE_RESPONSE, GroupMessageResponsePacket.class);
        packetTypeMap.put(HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
        packetTypeMap.put(HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);
        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    /**
     * ??????.
     * @param packet ???????????????
     * @param byteBufAllocator ByteBuf?????????
     * @return
     */
    public ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet) {
        // ??????ByteBuf??????
        ByteBuf byteBuf = byteBufAllocator.ioBuffer();
        // ?????????Java??????,????????????byte??????
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // ???????????????
        // 1. ?????? int 4?????????
        byteBuf.writeInt(MAGIC_NUMBER);
        // 2. ????????? 1?????????
        byteBuf.writeByte(packet.getVersion());
        // 3. ??????????????? 1?????????
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        // 4. ?????? 1?????????
        byteBuf.writeByte(packet.getCommand());
        // 5. ????????????
        byteBuf.writeInt(bytes.length);
        // 6. ???????????????
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    /**
     * ????????????ByteBuf.
     * @param byteBuf
     * @param packet
     * @return
     */
    public void encodeByteBuf(ByteBuf byteBuf, Packet packet) {
        // ?????????Java??????,????????????byte??????
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // ???????????????
        // 1. ?????? int 4?????????
        byteBuf.writeInt(MAGIC_NUMBER);
        // 2. ????????? 1?????????
        byteBuf.writeByte(packet.getVersion());
        // 3. ??????????????? 1?????????
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        // 4. ?????? 1?????????
        byteBuf.writeByte(packet.getCommand());
        // 5. ????????????
        byteBuf.writeInt(bytes.length);
        // 6. ???????????????
        byteBuf.writeBytes(bytes);
    }

    /**
     * ??????.
     * @param byteBuf
     * @return
     */
    public Packet decode(ByteBuf byteBuf) {
        // ????????????
        byteBuf.skipBytes(4);
        // ???????????????
        byteBuf.skipBytes(1);
        // ???????????????
        byte serializeAlgorithm  = byteBuf.readByte();
        // ??????
        byte command = byteBuf.readByte();
        // ????????????
        int dataLength = byteBuf.readInt();
        byte[] bytes = new byte[dataLength];
        // ?????????????????????????????????
        byteBuf.readBytes(bytes);
        // ?????????????????????????????????
        Serializer serializer = getSerializer(serializeAlgorithm);
        // ????????????????????????????????????
        Class<? extends Packet> requestType = getRequestType(command);
        if (serializer != null && requestType != null) {
            // ???????????????Java??????
            return serializer.deserialize(requestType, bytes);
        }
        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }
}
