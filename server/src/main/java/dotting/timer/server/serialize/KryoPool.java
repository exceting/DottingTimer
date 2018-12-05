package dotting.timer.server.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import dotting.timer.server.po.CoreSpan;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.SoftReferenceObjectPool;

/**
 * Create by 18073 on 2018/12/5.
 * 序列化工具,kryo对象池
 */
public class KryoPool implements PooledObjectFactory<Kryo> {

    public static class KryoResigerIds {
        public static final int SPAN = 1101;
    }

    private final static SoftReferenceObjectPool<Kryo> kryoPool;

    static {

        PooledObjectFactory<Kryo> factory = new KryoPool();

        kryoPool = new SoftReferenceObjectPool<>(factory);

    }

    public static Kryo create(boolean registrationRequired) {
        Kryo kryo = new Kryo();
        // 类型注册
        kryo.register(CoreSpan.class, new SpanSerializer(), KryoResigerIds.SPAN);
        kryo.setReferences(false);
        kryo.setRegistrationRequired(registrationRequired);
        return kryo;
    }

    public static Kryo create() {
        return create(false);
    }

    public static void serialize(Kryo kryo, Output output, Object obj) {
        kryo.writeClassAndObject(output, obj);
    }

    public static byte[] serialize(Object obj) {
        try {
            Kryo kryo = kryoPool.borrowObject();
            byte[] bs = serialize(kryo, obj);
            kryoPool.returnObject(kryo);
            return bs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] serialize(Kryo kryo, Object obj) {
        Output output = new Output(50, Integer.MAX_VALUE);
        kryo.writeClassAndObject(output, obj);
        output.close();
        return output.toBytes();
    }

    public static <T> T deserialize(byte[] data) {

        try {
            Kryo kryo = kryoPool.borrowObject();
            T res = deserialize(kryo, data);
            kryoPool.returnObject(kryo);
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static <T> T deserialize(Kryo kryo, byte[] bs) {
        T obj = deserialize(kryo, new Input(bs));
        return obj;
    }

    public static <T> T deserialize(Kryo kryo, Input input) {
        @SuppressWarnings("unchecked")
        T obj = (T) kryo.readClassAndObject(input);
        return obj;

    }

    @Override
    public void activateObject(PooledObject<Kryo> arg0) {

    }

    @Override
    public void destroyObject(PooledObject<Kryo> arg0) {

    }

    @Override
    public PooledObject<Kryo> makeObject() {
        Kryo kryo = create();
        return new DefaultPooledObject<>(kryo);
    }

    @Override
    public void passivateObject(PooledObject<Kryo> arg0) {

    }

    @Override
    public boolean validateObject(PooledObject<Kryo> arg0) {
        return true;
    }
}
