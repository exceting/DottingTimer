/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.server.receive;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 *
 * @author sunqinwen
 * @version \: ReceiverHandler.java,v 0.1 2018-12-05 16:03 
 *
 */
public class ReceiverHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
        // 读取收到的数据
        ByteBuf buf = msg.copy().content();
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, CharsetUtil.UTF_8);
        System.out.println("【NOTE】>>>>>> 收到客户端的数据："+body);// 回复一条信息给客户端
    }
}
