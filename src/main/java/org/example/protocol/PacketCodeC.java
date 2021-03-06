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
 * 对数据包进行编码、解码.
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
     * 编码.
     * @param packet 数据包对象
     * @param byteBufAllocator ByteBuf分配器
     * @return
     */
    public ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet) {
        // 创建ByteBuf对象
        ByteBuf byteBuf = byteBufAllocator.ioBuffer();
        // 序列化Java对象,序列化为byte数组
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 构造数据包
        // 1. 魔数 int 4个字节
        byteBuf.writeInt(MAGIC_NUMBER);
        // 2. 版本号 1个字节
        byteBuf.writeByte(packet.getVersion());
        // 3. 序列化算法 1个字节
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        // 4. 指令 1个字节
        byteBuf.writeByte(packet.getCommand());
        // 5. 数据长度
        byteBuf.writeInt(bytes.length);
        // 6. 真正的数据
        byteBuf.writeBytes(bytes);
        return byteBuf;
    }

    /**
     * 直接传入ByteBuf.
     * @param byteBuf
     * @param packet
     * @return
     */
    public void encodeByteBuf(ByteBuf byteBuf, Packet packet) {
        // 序列化Java对象,序列化为byte数组
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 构造数据包
        // 1. 魔数 int 4个字节
        byteBuf.writeInt(MAGIC_NUMBER);
        // 2. 版本号 1个字节
        byteBuf.writeByte(packet.getVersion());
        // 3. 序列化算法 1个字节
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        // 4. 指令 1个字节
        byteBuf.writeByte(packet.getCommand());
        // 5. 数据长度
        byteBuf.writeInt(bytes.length);
        // 6. 真正的数据
        byteBuf.writeBytes(bytes);
    }

    /**
     * 解码.
     * @param byteBuf
     * @return
     */
    public Packet decode(ByteBuf byteBuf) {
        // 跳过魔数
        byteBuf.skipBytes(4);
        // 跳过版本号
        byteBuf.skipBytes(1);
        // 序列化算法
        byte serializeAlgorithm  = byteBuf.readByte();
        // 指令
        byte command = byteBuf.readByte();
        // 数据长度
        int dataLength = byteBuf.readInt();
        byte[] bytes = new byte[dataLength];
        // 将数据读取到字节数组中
        byteBuf.readBytes(bytes);
        // 获取序列化算法的实现类
        Serializer serializer = getSerializer(serializeAlgorithm);
        // 根据指令获取真正的指令类
        Class<? extends Packet> requestType = getRequestType(command);
        if (serializer != null && requestType != null) {
            // 反序列化为Java对象
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
