/**
 * sharemer.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */
package dotting.timer.server.receive;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sunqinwen
 * @version \: Receiver.java,v 0.1 2018-12-05 15:35
 * 数据接收
 */
public class Receiver {

    private static Logger logger = LoggerFactory.getLogger(Receiver.class);

    public Receiver(int port) {
        new Thread(() -> {
            try {
                initReceiver(port);
            } catch (InterruptedException e) {
                logger.error("init receiver error !", e);
            }
        }).start();
    }

    private static void initReceiver(int port) throws InterruptedException {
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        b.group(group).channel(NioDatagramChannel.class).handler(new ReceiverHandler());
        logger.info(">>>>>>>>>the receiver udp server will init, and will listen port:{}", port);
        b.bind(port).sync().channel().closeFuture().await();
    }

}
