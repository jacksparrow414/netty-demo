package org.example.protocol.command;

import lombok.Data;

/**
 * 所有通信数据包的抽象类
 */
@Data
public abstract class Packet {

    /**
     * 协议版本号
     */
    private Byte version = 1;

    /**
     * 指令
     * @return
     */
    public abstract Byte getCommand();
}
