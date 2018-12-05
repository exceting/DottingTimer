package dotting.timer.core.test;


import dotting.timer.core.serialize.KryoPool;
import dotting.timer.core.span.CoreSpan;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.*;

/**
 * Create by 18073 on 2018/12/5.
 */
public class SpanSerializerTest {

    private DatagramSocket socket;

    private InetAddress host;

    private Integer port;

    @Before()
    public void beforeDo() {
        try {
            this.socket = new DatagramSocket(0);
            this.socket.setSoTimeout(100);
            this.host = InetAddress.getByName("127.0.0.1");
            this.port = 8009;
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("before done~");
    }

    @Test()
    public void test() throws Exception{
        CoreSpan span = new CoreSpan();
        span.setTraceId(1111L);
        span.setSpanId(22222L);
        span.setParentId(888L);
        span.setStartTime(14345325L);
        span.setEndTime(453466665L);
        span.setIsAsync(1);
        span.setIsError(0);
        span.setExpect(55L);
        span.setMoudle("sharemer-core");
        span.setTitle("getAllMusicById");
        span.setTags("{\"db\":\"mysql\"}");
        span.setCount(5555L);
        span.setAvg(56L);
        span.setMinTime(5L);
        span.setMaxTime(180L);

        byte[] bytes = KryoPool.serialize(span);

        DatagramPacket request = new DatagramPacket(bytes, bytes.length, host, port);
        socket.send(request);

        System.out.println("done~");
    }

    @After
    public void afterDo() {
        if(socket != null){
            socket.close();
        }
        System.out.println("after done~");
    }

}
