package org.example.serialize;

import org.example.serialize.impl.JSONSerializer;

/**
 * 序列化接口.
 * 处理完成序列化、反序列化的作用，还有一个重要的序列化算法
 */
public interface Serializer {

    /**
     * 将JSON作为默认的序列化算法
     */
    Serializer DEFAULT = new JSONSerializer();

    /**
     * 序列化的算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * 序列化
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 反序列化
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
