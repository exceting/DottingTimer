/**
 * Bilibili.com Inc.
 * Copyright (c) 2009-2018 All Rights Reserved.
 */

package dotting.timer.core.push;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import dotting.timer.core.serialize.KryoPool;
import dotting.timer.core.span.CoreSpan;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author sunqinwen
 * @version \: PushHandler.java,v 0.1 2018-09-20 19:12
 * 信息推送
 */
public class Pusher {

    private static Pusher receiver;

    private BlockingQueue<CoreSpan> queue;

    private DatagramSocket socket;

    private Map<Integer, InetAddress> hosts = Maps.newHashMap();

    private Map<Integer, Integer> ports = Maps.newHashMap();

    private int serverSize;

    public static void initReceiver(String hosts) throws Exception {
        if (receiver == null) {
            synchronized (Pusher.class) {
                if (receiver == null) {
                    receiver = new Pusher(hosts);
                }
            }
        }
    }

    private Pusher(String hosts) throws Exception {
        if (Strings.isNullOrEmpty(hosts)) {
            throw new Exception("dotting timer init receiver error! the hosts is empty!");
        }
        this.socket = new DatagramSocket(0);
        this.socket.setSoTimeout(100);
        String[] servers = hosts.split(",");
        serverSize = servers.length;
        for (int i = 0; i < serverSize; i++) {
            String[] iports = servers[i].split(":");
            if (iports.length != 2) {
                throw new Exception("dotting timer init receiver error! the hosts is wrong!");
            }
            this.hosts.put(i, InetAddress.getByName(iports[0]));
            this.ports.put(i, Integer.parseInt(iports[1]));
        }
        this.queue = new LinkedBlockingQueue<>();
        new Thread(this::pushTask).start();
    }

    public static Pusher getReceiver() {
        return receiver;
    }

    public void pushSpan(CoreSpan span) {
        queue.offer(span);
    }

    private void pushTask() {
        if (socket != null && queue != null && hosts != null && ports != null
                && hosts.size() > 0 && ports.size() > 0 && hosts.size() == ports.size()) {
            CoreSpan span;
            while (true) {
                try {
                    span = queue.take();
                    byte[] spanBytes = KryoPool.serialize(span);
                    int key = Arrays.hashCode(spanBytes) % serverSize;
                    DatagramPacket request = new DatagramPacket(spanBytes, spanBytes.length,
                            hosts.get(key), ports.get(key));
                    socket.send(request);
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
