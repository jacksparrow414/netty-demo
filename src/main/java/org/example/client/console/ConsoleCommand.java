package org.example.client.console;

import io.netty.channel.Channel;

import java.util.Scanner;

public interface ConsoleCommand {

    /**
     * 具体命令执行抽象.
     * @param scanner
     * @param channel
     */
    void exec(Scanner scanner, Channel channel);
}
