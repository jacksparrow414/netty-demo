package org.example.client.console;

import io.netty.channel.Channel;
import org.example.protocol.request.LoginRequestPacket;

import java.util.Scanner;

public class LoginConsoleCommand implements ConsoleCommand{

    @Override
    public void exec(Scanner scanner, Channel channel) {
        System.out.print("输入用户名登录: ");
        String username = scanner.nextLine();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUsername(username);
        // 密码使用默认的
        loginRequestPacket.setPassword("pwd");
        // 发送登录数据包
        channel.writeAndFlush(loginRequestPacket);
    }
}
