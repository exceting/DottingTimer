/**
 * sharemer.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.server.receive;

import dotting.timer.server.pipline.PipLine;
import dotting.timer.server.pipline.PipLineFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * @author sunqinwen
 * @version \: ReceiverHandler.java,v 0.1 2018-12-05 16:03
 */
public class ReceiverHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private PipLineFactory pipLineFactory;

    public ReceiverHandler(PipLineFactory pipLineFactory){
        this.pipLineFactory = pipLineFactory;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        ByteBuf buf = msg.copy().content();
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        pipLineFactory.getPipLine(bytes).push(bytes);
    }
}
