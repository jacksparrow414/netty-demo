package org.example.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import org.example.protocol.request.ListGroupMembersRequestPacket;
import org.example.protocol.response.ListGroupMembersResponsePacket;
import org.example.session.Session;
import org.example.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

public class ListGroupMembersRequestHandler extends SimpleChannelInboundHandler<ListGroupMembersRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersRequestPacket listGroupMembersRequestPacket) throws Exception {
        String groupId = listGroupMembersRequestPacket.getGroupId();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        List<Session> sessionList  = new ArrayList<>();
        channelGroup.forEach(item -> {
            Session session = SessionUtil.getSession(item);
            sessionList.add(session);
        });
        // 构建获取成员列表响应写回到客户端
        ListGroupMembersResponsePacket responsePacket = new ListGroupMembersResponsePacket();
        responsePacket.setGroupId(groupId);
        responsePacket.setSessionList(sessionList);
        // 直接向当前连接channel发送退出消息
        ctx.channel().writeAndFlush(responsePacket);
    }
}
