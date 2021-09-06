package org.example.serialize;

/**
 * 序列化算法.
 * JSON、Java自带的序列化、protoBuf等
 */
public interface SerializerAlgorithm {

    /**
     * JSON序列化.
     */
    byte JSON = 1;
}
