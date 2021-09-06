package org.example.serialize.impl;

import com.alibaba.fastjson.JSON;
import org.example.serialize.Serializer;
import org.example.serialize.SerializerAlgorithm;

/**
 * JSON序列化接口.
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
