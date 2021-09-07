package org.example.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import org.example.protocol.request.CreateGroupRequestPacket;
import org.example.protocol.response.CreateGroupResponsePacket;
import org.example.util.IDUtil;
import org.example.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket createGroupRequestPacket) {
        List<String> userIdList = createGroupRequestPacket.getUserIdList();
        // 创建房间--也就是创建一个Channel分组
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        // 保存映射关系
        List<String> userNameList = new ArrayList<>();
        userIdList.forEach(item -> {
            Channel channel = SessionUtil.getChannel(item);
            if(channel != null) {
                channelGroup.add(channel);
                userNameList.add(SessionUtil.getSession(channel).getUserName());
            }
        });

        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();createGroupResponsePacket.setSuccess(true);
        createGroupResponsePacket.setGroupId(IDUtil.randomId());
        createGroupResponsePacket.setUserNameList(userNameList);

        // 将创建成功之后的消息发送给各个客户端,这里使用群组的channel
        channelGroup.writeAndFlush(createGroupResponsePacket);
        System.out.print("群创建成功，id 为[" + createGroupResponsePacket.getGroupId() + "], ");
        System.out.println("群里面有：" + createGroupResponsePacket.getUserNameList());
    }
}
