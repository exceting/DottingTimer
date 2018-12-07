package dotting.timer.core.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import dotting.timer.core.span.CoreSpan;

/**
 * Create by 18073 on 2018/12/5.
 * 自定义span序列化器
 */
public class SpanSerializer extends Serializer<CoreSpan> {

    public SpanSerializer() {
        super(false, true);
    }

    @Override
    public void write(Kryo kryo, Output output, CoreSpan span) {
        output.writeLong(span.getTraceId(), true);
        output.writeLong(span.getSpanId(), true);
        output.writeLong(span.getParentId(), true);
        output.writeLong(span.getStartTime(), true);
        output.writeLong(span.getEndTime(), true);
        output.writeInt(span.getIsAsync());
        output.writeInt(span.getIsError());
        output.writeLong(span.getExpect(), true);
        output.writeString(span.getMoudle());
        output.writeString(span.getTitle());
        output.writeString(span.getTags());
        output.writeLong(span.getCount(), true);
        output.writeLong(span.getAvg(), true);
        output.writeLong(span.getMinTime(), true);
        output.writeLong(span.getMaxTime(), true);
    }

    @Override
    public CoreSpan read(Kryo kryo, Input input, Class<CoreSpan> type) {
        CoreSpan span = new CoreSpan();
        span.setTraceId(input.readLong(true));
        span.setSpanId(input.readLong(true));
        span.setParentId(input.readLong(true));
        span.setStartTime(input.readLong(true));
        span.setEndTime(input.readLong(true));
        span.setIsAsync(input.readInt());
        span.setIsError(input.readInt());
        span.setExpect(input.readLong(true));
        span.setMoudle(input.readString());
        span.setTitle(input.readString());
        span.setTags(input.readString());
        span.setCount(input.readLong(true));
        span.setAvg(input.readLong(true));
        span.setMinTime(input.readLong(true));
        span.setMaxTime(input.readLong(true));
        return span;
    }

}
