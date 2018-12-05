/**
 * sharemer.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.server.receive;

import dotting.timer.server.po.CoreSpan;
import dotting.timer.server.serialize.KryoPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

/**
 * @author sunqinwen
 * @version \: ReceiverHandler.java,v 0.1 2018-12-05 16:03
 */
public class ReceiverHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) {
        ByteBuf buf = msg.copy().content();
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);

        CoreSpan span = KryoPool.deserialize(req);

        if (span != null) {
            System.out.println("traceId = " + span.getTraceId());
            System.out.println("spanId = " + span.getSpanId());
            System.out.println("parentId = " + span.getParentId());
            System.out.println("startTime = " + span.getStartTime());
            System.out.println("endTime = " + span.getEndTime());
            System.out.println("isAsync = " + span.getIsAsync());
            System.out.println("isError = " + span.getIsError());
            System.out.println("expect = " + span.getExpect());
            System.out.println("moudle = " + span.getMoudle());
            System.out.println("title = " + span.getTitle());
            System.out.println("tags = " + span.getTags());
            System.out.println("count = " + span.getCount());
            System.out.println("avg = " + span.getAvg());
            System.out.println("minTime = " + span.getMinTime());
            System.out.println("maxTime = " + span.getMaxTime());
        } else {
            System.out.println("deser is null !");
        }
    }
}
