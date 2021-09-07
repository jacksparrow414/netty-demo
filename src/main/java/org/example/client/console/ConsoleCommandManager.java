package org.example.client.console;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleCommandManager implements ConsoleCommand{
    private Map<String, ConsoleCommand> consoleCommandMap;

    public ConsoleCommandManager() {
        consoleCommandMap =  new HashMap<>();
        consoleCommandMap.put("createGroup", new CreateGroupConsoleCommand());
        consoleCommandMap.put("joinGroup", new JoinGroupConsoleCommand());
        consoleCommandMap.put("quitGroup", new QuitGroupConsoleCommand());
        consoleCommandMap.put("listGroupMembers", new ListGroupMembersConsoleCommand());
    }

    @Override
    public void exec(Scanner scanner, Channel channel) {
        String command = scanner.next();
        ConsoleCommand consoleCommand = consoleCommandMap.get(command);
        if (Objects.isNull(consoleCommand)) {
            System.err.println("无法识别[" + command + "]指令，请重新输入!");
        }
        consoleCommand.exec(scanner, channel);
    }
}
